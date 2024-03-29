package com.example.accommodiq.services.impl.users;

import com.example.accommodiq.domain.Account;
import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.security.jwt.JwtTokenUtil;
import com.example.accommodiq.services.interfaces.users.ISessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class SessionServiceImpl implements ISessionService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public SessionServiceImpl(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public LoginResponseDto login(CredentialsDto credentialsDto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentialsDto.getEmail(), credentialsDto.getPassword())
            );
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);

            Optional<? extends GrantedAuthority> s = sc.getAuthentication().getAuthorities().stream().findFirst();
            if (s.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            AccountRole role = (AccountRole) s.get();
            long userId = ((Account) sc.getAuthentication().getPrincipal()).getUser().getId();

            String token = jwtTokenUtil.generateToken(credentialsDto.getEmail(), role, userId);
            return new LoginResponseDto(role, token);
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is not active. Make sure you have verified your email.");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}
