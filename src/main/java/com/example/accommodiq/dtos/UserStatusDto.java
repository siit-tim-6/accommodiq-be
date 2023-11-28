package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.AccountStatus;

public class UserStatusDto {
    private AccountStatus status;

    public UserStatusDto(AccountStatus status) {
        this.status = status;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
