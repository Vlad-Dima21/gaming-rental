package com.vladima.gamingrental.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceBaseResponseDTO extends DeviceBaseDTO {

    private Long deviceBaseId;
    private String deviceBaseImageUrl;
    private int noOfUnitsAvailable;

    private List<DeviceResponseDTO> deviceBaseUnits;

    public DeviceBaseResponseDTO(Long deviceBaseId, String deviceBaseName, String deviceBaseProducer, int deviceBaseYearOfRelease, String deviceBaseImageUrl, int noOfUnitsAvailable, List<DeviceResponseDTO> deviceBaseUnits) {
        super(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
        this.deviceBaseId = deviceBaseId;
        this.deviceBaseImageUrl = deviceBaseImageUrl;
        this.noOfUnitsAvailable = noOfUnitsAvailable;
        this.deviceBaseUnits = deviceBaseUnits;
    }
}
