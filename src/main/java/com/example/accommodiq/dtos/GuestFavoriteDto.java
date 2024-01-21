package com.example.accommodiq.dtos;

import jakarta.validation.constraints.NotNull;

public class GuestFavoriteDto {
    @NotNull(message = "Favorite ID is required")
    private Long favoriteId;

    public GuestFavoriteDto(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public GuestFavoriteDto() {
        super();
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }
}
