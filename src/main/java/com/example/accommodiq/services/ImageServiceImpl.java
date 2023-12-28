package com.example.accommodiq.services;

import com.example.accommodiq.dtos.ImageDataDto;
import com.example.accommodiq.services.interfaces.IImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements IImageService {
    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        return images.stream().map(this::uploadImage).collect(Collectors.toList());
    }

    @Override
    public ImageDataDto loadImageAsResource(String filename) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                MediaType mediaType = getContentType(filename);
                return new ImageDataDto(resource, mediaType);
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Error: " + ex.getMessage());
        }
    }

    private String uploadImage(MultipartFile file) {
        try {
            Path dirPath = Path.of(UPLOAD_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    private String generateUniqueFileName(String fileName) {
        return System.currentTimeMillis() + "_" + fileName;
    }

    private MediaType getContentType(String filename) {
        String fileExtension = Objects.requireNonNull(StringUtils.getFilenameExtension(filename)).toLowerCase();
        Map<String, MediaType> extensionMap = new HashMap<>();
        extensionMap.put("jpeg", MediaType.IMAGE_JPEG);
        extensionMap.put("jpg", MediaType.IMAGE_JPEG);
        extensionMap.put("png", MediaType.IMAGE_PNG);
        extensionMap.put("gif", MediaType.IMAGE_GIF);

        return extensionMap.getOrDefault(fileExtension, MediaType.APPLICATION_OCTET_STREAM);
    }
 }
