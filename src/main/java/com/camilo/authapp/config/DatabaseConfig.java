package com.camilo.authapp.config;

public record DatabaseConfig(String url, String username, String password) {
    public static DatabaseConfig fromEnvironment() {
        String url = valueOrDefault("DB_URL", "jdbc:mysql://localhost:3306/auth_demo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8");
        String username = valueOrDefault("DB_USER", "auth_user");
        String password = valueOrDefault("DB_PASSWORD", "auth_password");
        return new DatabaseConfig(url, username, password);
    }

    private static String valueOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}