package com.example.accommodiq.controllers;

import com.example.accommodiq.services.interfaces.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    final
    IReviewService service;

    @Autowired
    public ReviewController(IReviewService service) {
        this.service = service;
    }
}
