package com.camilo.authapp.service;

import com.camilo.authapp.data.UserRepository;
import com.camilo.authapp.model.User;

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public AuthService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public AuthResult register(String username, String fullName, String password, String confirmPassword) {
        if (isBlank(username) || isBlank(fullName) || isBlank(password) || isBlank(confirmPassword)) {
            return AuthResult.failure("Todos los campos son obligatorios");
        }

        if (!password.equals(confirmPassword)) {
            return AuthResult.failure("Las contraseñas no coinciden");
        }

        if (userRepository.existsByUsername(username)) {
            return AuthResult.failure("El usuario ya existe");
        }

        User user = new User(username, fullName, passwordHasher.hash(password));
        userRepository.save(user);
        return AuthResult.success("Registro exitoso", user);
    }

    public AuthResult login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return AuthResult.failure("Usuario y contraseña son obligatorios");
        }

        return userRepository.findByUsername(username)
                .filter(user -> passwordHasher.matches(password, user.passwordHash()))
                .map(user -> AuthResult.success("Login exitoso", user))
                .orElseGet(() -> AuthResult.failure("Credenciales inválidas"));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}