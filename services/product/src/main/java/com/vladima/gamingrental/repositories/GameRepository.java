package com.vladima.gamingrental.repositories;

import com.vladima.gamingrental.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
