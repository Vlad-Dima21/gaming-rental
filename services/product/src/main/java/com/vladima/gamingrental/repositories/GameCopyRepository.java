package com.vladima.gamingrental.repositories;

import com.vladima.gamingrental.models.GameCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameCopyRepository extends JpaRepository<GameCopy, Long> {
    @Query("SELECT gc FROM GameCopy gc " +
            "WHERE COALESCE((gc.gameBase.gameId = :gameId), true)" +
            "AND COALESCE((gc.gameDevice.deviceBaseId = :deviceId), true)")
    List<GameCopy> findCopies(Long gameId, Long deviceId);
}
