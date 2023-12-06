package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Account;

public interface IEmailService {
    void sendVerificationEmail(Long accountId, String accountEmail);
}
