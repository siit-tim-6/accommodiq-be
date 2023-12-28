package com.example.accommodiq.services.interfaces;

public interface IEmailService {
    void sendVerificationEmail(Long accountId, String accountEmail);
}
