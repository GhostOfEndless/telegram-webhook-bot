package ru.synthiq.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synthiq.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
