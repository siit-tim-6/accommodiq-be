package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.UpdatePasswordDto;
import com.example.accommodiq.dtos.UserLoginDto;
import com.example.accommodiq.services.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

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
    public Account registerUser(@RequestBody Account account) {
        account.setStatus(Account.AccountStatus.INACTIVE);
        return service.insert(account);
    }

    @PutMapping
    public Account manageUserAccount(@RequestBody Account account) {
        return service.update(account);
    }

    @DeleteMapping("/{accountId}")
    public Account deleteUser(@PathVariable Long accountId) {
        return service.delete(accountId);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        service.deleteAll();
    }

    @PostMapping("/login")
    public UserLoginDto login(@RequestBody CredentialsDto credentialsDto) {
        return service.login(credentialsDto);
    }

    @PutMapping("/{id}/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable Long id, @RequestBody UpdatePasswordDto passwordDto) {
        service.changePassword(id, passwordDto.getPassword());
    }

    @PutMapping("/{id}/report")
    public Account reportUser(@PathVariable Long id, @RequestBody Objects reportData) {
        // Implement the logic to report a user
        // You can use userService.reportUser(id, reportData) to delegate reporting to a service
        // Return appropriate response
        return null;
    }

    @RequestMapping(value = "/{id}/activate", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@PathVariable Long id) {
        service.changeStatus(id, Account.AccountStatus.ACTIVE);
    }

    @PutMapping("/{id}/block")
    @ResponseStatus(HttpStatus.OK)
    public void blockUser(@PathVariable Long id) {
        service.changeStatus(id, Account.AccountStatus.BLOCKED);
    }
}
