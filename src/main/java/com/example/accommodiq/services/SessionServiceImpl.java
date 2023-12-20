package com.example.accommodiq.services;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.security.jwt.JwtTokenUtil;
import com.example.accommodiq.services.interfaces.ISessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
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
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(credentialsDto.getEmail(),
                credentialsDto.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);


        Optional<? extends GrantedAuthority> s = sc.getAuthentication().getAuthorities().stream().findFirst();
        if (s.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        AccountRole role = (AccountRole) s.get();

        String token = jwtTokenUtil.generateToken(credentialsDto.getEmail(), role);
        return new LoginResponseDto(role, token);
    }
}
