package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReviewStatus;

public class ReviewStatusDto {
    private ReviewStatus status;

    public ReviewStatusDto(ReviewStatus status) {
        this.status = status;
    }

    public ReviewStatusDto() {
        super();
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }
}
