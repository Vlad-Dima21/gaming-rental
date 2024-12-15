package com.vladima.gamingrental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class RentalGameCopy {
    @EmbeddedId
    private RentalGameCopyId rentalGameCopyId;

    @MapsId("rentalId")
    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;
}
