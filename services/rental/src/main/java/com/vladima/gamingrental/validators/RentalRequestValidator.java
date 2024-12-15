package com.vladima.gamingrental.validators;

import com.vladima.gamingrental.dtos.RentalRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RentalRequestValidator implements ConstraintValidator<ValidateRentalRequest, RentalRequestDTO> {

    @Override
    public boolean isValid(RentalRequestDTO rentalDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (rentalDTO == null) {
            throw new IllegalArgumentException("Invalid rentalDTO");
        }
        return rentalDTO.getDeviceUnitId() != null || !rentalDTO.getGameCopiesId().isEmpty();
    }
}
