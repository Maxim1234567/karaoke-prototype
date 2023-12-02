package ru.otus.service;


import ru.otus.domain.JwtAuthentication;
import ru.otus.dto.JwtRequest;
import ru.otus.dto.JwtResponse;
import ru.otus.dto.UserDto;
import ru.otus.dto.UserWithJwtResponse;

public interface AuthService {
    UserWithJwtResponse saveAndGenerateToken(UserDto user);

    JwtResponse login(JwtRequest authRequest);

    JwtResponse getAccessToken(String refreshToken);

    JwtResponse refresh(String refreshToken);

    JwtAuthentication getAuthInfo();
}
