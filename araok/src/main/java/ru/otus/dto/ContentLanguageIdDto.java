package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentLanguageId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentLanguageIdDto {
    private ContentDto content;

    private LanguageDto language;

    public static ContentLanguageId toDomainObject(ContentLanguageIdDto dto) {
        return ContentLanguageId.builder()
                .content(
                        ContentDto.toDomainObject(dto.getContent())
                )
                .language(
                        LanguageDto.toDomainObject(dto.getLanguage())
                )
                .build();
    }

    public static ContentLanguageIdDto toDto(ContentLanguageId contentLanguageId) {
        return ContentLanguageIdDto.builder()
                .content(
                        ContentDto.toDto(contentLanguageId.getContent())
                )
                .language(
                        LanguageDto.toDto(contentLanguageId.getLanguage())
                )
                .build();
    }
}
