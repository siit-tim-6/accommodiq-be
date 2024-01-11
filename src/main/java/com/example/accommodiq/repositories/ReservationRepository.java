package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    Collection<Reservation> findByGuestId(Long guestId);

    Collection<Reservation> findByAccommodationId(Long accommodationId);

    @Transactional
    void deleteByAccommodationId(Long accommodationId);

    @Transactional
    void deleteByGuestId(Long guestId);

    List<Reservation> findByStatusAndGuestIdAndEndDateGreaterThanOrderByStartDateDesc(ReservationStatus status, Long guestId, Long endDate);

    List<Reservation> findByStatusAndAccommodation_HostIdAndEndDateGreaterThanOrderByStartDateDesc(ReservationStatus status, Long hostId, Long endDate);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.accommodation.id = :accommodationId " +
            "AND r.startDate < :availabilityEnd AND r.endDate > :availabilityStart")
    Long countOverlappingReservations(@Param("accommodationId") Long accommodationId,
                                      @Param("availabilityStart") Long availabilityStart,
                                      @Param("availabilityEnd") Long availabilityEnd);

    Collection<Reservation> findByGuestIdAndAccommodationIdAndStatusNotInAndEndDateGreaterThanAndEndDateLessThan(
            Long guestId,
            Long accommodationId,
            Collection<ReservationStatus> excludedStatuses,
            Long endDateAfter,
            Long endDateBefore);

    Collection<Reservation> findByGuestIdAndAccommodationIdInAndStatusNotAndEndDateLessThan(Long guestId, Collection<Long> accommodationIds, ReservationStatus status, Long endDate);

    Collection<Reservation> findByGuestIdAndAccommodationIdAndStatusNotInAndEndDateLessThan(Long guestId, Long accommodationId, List<ReservationStatus> list, long currentTime);

    Collection<Reservation> getReservationByAccommodationIdAndStartDateBetweenOrEndDateBetweenAndStatus(Long accommodation_id, Long startDate, Long startDate2, Long endDate, Long endDate2, ReservationStatus status);
}
