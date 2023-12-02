package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.Setting;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingDto {
    private Long id;

    private ContentDto content;

    private List<MarkDto> marks;

    public static Setting toDomainObject(SettingDto dto) {
        return Setting.builder()
                .id(dto.id)
                .content(ContentDto.toDomainObject(dto.content))
                .marks(dto.marks.stream().map(MarkDto::toDomainObject).toList())
                .build();
    }

    public static SettingDto toDto(Setting setting) {
        return SettingDto.builder()
                .id(setting.getId())
                .content(ContentDto.toDto(setting.getContent()))
                .marks(setting.getMarks().stream().map(MarkDto::toDto).toList())
                .build();
    }
}