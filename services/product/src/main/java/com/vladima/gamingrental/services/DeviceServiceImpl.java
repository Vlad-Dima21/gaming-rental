package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.DeviceResponseDTO;
import com.vladima.gamingrental.exceptions.EntityOperationException;
import com.vladima.gamingrental.models.Device;
import com.vladima.gamingrental.repositories.DeviceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository repository;

    @Override
    public DeviceResponseDTO getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityOperationException(
                "Device not found",
                MessageFormat.format("No such device as {0}", id),
                HttpStatus.NOT_FOUND
        )).toResponse();
    }

    @Override
    public List<DeviceResponseDTO> getByDeviceBaseId(Long id) {
        return repository.findByDeviceBaseId(id)
                .stream()
                .map(Device::toResponse)
                .toList();
    }
}
