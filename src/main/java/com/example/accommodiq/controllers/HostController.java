package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.services.interfaces.IHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hosts")
public class HostController {
    final private IHostService hostService;

    @Autowired
    public HostController(IHostService hostService) {
        this.hostService = hostService;
    }

    @PostMapping("/{hostId}/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewAccommodation(@PathVariable Long hostId, @RequestBody AccommodationCreateDto accommodation) {

    }
}
