package com.camilo.authapp.support;

import com.camilo.authapp.data.UserRepository;
import com.camilo.authapp.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public void save(User user) {
        users.put(user.username(), user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }
}