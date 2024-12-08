package com.vladima.gamingrental.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameCopyResponseDTO {
    private GameDTO gameBase;
    private DeviceBaseResponseDTO gameDevice;
}
