package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.Language;

import java.util.List;

public interface LanguageRepository extends CrudRepository<Language, Long> {
    @Override
    List<Language> findAll();
}
