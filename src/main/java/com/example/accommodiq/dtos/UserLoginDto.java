package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.User;

public class UserLoginDto {
    private User user;
    private Account.Role role;

    public UserLoginDto(User user, Account.Role role) {
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

    public Account.Role getRole() {
        return role;
    }

    public void setRole(Account.Role role) {
        this.role = role;
    }
}
