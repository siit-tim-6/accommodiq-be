package com.example.accommodiq.dtos;

import jakarta.validation.constraints.NotBlank;

public class CredentialsDto {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;

    public CredentialsDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public CredentialsDto() {
        super();
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
}
