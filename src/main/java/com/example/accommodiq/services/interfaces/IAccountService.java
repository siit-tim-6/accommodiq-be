package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.AccountStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface IAccountService extends UserDetailsService {
    Collection<Account> getAll();
    Account findAccount(Long accountId);

    void insert(RegisterDto registerDto);

    AccountDetailsDto update(Account account);

    Account delete(Long accountId);

    void deleteAll();

    UserLoginDto login(CredentialsDto credentialsDto);

    void changeStatus(Long id, AccountStatus accountStatus);

    void changePassword(Account account, UpdatePasswordDto passwordDto);
}
