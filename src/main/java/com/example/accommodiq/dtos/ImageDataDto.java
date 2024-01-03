package com.example.accommodiq.dtos;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record ImageDataDto(Resource resource, MediaType mediaType) {
}
