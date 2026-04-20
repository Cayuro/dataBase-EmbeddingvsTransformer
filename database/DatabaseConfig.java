package com.vision.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseConfig - Configuración de conexión JDBC
 * 
 * ¿QUÉ HACE?:
 *   1. Lee configuración desde variables de entorno o propiedades Java.
 *   2. Expone una conexión JDBC directa cuando se solicita.
 *   3. Oculta los detalles del motor de BD al resto de la app.
 * 
 * FLUJO:
 *   App inicia → DatabaseConfig.initialize() 
 *             → valida/guarda configuración
 *             → App corre queries
 *             → App cierra → no hay pool que cerrar
 */
public class DatabaseConfig {
     private static String jdbcUrl;
     private static String username;
     private static String password;

    private static String getConfigValue(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        return defaultValue;
    }

    public static synchronized void initialize() throws SQLException {
        if (jdbcUrl == null) {
            String dbType = getConfigValue("DB_TYPE", "sqlite").toLowerCase();

            // QUE: guardamos parámetros de conexión en memoria.
            // PARA QUE: el resto de la app no conozca credenciales.
            if (dbType.equals("postgresql") || dbType.equals("postgres")) {
                jdbcUrl = getConfigValue("DB_URL", "jdbc:postgresql://localhost:5432/vision_mouse");
                username = getConfigValue("DB_USER", "vision_user");
                password = getConfigValue("DB_PASSWORD", "password123");
            } else if (dbType.equals("mysql")) {
                jdbcUrl = getConfigValue("DB_URL", "jdbc:mysql://localhost:3306/vision_mouse");
                username = getConfigValue("DB_USER", "vision_user");
                password = getConfigValue("DB_PASSWORD", "password123");
            } else {
                jdbcUrl = getConfigValue("DB_URL", "jdbc:sqlite:vision_mouse.db");
                username = null;
                password = null;
            }

            System.out.println("✓ Database configuration initialized successfully");
        }
    }

    /**
     * Devuelve el JDBC URL activo del pool actual.
     *
     * QUE: expone la URL real usada por Hikari.
     * PARA QUE: otros componentes (ej. inicializador) adapten SQL por motor.
     * COMO: lee la propiedad del DataSource ya inicializado.
     */
    public static String getJdbcUrl() {
        return jdbcUrl != null ? jdbcUrl : "";
    }

    /**
     * Indica si el motor actual es PostgreSQL.
     */
    public static boolean isPostgreSql() {
        return getJdbcUrl().startsWith("jdbc:postgresql:");
    }

    public static Connection getConnection() throws SQLException {
        if (jdbcUrl == null) {
            throw new SQLException("Database not initialized. Call DatabaseConfig.initialize() first");
        }

        if (username != null && password != null) {
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);
            return DriverManager.getConnection(jdbcUrl, props);
        }

        return DriverManager.getConnection(jdbcUrl);
    }

    public static void close() {
        // No hay pool que cerrar cuando usamos DriverManager directamente.
    }

    public static boolean isInitialized() {
        return jdbcUrl != null;
    }
}
