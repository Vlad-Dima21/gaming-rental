package com.vladima.gamingrental.models;

import com.vladima.gamingrental.dtos.GameCopyResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class GameCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameCopyId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game gameBase;

    @ManyToOne
    @JoinColumn(name = "device_base_id")
    private DeviceBase gameDevice;

    public GameCopyResponseDTO toResponse() {
        return new GameCopyResponseDTO(gameCopyId, gameBase.toDTO(), gameDevice.toResponse());
    }
}
