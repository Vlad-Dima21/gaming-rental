package com.vladima.gamingrental.repositories;

import com.vladima.gamingrental.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d FROM Device d JOIN DeviceBase db ON db = d.deviceBase WHERE db.deviceBaseId = :id")
    List<Device> findByDeviceBaseId(Long id);
}
