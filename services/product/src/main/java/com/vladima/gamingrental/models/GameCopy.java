package com.vladima.gamingrental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(nullable = false)
    private boolean isAvailable = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game gameBase;

    @ManyToOne
    @JoinColumn(name = "device_base_id")
    private DeviceBase gameDevice;


    public GameCopy(Game gameBase, DeviceBase gameDevice, boolean isAvailable) {
        this.isAvailable = isAvailable;
        this.gameBase = gameBase;
        this.gameDevice = gameDevice;
    }
}
