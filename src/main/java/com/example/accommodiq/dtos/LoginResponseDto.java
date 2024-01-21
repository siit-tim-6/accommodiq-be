package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.AccountRole;

public class LoginResponseDto {
    AccountRole role;
    String jwt;

    public LoginResponseDto(AccountRole role, String jwt) {
        this.role = role;
        this.jwt = jwt;
    }

    public LoginResponseDto() {
        super();
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
