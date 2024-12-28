package com.vladima.gamingrental.repositories;

import com.vladima.gamingrental.models.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r " +
            "WHERE COALESCE((r.rentalClientId = :clientId), true)" +
            "AND COALESCE(((r.rentalDeviceId = :deviceId)), true)" +
            "AND CASE WHEN :returned = true THEN (r.rentalReturnDate IS NOT NULL) WHEN :returned = false THEN (r.rentalReturnDate IS NULL) ELSE true END " +
            "AND (:pastDue = false OR r.rentalReturnDate > r.rentalDueDate)")
    Page<Rental> getRentals(
            Long clientId, Long deviceId, Boolean returned, boolean pastDue, PageRequest pageRequest
    );

    @Query("""
            SELECT rgc.rentalGameCopyId.gameCopyId \
            FROM Rental r \
               JOIN RentalGameCopy rgc ON r.rentalId = rgc.rentalGameCopyId.rentalId \
            WHERE rgc.rentalGameCopyId.gameCopyId IN :gameCopyIds \
                AND r.rentalDeviceId = :deviceId \
                AND r.rentalReturnDate IS NULL""")
//    @Query("SELECT UNNEST(rgc.copies.rentalGameCopyId.gameCopyId) " +
//            "FROM (" +
//            "   SELECT r.rentalGameCopies AS copies FROM Rental r WHERE r.rentalReturnDate IS NULL AND r.rentalDeviceId = :deviceId" +
//            ") rgc " +
//            "WHERE rgc.copies.rentalGameCopyId.gameCopyId IN :gameCopyIds")
    List<Long> getUnavailableGameCopyIds(Long deviceId, List<Long> gameCopyIds);

    @Query("SELECT NOT EXISTS(SELECT r.rentalDeviceId FROM Rental r WHERE r.rentalDeviceId = :deviceId AND r.rentalReturnDate IS NULL)")
    Boolean isDeviceAvailable(Long deviceId);
}
