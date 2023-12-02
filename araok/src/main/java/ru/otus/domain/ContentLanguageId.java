package ru.otus.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ContentLanguageId implements Serializable {
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "CONTENT_ID", updatable = false, insertable = false)
    private Content content;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "LANGUAGE_ID", updatable = false, insertable = false)
    private Language language;
}
