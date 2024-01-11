package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.AccommodationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, JpaSpecificationExecutor<Accommodation> {
    Collection<Accommodation> findByHostId(Long hostId);
    Collection<Accommodation> findAllByStatus(AccommodationStatus accommodationStatus);
    @Transactional
    void deleteAllByHostId(Long accountId);

    @Query(value = "SELECT * FROM accommodation WHERE id IN (SELECT accommodation_id FROM accommodation_reviews WHERE reviews_id IN :reviewIds)", nativeQuery = true)
    Collection<Accommodation> findAccommodationsContainingReviews(@Param("reviewIds") Collection<Long> reviewIds);

    Accommodation findAccommodationByReviewsContaining(Review review);
}
