package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
     Collection<Reservation> findByUserId(Long userId);
     Collection<Reservation> findByAccommodationId(Long accommodationId);
     @Transactional
     void deleteByAccommodationId(Long accommodationId);

     @Transactional
     void deleteByUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.accommodation.id = :accommodationId " +
            "AND r.startDate < :availabilityEnd AND r.endDate > :availabilityStart")
    Long countOverlappingReservations(@Param("accommodationId") Long accommodationId,
                                      @Param("availabilityStart") Long availabilityStart,
                                      @Param("availabilityEnd") Long availabilityEnd);

}
