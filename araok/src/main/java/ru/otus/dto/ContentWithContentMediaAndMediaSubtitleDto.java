package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentWithContentMediaAndMediaSubtitleDto {
    private ContentDto content;

    private MediaSubtitleDto mediaSubtitle;

    private ContentMediaDto preview;

    private ContentMediaDto video;
}
