package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.UserLoginDto;
import com.example.accommodiq.services.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    final
    IAccountService service;

    @Autowired
    public UserController(IAccountService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<Account> getAccounts() {
        return service.getAll();
    }

    @GetMapping("/{accountId}")
    public Account findAccountById(@PathVariable Long accountId) {
        return service.findAccount(accountId);
    }

    @PostMapping
    public Account insert(@RequestBody Account account) {
        return service.insert(account);
    }

    @PutMapping
    public Account update(@RequestBody Account account) {
        return service.update(account);
    }

    @DeleteMapping("/{accountId}")
    public Account delete(@PathVariable Long accountId) {
        return service.delete(accountId);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

    @PostMapping("/login")
    public UserLoginDto login(@RequestBody CredentialsDto credentialsDto) {
        return service.login(credentialsDto);
    }
}
