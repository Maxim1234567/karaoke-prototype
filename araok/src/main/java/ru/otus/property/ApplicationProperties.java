package ru.otus.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.otus.service.PropertyProvider;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties implements PropertyProvider {
    private final long countContentDownloads;

    private final List<RequestProperties> requests;
}
