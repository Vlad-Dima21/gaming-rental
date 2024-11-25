package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.ClientDTO;

public interface ClientService {
    ClientDTO getById(Long id);
    ClientDTO create(ClientDTO clientDTO);
    void delete(Long id);
}
