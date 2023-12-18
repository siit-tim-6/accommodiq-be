package com.example.accommodiq.dtos;

public class AccommodationUpdateDto extends AccommodationCreateDto {
    private Long id;

    public AccommodationUpdateDto(Long id) {
        super();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
