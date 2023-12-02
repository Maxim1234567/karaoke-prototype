package ru.otus.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Table(name = "CONTENT_MEDIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentMedia {
    @EmbeddedId
    private ContentMediaId contentMediaId;

    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    private byte[] media;
}
