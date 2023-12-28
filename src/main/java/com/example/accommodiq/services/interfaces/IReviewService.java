package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewStatusDto;

import java.util.Collection;

public interface IReviewService {

    Collection<Review> getAll();

    Review findReview(Long reviewId);

    Review insert(Long hostId, Review review);

    Review update(Review review);

    MessageDto delete(Long reviewId);

    void deleteAll();

    Review setReviewStatus(Long reviewId, ReviewStatusDto reviewStatusDto);

    Collection<Review> getHostReviews(Long hostId);

    Collection<Review> getAccommodationReviews(Long accommodationId);

    void deleteByGuestId(Long id);
}
