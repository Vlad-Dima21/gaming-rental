package com.vladima.gamingrental.models;

import com.vladima.gamingrental.dtos.DeviceResponseDTO;
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
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @Column(nullable = false)
    private int deviceNumberOfControllers;

    @Column(nullable = false, name = "device_is_available")
    private boolean deviceAvailable = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_base_id")
    private DeviceBase deviceBase;

    public DeviceResponseDTO toResponse() {
        return new DeviceResponseDTO(
                deviceId,
                deviceBase.getDeviceBaseId(),
                deviceNumberOfControllers,
                deviceAvailable
        );
    }
}
