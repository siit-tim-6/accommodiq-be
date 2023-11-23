package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Review;

import java.util.Collection;

public interface IReviewService {

    Collection<Review> getAll();

    Review findReview(Long reviewId);

    Review insert(Review review);

    Review update(Review review);

    Review delete(Long reviewId);

    void deleteAll();

}
