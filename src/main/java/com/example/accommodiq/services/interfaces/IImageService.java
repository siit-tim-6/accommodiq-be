package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.ImageDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<String> uploadImages(List<MultipartFile> images);
    ImageDataDto loadImageAsResource(String filename);
}
