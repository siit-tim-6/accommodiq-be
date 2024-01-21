package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReviewStatus;
import jakarta.validation.constraints.NotNull;

public class ReviewStatusDto {
    @NotNull(message = "Status is required")
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
