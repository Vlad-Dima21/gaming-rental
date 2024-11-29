package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.ClientDTO;
import com.vladima.gamingrental.dtos.ClientResponseDTO;

public interface ClientService {
    ClientResponseDTO getById(Long id);
    ClientResponseDTO create(ClientDTO clientDTO);
    void delete(Long id);
}
