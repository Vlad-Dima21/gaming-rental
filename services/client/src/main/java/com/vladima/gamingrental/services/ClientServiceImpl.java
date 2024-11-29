package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.ClientDTO;
import com.vladima.gamingrental.dtos.ClientResponseDTO;
import com.vladima.gamingrental.exceptions.EntityOperationException;
import com.vladima.gamingrental.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;

    @Override
    public ClientResponseDTO getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityOperationException(
                "Client not found",
                "Error fetching client with id " + id,
                HttpStatus.NOT_FOUND
            )
        ).toResponse();
    }

    @Override
    public ClientResponseDTO create(ClientDTO clientDTO) {
        var existingClientEmail = repository.findByClientEmail(clientDTO.getClientEmail());
        var existingClientPhone = repository.findByClientPhone(clientDTO.getClientPhone());
        if (existingClientEmail != null) {
            throw new EntityOperationException(
                    "Client not registered",
                    "Email is already in use",
                    HttpStatus.CONFLICT,
                    "clientEmail"
            );
        }
        if (existingClientPhone != null) {
            throw new EntityOperationException(
                    "Client not registered",
                    "Phone is already in use",
                    HttpStatus.CONFLICT,
                    "clientPhone"
            );
        }
        return repository.save(clientDTO.toModel()).toResponse();
    }

    @Override
    public void delete(Long id) {
        var client = repository.findById(id).orElseThrow(() ->
                new EntityOperationException(
                        "Client not found",
                        "Error fetching client with id " + id,
                        HttpStatus.NOT_FOUND
                )
        );
        repository.deleteById(id);
    }
}
