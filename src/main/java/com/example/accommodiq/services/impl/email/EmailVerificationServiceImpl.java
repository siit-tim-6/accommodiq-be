package com.example.accommodiq.services.impl.email;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.email.IEmailVerificationService;
import com.example.accommodiq.services.interfaces.email.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class EmailVerificationServiceImpl implements IEmailVerificationService {

    final IAccountService accountService;
    final IVerificationTokenService verificationTokenService;

    @Autowired
    public EmailVerificationServiceImpl(IAccountService accountService, IVerificationTokenService verificationTokenService) {
        this.accountService = accountService;
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    public ResponseEntity<String> activateAccount(String verificationToken, Long accountId) {
        Optional<Account> optionalAccount = Optional.ofNullable(accountService.findAccount(accountId));

        if (optionalAccount.isEmpty())
            return ResponseEntity.badRequest().body("Invalid activation link");

        Account account = optionalAccount.get();

        if (!isVerificationTokenValid(accountId, account.getEmail(), verificationToken))
            return ResponseEntity.badRequest().body("Account is already activated");

        if (account.getStatus() != AccountStatus.INACTIVE)
            return ResponseEntity.badRequest().body("Account is already activated");

        if(isActivationLinkActive(account)) {
            account.setStatus(AccountStatus.ACTIVE);
            accountService.update(account);
            return ResponseEntity.ok("Activation successful");
        }

        accountService.delete(accountId);
        return ResponseEntity.badRequest().body("Activation link has expired, please register again");
    }


    private boolean isVerificationTokenValid(Long accountId, String accountEmail, String verificationToken) {
        return verificationTokenService.verifyVerificationToken(accountId, accountEmail, verificationToken);
    }

    private boolean isActivationLinkActive(Account account) {
        return account.getActivationExpires() >= Instant.now().toEpochMilli();
    }
}
