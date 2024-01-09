package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.ILoggedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoggedUserServiceImpl implements ILoggedUserService {
    private final IAccountService accountService;

    @Autowired
    public LoggedUserServiceImpl(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public User getLoggedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        return account.getUser();
    }
}
