package ru.otus.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.domain.AgeLimit;
import ru.otus.domain.Content;
import ru.otus.domain.Language;
import ru.otus.domain.User;
import ru.otus.enums.RoleEnum;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.Utils.assertEqualsContent;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContentRepositoryTest {

    @Autowired
    private ContentRepository contentRepository;

    private Content contentBefore7days;

    private Content contentAfter7days;

    @BeforeEach
    public void setUp() {
        AgeLimit limitBefore7days = AgeLimit.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        AgeLimit limitAfter7days = AgeLimit.builder()
                .id(5L)
                .description("prohibited for children")
                .limit(18L)
                .build();

        Language languageBefore7days = Language.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        Language languageAfter7days = Language.builder()
                .id(2L)
                .code2("EN")
                .language("English")
                .build();

        User user = User.builder()
                .id(1000000000L)
                .name("Maxim")
                .phone("9993338951")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        contentBefore7days = Content.builder()
                .id(1L)
                .name("Unknown Content")
                .limit(limitBefore7days)
                .artist("Unknown Artist")
                .user(user)
                .createDate(LocalDate.now())
                .language(languageBefore7days)
                .build();

        contentAfter7days = Content.builder()
                .id(2L)
                .name("Unknown Content 2")
                .limit(limitAfter7days)
                .artist("Unknown Artist 2")
                .user(user)
                .createDate(LocalDate.now().plusDays(-8))
                .language(languageAfter7days)
                .build();
    }

    @Test
    public void shouldCorrectReturnNewContentBefore7days() {
        contentRepository.save(contentBefore7days);
        contentRepository.save(contentAfter7days);

        List<Content> contentsBefore7days = contentRepository.findByCreateDateLessThanNow();

        assertThat(contentsBefore7days).isNotNull()
                .hasSize(1);

        assertEqualsContent(contentBefore7days, contentsBefore7days.get(0));
    }

    @Test
    public void shouldCorrectReturnAllContent() {
        contentRepository.save(contentBefore7days);
        contentRepository.save(contentAfter7days);

        List<Content> contents = contentRepository.findAll();

        assertThat(contents).isNotNull()
                .hasSize(2);

        Content contentBefore = contents.stream()
                .filter(c -> c.getId().equals(contentBefore7days.getId()))
                .findFirst()
                .orElseThrow();

        Content contentAfter = contents.stream()
                .filter(c -> c.getId().equals(contentAfter7days.getId()))
                .findFirst()
                .orElseThrow();

        assertEqualsContent(contentBefore7days, contentBefore);
        assertEqualsContent(contentAfter7days, contentAfter);
    }

    @Test
    public void shouldCorrectReturnContentByNameIgnoreCase() {
        contentRepository.save(contentBefore7days);
        contentRepository.save(contentAfter7days);

        List<Content> contents = contentRepository.findByNameContainingIgnoreCase("unknown");

        assertThat(contents).isNotNull()
                .hasSize(2);

        Content contentBefore = contents.stream()
                .filter(c -> c.getId().equals(contentBefore7days.getId()))
                .findFirst()
                .orElseThrow();

        Content contentAfter = contents.stream()
                .filter(c -> c.getId().equals(contentAfter7days.getId()))
                .findFirst()
                .orElseThrow();

        assertEqualsContent(contentBefore7days, contentBefore);
        assertEqualsContent(contentAfter7days, contentAfter);
    }

    @Test
    public void shouldCorrectReturnContentById() {
        contentRepository.save(contentBefore7days);

        Content content = contentRepository.findById(contentBefore7days.getId())
                .orElseThrow();

        assertEqualsContent(contentBefore7days, content);
    }
}
