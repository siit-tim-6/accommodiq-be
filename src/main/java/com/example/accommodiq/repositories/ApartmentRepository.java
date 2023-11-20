package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findByOwnerId(Long ownerId);
}
