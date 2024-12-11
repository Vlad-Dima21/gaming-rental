package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.DeviceBaseResponseDTO;
import com.vladima.gamingrental.exceptions.EntityOperationException;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import com.vladima.gamingrental.models.DeviceBase;
import com.vladima.gamingrental.repositories.DeviceBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeviceBaseServiceImpl implements DeviceBaseService {
    private final DeviceBaseRepository repository;
    private static final int PAGE_SIZE = 3;

    @Override
    public DeviceBaseResponseDTO getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityOperationException(
                        "Device not found",
                        "Error fetching device with id " + id,
                        HttpStatus.NOT_FOUND
                )).toResponse();
    }

    @Override
    public PageableResponseDTO<DeviceBaseResponseDTO> getFiltered(String name, String producer, Integer year, boolean ifAvailable, int page, SortDirection sort) {
        var pageRequest = PageRequest.of(page - 1, PAGE_SIZE, sort.by("deviceBaseName"));
        var result = repository.findFiltered(name, producer, year, ifAvailable, pageRequest);
        return new PageableResponseDTO<>(result.getTotalPages(), result.stream().map(DeviceBase::toResponse).toList());
    }

    @Override
    public List<DeviceBaseResponseDTO> getAll() {
        return repository.findAll().stream().map(DeviceBase::toResponse).toList();
    }
}
