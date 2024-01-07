package com.example.accommodiq.services.impl.email;

import com.example.accommodiq.services.interfaces.email.IEmailService;
import com.example.accommodiq.services.interfaces.email.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    final private JavaMailSender javaMailSender;

    final private IVerificationTokenService verificationTokenService;

    @Value("${email.address}")
    private String fromEmail;


    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, IVerificationTokenService verificationTokenService) {
        this.javaMailSender = javaMailSender;
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    public void sendVerificationEmail(Long accountId, String accountEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
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
