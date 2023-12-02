package ru.otus.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.domain.AgeLimit;
import ru.otus.domain.Content;
import ru.otus.domain.Language;
import ru.otus.domain.Mark;
import ru.otus.domain.Setting;
import ru.otus.domain.User;
import ru.otus.enums.RoleEnum;
import ru.otus.exception.NotFoundSettingException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SettingRepositoryTest {

    private Long CONTENT_ID;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private MarkRepository markRepository;

    private Setting setting;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    public void setUp() {
        AgeLimit limit = AgeLimit.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        User user = User.builder()
                .id(1000000000L)
                .name("Maxim")
                .phone("9993338951")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        Language language = Language.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        Content content = Content.builder()
                .id(1L)
                .name("Unknown Content")
                .limit(limit)
                .artist("Unknown Artist")
                .user(user)
                .createDate(LocalDate.now())
                .language(language)
                .build();

        Mark mark1 = Mark.builder()
                .start(1L)
                .end(2L)
                .repeat(1L)
                .delay(1L)
                .build();

        Mark mark2 = Mark.builder()
                .start(2L)
                .end(3L)
                .repeat(2L)
                .delay(2L)
                .build();

        Mark mark3 = Mark.builder()
                .start(3L)
                .end(4L)
                .repeat(3L)
                .delay(3L)
                .build();

        setting = Setting.builder()
                .marks(List.of(
                        mark1, mark2, mark3
                ))
                .content(content)
                .build();

        contentRepository.save(content);

        CONTENT_ID = content.getId();
    }

    @Test
    public void shouldCorrectSaveSetting() {
        Setting saveSetting = settingRepository.save(setting);

        Setting result = settingRepository.findById(saveSetting.getId()).get();

        assertThatSetting(setting, result);
    }

    @Test
    public void shouldCorrectDeleteSetting() {
        settingRepository.save(setting);

        Setting result = settingRepository.findByContentId(setting.getContent().getId()).get();

        assertThat(result)
                .matches(s -> Objects.nonNull(s.getId()));
        assertThat(result.getMarks()).isNotNull()
                .allMatch(m -> Objects.nonNull(m.getId()));

        assertThatSetting(setting, result);

        em.clear();

        settingRepository.deleteByContentId(result.getContent().getId());
        markRepository.deleteAllById(result.getMarks().stream().map(Mark::getId).toList());

        assertThrows(NotFoundSettingException.class, () -> settingRepository.findById(result.getId()).orElseThrow(NotFoundSettingException::new));
        for (Mark mark: setting.getMarks())
            assertThrows(NotFoundSettingException.class, () -> markRepository.findById(mark.getId()).orElseThrow(NotFoundSettingException::new));
    }

    @Test
    public void shouldCorrectUpdateSetting() {
        Setting saveSetting = settingRepository.save(setting);

        Mark newMark1 = Mark.builder()
                .id(saveSetting.getMarks().get(0).getId())
                .start(10L)
                .end(20L)
                .repeat(30L)
                .delay(40L)
                .build();

        Mark newMark2 = Mark.builder()
                .id(saveSetting.getMarks().get(1).getId())
                .start(50L)
                .end(60L)
                .repeat(70L)
                .delay(80L)
                .build();

        Mark newMark3 = Mark.builder()
                        .start(90L)
                        .end(100L)
                        .repeat(110L)
                        .delay(120L)
                        .build();

        Mark newMark4 = Mark.builder()
                        .start(130L)
                        .end(140L)
                        .repeat(150L)
                        .delay(160L)
                        .build();

        Setting newSetting = Setting.builder()
                .id(saveSetting.getId())
                .content(saveSetting.getContent())
                .marks(List.of(newMark1, newMark2, newMark3, newMark4))
                .build();

        em.clear();

        markRepository.saveAll(newSetting.getMarks());
        settingRepository.save(newSetting);

        Setting result = settingRepository.findById(newSetting.getId()).get();

        assertThatSetting(newSetting, result);
    }

    @Test
    public void shouldCorrectReturnSettingByContentId() {
        settingRepository.save(setting);

        Setting result = settingRepository.findByContentId(setting.getContent().getId()).get();

        assertThatSetting(setting, result);
    }

    private void assertThatSetting(Setting expected, Setting result) {
        assertThatMarkList(expected.getMarks(), result.getMarks());
    }

    private void assertThatMarkList(List<Mark> expected, List<Mark> result) {
        assertThat(result).isNotNull()
                .hasSize(expected.size());

        for(int i = 0; i < expected.size(); i++) {
            assertThatMark(expected.get(i), result.get(i));
        }
    }

    private void assertThatMark(Mark expected, Mark result) {
        assertThat(result).isNotNull()
                .matches(m -> m.getStart().equals(expected.getStart()))
                .matches(m -> m.getEnd().equals(expected.getEnd()))
                .matches(m -> m.getDelay().equals(expected.getDelay()))
                .matches(m -> m.getRepeat().equals(expected.getRepeat()));
    }
}
