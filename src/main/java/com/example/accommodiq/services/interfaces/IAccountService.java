package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.UserLoginDto;
import com.example.accommodiq.enums.AccountStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface IAccountService extends UserDetailsService {
    Collection<Account> getAll();
    Account findAccount(Long accountId);

    Account insert(Account account);

    Account update(Account account);

    Account delete(Long accountId);

    void deleteAll();

    UserLoginDto login(CredentialsDto credentialsDto);

    void changeStatus(Long id, AccountStatus accountStatus);

    void changePassword(Long id, String password);
}
