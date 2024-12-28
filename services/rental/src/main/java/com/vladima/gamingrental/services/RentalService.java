package com.vladima.gamingrental.services;

import com.vladima.gamingrental.dtos.*;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;

public interface RentalService {
    PageableResponseDTO<RentalResponseDTO> getRentals(Long clientId, Long deviceId, Boolean returned, boolean pastDue, Integer page, SortDirection sort);
    RentalResponseDTO createRental(RentalRequestDTO rental);
    RentalResponseDTO rentalReturned(Long id);
}
