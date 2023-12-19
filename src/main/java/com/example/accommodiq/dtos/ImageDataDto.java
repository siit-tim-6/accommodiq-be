package com.example.accommodiq.dtos;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public class ImageDataDto {
    private final Resource resource;
    private final MediaType mediaType;

    public ImageDataDto(Resource resource, MediaType mediaType) {
        this.resource = resource;
        this.mediaType = mediaType;
    }

    public Resource getResource() {
        return resource;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
