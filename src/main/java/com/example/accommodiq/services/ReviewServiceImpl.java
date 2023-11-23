package com.example.accommodiq.services;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.repositories.ReviewRepository;
import com.example.accommodiq.services.interfaces.IReviewService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static com.example.accommodiq.utilities.ReportUtils.throwNotFound;

@Service
public class ReviewServiceImpl implements IReviewService {

    final ReviewRepository allReviews;

    @Autowired
    public ReviewServiceImpl(ReviewRepository allReviews) {
        this.allReviews = allReviews;
    }

    @Override
    public Collection<Review> getAll() {
        return allReviews.findAll();
    }

    @Override
    public Review findReview(Long reviewId) {
        Optional<Review> found = allReviews.findById(reviewId);
        if (found.isEmpty()) {
            throwNotFound("reviewNotFound");
        }
        return found.get();
    }

    @Override
    public Review insert(Review review) {
        try{
            allReviews.save(review);
            allReviews.flush();
            return review;
        } catch (Exception ex) {
            throwNotFound("reviewInsertFailed");
        }
        return review;
    }

    @Override
    public Review update(Review review) {
        try{
            findReview(review.getId());
            allReviews.save(review);
            allReviews.flush();
            return review;
        } catch (ConstraintViolationException ex) {
            throwNotFound("reviewUpdateFailed");
        }
        return review;
    }

    @Override
    public Review delete(Long reviewId) {
           Review review = findReview(reviewId);
            allReviews.delete(review);
            allReviews.flush();
            return review;
    }

    @Override
    public void deleteAll() {
        allReviews.deleteAll();
        allReviews.flush();
    }

    @Override
    public void setReviewStatus(Long reviewId, ReviewStatus reviewStatus) {
        Review review = findReview(reviewId);
        review.setStatus(reviewStatus);
        allReviews.save(review);
        allReviews.flush();
    }

    @Override
    public void addReview(Long hostId, Review review) {

    }

    @Override
    public Collection<Review> getHostReviews(Long hostId) {
        return null;
    }

    @Override
    public Collection<Review> getAccommodationReviews(Long accommodationId) {
        return null;
    }
}
