package com.vladima.gamingrental.repositories;

import com.vladima.gamingrental.models.DeviceBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceBaseRepository extends JpaRepository<DeviceBase, Long> {

    @Query("SELECT db FROM DeviceBase db " +
            "WHERE db.deviceBaseName ILIKE CONCAT('%', COALESCE(:name, '') , '%')" +
            "AND db.deviceBaseProducer ILIKE CONCAT('%', COALESCE(:producer, '') , '%')" +
            "AND COALESCE((db.deviceBaseYearOfRelease = :year), true) " +
            "AND (:ifAvailable = false OR EXISTS (SELECT dbv FROM db.devices dbv WHERE dbv.deviceAvailable ))")
    Page<DeviceBase> findFiltered(
            String name, String producer, Integer year, boolean ifAvailable, PageRequest pageRequest
    );
}
