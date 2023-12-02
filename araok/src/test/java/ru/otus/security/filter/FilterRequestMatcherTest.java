package ru.otus.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import ru.otus.property.RequestProperties;
import ru.otus.service.PropertyProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FilterRequestMatcherTest {
    private FilterRequestMatcher filterRequestMatcher;

    @Mock
    private PropertyProvider propertyProvider;

    private List<RequestProperties> requests;

    @BeforeEach
    public void setUp() {
        RequestProperties request1 = new RequestProperties(
                "^(http|https)://(.+):[0-9]+/api/limit$",
                "GET"
        );

        RequestProperties request2 = new RequestProperties(
                "^(http|https)://(.+):[0-9]+/api/content$",
                "GET"
        );

        RequestProperties request3 = new RequestProperties(
                "^(http|https)://(.+):[0-9]+/api/content/[0-9a-zA-Z ]+$",
                "GET"
        );

        RequestProperties request4 = new RequestProperties(
                "^(http|https)://(.+):[0-9]+/api/language$",
                "GET"
        );

        RequestProperties request5 = new RequestProperties(
                "^(http|https)://(.+):[0-9]+/api/media/[0-9]+/3$",
                "GET"
        );

        requests = List.of(
                request1, request2, request3, request4, request5
        );

        filterRequestMatcher = new FilterRequestMatcher(propertyProvider);
    }

    @Test
    public void shouldReturnTruePathApiLimit() {
        assertTruePath("https://host:0000/api/limit");
    }

    @Test
    public void shouldReturnFalsePathApiLimit() {
        assertFalsePath("https://host:0000/api/limits");
    }

    @Test
    public void shouldReturnTruePathApiContent() {
        assertTruePath("https://host:0000/api/content");
    }

    @Test
    public void shouldReturnFalsePathApiContent() {
        assertFalsePath("https://host:0000/api/contents");
    }

    @Test
    public void shouldReturnTruePathApiContentName() {
        assertTruePath("https://host:0000/api/content/correct name");
    }

    @Test
    public void shouldReturnFalsePathApiContentName() {
        assertFalsePath("https://host:0000/api/content/id/12345");
    }

    @Test
    public void shouldReturnTruePathApiLanguage() {
        assertTruePath("https://host:0000/api/language");
    }

    @Test
    public void shouldReturnFalsePathApiLanguage() {
        assertFalsePath("https://host:0000/api/languages");
    }

    @Test
    public void shouldReturnTruePathApiMedia() {
        assertTruePath("https://host:0000/api/media/12345/3");
    }

    @Test
    public void shouldReturnFalsePathApiMedia() {
        assertFalsePath("https://host:0000/api/media");
    }

    private void assertTruePath(String url) {
        given(propertyProvider.getRequests())
                .willReturn(requests);

        HttpServletRequest request = TestHttpServletRequest.builder()
                .requestURI(url)
                .requestURL(url)
                .method(HttpMethod.GET)
                .build();

        assertTrue(filterRequestMatcher.matches(request));
    }

    private void assertFalsePath(String url) {
        given(propertyProvider.getRequests())
                .willReturn(requests);

        HttpServletRequest request = TestHttpServletRequest.builder()
                .requestURI(url)
                .requestURL(url)
                .method(HttpMethod.GET)
                .build();

        assertFalse(filterRequestMatcher.matches(request));
    }
}