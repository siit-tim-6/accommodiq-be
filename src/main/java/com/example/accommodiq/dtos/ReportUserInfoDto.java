package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.enums.AccountRole;

public class ReportUserInfoDto {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountRole getRole() {
        return role;
    }

    private AccountRole role;

    public ReportUserInfoDto() {
        super();
    }

    public ReportUserInfoDto(Account account) {
        this.id = account.getId();
        this.name = account.getUser().getFirstName() + " " + account.getUser().getLastName();
        this.role = account.getRole();
    }


}
