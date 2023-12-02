package ru.otus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SUBTITLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subtitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String line;

    @Column(name = "TO_BEGIN")
    private Long to;

    @Column(name = "FROM_END")
    private Long from;
}
