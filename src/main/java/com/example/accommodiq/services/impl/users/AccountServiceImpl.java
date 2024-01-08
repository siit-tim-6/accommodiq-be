package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.*;
import com.example.accommodiq.dtos.AccountDetailsDto;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.dtos.UpdatePasswordDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.enums.AccountStatus;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.repositories.AccountRepository;
import com.example.accommodiq.services.interfaces.accommodations.IAccommodationService;
import com.example.accommodiq.services.interfaces.accommodations.IReservationService;
import com.example.accommodiq.services.interfaces.email.IEmailService;
import com.example.accommodiq.services.interfaces.feedback.IReportService;
import com.example.accommodiq.services.interfaces.feedback.IReviewService;
import com.example.accommodiq.services.interfaces.notifications.INotificationSettingService;
import com.example.accommodiq.services.interfaces.users.IAccountService;
import com.example.accommodiq.utilities.ErrorUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class AccountServiceImpl implements IAccountService {
    final AccountRepository allAccounts;

    final IEmailService emailService;

    final INotificationSettingService notificationSettingService;

    final IReservationService reservationService;

    final AccommodationRepository accommodationRepository;
    final IReportService reportService;

    final IReviewService reviewService;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public AccountServiceImpl(AccountRepository allAccounts, IEmailService emailService, INotificationSettingService notificationSettingService, IReservationService reservationService, AccommodationRepository accommodationRepository, IReportService reportService, IReviewService reviewService) {
        this.allAccounts = allAccounts;
        this.emailService = emailService;
        this.notificationSettingService = notificationSettingService;
        this.reservationService = reservationService;
        this.accommodationRepository = accommodationRepository;
        this.reportService = reportService;
        this.reviewService = reviewService;
    }

    @Override
    public Collection<Account> getAll() {
        System.out.println(allAccounts.findAll());
        return allAccounts.findAll();
    }

    @Override
    public Account findAccount(Long accountId) {
        Optional<Account> found = allAccounts.findById(accountId);
        if (found.isEmpty()) {
            String value = bundle.getString("userNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    @Transactional
    public void insert(RegisterDto registerDto) {
        if (emailExists(registerDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        if (registerDto.getRole() == AccountRole.ADMIN)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot register as admin");

        Account account = new Account(registerDto);
        try {
            User user = account.getUser();
            if (account.getRole() == AccountRole.GUEST) {
                account.setUser(new Guest(user));
            }
            if (account.getRole() == AccountRole.HOST) {
                account.setUser(new Host(user));
            }
            allAccounts.save(account);
            allAccounts.flush();
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account cannot be inserted");
        }
        notificationSettingService.setNotificationSettingsForUser(account.getUser(), account.getRole());
        emailService.sendVerificationEmail(account.getId(), account.getEmail());
    }

    @Override
    public AccountDetailsDto update(Account account) {
        try {
            findAccount(account.getId()); // this will throw ResponseStatusException if account is not found
            allAccounts.save(account);
            allAccounts.flush();
            return new AccountDetailsDto(account);
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException)) {
                e = c;
            }
            if ((c != null)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account cannot be updated");
            }
            throw ex;
        }
    }

    @Override
    public Account delete(Long accountId) {
        Account found = findAccount(accountId); // this will throw AccountNotFoundException if Account is not found
        if (found.getRole() == AccountRole.HOST) {
            List<Reservation> reservations = reservationService.findHostReservationsNotEndedYet(accountId);
            if (!reservations.isEmpty()) {
                throw ErrorUtils.generateBadRequest("hostHasAcceptedReservations");
            }
            accommodationRepository.deleteAllByHostId(accountId);
            accommodationRepository.flush();
        }

        if (found.getRole() == AccountRole.GUEST) {
            reviewService.deleteByGuestId(accountId);

            List<Reservation> reservations = reservationService.findGuestAcceptedReservationsNotEndedYet(accountId);
            if (!reservations.isEmpty()) {
                throw ErrorUtils.generateBadRequest("guestHasAcceptedReservations");
            }

            reservationService.deleteByGuestId(accountId);
        }

        reportService.deleteByReportingUserId(accountId);
        reportService.deleteByReportedUserId(accountId);

        allAccounts.delete(found);
        allAccounts.flush();
        return found;
    }

    @Override
    public void deleteAll() {
        allAccounts.deleteAll();
        allAccounts.flush();
    }

    @Override
    public void changeStatus(Long id, AccountStatus accountStatus) {
        Account account = findAccount(id);
        account.setStatus(accountStatus);
        allAccounts.save(account);
        allAccounts.flush();
    }

    @Override
    public void changePassword(Account account, UpdatePasswordDto passwordDto) {
        account.setPassword(passwordDto.getNewPassword());
        allAccounts.save(account);
        allAccounts.flush();
    }

    private boolean emailExists(String email) {
        return allAccounts.findAccountByEmail(email) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = allAccounts.findAccountByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }

    @Override
    public AccountDetailsDto getAccountDetails(Long accountId) {
        Account account = findAccount(accountId);
        return new AccountDetailsDto(account);
    }
}
