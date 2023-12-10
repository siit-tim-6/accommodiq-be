package com.example.accommodiq.controllers;

import com.example.accommodiq.services.interfaces.IImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/images")
@CrossOrigin
public class ImageController {
    final private IImageService imagesService;

    public ImageController(IImageService imagesService) {
        this.imagesService = imagesService;
    }

    @PostMapping("/upload")
    public Collection<String> uploadImages(@RequestParam("images") List<MultipartFile> images) {
        return imagesService.uploadImages(images);
    }
}
