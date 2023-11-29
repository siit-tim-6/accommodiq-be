package com.example.accommodiq.dtos;

public class GuestFavoriteDto {
    private Long favoriteId;

    public GuestFavoriteDto(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public GuestFavoriteDto() {}

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }
}
