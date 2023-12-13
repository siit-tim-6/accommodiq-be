package com.example.accommodiq.domain;

import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.enums.AccountRole;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private AccountRole role;
    private AccountStatus status;
    private Long activationExpires = Instant.now().plus(1, ChronoUnit.MINUTES).toEpochMilli();


    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public Account() {
    }

    public Account(Long id, String email, String password, AccountRole role, AccountStatus status, Long activationExpires, User user) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.activationExpires = activationExpires;
        this.user = user;
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
}
