package ru.synthiq.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synthiq.entity.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
