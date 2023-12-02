package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.SettingDto;
import ru.otus.service.SettingService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SettingController {
    private final SettingService settingService;

    @PostMapping("/api/setting")
    public ResponseEntity<SettingDto> save(@RequestBody SettingDto setting) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(settingService.save(setting));
    }

    @GetMapping("/api/setting/{contentId}")
    public ResponseEntity<SettingDto> getSettingByContentId(@PathVariable("contentId") long contentId) {
        return ResponseEntity.ok(
                settingService.findByContentId(contentId)
        );
    }
}
