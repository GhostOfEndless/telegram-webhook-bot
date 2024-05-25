package ru.synthiq.service;

import ru.synthiq.entity.AppUser;

public interface AppUserService {

    String registerUser(AppUser appUser);
    String setEmail(AppUser appUser, String email);
}
