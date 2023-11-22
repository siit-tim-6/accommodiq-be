package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.dtos.UpdatePasswordDto;
import com.example.accommodiq.dtos.UserLoginDto;
import com.example.accommodiq.services.interfaces.IAccountService;
import com.example.accommodiq.services.interfaces.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    final
    IAccountService service;
    final IReportService reportService;

    @Autowired
    public UserController(IAccountService service, IReportService reportService) {
        this.service = service;
        this.reportService = reportService;
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

    @PostMapping("/login")
    public UserLoginDto login(@RequestBody CredentialsDto credentialsDto) {
        return service.login(credentialsDto);
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

    @PutMapping("/{id}/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable Long id, @RequestBody UpdatePasswordDto passwordDto) {
        service.changePassword(id, passwordDto.getPassword());
    }

    @PutMapping("/{id}/report")
    @ResponseStatus(HttpStatus.OK)
    public void reportUser(@PathVariable Long id, @RequestBody ReportDto reportDto) {
        reportService.reportUser(id, reportDto);
    }

}
