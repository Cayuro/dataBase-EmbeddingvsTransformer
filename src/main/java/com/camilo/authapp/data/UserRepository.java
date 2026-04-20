package com.camilo.authapp.data;

import com.camilo.authapp.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    void save(User user);

    boolean existsByUsername(String username);
}