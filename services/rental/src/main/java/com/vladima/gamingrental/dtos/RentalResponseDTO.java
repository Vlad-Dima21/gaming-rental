package com.vladima.gamingrental.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalResponseDTO extends RepresentationModel<RentalResponseDTO> {
    private Long rentalId;
    private LocalDateTime rentalDueDate;
    private LocalDateTime rentalReturnDate;
    private Long rentalClientId;
    private Long rentalDeviceId;
    private List<Long> rentalGameCopyIds;
}

//https://youtu.be/jdeSV0GRvwI?t=10802