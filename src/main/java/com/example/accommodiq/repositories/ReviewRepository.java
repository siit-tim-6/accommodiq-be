package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Transactional
    void deleteByGuestId(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from accommodation_reviews where reviews_id = ?1", nativeQuery = true)
    void deleteFromAccommodationReviews(@Param("reviews_id") Long reviewsId);

    @Transactional
    @Modifying
    @Query(value = "delete from host_reviews where reviews_id = ?1", nativeQuery = true)
    void deleteFromHostReviews(@Param("reviews_id") Long reviewsId);

    List<Review> findByGuestId(Long guestId);

}
