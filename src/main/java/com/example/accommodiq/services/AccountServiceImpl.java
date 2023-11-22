package com.example.accommodiq.services;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.UserLoginDto;
import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.repositories.AccountRepository;
import com.example.accommodiq.services.interfaces.IAccountService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class AccountServiceImpl implements IAccountService {

    final
    AccountRepository allAccounts;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account cannot be inserted");
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account cannot be updated");
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

    @Override
    public UserLoginDto login(CredentialsDto credentialsDto) {
        Account account = allAccounts.findAccountByEmail(credentialsDto.getEmail());
        if (account == null || !Objects.equals(account.getPassword(), credentialsDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        return new UserLoginDto(account);
    }

    @Override
    public void changeStatus(Long id, AccountStatus accountStatus) {
        Account account = findAccount(id);
        account.setStatus(accountStatus);
        allAccounts.save(account);
        allAccounts.flush();
    }

    @Override
    public void changePassword(Long id, String password) {
        Account account = findAccount(id);
        account.setPassword(password);
        allAccounts.save(account);
        allAccounts.flush();
    }
}
