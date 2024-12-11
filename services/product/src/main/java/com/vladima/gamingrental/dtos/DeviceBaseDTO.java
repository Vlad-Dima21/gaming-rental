package com.vladima.gamingrental.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceBaseDTO {
    private Long deviceBaseId;
    private String deviceBaseName;
    private String deviceBaseProducer;
    private int deviceBaseYearOfRelease;
}
