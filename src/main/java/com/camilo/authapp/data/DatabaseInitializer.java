package com.camilo.authapp.data;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private final MySqlConnectionFactory connectionFactory;

    public DatabaseInitializer(MySqlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void initialize() {
        String ddl = """
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    username VARCHAR(80) NOT NULL,
                    full_name VARCHAR(120) NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_users_username (username)
                )
                """;

        try (Connection connection = connectionFactory.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(ddl);
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo inicializar la base de datos", exception);
        }
    }
}