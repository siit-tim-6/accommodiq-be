package com.example.accommodiq.services.interfaces.feedback;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.enums.ReviewStatus;

import java.util.Collection;
import java.util.Set;

public interface IReviewService {

    Collection<Review> getAll();

    Review findReview(Long reviewId);

    Review insert(Review review);

    Review update(Review review);

    MessageDto delete(Long reviewId);

    void deleteAll();

    MessageDto setReviewStatus(Long reviewId, ReviewStatus reviewStatusDto);

    Collection<Review> getAccommodationReviews(Long accommodationId);

    void deleteByGuestId(Long id);

    Collection<Review> findAllByGuestId(Long guestId);

    Set<Review> findReviewsByGuestIdAndHostId(Long guestId, Long hostId);
}
