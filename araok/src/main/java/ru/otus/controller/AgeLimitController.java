package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.AgeLimitDto;
import ru.otus.service.AgeLimitService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AgeLimitController {
    private final AgeLimitService ageLimitService;

    @GetMapping("/api/limit")
    public ResponseEntity<List<AgeLimitDto>> getAll() {
        log.info("/api/limit");

        return ResponseEntity.ok(ageLimitService.getAll());
    }
}
