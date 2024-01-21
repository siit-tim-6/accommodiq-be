package com.example.accommodiq.services.interfaces.users;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;

public interface ISessionService {
    LoginResponseDto login(CredentialsDto credentialsDto);
}
