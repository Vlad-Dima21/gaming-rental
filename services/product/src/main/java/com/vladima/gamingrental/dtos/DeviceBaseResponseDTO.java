package com.vladima.gamingrental.dtos;

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

    private String deviceBaseImageUrl;
    private int noOfUnitsAvailable;

    private List<DeviceResponseDTO> deviceBaseUnits;

    public DeviceBaseResponseDTO(Long deviceBaseId, String deviceBaseName, String deviceBaseProducer, int deviceBaseYearOfRelease, String deviceBaseImageUrl, int noOfUnitsAvailable, List<DeviceResponseDTO> deviceBaseUnits) {
        super(deviceBaseId, deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
        this.deviceBaseImageUrl = deviceBaseImageUrl;
        this.noOfUnitsAvailable = noOfUnitsAvailable;
        this.deviceBaseUnits = deviceBaseUnits;
    }
}
