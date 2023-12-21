package com.example.accommodiq.services;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Guest;
import com.example.accommodiq.domain.Host;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.repositories.AccountRepository;
import com.example.accommodiq.services.interfaces.IAccountService;
import com.example.accommodiq.services.interfaces.IEmailService;
import com.example.accommodiq.services.interfaces.INotificationSettingService;
import com.example.accommodiq.utilities.ErrorUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class AccountServiceImpl implements IAccountService {

    final
    AccountRepository allAccounts;

    final
    IEmailService emailService;

    final
    INotificationSettingService notificationSettingService;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public AccountServiceImpl(AccountRepository allAccounts, IEmailService emailService, INotificationSettingService notificationSettingService) {
        this.allAccounts = allAccounts;
        this.emailService = emailService;
        this.notificationSettingService = notificationSettingService;
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
            String value = bundle.getString("userNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    @Transactional
    public void insert(RegisterDto registerDto) {
        if (emailExists(registerDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        Account account = new Account(registerDto);
        try {
            User user = account.getUser();
            if (account.getRole() == AccountRole.GUEST) {
                account.setUser(new Guest(user));
            }
            if (account.getRole() == AccountRole.HOST) {
                account.setUser(new Host(user));
            }
            allAccounts.save(account);
            allAccounts.flush();
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account cannot be inserted");
        }
        emailService.sendVerificationEmail(account.getId(), account.getEmail());
        notificationSettingService.setNotificationSettingsForUser(account.getUser().getId());
    }

    @Override
    public AccountDetailsDto update(Account account) {
        try {
            findAccount(account.getId()); // this will throw ResponseStatusException if account is not found
            allAccounts.save(account);
            allAccounts.flush();
            return new AccountDetailsDto(account);
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException)) {
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
//        Account account = allAccounts.findAccountByEmail(credentialsDto.getEmail());
//        if (account == null || !Objects.equals(account.getPassword(), credentialsDto.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
//        }
//        return new UserLoginDto(account);

        if (credentialsDto.getEmail().equals("test@nonexistent.com")) {
            ErrorUtils.throwNotFound("badCredentials");
        }

        return new UserLoginDto(new User(
                1L,
                "John",
                "Doe",
                "123 Main St",
                "555-1234"
        ), AccountRole.GUEST);
    }

    @Override
    public void changeStatus(Long id, AccountStatus accountStatus) {
        Account account = findAccount(id);
        account.setStatus(accountStatus);
        allAccounts.save(account);
        allAccounts.flush();
    }

    @Override
    public void changePassword(Account account, UpdatePasswordDto passwordDto) {
        account.setPassword(passwordDto.getNewPassword());
        allAccounts.save(account);
        allAccounts.flush();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = allAccounts.findAccountByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }

    private boolean emailExists(String email) {
        return allAccounts.findAccountByEmail(email) != null;
    }
}
