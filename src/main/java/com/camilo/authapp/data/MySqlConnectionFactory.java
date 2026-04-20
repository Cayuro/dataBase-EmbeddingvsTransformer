package com.camilo.authapp.data;

import com.camilo.authapp.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionFactory {
    private final DatabaseConfig databaseConfig;

    public MySqlConnectionFactory(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                databaseConfig.url(),
                databaseConfig.username(),
                databaseConfig.password()
        );
    }
}