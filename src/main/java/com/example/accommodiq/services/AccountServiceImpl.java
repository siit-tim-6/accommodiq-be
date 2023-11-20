package com.example.accommodiq.services;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Account;
import com.example.accommodiq.repositories.AccountRepository;
import com.example.accommodiq.services.interfaces.IAccountService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountServiceImpl implements IAccountService {

    final
    AccountRepository allAccounts;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public AccountServiceImpl(AccountRepository allAccounts) {
        this.allAccounts = allAccounts;
    }

    @Override
    public Collection<Account> getAll() {
        System.out.println(allAccounts.findAll());
        return allAccounts.findAll();
    }

    @Override
    public Account findAccount(Long accountId) {
        Optional<Account> found = allAccounts.findById(accountId);
        if (found.isEmpty()) {
            String value = bundle.getString("notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Account insert(Account account) {
        try {
            allAccounts.save(account);
            allAccounts.flush();
            return account;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error");
        }
    }

    @Override
    public Account update(Account account) {
        try {
            findAccount(account.getId()); // this will throw ResponseStatusException if account is not found
            allAccounts.save(account);
            allAccounts.flush();
            return account;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = c;
            }
            if ((c != null)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error2");
            }
            throw ex;
        }
    }

    @Override
    public Account delete(Long accountId) {
        Account found = findAccount(accountId); // this will throw AccountNotFoundException if Account is not found
        allAccounts.delete(found);
        allAccounts.flush();
        return found;
    }

    @Override
    public void deleteAll() {
        allAccounts.deleteAll();
        allAccounts.flush();
    }
}
