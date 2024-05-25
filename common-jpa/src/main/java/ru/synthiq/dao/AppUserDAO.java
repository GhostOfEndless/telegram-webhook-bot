package ru.synthiq.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synthiq.entity.AppUser;

import java.util.Optional;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByTelegramUserId(Long id);
    Optional<AppUser> findByEmail(String email);
}
