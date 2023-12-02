package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentLanguage;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentLanguageDto {
    private ContentLanguageIdDto contentLanguageId;

    public static ContentLanguage toDomainObject(ContentLanguageDto dto) {
        return ContentLanguage.builder()
                .contentLanguageId(
                        ContentLanguageIdDto.toDomainObject(dto.contentLanguageId)
                )
                .build();
    }

    public static ContentLanguageDto toDto(ContentLanguage contentLanguage) {
        return ContentLanguageDto.builder()
                .contentLanguageId(
                        ContentLanguageIdDto.toDto(contentLanguage.getContentLanguageId())
                )
                .build();
    }
}
