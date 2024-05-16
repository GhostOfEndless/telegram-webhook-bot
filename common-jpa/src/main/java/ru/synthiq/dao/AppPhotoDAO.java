package ru.synthiq.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synthiq.entity.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}
