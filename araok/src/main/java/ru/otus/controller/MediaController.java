package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.ContentMediaDto;
import ru.otus.service.MediaService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @GetMapping(value = "/api/media/{contentId}/{typeId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getMedia(@PathVariable("contentId") Long contentId, @PathVariable("typeId") Long typeId) {
        log.info("/api/media/{}/{}", contentId, typeId);

        return ResponseEntity.ok(
                mediaService.findMediaByContentIdAndTypeId(contentId, typeId)
        );
    }

    @PostMapping(value = "/api/media")
    public ResponseEntity<ContentMediaDto> save(@RequestBody ContentMediaDto contentMedia) {
        log.info("/api/media");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                mediaService.save(contentMedia)
        );
    }
}
