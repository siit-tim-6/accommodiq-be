package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Account;

import java.util.Collection;

public interface IAccountService {
    public Collection<Account> getAll();
    public Account findAccount(Long accountId);

    public Account insert(Account account);

    public Account update(Account account);

    public Account delete(Long accountId);

    public void deleteAll();
}
