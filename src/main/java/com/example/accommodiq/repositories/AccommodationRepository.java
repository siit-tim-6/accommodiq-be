package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.AccommodationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, JpaSpecificationExecutor<Accommodation> {
    Collection<Accommodation> findByHostId(Long hostId);
    Collection<Accommodation> findAllByStatus(AccommodationStatus accommodationStatus);
    @Transactional
    void deleteAllByHostId(Long accountId);
    Collection<Accommodation> findByReviews(Collection<Review> reviews);
}
