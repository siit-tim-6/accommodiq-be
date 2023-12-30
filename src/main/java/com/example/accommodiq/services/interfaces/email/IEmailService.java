package com.example.accommodiq.services.interfaces.email;

public interface IEmailService {
    void sendVerificationEmail(Long accountId, String accountEmail);
}
