package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Transactional
    void deleteByGuestId(Long id);
}
