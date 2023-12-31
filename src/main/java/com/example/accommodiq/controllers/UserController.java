package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.domain.Notification;
import com.example.accommodiq.domain.NotificationSetting;
import com.example.accommodiq.domain.User;
import com.example.accommodiq.dtos.*;
import com.example.accommodiq.services.interfaces.feedback.IReportService;
import com.example.accommodiq.services.interfaces.notifications.INotificationService;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.services.interfaces.users.IUserService;
import com.example.accommodiq.utilities.ErrorUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public UserController(IAccountService accountService, INotificationService notificationService, IUserService userService, IReportService reportService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.reportService = reportService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class, type = "array"))})})
    public Collection<Account> getAccounts() {
        return accountService.getAll();
    }

    @GetMapping("/me")
    @Operation(summary = "Get personal account data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDetailsDto.class))})})
    public AccountDetailsDto getPersonalAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        return new AccountDetailsDto(account);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))})})
    public Account findAccountById(@Parameter(description = "Id of user to get data") @PathVariable Long accountId) {
        return accountService.findAccount(accountId);
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Register new user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterDto.class))})})
    public RegisterDto registerUser(@RequestBody RegisterDto registerDto) {
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        accountService.insert(registerDto);
        return registerDto;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @Operation(summary = "Update user account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDetailsDto.class))})})
    public AccountDetailsDto manageUserAccount(@RequestBody AccountDetailsDto accountDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account accountToManage = (Account) accountService.loadUserByUsername(email);
        accountDetails.putDetailsIntoAccount(accountToManage);
        return accountService.update(accountToManage);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @Operation(summary = "Delete user account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))})})
    public Account deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        accountService.delete(account.getId());
        return account;
    }

    @PutMapping(value = "/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Change user status")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserStatusDto.class))})})
    public void changeStatus(@Parameter(description = "Id of user to change status") @PathVariable Long id, @RequestBody UserStatusDto statusDto) {
        accountService.changeStatus(id, statusDto.getStatus());
    }

    @PutMapping("/password")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change user password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdatePasswordDto.class))})})
    public void changePassword(@RequestBody UpdatePasswordDto passwordDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), account.getPassword())) {
            throw ErrorUtils.generateBadRequest("wrongOldPassword");
        }

        passwordDto.encode(passwordEncoder);
        accountService.changePassword(account, passwordDto);
    }

    @PostMapping("/{userId}/notifications")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @Operation(summary = "Send new notification")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))})})
    public Notification createNotification(@Parameter(description = "Id of user to send notification") @PathVariable Long userId, @RequestBody Notification notification) {
        return notificationService.insert(userId, notification);
    }

    @GetMapping("/notifications")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @Operation(summary = "Get all notifications for user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationDto.class, type = "array"))})})
    public Collection<NotificationDto> getUsersNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        User user = account.getUser();
        return user.getNotifications().stream().map(NotificationDto::new).toList();
    }

    @GetMapping("/notification-settings")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @Operation(summary = "Get all notification settings for user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationSettingDto.class, type = "array"))})})
    public Collection<NotificationSettingDto> getUsersNotificationSettings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = (Account) accountService.loadUserByUsername(email);
        User user = account.getUser();
        return user.getNotificationSettings().stream().map(NotificationSettingDto::new).toList();
    }

    @PutMapping("/notification-settings")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @Operation(summary = "Update notification settings for user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationSettingDto.class, type = "array"))})})
    public Collection<NotificationSettingDto> updateNotificationSettings(@RequestBody Collection<NotificationSetting> notificationSettings) {
        return notificationSettings.stream().map(NotificationSettingDto::new).toList(); // mocked
    }

    @PostMapping("/{id}/reports")
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Report user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReportDto.class))})})
    public void reportUser(@Parameter(description = "Id of user to be reported") @PathVariable Long id, @RequestBody ReportDto reportDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account reportingAccount = (Account) accountService.loadUserByUsername(email);
        reportService.reportUser(id,reportingAccount.getId(), reportDto);
    }

    @GetMapping("/{userId}/profile")
    public AccountDetailsDto getAccountDetails(@PathVariable Long userId) {
        Account account = accountService.findAccount(userId);
        return new AccountDetailsDto(account);
    }
}
