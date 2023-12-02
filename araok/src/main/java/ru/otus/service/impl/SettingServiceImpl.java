package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.Mark;
import ru.otus.domain.Setting;
import ru.otus.dto.ContentDto;
import ru.otus.dto.SettingDto;
import ru.otus.exception.NotFoundContentException;
import ru.otus.exception.NotFoundSettingException;
import ru.otus.repository.ContentRepository;
import ru.otus.repository.MarkRepository;
import ru.otus.repository.SettingRepository;
import ru.otus.service.SettingService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final MarkRepository markRepository;

    private final SettingRepository settingRepository;

    private final ContentRepository contentRepository;

    @Override
    @Transactional
    public SettingDto save(SettingDto setting) {
        Setting oldSetting = settingRepository.findByContentId(setting.getContent().getId())
                .orElse(Setting.builder()
                        .content(null)
                        .marks(List.of())
                        .build());

        if(Objects.isNull(oldSetting.getContent()))
            oldSetting.setContent(
                    contentRepository.findById(setting.getContent().getId())
                            .orElseThrow(NotFoundContentException::new)
            );


        markRepository.deleteAllByIds(oldSetting.getMarks().stream().map(Mark::getId).toList());
        settingRepository.deleteByContentId(setting.getContent().getId());

        setting.setContent(ContentDto.toDto(oldSetting.getContent()));

        return SettingDto.toDto(
                settingRepository.save(SettingDto.toDomainObject(setting))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SettingDto findByContentId(long contentId) {
        return settingRepository
                .findByContentId(contentId)
                .map(SettingDto::toDto)
                .orElseThrow(NotFoundSettingException::new);
    }
}
