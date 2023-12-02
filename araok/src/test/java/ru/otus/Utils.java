package ru.otus;

import org.junit.jupiter.api.Test;
import ru.otus.domain.AgeLimit;
import ru.otus.domain.Content;
import ru.otus.domain.Language;
import ru.otus.domain.User;
import ru.otus.dto.AgeLimitDto;
import ru.otus.dto.ContentDto;
import ru.otus.dto.LanguageDto;
import ru.otus.dto.UserDto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class Utils {
    public static void assertEqualsContent(Content excepted, Content result) {
        AgeLimit limit = result.getLimit();
        User user = result.getUser();
        Language language = result.getLanguage();

        assertThat(limit).isNotNull()
                .matches(l -> l.getId().equals(excepted.getLimit().getId()))
                .matches(l -> l.getDescription().equals(excepted.getLimit().getDescription()))
                .matches(l -> l.getLimit().equals(excepted.getLimit().getLimit()));

        assertEqualsUser(excepted.getUser(), user);

        assertThat(language).isNotNull()
                .matches(l -> l.getId().equals(excepted.getLanguage().getId()))
                .matches(l -> l.getCode2().equals(excepted.getLanguage().getCode2()))
                .matches(l -> l.getLanguage().equals(excepted.getLanguage().getLanguage()));

        assertThat(result).isNotNull()
                .matches(c -> c.getId().equals(excepted.getId()))
                .matches(c -> c.getArtist().equals(excepted.getArtist()))
                .matches(c -> c.getName().equals(excepted.getName()))
                .matches(c -> c.getCreateDate().equals(excepted.getCreateDate()));
    }

    public static void assertEqualsUser(User excepted, User result) {
        assertThat(result).isNotNull()
                .matches(u -> u.getId().equals(excepted.getId()))
                .matches(u -> u.getName().equals(excepted.getName()))
                .matches(u -> u.getPhone().equals(excepted.getPhone()))
                .matches(u -> u.getPassword().equals(excepted.getPassword()))
                .matches(u -> u.getBirthDate().equals(excepted.getBirthDate()))
                .matches(u -> u.getRole().equals(excepted.getRole()));
    }

    public static void assertEqualsContentDto(Content excepted, ContentDto result) {
        AgeLimitDto limit = result.getLimit();
        UserDto user = result.getUser();
        LanguageDto language = result.getLanguage();

        assertThat(limit).isNotNull()
                .matches(l -> l.getId().equals(excepted.getLimit().getId()))
                .matches(l -> l.getDescription().equals(excepted.getLimit().getDescription()))
                .matches(l -> l.getLimit().equals(excepted.getLimit().getLimit()));

        assertEqualsUserDto(excepted.getUser(), user);

        assertThat(language).isNotNull()
                .matches(l -> l.getId().equals(excepted.getLanguage().getId()))
                .matches(l -> l.getCode2().equals(excepted.getLanguage().getCode2()))
                .matches(l -> l.getLanguage().equals(excepted.getLanguage().getLanguage()));

        assertThat(result).isNotNull()
                .matches(c -> c.getId().equals(excepted.getId()))
                .matches(c -> c.getArtist().equals(excepted.getArtist()))
                .matches(c -> c.getName().equals(excepted.getName()))
                .matches(c -> c.getCreateDate().equals(excepted.getCreateDate()));
    }

    public static void assertEqualsUserDto(User excepted, UserDto result) {
        assertThat(result).isNotNull()
                .matches(u -> u.getId().equals(excepted.getId()))
                .matches(u -> u.getName().equals(excepted.getName()))
                .matches(u -> u.getPhone().equals(excepted.getPhone()))
                .matches(u -> u.getPassword().equals(excepted.getPassword()))
                .matches(u -> u.getBirthDate().equals(excepted.getBirthDate()))
                .matches(u -> u.getRole().equals(excepted.getRole()));
    }

    @Test
    public void test() throws IOException {
        File file = new File("C:\\Users\\user\\Desktop\\ARAOK\\data\\data6\\video6.mp4");
        byte[] bytes = Files.readAllBytes(file.toPath());

        try (PrintWriter fos = new PrintWriter("C:\\Users\\user\\Desktop\\ARAOK\\data\\data6\\video6.txt")) {
            for(byte b: bytes)
                fos.write(b + ", ");
        }
    }

    /*
    fun timerToMilliSeconds(timer: String): Int {
    val minutes = timer.split(":")[0].toInt()
    val seconds = timer.split(":")[1].toInt()

    return minutes * 60 * 1000 + seconds * 1000
}
     */

    private int timerToMillSeconds(String timer) {
        int hour = 0;
        int minute = 0;
        int seconds = 0;
        int milleSeconds = 0;

        if(timer.split(":").length == 3) {
            hour = Integer.parseInt(timer.split(":")[0]);
            minute = Integer.parseInt(timer.split(":")[1]);
            seconds = Integer.parseInt(timer.split(":")[2].substring(0, 2));
            milleSeconds = 0;
        } else {
            minute = Integer.parseInt(timer.split(":")[0]);
            seconds = Integer.parseInt(timer.split(":")[1]);
        }

        if(timer.split(",").length != 0) {
            milleSeconds = Integer.parseInt(timer.split(":")[1]);
        }

        return hour * 60 * 60 * 1000 + minute * 60 * 1000 + seconds * 1000 + milleSeconds;
    }

    @Test
    public void convertTextSubtitleToSubtitle() throws FileNotFoundException {
        try(Scanner scanner = new Scanner(new File("C:\\Users\\user\\Desktop\\ARAOK\\data\\subtitle_godzilla.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Pattern pattern = Pattern.compile("\\[\\d{2}:\\d{2}:\\d{2},\\d{3}\\]", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(line);

                matcher.find();
                String strBeing = matcher.group();

                matcher.find();
                String strEnd = matcher.group();

                int intBegin = timerToMillSeconds(strBeing.replace("[", "").replace("]", ""));
                int intEnd = timerToMillSeconds(strEnd.replace("[", "").replace("]", ""));

                String insertLine = line.replaceAll("\\[\\d{2}:\\d{2}:\\d{2},\\d{3}\\]", "").replace("'", "''");

                System.out.println("insert into subtitle (line, to_begin, from_end, media_subtitle_id) " +
                        "values ('" +  insertLine + "', " + intBegin + ", " + intEnd + ", " + 8 + ");");
            }
        }
    }
}
