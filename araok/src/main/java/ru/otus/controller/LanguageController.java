package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.LanguageDto;
import ru.otus.service.LanguageService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping("/api/language")
    public ResponseEntity<List<LanguageDto>> getAll() {
        log.info("/api/language");

        return ResponseEntity.ok(languageService.getAll());
    }
}
