package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.Setting;
import ru.otus.dto.AgeLimitDto;
import ru.otus.dto.ContentDto;
import ru.otus.dto.LanguageDto;
import ru.otus.dto.MarkDto;
import ru.otus.dto.SettingDto;
import ru.otus.dto.UserDto;
import ru.otus.enums.RoleEnum;
import ru.otus.exception.NotFoundContentException;
import ru.otus.exception.NotFoundSettingException;
import ru.otus.repository.ContentRepository;
import ru.otus.repository.MarkRepository;
import ru.otus.repository.SettingRepository;
import ru.otus.service.impl.SettingServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SettingServiceTest {
    @Mock
    private MarkRepository markRepository;

    @Mock
    private SettingRepository settingRepository;

    @Mock
    private ContentRepository contentRepository;

    private SettingService settingService;

    private SettingDto setting;

    @BeforeEach
    public void setUp() {
        AgeLimitDto limit = AgeLimitDto.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        UserDto user = UserDto.builder()
                .id(1L)
                .name("Maxim")
                .phone("89993338951")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        LanguageDto language = LanguageDto.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        ContentDto content = ContentDto.builder()
                .id(1L)
                .name("Unknown Content")
                .limit(limit)
                .artist("Unknown Artist")
                .user(user)
                .createDate(LocalDate.now())
                .language(language)
                .build();

        MarkDto mark1 = MarkDto.builder()
                .start(1L)
                .end(2L)
                .repeat(1L)
                .delay(1L)
                .build();

        MarkDto mark2 = MarkDto.builder()
                .start(2L)
                .end(3L)
                .repeat(2L)
                .delay(2L)
                .build();

        MarkDto mark3 = MarkDto.builder()
                .start(3L)
                .end(4L)
                .repeat(3L)
                .delay(3L)
                .build();

        setting = SettingDto.builder()
                .marks(List.of(
                        mark1, mark2, mark3
                ))
                .content(content)
                .build();

        settingService = new SettingServiceImpl(
                markRepository,
                settingRepository,
                contentRepository
        );
    }

    @Test
    public void shouldCorrectSaveSetting() {
        given(settingRepository.findByContentId(eq(setting.getContent().getId())))
                .willReturn(Optional.of(SettingDto.toDomainObject(setting)));
        given(settingRepository.save(any(Setting.class)))
                .willReturn(SettingDto.toDomainObject(setting));

        SettingDto result = settingService.save(setting);

        verify(settingRepository, times(1))
                .save(any(Setting.class));
        verify(settingRepository, times(1))
                .findByContentId(eq(setting.getContent().getId()));

        assertThatSetting(setting, result);
    }

    @Test
    public void shouldNotThrowIfOptionalEmpty() {
        given(settingRepository.findByContentId(eq(setting.getContent().getId())))
                .willReturn(Optional.empty());
        given(settingRepository.save(any(Setting.class)))
                .willReturn(SettingDto.toDomainObject(setting));
        given(contentRepository.findById(eq(setting.getContent().getId())))
                .willReturn(Optional.of(ContentDto.toDomainObject(setting.getContent())));

        SettingDto result = assertDoesNotThrow(() -> settingService.save(setting));

        verify(settingRepository, times(1))
                .findByContentId(eq(setting.getContent().getId()));
        verify(settingRepository, times(1))
                .save(any(Setting.class));
        verify(contentRepository, times(1))
                .findById(eq(setting.getContent().getId()));

        assertThatSetting(setting, result);
    }

    @Test
    public void shouldThrowNotFoundContentIfOptionalEmptyAndContentNotFound() {
        given(settingRepository.findByContentId(eq(setting.getContent().getId())))
                .willReturn(Optional.empty());
        given(contentRepository.findById(eq(setting.getContent().getId())))
                .willReturn(Optional.empty());

        assertThrows(NotFoundContentException.class, () -> settingService.save(setting));

        verify(settingRepository, times(1))
                .findByContentId(eq(setting.getContent().getId()));
        verify(settingRepository, times(0))
                .save(any(Setting.class));
        verify(contentRepository, times(1))
                .findById(eq(setting.getContent().getId()));
    }

    @Test
    public void shouldCorrectReturnSettingByContentId() {
        given(settingRepository.findByContentId(eq(setting.getContent().getId())))
                .willReturn(Optional.of(SettingDto.toDomainObject(setting)));

        SettingDto result = settingService.findByContentId(setting.getContent().getId());

        verify(settingRepository, times(1))
                .findByContentId(eq(setting.getContent().getId()));

        assertThatSetting(setting, result);
    }

    @Test
    public void shouldThrowNotFoundSettingExceptionEmptyFindByContentId() {
        given(settingRepository.findByContentId(eq(setting.getContent().getId())))
                .willReturn(Optional.empty());

        assertThrows(NotFoundSettingException.class, () -> settingService.findByContentId(setting.getContent().getId()));

        verify(settingRepository, times(1))
                .findByContentId(eq(setting.getContent().getId()));
    }

    private void assertThatSetting(SettingDto expected, SettingDto result) {
        assertThatMarkList(expected.getMarks(), result.getMarks());
    }

    private void assertThatMarkList(List<MarkDto> expected, List<MarkDto> result) {
        assertThat(result).isNotNull()
                .hasSize(expected.size());

        for(int i = 0; i < expected.size(); i++) {
            assertThatMark(expected.get(i), result.get(i));
        }
    }

    private void assertThatMark(MarkDto expected, MarkDto result) {
        assertThat(result).isNotNull()
                .matches(m -> m.getStart().equals(expected.getStart()))
                .matches(m -> m.getEnd().equals(expected.getEnd()))
                .matches(m -> m.getDelay().equals(expected.getDelay()))
                .matches(m -> m.getRepeat().equals(expected.getRepeat()));
    }
}