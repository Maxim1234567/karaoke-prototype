package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.Language;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {
    private Long id;

    private String language;

    private String code2;

    public static Language toDomainObject(LanguageDto dto) {
        return Language.builder()
                .id(dto.id)
                .language(dto.language)
                .code2(dto.code2)
                .build();
    }

    public static LanguageDto toDto(Language language) {
        return LanguageDto.builder()
                .id(language.getId())
                .language(language.getLanguage())
                .code2(language.getCode2())
                .build();
    }
}
