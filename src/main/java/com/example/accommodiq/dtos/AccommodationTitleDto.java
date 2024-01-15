package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;

public class AccommodationTitleDto {
    private long id;
    private String title;

    public AccommodationTitleDto(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public AccommodationTitleDto(Accommodation accommodation) {
        this.id = accommodation.getId();
        this.title = accommodation.getTitle();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
