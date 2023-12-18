package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Accommodation;

public class AccommodationUpdateDto extends AccommodationCreateDto {
    private Long id;

    public AccommodationUpdateDto(Long id) {
        super();
        this.id = id;
    }

    public AccommodationUpdateDto(Accommodation accommodation) {
        super(accommodation);
        this.id = accommodation.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
