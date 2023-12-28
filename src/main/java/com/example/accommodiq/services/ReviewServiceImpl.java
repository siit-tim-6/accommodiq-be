package com.example.accommodiq.services;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewStatusDto;
import com.example.accommodiq.enums.ReviewStatus;
import com.example.accommodiq.repositories.ReviewRepository;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import com.example.accommodiq.services.interfaces.IHostService;
import com.example.accommodiq.services.interfaces.IReviewService;
import com.example.accommodiq.utilities.ErrorUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.accommodiq.utilities.ErrorUtils.generateNotFound;

@Service
public class ReviewServiceImpl implements IReviewService {

    final ReviewRepository allReviews;
    final IHostService hostService;
    final IAccommodationService accommodationService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository allReviews, IHostService hostService, IAccommodationService accommodationService) {
        this.allReviews = allReviews;
        this.hostService = hostService;
        this.accommodationService = accommodationService;
    }

    @Override
    public Collection<Review> getAll() {
        return allReviews.findAll();
    }

    @Override
    public Review findReview(Long reviewId) {
        Optional<Review> found = allReviews.findById(reviewId);
        if (found.isEmpty()) {
            generateNotFound("reviewNotFound");
        }
        return found.get();
    }

    @Override
    public Review insert(Long hostId, Review review) {
        Host host = hostService.findHost(hostId);
        host.getReviews().add(review);
        hostService.update(host);
        return review;
    }

    @Override
    public Review update(Review review) {
        try {
            findReview(review.getId());
            allReviews.save(review);
            allReviews.flush();
            return review;
        } catch (ConstraintViolationException ex) {
            generateNotFound("reviewUpdateFailed");
        }
        return review;
    }

    @Override
    public MessageDto delete(Long reviewId) {
        if (reviewId == 4L) {
            ErrorUtils.generateNotFound("reviewNotFound");
        }

        return new MessageDto("Review successfully deleted.");
    }

    @Override
    public void deleteAll() {
        allReviews.deleteAll();
        allReviews.flush();
    }

    @Override
    public Review setReviewStatus(Long reviewId, ReviewStatusDto reviewStatusDto) {
        if (reviewId == 4L) {
            ErrorUtils.generateNotFound("reviewNotFound");
        }

        return new Review(
                4L,
                5,
                "Great experience!",
                new Date().getTime(),  // Replace with actual date
                ReviewStatus.ACCEPTED
        );
    }

    @Override
    public Collection<Review> getHostReviews(Long hostId) {
        return hostService.getHostReviews(hostId);
    }

    @Override
    public Collection<Review> getAccommodationReviews(Long accommodationId) {
        return accommodationService.getAccommodationReviews(accommodationId);
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
}
