package com.example.accommodiq.dtos;

public class HostReviewCardDto {
    private ReviewDto review;
    private Long hostId;
    private String name;

    public HostReviewCardDto() {
        super();
    }

public HostReviewCardDto(ReviewDto review, Long hostId, String name) {
        this.review = review;
        this.hostId = hostId;
        this.name = name;
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
