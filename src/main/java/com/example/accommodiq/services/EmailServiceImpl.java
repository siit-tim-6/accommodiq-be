package com.example.accommodiq.services;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.services.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    final private JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendVerificationEmail(Account account) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject("AccommodIQ - Email Verification");
        message.setText("Hello there, " + ",\n\n" +
                "Please click on the link below to verify your email address:\n\n" +
                "http://localhost:8000/users/"+account.getId()+"status" + "\n\n" +
                "Thank you,\n" +
                "AccommodIQ Team");

        javaMailSender.send(message);
    }

    private String generateVerificationToken() {
        // Implement logic to generate a unique verification token
        // This token will be used to verify the user's email
        return "random_token";
    }
}
