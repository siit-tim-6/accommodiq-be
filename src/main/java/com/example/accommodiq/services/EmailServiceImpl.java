package com.example.accommodiq.services;

import com.example.accommodiq.services.interfaces.IEmailService;
import com.example.accommodiq.services.interfaces.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    final private JavaMailSender javaMailSender;

    final private IVerificationTokenService verificationTokenService;


    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, IVerificationTokenService verificationTokenService) {
        this.javaMailSender = javaMailSender;
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    public void sendVerificationEmail(Long accountId, String accountEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        String fromEmail = "accommodiqproject@gmail.com";
        message.setFrom(fromEmail);
        message.setTo(accountEmail);
        message.setSubject("AccommodIQ - Email Verification");
        message.setText("Hello there, " + ",\n\n" +
                "Please click on the link below to verify your email address:\n\n" +
                "http://localhost:8000/email-verification?token="+verificationTokenService.generateVerificationToken(accountId,accountEmail)+"&accountId="+accountId + "\n\n" +
                "Thank you,\n" +
                "AccommodIQ Team");

        javaMailSender.send(message);
    }
}
