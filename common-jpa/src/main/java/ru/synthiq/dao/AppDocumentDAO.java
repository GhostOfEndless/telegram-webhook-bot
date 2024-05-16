package ru.synthiq.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synthiq.entity.AppDocument;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {
}
