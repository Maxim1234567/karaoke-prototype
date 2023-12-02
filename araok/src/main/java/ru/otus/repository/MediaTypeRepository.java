package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.MediaType;

import java.util.Optional;

public interface MediaTypeRepository extends JpaRepository<MediaType, Long> {
    Optional<MediaType> findById(Long id);
}
