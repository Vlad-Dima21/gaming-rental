package com.vladima.gamingrental.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalResponseDTO {
    private Long rentalId;
    private LocalDateTime rentalDueDate;
    private LocalDateTime rentalReturnDate;
    private ClientDTO rentalClient;
    private DeviceDTO rentalDevice;
    private List<GameCopyDTO> rentalGames;
}

//https://youtu.be/jdeSV0GRvwI?t=10802