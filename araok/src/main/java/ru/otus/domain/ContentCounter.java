package ru.otus.domain;

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

@Entity
@Table(name = "CONTENT_COUNTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "CONTENT_ID")
    private Content content;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    private User user;

    private Long count;
}
