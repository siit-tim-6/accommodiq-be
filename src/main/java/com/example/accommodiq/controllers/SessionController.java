package com.example.accommodiq.controllers;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.UserLoginDto;
import com.example.accommodiq.services.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
public class SessionController {
    final IAccountService accountService;

    @Autowired
    public SessionController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public UserLoginDto login(@RequestBody CredentialsDto credentialsDto) {
        return accountService.login(credentialsDto);
    }
}
