package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    @Query(value = "SELECT * FROM review WHERE guest_id = :guestId AND id IN (SELECT reviews_id FROM host_reviews WHERE host_id = :hostId)", nativeQuery = true)
    Set<Review> findReviewsByGuestIdAndHostId(@Param("guestId") Long guestId, @Param("hostId") Long hostId);

    @Query(value = "SELECT * FROM review WHERE guest_id = :guestId AND id IN (SELECT reviews_id FROM accommodation_reviews WHERE accommodation_id = :accommodationId)", nativeQuery = true)
    Set<Review> findReviewsByGuestIdAndAccommodationId(@Param("guestId") Long guestId,@Param("accommodationId") Long accommodationId);

    List<Review> findByStatus(@Param("reviewStatus") ReviewStatus reviewStatus);
}
