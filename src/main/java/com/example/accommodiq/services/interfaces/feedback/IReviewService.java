package com.example.accommodiq.services.interfaces.feedback;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.dtos.ReviewStatusDto;

import java.util.Collection;
import java.util.List;

public interface IReviewService {

    Collection<Review> getAll();

    Review findReview(Long reviewId);

    Review insert(Long hostId, Review review);

    Review update(Review review);

    MessageDto delete(Long reviewId);

    void deleteAll();

    MessageDto setReviewStatus(Long reviewId, ReviewStatusDto reviewStatusDto);

    Collection<ReviewDto> getHostReviews(Long hostId, Long loggedInId);

    Collection<Review> getAccommodationReviews(Long accommodationId);

    void deleteByGuestId(Long id);

    Collection<Review> findAllByGuestId(Long guestId);
}
