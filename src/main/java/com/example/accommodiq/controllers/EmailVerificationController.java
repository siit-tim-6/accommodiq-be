package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.services.interfaces.IAccountService;
import com.example.accommodiq.services.interfaces.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/email-verification")
public class EmailVerificationController {

    final private IAccountService accountService;

    final private IVerificationTokenService verificationTokenService;

    @Autowired
    public EmailVerificationController(IAccountService accountService, IVerificationTokenService verificationTokenService) {
        this.accountService = accountService;
        this.verificationTokenService = verificationTokenService;
    }

    @GetMapping
    public ResponseEntity<String> activateAccount(@RequestParam("token") String verificationToken, @RequestParam("accountId") Long accountId) {
        Optional<Account> optionalAccount = Optional.ofNullable(accountService.findAccount(accountId));

        if (optionalAccount.isPresent() && optionalAccount.get().getActivationExpires() > Instant.now().toEpochMilli()) {
            Account account = optionalAccount.get();
            if (verificationTokenService.verifyVerificationToken(accountId, account.getEmail(), verificationToken)) {
                if (account.getStatus() == AccountStatus.INACTIVE) {
                    account.setStatus(AccountStatus.ACTIVE);
                    accountService.update(account);
                    return ResponseEntity.ok("Activation successful");
                } else {
                    return ResponseEntity.badRequest().body("Account is already activated");
                }

            }
        }
        return ResponseEntity.badRequest().body("Invalid activation link");
    }

}
