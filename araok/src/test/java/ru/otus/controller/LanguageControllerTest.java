package ru.otus.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.dto.LanguageDto;
import ru.otus.security.filter.JwtFilter;
import ru.otus.service.LanguageService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LanguageController.class)
public class LanguageControllerTest {
    private static String JSON_LANGUAGES =
            "[\n" +
            "    {\n" +
            "        \"id\": 1,\n" +
            "        \"language\": \"Russian\",\n" +
            "        \"code2\": \"RU\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 2,\n" +
            "        \"language\": \"English\",\n" +
            "        \"code2\": \"EN\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 3,\n" +
            "        \"language\": \"German\",\n" +
            "        \"code2\": \"DE\"\n" +
            "    }\n" +
            "]";

    @MockBean
    private LanguageService languageService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private List<LanguageDto> languages;

    @BeforeEach
    public void setUp() {
        LanguageDto language1 = LanguageDto.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        LanguageDto language2 = LanguageDto.builder()
                .id(2L)
                .code2("EN")
                .language("English")
                .build();

        LanguageDto language3 = LanguageDto.builder()
                .id(3L)
                .code2("DE")
                .language("German")
                .build();

        languages = List.of(
                language1, language2, language3
        );

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser
    public void shouldCorrectReturnAllLanguages() throws Exception {
        given(languageService.getAll())
                .willReturn(languages);

        mvc.perform(get("/api/language")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
        ).andExpect(status().isOk())
        .andExpect(content().json(JSON_LANGUAGES));

        verify(languageService, times(1)).getAll();
    }
}
