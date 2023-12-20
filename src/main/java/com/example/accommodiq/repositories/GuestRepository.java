package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
