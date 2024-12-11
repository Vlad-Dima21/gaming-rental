package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.DeviceResponseDTO;

import java.util.List;

public interface DeviceService {
    List<DeviceResponseDTO> getByDeviceBaseId(Long id);
}
