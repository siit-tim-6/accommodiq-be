package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.ReviewStatus;

import java.util.Collection;

public interface IReviewService {

    Collection<Review> getAll();

    Review findReview(Long reviewId);

    Review insert(Review review);

    Review update(Review review);

    Review delete(Long reviewId);

    void deleteAll();

    void setReviewStatus(Long reviewId, ReviewStatus reviewStatus);

    void addReview(Long hostId, Review review);

    Collection<Review> getHostReviews(Long hostId);

    Collection<Review> getAccommodationReviews(Long accommodationId);
}
