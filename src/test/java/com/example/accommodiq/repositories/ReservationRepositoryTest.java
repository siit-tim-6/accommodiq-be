package com.example.accommodiq.repositories;

import com.example.accommodiq.enums.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    private final List<ReservationStatus> acceptedStatusOnlyList = List.of(ReservationStatus.ACCEPTED);


    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/not-overlapping-reservations.sql"})
    public void shouldNotFindOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(0, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/default-overlapping-reservations.sql"})
    public void shouldFindOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(3L, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/wrapping-reservation.sql"})
    public void shouldFindWrappingReservation() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(1L, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/default-overlapping-reservations.sql", "classpath:data/reservations/not-overlapping-reservations.sql", "classpath:data/reservations/wrapping-reservation.sql"})
    public void shouldFindAllOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(4L, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/not-accepted-reservations.sql"})
    public void shouldNotFindAcceptedReservations() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(0L, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/not-accepted-reservations.sql"})
    public void shouldFindOneOverlappingReservation() {
        List<ReservationStatus> statuses = List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING);
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, 1L, 100L, 200L, statuses);

        assertEquals(1L, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/default-overlapping-reservations.sql"})
    public void shouldFindNoneForWrongGuest() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(2L, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(0L, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/edge-not-overlapping-reservations.sql"})
    public void shouldFindNoneForEdgeNotOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(null, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(0L, count);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/different-accommodations-overlapping-reservations.sql"})
    public void shouldFindNoneForDifferentAccommodationsOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservationsOrGuestOverlappingReservations(3L, 1L, 100L, 200L, acceptedStatusOnlyList);

        assertEquals(0L, count);
    }
}
