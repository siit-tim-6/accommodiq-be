package com.example.accommodiq.services;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.services.interfaces.IReviewService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ReviewServiceImpl implements IReviewService {

    @Override
    public Collection<Review> getAll() {
        return null;
    }

    @Override
    public Review findReview(Long reviewId) {
        return null;
    }

    @Override
    public Review insert(Review review) {
        return null;
    }

    @Override
    public Review update(Review review) {
        return null;
    }

    @Override
    public Review delete(Long reviewId) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
