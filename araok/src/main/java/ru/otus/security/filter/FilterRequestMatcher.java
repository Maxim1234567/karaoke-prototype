package ru.otus.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import ru.otus.service.PropertyProvider;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterRequestMatcher implements RequestMatcher {
    private final PropertyProvider propertyProvider;

    @Override
    public boolean matches(HttpServletRequest request) {
        String pathUri = String.valueOf(request.getRequestURL());
        String method = request.getMethod();

        boolean result = matches(pathUri, method);

        log.info("pathUri: " + pathUri);
        log.info("method: " + method);
        log.info("matches: " + result);

        return result;
    }


    private boolean matches(String pathUri, String method) {
        return propertyProvider.getRequests()
                .stream()
                .map(r -> r.getMethod().equals(method) && Pattern.matches(r.getRegexpUrl(), pathUri))
                .filter(b -> b == Boolean.TRUE)
                .findFirst()
                .orElse(Boolean.FALSE);
    }
}
