package com.example.accommodiq.domain;

import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.enums.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;

@Entity
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private AccountRole role;
    private AccountStatus status;
    private Long activationExpires = Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli();

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    @Transient
    private String jwt;
    private Long lastPasswordResetDate;

    public Account() {
    }

    public Account(Long id, String email, String password, AccountRole role, AccountStatus status, Long activationExpires, User user, String jwt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.activationExpires = activationExpires;
        this.user = user;
        this.jwt = jwt;
        this.lastPasswordResetDate = Instant.now().toEpochMilli();
    }

    public static Account createAccount(RegisterDto registerDto) {
        Account account = new Account();
        account.setEmail(registerDto.getEmail());
        account.setPassword(registerDto.getPassword());
        account.setRole(registerDto.getRole());
        account.setStatus(AccountStatus.INACTIVE);
        account.setUser(registerDto.getUser());
        return account;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.role);
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == AccountStatus.ACTIVE;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Long getActivationExpires() {
        return activationExpires;
    }

    public void setActivationExpires(Long activationExpires) {
        this.activationExpires = activationExpires;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Long lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @PrePersist
    @PostLoad
    public void prePersist() {
        if (lastPasswordResetDate == null) {
            lastPasswordResetDate = Instant.now().toEpochMilli();
        }
    }
}
