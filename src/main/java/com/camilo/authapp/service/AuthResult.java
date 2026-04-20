package com.camilo.authapp.service;

import com.camilo.authapp.model.User;

public record AuthResult(boolean success, String message, User user) {
    public static AuthResult success(String message, User user) {
        return new AuthResult(true, message, user);
    }

    public static AuthResult failure(String message) {
        return new AuthResult(false, message, null);
    }
}