package com.example.accommodiq.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    public List<String> uploadImages(List<MultipartFile> images);
}
