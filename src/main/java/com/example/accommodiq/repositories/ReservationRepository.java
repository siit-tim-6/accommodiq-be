package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Collection<Reservation> findByUserId(Long userId);

    Collection<Reservation> findByAccommodationId(Long accommodationId);

    @Transactional
    void deleteByAccommodationId(Long accommodationId);

    @Transactional
    void deleteByUserId(Long userId);

    List<Reservation> findByStatusAndUserIdAndEndDateGreaterThanOrderByStartDateDesc(ReservationStatus status, Long userId, Long endDate);

    List<Reservation> findByStatusAndAccommodation_HostIdAndEndDateGreaterThanOrderByStartDateDesc(ReservationStatus status, Long hostId, Long endDate);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.accommodation.id = :accommodationId " +
            "AND r.startDate < :availabilityEnd AND r.endDate > :availabilityStart")
    Long countOverlappingReservations(@Param("accommodationId") Long accommodationId,
                                      @Param("availabilityStart") Long availabilityStart,
                                      @Param("availabilityEnd") Long availabilityEnd);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.accommodation.id IN :accommodationIds AND r.status <> :status AND r.endDate < :currentTime")
    Collection<Reservation> findByUserIdAndAccommodationIdInAndStatusNotAndEndDateBefore(
            @Param("userId") Long userId,
            @Param("accommodationIds") List<Long> accommodationIds,
            @Param("status") ReservationStatus status,
            @Param("currentTime") Long currentTime
    );
}
