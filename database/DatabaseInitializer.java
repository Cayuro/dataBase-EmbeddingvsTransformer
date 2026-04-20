package com.vision.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * DatabaseInitializer - Crea la estructura de tablas
 * 
 * ¿QUÉ HACE?:
 *   Si la BD está vacía, crea la tabla "users" con columnas:
 *   - id (PRIMARY KEY, autoincremento)
 *   - email (UNIQUE, no puede haber dos iguales)
 *   - nombre (texto)
 *   - password_hash (hash Bcrypt)
 *   - created_at (fecha de creación)
 * 
 * ¿CUÁNDO SE EJECUTA?:
 *   Una vez, la primera vez que se abre la app
 */
public class DatabaseInitializer {

    public static void initializeDatabase() throws SQLException {
        // QUE: abrimos una conexion y un Statement SQL.
        // PARA QUE: ejecutar DDL (CREATE TABLE) una sola vez al iniciar.
        // COMO: try-with-resources para cerrar automaticamente.
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // QUE: definimos SQL por motor de base de datos.
            // PARA QUE: SQLite y PostgreSQL tienen diferencias de sintaxis.
            // COMO: elegimos la sentencia segun el JDBC URL configurado.
            String createTableSQL;

            if (DatabaseConfig.isPostgreSql()) {
                // PostgreSQL usa GENERATED ... AS IDENTITY para autoincremento.
                createTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            nombre VARCHAR(255) NOT NULL,
                            password_hash TEXT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                        """;
            } else {
                // SQLite usa INTEGER PRIMARY KEY AUTOINCREMENT.
                createTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            email TEXT NOT NULL UNIQUE,
                            nombre TEXT NOT NULL,
                            password_hash TEXT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                        """;
            }

            // QUE: ejecutamos DDL idempotente.
            // PARA QUE: si la tabla ya existe no falle el arranque.
            stmt.execute(createTableSQL);
            System.out.println("Database table 'users' created or already exists");

        } catch (SQLException e) {
            // QUE: registramos error y re-lanzamos.
            // PARA QUE: el caller pueda detener la app si la BD no inicializa.
            System.err.println("Error initializing database: " + e.getMessage());
            throw e;
        }
    }
}
