package com.example.accommodiq.services.interfaces;

public interface IVerificationTokenService {

    public String generateVerificationToken(Long userId, String userEmail);

    boolean verifyVerificationToken(Long accountId, String accountEmail, String token);
}
