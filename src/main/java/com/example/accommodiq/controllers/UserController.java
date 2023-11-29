package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.services.interfaces.IAccountService;
import com.example.accommodiq.services.interfaces.IReportService;
import com.example.accommodiq.services.interfaces.INotificationService;
import com.example.accommodiq.services.interfaces.INotificationSettingService;
import com.example.accommodiq.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    final IAccountService accountService;
    final INotificationService notificationService;
    final IUserService userService;
    final INotificationSettingService notificationSettingService;
    final IReportService reportService;

    @Autowired
    public UserController(IAccountService accountService, INotificationService notificationService, IUserService userService,
                          INotificationSettingService notificationSettingService, IReportService reportService) {
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.notificationSettingService = notificationSettingService;
        this.reportService = reportService;
    }

    @GetMapping
    public Collection<Account> getAccounts() {
        return accountService.getAll();
    }

    @GetMapping("/{accountId}")
    public Account findAccountById(@PathVariable Long accountId) {
        return accountService.findAccount(accountId);
    }

    @PostMapping
    public Account registerUser(@RequestBody Account account) {
        account.setStatus(AccountStatus.INACTIVE);
        Account savedAccount = accountService.insert(account);
        notificationSettingService.setNotificationSettingsForUser(savedAccount.getUser());
        return savedAccount;
    }

    @PutMapping
    public Account manageUserAccount(@RequestBody Account account) {
        return accountService.update(account);
    }

    @DeleteMapping("/{accountId}")
    public Account deleteUser(@PathVariable Long accountId) {
        return accountService.delete(accountId);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        accountService.deleteAll();
    }

    @PutMapping(value = "/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(@PathVariable Long id, @RequestBody UserStatusDto statusDto) {
        accountService.changeStatus(id, statusDto.getStatus());
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable Long id, @RequestBody UpdatePasswordDto passwordDto) {
        accountService.changePassword(id, passwordDto.getPassword());
    }

    @PostMapping("/{userId}/notifications")
    public Notification createNotification(@PathVariable Long userId, @RequestBody Notification notification) {
        User user = userService.findUser(userId);
        notification.setUser(user);
        return notificationService.insert(notification);
    }

    @GetMapping("/{userId}/notifications")
    public Collection<NotificationDto> getUsersNotifications(@PathVariable Long userId) {
        return notificationService.findUsersNotifications(userId).stream().map(NotificationDto::new).toList();
    }

    @GetMapping("/{userId}/notification-settings")
    public Collection<NotificationSettingDto> getUsersNotificationSettings(@PathVariable Long userId) {
        return notificationSettingService.findUsersNotificationSettings(userId);
    }

    @PutMapping("/{userId}/notification-settings")
    public Collection<NotificationSettingDto> updateNotificationSettings(@PathVariable Long userId, @RequestBody Collection<NotificationSetting> notificationSettings) {
        User user = userService.findUser(userId);
        return notificationSettingService.updateNotificationSettingsForUser(user, notificationSettings);
    }

    @PostMapping("/{id}/reports")
    @ResponseStatus(HttpStatus.OK)
    public void reportUser(@PathVariable Long id, @RequestBody ReportDto reportDto) {
        reportService.reportUser(id, reportDto);
    }

}
