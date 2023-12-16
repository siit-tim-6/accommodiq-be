package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.User;
import com.example.accommodiq.enums.AccountRole;

public class RegisterDto {
    private AccountRole role;
    private String email;
    private String password;
    private User user;

    public RegisterDto() {

    }

    public RegisterDto(AccountRole accountRole, String email, String password, User user) {
        this.role = accountRole;
        this.email = email;
        this.password = password;
        this.user = user;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
