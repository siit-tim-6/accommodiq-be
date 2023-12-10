package com.example.accommodiq.services;

import com.example.accommodiq.services.interfaces.IImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements IImageService {
    private static final String UPLOAD_DIR = "../../../resources/uploads/accommodation/";

    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        return images.stream().map(this::uploadImage).collect(Collectors.toList());
    }

    private String uploadImage(MultipartFile file) {
        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            Files.copy(file.getInputStream(), filePath);

            return  filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    private String generateUniqueFileName(String fileName) {
        return fileName + "_" + System.currentTimeMillis();
    }
 }
