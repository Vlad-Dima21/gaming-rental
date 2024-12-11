package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.DeviceBaseResponseDTO;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;

import java.util.List;

public interface DeviceBaseService {
    public DeviceBaseResponseDTO getById(Long id);

    public PageableResponseDTO<DeviceBaseResponseDTO> getFiltered(String name, String producer, Integer year, boolean ifAvailable, int page, SortDirection sort);

    List<DeviceBaseResponseDTO> getAll();
}
