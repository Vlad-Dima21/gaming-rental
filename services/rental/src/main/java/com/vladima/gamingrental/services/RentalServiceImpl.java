package com.vladima.gamingrental.services;

import com.vladima.gamingrental.clients.ClientClient;
import com.vladima.gamingrental.clients.DeviceClient;
import com.vladima.gamingrental.clients.GameCopyClient;
import com.vladima.gamingrental.controllers.RentalController;
import com.vladima.gamingrental.dtos.RentalRequestDTO;
import com.vladima.gamingrental.dtos.RentalResponseDTO;
import com.vladima.gamingrental.exceptions.EntityOperationException;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import com.vladima.gamingrental.models.Rental;
import com.vladima.gamingrental.models.RentalGameCopy;
import com.vladima.gamingrental.repositories.RentalRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final int PAGE_SIZE = 3;
    private final RentalRepository repository;
    private final ClientClient clientClient;
    private final DeviceClient deviceClient;
    private final GameCopyClient gameCopyClient;

    private void getClientById(Long clientId) {
        var client = clientClient.getClientById(clientId);
        if (client.getStatusCode().isError()) {
            throw new EntityOperationException(
                    "Client not found",
                    MessageFormat.format("No such client with id {0}", clientId),
                    HttpStatus.NOT_FOUND
            );
        }
        client.getBody();
    }

    private void getDeviceById(Long deviceId) {
        var device = deviceClient.getDeviceById(deviceId);
        if (device.getStatusCode().isError()) {
            throw new EntityOperationException(
                    "Device not found",
                    MessageFormat.format("No such device with id {0}", deviceId),
                    HttpStatus.NOT_FOUND
            );
        }
        device.getBody();
    }

    private void getGameCopies(Long deviceUnitId, List<Long> gameCopiesId) {
        var gameCopies = gameCopyClient.getGameCopies(deviceUnitId);
        if (gameCopies.getStatusCode().isError()) {
            throw new EntityOperationException(
                    "Game copies not found",
                    MessageFormat.format("No such game copies for device unit with id {0}", deviceUnitId),
                    HttpStatus.NOT_FOUND
            );
        }
        var gameCopiesList = gameCopies.getBody();
        for (var gameCopyId : gameCopiesId) {
            if (gameCopiesList.stream().noneMatch(gameCopy -> gameCopy.getGameCopyId().equals(gameCopyId))) {
                throw new EntityOperationException(
                        "Game copy not found",
                        MessageFormat.format("No such game copy with id {0}", gameCopyId),
                        HttpStatus.NOT_FOUND
                );
            }
        }
    }

    @Override
    @CircuitBreaker(name = "rentalService", fallbackMethod = "getRentalsFallback")
    public PageableResponseDTO<RentalResponseDTO> getRentals(Long clientId, Long deviceId, Boolean returned, boolean pastDue, Integer page, SortDirection sort) {
        var pageRequest = PageRequest.of(page != null ? page - 1 : 0, PAGE_SIZE);
        if (clientId != null) getClientById(clientId);
        if (deviceId != null) getDeviceById(deviceId);
        pageRequest = sort != null ? pageRequest.withSort(sort.by("rentalReturnDate")): pageRequest;
        var rentals = repository.getRentals(clientId, deviceId, returned, pastDue, pageRequest);
        return new PageableResponseDTO<>(rentals.getTotalPages(), rentals.map(rental -> {
            var returnLink = rental.getRentalReturnDate() == null ? linkTo(methodOn(RentalController.class).returnRental(rental.getRentalId())).withRel("return") : null;
            return returnLink != null ? rental.toResponseDTO().add(returnLink) : rental.toResponseDTO();
        }).toList());
    }

    public PageableResponseDTO<RentalResponseDTO> getRentalsFallback(Long clientId, Long deviceId, Boolean returned, boolean pastDue, Integer page, SortDirection sort, Throwable throwable) {
        return new PageableResponseDTO<>(0, List.of());
    }

    @Override
    public RentalResponseDTO createRental(RentalRequestDTO rental) {
        getClientById(rental.getClientId());
        getDeviceById(rental.getDeviceUnitId());
        getGameCopies(rental.getDeviceUnitId(), rental.getGameCopiesId());
        if (!repository.isDeviceAvailable(rental.getDeviceUnitId())) {
            throw new EntityOperationException(
                "Device unit not available",
                "",
                HttpStatus.BAD_REQUEST
            );
        }
        var conflictCopies = repository.getUnavailableGameCopyIds(rental.getDeviceUnitId(), rental.getGameCopiesId());
        if (!conflictCopies.isEmpty()) {
            throw new EntityOperationException(
                "Invalid game copies",
                "Copies may not exist or be unavailable",
                HttpStatus.BAD_REQUEST
            );
        }

        var rentalEntity = new Rental(
                rental.getClientId(),
                rental.getDeviceUnitId(),
                LocalDateTime.now().plusDays(rental.getNumberOfDays())
        );
        rentalEntity.setRentalGameCopies(
            rental.getGameCopiesId().stream().map(gameCopyId -> new RentalGameCopy(rentalEntity, gameCopyId)).toList()
        );

        var savedModel = repository.save(rentalEntity);
        var returnLink = linkTo(methodOn(RentalController.class).returnRental(savedModel.getRentalId())).withRel("return");
        return savedModel.toResponseDTO().add(returnLink);
    }

    @Override
    @Transactional
    public RentalResponseDTO rentalReturned(Long id) {
        var rental = repository.findById(id).orElseThrow(() ->
                new EntityOperationException(
                        "Rental not found",
                        "",
                        HttpStatus.NOT_FOUND
                )
        );
        if (rental.getRentalReturnDate() != null) {
            throw new EntityOperationException(
                    "Rental already returned",
                    MessageFormat.format("Was returned on {0}",rental.getRentalReturnDate().toLocalDate()),
                    HttpStatus.BAD_REQUEST
            );
        }
        rental.setRentalReturnDate(LocalDateTime.now());
        return rental.toResponseDTO();
    }
}
