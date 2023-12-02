package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.JwtRequest;
import ru.otus.dto.JwtResponse;
import ru.otus.dto.RefreshJwtRequest;
import ru.otus.dto.UserDto;
import ru.otus.dto.UserWithJwtResponse;
import ru.otus.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/auth/registration")
    public ResponseEntity<UserWithJwtResponse> registration(@RequestBody UserDto user) {
        log.info("/auth/registration");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.saveAndGenerateToken(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        log.info("/auth/login");

        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        log.info("/auth/token");

        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        log.info("/auth/refresh");

        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
