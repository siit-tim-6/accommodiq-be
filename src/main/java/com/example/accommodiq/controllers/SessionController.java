package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;
import com.example.accommodiq.services.interfaces.ISessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
public class SessionController {
    private final ISessionService sessionService;
    @Autowired
    public SessionController(ISessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public LoginResponseDto login(@RequestBody CredentialsDto credentialsDto) {
        return sessionService.login(credentialsDto);
    }
}
