package com.example.accommodiq.controllers;

import com.example.accommodiq.services.interfaces.email.IEmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email-verification")
@CrossOrigin
public class EmailVerificationController {
    final private IEmailVerificationService emailVerificationService;

    @Autowired
    public EmailVerificationController(IEmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping
    @Operation(summary = "Send verification email")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String verificationToken, @RequestParam("accountId") Long accountId) {
        return emailVerificationService.activateAccount(verificationToken, accountId);
    }
}
