package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;
import com.example.accommodiq.services.interfaces.users.ISessionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@CrossOrigin
public class SessionController {
    private final ISessionService sessionService;
    @Autowired
    public SessionController(ISessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @Operation(summary = "User Login")
    public LoginResponseDto login(@RequestBody CredentialsDto credentialsDto) {
        return sessionService.login(credentialsDto);
    }
}
