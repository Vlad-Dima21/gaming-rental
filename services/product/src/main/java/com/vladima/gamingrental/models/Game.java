package com.vladima.gamingrental.models;

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
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column(nullable = false, unique = true)
    private String gameName;

    @Column(nullable = false)
    private String gameGenre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameBase", orphanRemoval = true)
    private List<GameCopy> gameCopies;

    public Game(String gameName, String gameGenre) {
        this.gameName = gameName;
        this.gameGenre = gameGenre;
    }

    public Game(String gameName, String gameGenre, List<GameCopy> gameCopies) {
        this.gameName = gameName;
        this.gameGenre = gameGenre;
        this.gameCopies = gameCopies;
    }

}
