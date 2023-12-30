package com.example.accommodiq.services.impl.email;

import com.example.accommodiq.services.interfaces.email.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class VerificationTokenServiceImpl implements IVerificationTokenService {

    private static final int TOKEN_LENGTH = 10;

    @Value("${email.token.secret}")
    private static String SECRET_KEY;

    @Override
    public String generateVerificationToken(Long userId, String userEmail) {
        String data = userId + userEmail + SECRET_KEY;
        return hashData(data);
    }

    @Override
    public boolean verifyVerificationToken(Long accountId, String accountEmail, String token) {
        String verificationToken = generateVerificationToken(accountId, accountEmail);
        return verificationToken.equals(token);
    }

    private String hashData(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash).substring(0, TOKEN_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing data", e);
        }
    }
}
