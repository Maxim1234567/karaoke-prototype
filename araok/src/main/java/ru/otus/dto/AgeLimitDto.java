package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.AgeLimit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgeLimitDto {
    private Long id;

    private String description;

    private Long limit;

    public static AgeLimit toDomainObject(AgeLimitDto dto) {
        return AgeLimit.builder()
                .id(dto.id)
                .description(dto.description)
                .limit(dto.limit)
                .build();
    }

    public static AgeLimitDto toDto(AgeLimit ageLimit) {
        return AgeLimitDto.builder()
                .id(ageLimit.getId())
                .description(ageLimit.getDescription())
                .limit(ageLimit.getLimit())
                .build();
    }
}
