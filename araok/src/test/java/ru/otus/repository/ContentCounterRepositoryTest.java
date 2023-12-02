package ru.otus.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.domain.AgeLimit;
import ru.otus.domain.Content;
import ru.otus.domain.ContentCounter;
import ru.otus.domain.Language;
import ru.otus.domain.User;
import ru.otus.enums.RoleEnum;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.Utils.assertEqualsContent;
import static ru.otus.Utils.assertEqualsUser;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContentCounterRepositoryTest {
    @Autowired
    private ContentCounterRepository contentCounterRepository;

    @Autowired
    private ContentRepository contentRepository;

    private ContentCounter contentGreaterThanThousand;

    private ContentCounter contentLessThanThousand;

    private ContentCounter contentEqualsThousand;

    @BeforeEach
    public void setUp() {
        AgeLimit limit1 = AgeLimit.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        AgeLimit limit2 = AgeLimit.builder()
                .id(5L)
                .description("prohibited for children")
                .limit(18L)
                .build();

        AgeLimit limit3 = AgeLimit.builder()
                .id(3L)
                .description("for children over 12 years of age")
                .limit(12L)
                .build();

        Language language1 = Language.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        Language language2 = Language.builder()
                .id(2L)
                .code2("EN")
                .language("English")
                .build();

        Language language3 = Language.builder()
                .id(3L)
                .code2("DE")
                .language("German")
                .build();

        User user = User.builder()
                .id(1000000000L)
                .name("Maxim")
                .phone("9993338951")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        Content content1 = Content.builder()
                .name("Unknown Content")
                .limit(limit1)
                .artist("Unknown Artist")
                .user(user)
                .createDate(LocalDate.now())
                .language(language1)
                .build();

        Content content2 = Content.builder()
                .name("Unknown Content 2")
                .limit(limit2)
                .artist("Unknown Artist 2")
                .user(user)
                .createDate(LocalDate.now().plusDays(8))
                .language(language2)
                .build();

        Content content3 = Content.builder()
                .name("Unknown Content 3")
                .limit(limit3)
                .artist("Unknown Artist 3")
                .createDate(LocalDate.now().plusDays(12))
                .language(language3)
                .build();

        contentRepository.save(content1);
        contentRepository.save(content2);
        contentRepository.save(content3);

        contentGreaterThanThousand = ContentCounter.builder()
                .content(content1)
                .user(user)
                .count(1001L)
                .build();

        contentLessThanThousand = ContentCounter.builder()
                .content(content2)
                .user(user)
                .count(999L)
                .build();

        contentEqualsThousand = ContentCounter.builder()
                .content(content3)
                .user(user)
                .count(1000L)
                .build();
    }

    @Test
    public void shouldCorrectReturnContentGreaterThanThousand() {
        contentCounterRepository.save(contentGreaterThanThousand);
        contentCounterRepository.save(contentLessThanThousand);
        contentCounterRepository.save(contentEqualsThousand);

        List<ContentCounter> contentCounters = contentCounterRepository.findByCountGreaterThan(1000L);

        assertThat(contentCounters).isNotNull()
                .hasSize(1);

        assertThat(contentCounters.get(0)).isNotNull()
                .matches(c -> c.getId().equals(contentGreaterThanThousand.getId()))
                .matches(c -> c.getCount().equals(contentGreaterThanThousand.getCount()));

        assertEqualsUser(contentGreaterThanThousand.getUser(), contentCounters.get(0).getUser());
        assertEqualsContent(contentGreaterThanThousand.getContent(), contentCounters.get(0).getContent());
    }
}
