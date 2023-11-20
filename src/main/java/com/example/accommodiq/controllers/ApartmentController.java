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
@RequestMapping("/api")
public class ApartmentController {
    final
    IApartmentService apartmentService;

    final
    IUserService userService;

    public ApartmentController(IApartmentService apartmentService, IUserService userService) {
        this.apartmentService = apartmentService;
        this.userService = userService;
    }

    @GetMapping("/apartments")
    public Collection<Apartment> getApartments() {
        return apartmentService.getAll();
    }

    @GetMapping("/apartments/{apartmentId}")
    public Apartment findApartmentById(@PathVariable Long apartmentId) {
        return apartmentService.findApartment(apartmentId);
    }

    @PutMapping("/apartments")
    public Apartment update(@RequestBody Apartment apartment) {
        return apartmentService.update(apartment);
    }

    @DeleteMapping("/apartments/{apartmentId}")
    public Apartment delete(@PathVariable Long apartmentId) {
        return apartmentService.delete(apartmentId);
    }

    @DeleteMapping("/apartments")
    public void deleteAll() {
        apartmentService.deleteAll();
    }

    @PutMapping("/apartments/{apartmentId}/accept")
    public Apartment acceptApartment(@PathVariable Long apartmentId) {
        return apartmentService.setApartmentAcceptance(apartmentId, true);
    }

    @PutMapping("/apartments/{apartmentId}/deny")
    public Apartment denyApartment(@PathVariable Long apartmentId) {
        return apartmentService.setApartmentAcceptance(apartmentId, false);
    }

    @PostMapping("/users/{ownerId}/apartments")
    public Apartment insert(@PathVariable(value = "ownerId") Long tutorialId, @RequestBody Apartment apartment) {
        User owner = userService.findUser(tutorialId);
        if (owner.getRole() != User.Role.OWNER)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User is not an owner");

        apartment.setOwner(owner);
        return apartmentService.insert(apartment);
    }

    @GetMapping("/users/{ownerId}/apartments")
    public List<Apartment> getApartmentsByOwnerId(@PathVariable Long ownerId) {
        return apartmentService.findApartmentsByOwnerId(ownerId);
    }
}
