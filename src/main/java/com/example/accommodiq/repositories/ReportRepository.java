package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
