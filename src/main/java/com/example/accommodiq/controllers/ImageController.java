package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.ImageDataDto;
import com.example.accommodiq.services.interfaces.accommodations.IImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/images")
@CrossOrigin
public class ImageController {
    final private IImageService imagesService;

    @Autowired
    public ImageController(IImageService imagesService) {
        this.imagesService = imagesService;
    }

    @PostMapping()
    @Operation(summary = "Upload images")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<List<String>> uploadImages(@RequestParam("images") List<MultipartFile> images) {
        List<String> uploadedImages = imagesService.uploadImages(images);
        return ResponseEntity.ok(uploadedImages);
    }

    @GetMapping("/{filename:.+}")
    @Operation(summary = "Get image")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "image/jpeg")})})
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        ImageDataDto imageData = imagesService.loadImageAsResource(filename);
        return ResponseEntity
                .ok()
                .contentType(imageData.mediaType())
                .body(imageData.resource());
    }
}
