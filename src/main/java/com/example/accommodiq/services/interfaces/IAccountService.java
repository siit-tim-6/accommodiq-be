package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Account;
import java.util.Collection;

public interface IAccountService {
    Collection<Account> getAll();
    Account findAccount(Long accountId);

    Account insert(Account account);

    Account update(Account account);

    Account delete(Long accountId);

    void deleteAll();
}
