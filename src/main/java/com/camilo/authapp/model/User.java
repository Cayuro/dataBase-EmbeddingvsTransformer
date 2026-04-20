package com.camilo.authapp.model;

public record User(String username, String fullName, String passwordHash) {
}