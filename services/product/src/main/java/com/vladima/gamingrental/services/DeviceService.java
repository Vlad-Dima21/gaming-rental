package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.DeviceResponseDTO;

import java.util.List;

public interface DeviceService {
    DeviceResponseDTO getById(Long id);
    List<DeviceResponseDTO> getByDeviceBaseId(Long id);
}
