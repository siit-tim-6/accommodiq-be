package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.services.interfaces.*;
import com.example.accommodiq.utilities.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@CrossOrigin()
public class UserController {
    final IAccountService accountService;
    final INotificationService notificationService;
    final IUserService userService;
    final INotificationSettingService notificationSettingService;
    final IReportService reportService;
    final PasswordEncoder passwordEncoder;
    final IEmailService emailService;

    @Autowired
    public UserController(IAccountService accountService, INotificationService notificationService, IUserService userService,
                          INotificationSettingService notificationSettingService, IReportService reportService, PasswordEncoder passwordEncoder, IEmailService emailService) {
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.notificationSettingService = notificationSettingService;
        this.reportService = reportService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping
    public Collection<Account> getAccounts() {
        return accountService.getAll();
    }

    @GetMapping("/me")
    public AccountDetailsDto getPersonalAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        return new AccountDetailsDto(account);
    }

    @GetMapping("/{accountId}")
    public Account findAccountById(@PathVariable Long accountId) {
        return accountService.findAccount(accountId);
    }

    @PostMapping
    @Transactional
    public RegisterDto registerUser(@RequestBody RegisterDto registerDto) {
        Account account = Account.createAccount(registerDto);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account savedAccount = accountService.insert(account);
        emailService.sendVerificationEmail(savedAccount.getId(),savedAccount.getEmail());
        notificationSettingService.setNotificationSettingsForUser(savedAccount.getUser().getId());
        return registerDto;
    }

    @PutMapping
    public Account manageUserAccount(@RequestBody AccountDetailsDto accountDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account accountToManage = (Account) accountService.loadUserByUsername(email);
        accountDetails.putDetailsIntoAccount(accountToManage);
        return accountService.update(accountToManage);
    }

    @DeleteMapping()
    public Account deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        accountService.delete(account.getId());
        return account;
    }

    @PutMapping(value = "/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(@PathVariable Long id, @RequestBody UserStatusDto statusDto) {
        accountService.changeStatus(id, statusDto.getStatus());
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody UpdatePasswordDto passwordDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), account.getPassword())) {
            ReportUtils.throwBadRequest("wrongOldPassword");
        }

        passwordDto.encode(passwordEncoder);
        accountService.changePassword(account, passwordDto);
    }

    @PostMapping("/{userId}/notifications")
    public Notification createNotification(@PathVariable Long userId, @RequestBody Notification notification) {
        return notificationService.insert(userId, notification);
    }

    @GetMapping("/{userId}/notifications")
    public Collection<NotificationDto> getUsersNotifications(@PathVariable Long userId) {
        User user = userService.findUser(userId);
        return user.getNotifications().stream().map(NotificationDto::new).toList();
    }

    @GetMapping("/notification-settings")
    public Collection<NotificationSettingDto> getUsersNotificationSettings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        User user = account.getUser();
        return user.getNotificationSettings().stream().map(NotificationSettingDto::new).toList();
    }

    @PutMapping("/{userId}/notification-settings")
    public Collection<NotificationSettingDto> updateNotificationSettings(@PathVariable Long userId, @RequestBody Collection<NotificationSetting> notificationSettings) {
        User user = userService.findUser(userId);
        //return notificationSettingService.updateNotificationSettingsForUser(user, notificationSettings);
        return notificationSettings.stream().map(NotificationSettingDto::new).toList();
    }

    @PostMapping("/{id}/reports")
    @ResponseStatus(HttpStatus.OK)
    public void reportUser(@PathVariable Long id, @RequestBody ReportDto reportDto) {
        reportService.reportUser(id, reportDto);
    }

}
