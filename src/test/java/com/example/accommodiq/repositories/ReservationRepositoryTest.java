package com.example.accommodiq.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.testng.Assert.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/not-overlapping-reservations.sql"})
    public void shouldNotFindOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservations(1L, 100L, 200L);

        assertEquals(count, 0L);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/default-overlapping-reservations.sql"})
    public void shouldFindOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservations(1L, 100L, 200L);

        assertEquals(count, 3L);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/wrapping-reservation.sql"})
    public void shouldFindWrappingReservation() {
        Long count = reservationRepository.countOverlappingReservations(1L, 100L, 200L);

        assertEquals(count, 1L);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/default-overlapping-reservations.sql", "classpath:data/reservations/not-overlapping-reservations.sql", "classpath:data/reservations/wrapping-reservation.sql"})
    public void shouldFindAllOverlappingReservations() {
        Long count = reservationRepository.countOverlappingReservations(1L, 100L, 200L);

        assertEquals(count, 4L);
    }

    @Test
    @Sql({"classpath:data/reservations/base.sql", "classpath:data/reservations/not-accepted-reservations.sql"})
    public void shouldNotFindAcceptedReservations() {
        Long count = reservationRepository.countOverlappingReservations(1L, 100L, 200L);

        assertEquals(count, 0L);
    }
}
