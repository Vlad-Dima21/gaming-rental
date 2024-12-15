package com.vladima.gamingrental.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class RentalGameCopyId implements Serializable {
    private Long rentalId;
    private Long gameCopyId;
}
