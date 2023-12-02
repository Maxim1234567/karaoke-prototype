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
import ru.otus.dto.AgeLimitDto;
import ru.otus.security.filter.JwtFilter;
import ru.otus.service.AgeLimitService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgeLimitController.class)
public class AgeLimitControllerTest {
    private static String JSON_AGE_LIMITS =
            "[\n" +
            "    {\n" +
            "        \"id\": 1,\n" +
            "        \"description\": \"for children under 6 years of age\",\n" +
            "        \"limit\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 5,\n" +
            "        \"description\": \"prohibited for children\",\n" +
            "        \"limit\": 18\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 3,\n" +
            "        \"description\": \"for children over 12 years of age\",\n" +
            "        \"limit\": 12\n" +
            "    }\n" +
            "]";

    @MockBean
    private AgeLimitService ageLimitService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private List<AgeLimitDto> ageLimits;

    @BeforeEach
    public void setUp() {
        AgeLimitDto limit1 = AgeLimitDto.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        AgeLimitDto limit2 = AgeLimitDto.builder()
                .id(5L)
                .description("prohibited for children")
                .limit(18L)
                .build();

        AgeLimitDto limit3 = AgeLimitDto.builder()
                .id(3L)
                .description("for children over 12 years of age")
                .limit(12L)
                .build();

        ageLimits = List.of(
                limit1, limit2, limit3
        );

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser
    public void shouldCorrectReturnAllAgeLimits() throws Exception {
        given(ageLimitService.getAll())
                .willReturn(ageLimits);

        mvc.perform(get("/api/limit")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
        ).andExpect(status().isOk())
        .andExpect(content().json(JSON_AGE_LIMITS));

        verify(ageLimitService, times(1)).getAll();
    }
}
