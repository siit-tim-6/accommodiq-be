package com.example.accommodiq.services.interfaces.email;

public interface IVerificationTokenService {

    String generateVerificationToken(Long userId, String userEmail);

    boolean verifyVerificationToken(Long accountId, String accountEmail, String token);
}
