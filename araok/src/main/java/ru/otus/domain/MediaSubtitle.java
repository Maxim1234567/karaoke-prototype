package ru.otus.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "MEDIA_SUBTITLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaSubtitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "CONTENT_ID")
    private Content content;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "LANGUAGE_ID")
    private Language language;

    @OneToMany(targetEntity = Subtitle.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDIA_SUBTITLE_ID")
    private List<Subtitle> subtitles;
}
