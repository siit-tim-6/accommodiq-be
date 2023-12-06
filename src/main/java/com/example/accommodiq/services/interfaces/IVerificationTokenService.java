package com.example.accommodiq.services.interfaces;

public interface IVerificationTokenService {

    public String generateVerificationToken(Long userId, String userEmail);
}
