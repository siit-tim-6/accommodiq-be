package com.example.accommodiq.services.interfaces.users;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.AccountDetailsDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.dtos.UpdatePasswordDto;
import com.example.accommodiq.enums.AccountStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface IAccountService extends UserDetailsService {
    Collection<Account> getAll();

    Account findAccount(Long accountId);

    void insert(RegisterDto registerDto);

    AccountDetailsDto update(Account account);

    void delete(Long accountId);

    void deleteAll();

    void changeStatusByUserId(Long id, AccountStatus accountStatus);

    void changePassword(Account account, UpdatePasswordDto passwordDto);

    AccountDetailsDto getAccountDetails(Long accountId);

    Account findAccountByUserId(Long userId);

    MessageDto changeUserStatus(Long userId, AccountStatus status);
}
