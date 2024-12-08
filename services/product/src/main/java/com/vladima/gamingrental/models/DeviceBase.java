package com.vladima.gamingrental.models;

import com.vladima.gamingrental.dtos.DeviceBaseResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class DeviceBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceBaseId;

    @Column(nullable = false, unique = true)
    private String deviceBaseName;

    @Column(nullable = false)
    private String deviceBaseProducer;

    private int deviceBaseYearOfRelease;

    @Column(length = 300)
    private String deviceBaseImageUrl;

    @OneToMany(mappedBy = "deviceBase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    public DeviceBaseResponseDTO toResponse() {
        return new DeviceBaseResponseDTO(
                deviceBaseId,
                deviceBaseName,
                deviceBaseProducer,
                deviceBaseYearOfRelease,
                deviceBaseImageUrl,
                devices.size(),
                devices.stream().map(Device::toResponse).toList());
    }
}
