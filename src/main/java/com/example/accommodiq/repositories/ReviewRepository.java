package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
