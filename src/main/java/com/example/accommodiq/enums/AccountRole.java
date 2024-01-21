package com.example.accommodiq.enums;

import org.springframework.security.core.GrantedAuthority;

public enum AccountRole implements GrantedAuthority {
    GUEST,
    HOST,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
