package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
}
