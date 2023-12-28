package com.example.accommodiq.services.interfaces;

import org.springframework.http.ResponseEntity;

public interface IEmailVerificationService {
    ResponseEntity<String> activateAccount(String verificationToken, Long accountId);

}
