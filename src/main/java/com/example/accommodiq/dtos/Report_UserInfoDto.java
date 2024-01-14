package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.enums.AccountRole;

public class Report_UserInfoDto {
    private Long id;
    private String name;
    private AccountRole role;

    public Report_UserInfoDto() {
        super();
    }

    public Report_UserInfoDto(Account account) {
        this.id = account.getId();
        this.name = account.getUser().getFirstName() + " " + account.getUser().getLastName();
        this.role = account.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountRole getRole() {
        return role;
    }
}
