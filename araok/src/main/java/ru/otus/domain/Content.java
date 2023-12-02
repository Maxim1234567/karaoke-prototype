package ru.otus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "CONTENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "LIMIT_ID")
    private AgeLimit limit;

    private String artist;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "CREATE_DATE", updatable = false)
    private LocalDate createDate;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "LANGUAGE_ID")
    private Language language;
}
