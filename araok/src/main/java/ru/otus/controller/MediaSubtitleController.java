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
import ru.otus.dto.LanguageDto;
import ru.otus.dto.MediaSubtitleDto;
import ru.otus.service.MediaSubtitleService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MediaSubtitleController {
    private final MediaSubtitleService mediaSubtitleService;

    @GetMapping("/api/subtitle/{contentId}/{languageId}")
    public ResponseEntity<MediaSubtitleDto> getSubtitle(@PathVariable("contentId") Long contentId, @PathVariable("languageId") Long languageId) {
        log.info("/api/subtitle/{}/{}", contentId, languageId);

        return ResponseEntity.ok(
                mediaSubtitleService.findMediaSubtitleByContentIdAndLanguageId(
                        contentId,
                        languageId
                )
        );
    }

    @PostMapping("/api/subtitle")
    public ResponseEntity<MediaSubtitleDto> save(@RequestBody MediaSubtitleDto mediaSubtitle) {
        log.info("/api/subtitle");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                mediaSubtitleService.save(mediaSubtitle)
        );
    }

    @GetMapping("/api/subtitle/{contentId}")
    public ResponseEntity<List<LanguageDto>> getAllLanguage(@PathVariable("contentId") long contentId) {
        log.info("/api/subtitle/{}", contentId);

        return ResponseEntity.ok(
                mediaSubtitleService.findAllLanguageSubtitleByContentId(contentId)
        );
    }
}
