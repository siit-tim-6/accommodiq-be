package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.Review;

public class HostReviewCardDto {
    private ReviewDto review;
    private Long hostId;
    private String name;

    public HostReviewCardDto() {
        super();
    }

public HostReviewCardDto(Review review, Host host) {
        this.review = new ReviewDto(review);
        this.hostId = host.getId();
        this.name = host.getFirstName() + " " + host.getLastName();
    }

    public ReviewDto getReview() {
        return review;
    }

    public void setReview(ReviewDto review) {
        this.review = review;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
