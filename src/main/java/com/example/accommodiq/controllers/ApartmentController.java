package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Apartment;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.repositories.UserRepository;
import com.example.accommodiq.services.interfaces.IApartmentService;
import com.example.accommodiq.services.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.stream.events.Comment;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v2/apartment")
public class ApartmentController {
    final
    IApartmentService service;

    public ApartmentController(IApartmentService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<Apartment> getApartments() {
        return service.getAll();
    }

    @GetMapping("/{apartmentId}")
    public Apartment findApartmentById(@PathVariable Long apartmentId) {
        return service.findApartment(apartmentId);
    }

    @PostMapping
    public Apartment insert(@RequestBody Apartment apartment) {
        return service.insert(apartment);
    }

    @PutMapping
    public Apartment update(@RequestBody Apartment apartment) {
        return service.update(apartment);
    }

    @DeleteMapping("/{apartmentId}")
    public Apartment delete(@PathVariable Long apartmentId) {
        return service.delete(apartmentId);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }
}
