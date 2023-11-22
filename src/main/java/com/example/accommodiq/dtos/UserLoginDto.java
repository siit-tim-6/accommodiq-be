package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.enums.AccountRole;

public class UserLoginDto {
    private User user;
    private AccountRole role;

    public UserLoginDto(User user, AccountRole role) {
        this.user = user;
        this.role = role;
    }

    public UserLoginDto(Account account) {
        this.user = account.getUser();
        this.role = account.getRole();
    }

    public UserLoginDto() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }
}
