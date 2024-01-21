package com.example.accommodiq.services.impl.feedback;

import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.repositories.ReviewRepository;
import com.example.accommodiq.services.interfaces.feedback.IReviewService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.example.accommodiq.utilities.ErrorUtils.generateNotFound;

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
            throw generateNotFound("reviewNotFound");
        }
        return found.get();
    }

    @Override
    public Review insert(Review review) {
        try {
            allReviews.save(review);
            allReviews.flush();
            return review;
        } catch (ConstraintViolationException ex) {
            throw generateNotFound("reviewInsertFailed");
        }
    }

    @Override
    public Review update(Review review) {
        try {
            findReview(review.getId());
            allReviews.save(review);
            allReviews.flush();
            return review;
        } catch (ConstraintViolationException ex) {
            throw generateNotFound("reviewUpdateFailed");
        }
    }

    @Override
    public MessageDto delete(Long reviewId) {
        Review review = findReview(reviewId);
        allReviews.deleteFromAccommodationReviews(reviewId);
        allReviews.deleteFromHostReviews(reviewId);
        allReviews.delete(review);
        allReviews.flush();
        return new MessageDto("Review successfully deleted.");
    }

    @Override
    public void deleteAll() {
        allReviews.deleteAll();
        allReviews.flush();
    }

    @Override
    public MessageDto setReviewStatus(Long reviewId, ReviewStatus reviewStatus) {
        Review review = findReview(reviewId);
        review.setStatus(reviewStatus);
        allReviews.save(review);
        allReviews.flush();
        return new MessageDto("Review status updated successfully");
    }

    @Override
    public void deleteByGuestId(Long id) {
        List<Review> reviews = allReviews.findByGuestId(id);
        for (Review review : reviews) {
            allReviews.deleteFromAccommodationReviews(review.getId());
            allReviews.deleteFromHostReviews(review.getId());
        }
        allReviews.deleteByGuestId(id);
        allReviews.flush();
    }

    @Override
    public Collection<Review> findAllByGuestId(Long guestId) {
        return allReviews.findByGuestId(guestId);
    }

    @Override
    public Collection<ReviewDto> getPendingReviews() {
        return allReviews.findByStatus(ReviewStatus.PENDING).stream().map(ReviewDto::new).toList();
    }
}
