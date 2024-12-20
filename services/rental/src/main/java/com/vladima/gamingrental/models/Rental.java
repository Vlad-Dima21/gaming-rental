package com.vladima.gamingrental.models;

import com.vladima.gamingrental.dtos.RentalResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @Column
    private LocalDateTime rentalDueDate;

    @Column
    private LocalDateTime rentalReturnDate;

    private Long rentalClientId;

    private Long rentalDeviceId;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalGameCopy> rentalGameCopies;

    public Rental(Long rentalClientId, Long rentalDeviceId, LocalDateTime rentalDueDate) {
        this.rentalClientId = rentalClientId;
        this.rentalDeviceId = rentalDeviceId;
        this.rentalDueDate = rentalDueDate;
        this.rentalGameCopies = rentalGameCopies;
    }

    public RentalResponseDTO toResponseDTO() {
        return new RentalResponseDTO(rentalId, rentalDueDate, rentalReturnDate, rentalClientId, rentalDeviceId, rentalGameCopies.stream().map(RentalGameCopy::getGameCopyId).toList());
    }
}
