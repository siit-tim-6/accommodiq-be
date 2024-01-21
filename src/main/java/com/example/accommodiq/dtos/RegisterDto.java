package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.User;
import com.example.accommodiq.enums.AccountRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterDto {
    @NotNull(message = "Role is required")
    private AccountRole role;
    @NotNull(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull(message = "User is required")
    private User user;

    public RegisterDto() {
        super();
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
