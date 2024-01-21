package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;

public class AccountStatusDto {
    @NotNull(message = "Status is required")
    AccountStatus status;

    public AccountStatusDto() {
        super();
    }

    public AccountStatus getStatus() {
        return status;
    }
}
