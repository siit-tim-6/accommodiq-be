package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.User;
import com.example.accommodiq.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class UserController {

    final
    IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable Long userId) {
        return service.findUser(userId);
    }

    @PostMapping
    public User insert(@RequestBody User user) {
        return service.insert(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return service.update(user);
    }

    @DeleteMapping("/{userId}")
    public User delete(@PathVariable Long userId) {
        return service.delete(userId);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

}