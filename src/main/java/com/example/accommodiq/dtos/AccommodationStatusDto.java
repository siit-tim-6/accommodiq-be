package com.example.accommodiq.dtos;

public class AccommodationStatusDto {
    private boolean accepted;

    public AccommodationStatusDto() {
        super();
    }

    public AccommodationStatusDto(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
