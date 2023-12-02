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
public class ContentTypeId implements Serializable {
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "CONTENT_ID", insertable = false, updatable = false)
    private Content content;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "TYPE_ID", insertable = false, updatable = false)
    private MediaType mediaType;
}
