package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.IAccountService;
import com.example.accommodiq.services.interfaces.INotificationService;
import com.example.accommodiq.services.interfaces.IReportService;
import com.example.accommodiq.services.interfaces.IUserService;
import com.example.accommodiq.utilities.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@CrossOrigin()
public class UserController {
    final IAccountService accountService;
    final INotificationService notificationService;
    final IUserService userService;
    final IReportService reportService;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(IAccountService accountService, INotificationService notificationService, IUserService userService,
                          IReportService reportService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.reportService = reportService;
        this.passwordEncoder = passwordEncoder;
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
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        accountService.insert(registerDto);
        return registerDto;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST') or hasAuthority('ADMIN')")
    public AccountDetailsDto manageUserAccount(@RequestBody AccountDetailsDto accountDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account accountToManage = (Account) accountService.loadUserByUsername(email);
        accountDetails.putDetailsIntoAccount(accountToManage);
        return accountService.update(accountToManage);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
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
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody UpdatePasswordDto passwordDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), account.getPassword())) {
            ErrorUtils.throwBadRequest("wrongOldPassword");
        }

        passwordDto.encode(passwordEncoder);
        accountService.changePassword(account, passwordDto);
    }

    @PostMapping("/{userId}/notifications")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    public Notification createNotification(@PathVariable Long userId, @RequestBody Notification notification) {
        return notificationService.insert(userId, notification);
    }

    @GetMapping("/{userId}/notifications")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    public Collection<NotificationDto> getUsersNotifications(@PathVariable Long userId) {
        User user = userService.findUser(userId);
        return user.getNotifications().stream().map(NotificationDto::new).toList();
    }

    @GetMapping("/notification-settings")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    public Collection<NotificationSettingDto> getUsersNotificationSettings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        User user = account.getUser();
        return user.getNotificationSettings().stream().map(NotificationSettingDto::new).toList();
    }

    @PutMapping("/{userId}/notification-settings")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    public Collection<NotificationSettingDto> updateNotificationSettings(@PathVariable Long userId, @RequestBody Collection<NotificationSetting> notificationSettings) {
        System.out.println(userId);
        return notificationSettings.stream().map(NotificationSettingDto::new).toList();
    }

    @PostMapping("/{id}/reports")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @ResponseStatus(HttpStatus.OK)
    public void reportUser(@PathVariable Long id, @RequestBody ReportDto reportDto) {
        reportService.reportUser(id, reportDto);
    }
}
