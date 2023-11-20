package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
     Collection<Reservation> findByUserId(Long userId);
     Collection<Reservation> findByApartmentId(Long apartmentId);
}
