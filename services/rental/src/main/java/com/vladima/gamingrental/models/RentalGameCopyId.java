package com.vladima.gamingrental.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RentalGameCopyId implements Serializable {
    private Long rentalId;
    @Getter
    private Long gameCopyId;
}
