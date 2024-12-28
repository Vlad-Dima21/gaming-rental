package com.vladima.gamingrental.dtos;

import com.vladima.gamingrental.validators.ValidateRentalRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@ValidateRentalRequest
public class RentalRequestDTO {

    @NotNull
    private Long clientId;

    @NotNull
    private Long deviceUnitId;

    private List<Long> gameCopiesId = new ArrayList<>();

    @NotNull
    @Range(min = 30, max = 90, message = "Products can be rented for one to three months")
    private Long numberOfDays;
}
