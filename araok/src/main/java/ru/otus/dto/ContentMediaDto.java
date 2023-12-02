package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentMedia;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentMediaDto {
    private ContentMediaIdDto contentMediaId;

    private byte[] media;

    public static ContentMedia toDomainObject(ContentMediaDto dto) {
        return ContentMedia.builder()
                .contentMediaId(
                        ContentMediaIdDto.toDomainObject(dto.contentMediaId)
                )
                .media(dto.media)
                .build();
    }

    public static ContentMediaDto toDto(ContentMedia contentMedia) {
        return ContentMediaDto.builder()
                .contentMediaId(
                        ContentMediaIdDto.toDto(contentMedia.getContentMediaId())
                )
                .media(contentMedia.getMedia())
                .build();
    }
}
