package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.constant.TypeContent;
import ru.otus.dto.ContentDto;
import ru.otus.dto.ContentWithContentMediaAndMediaSubtitleDto;
import ru.otus.service.ContentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/api/content")
    public ResponseEntity<List<ContentDto>> getContents(@RequestParam("type") TypeContent type) {
        log.info("/api/content");

        return ResponseEntity.ok(
                contentService.findContentsByType(type)
        );
    }

    @GetMapping("/api/content/{name}")
    public ResponseEntity<List<ContentDto>> getContentsByName(@PathVariable("name") String name) {
        log.info("/api/content/{}", name);

        return ResponseEntity.ok(contentService.findContentsByName(name));
    }

    @GetMapping("/api/content/id/{id}")
    public ResponseEntity<ContentDto> getContentById(@PathVariable("id") Long id) {
        log.info("/api/content/id/{}", id);

        return ResponseEntity.ok(
                contentService.findContentById(id)
        );
    }

    @PostMapping("/api/content")
    public ResponseEntity<String> save(@RequestBody ContentWithContentMediaAndMediaSubtitleDto content) {
        log.info("/api/content");

        contentService.save(content);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
}
