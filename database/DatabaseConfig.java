package com.vision.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DatabaseConfig - Configuración y pool de conexiones
 * 
 * ¿QUÉ HACE?:
 *   1. Crea/abre archivo SQLite (vision_mouse.db)
 *   2. Mantiene un "pool" (conjunto) de conexiones reutilizables
 *   3. Evita crear conexión nueva para cada query (lento)
 * 
 * ¿QUE ES HIKARICP?:
 *   - Un administrador de conexiones muy rápido
 *   - En lugar de: query → abre conexión → cierra conexión
 *   - Hace: query → toma conexión del pool → devuelve al pool
 * 
 * FLUJO:
 *   App inicia → DatabaseConfig.initialize() 
 *             → crea pool de 5 conexiones
 *             → App corre queries
 *             → App cierra → pool cierra
 */
public class DatabaseConfig {
    private static HikariDataSource dataSource;

    public static synchronized void initialize() throws SQLException {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            
            // ========== OPCION 1: SQLite (ACTUALMENTE ACTIVO) ==========
            // QUE: Base de datos embebida (archivo .db)
            // COMO: Crea archivo vision_mouse.db automáticamente
            // PARA QUE: Desarrollo y testing
            config.setJdbcUrl("jdbc:sqlite:vision_mouse.db");
            
            // ========== OPCION 2: MySQL (COMENTADO - Descomenta para usar) ==========
            // QUE: Base de datos cliente-servidor
            // COMO: Conecta a servidor MySQL en localhost:3306
            // PARA QUE: Producción, múltiples usuarios
            // REQUISITOS PREVIOS:
            //   1. Instalar MySQL Server
            //   2. Crear base de datos: CREATE DATABASE vision_mouse;
            //   3. Crear usuario: CREATE USER 'vision_user'@'localhost' IDENTIFIED BY 'password123';
            //   4. Dar permisos: GRANT ALL PRIVILEGES ON vision_mouse.* TO 'vision_user'@'localhost';
            //   5. En pom.xml: cambiar sqlite-jdbc por mysql-connector-java
            //
            // DESCOMENTA ESTA LINEA PARA ACTIVAR MYSQL:
            // config.setJdbcUrl("jdbc:mysql://localhost:3306/vision_mouse");
            // config.setUsername("vision_user");           // Usuario de MySQL
            // config.setPassword("password123");           // Contraseña de MySQL
            
            // ========== OPCION 3: PostgreSQL (COMENTADO - Descomenta para usar) ==========
            // QUE: Base de datos cliente-servidor avanzada
            // COMO: Conecta a servidor PostgreSQL en localhost:5432
            // PARA QUE: Producción grande, datos complejos
            // REQUISITOS PREVIOS:
            //   1. Instalar PostgreSQL Server
            //   2. Crear base de datos: CREATE DATABASE vision_mouse;
            //   3. Crear usuario: CREATE USER vision_user WITH PASSWORD 'password123';
            //   4. Dar permisos: GRANT ALL PRIVILEGES ON DATABASE vision_mouse TO vision_user;
            //   5. En pom.xml: cambiar sqlite-jdbc por postgresql
            //
            // DESCOMENTA ESTAS LINEAS PARA ACTIVAR POSTGRESQL:
            // config.setJdbcUrl("jdbc:postgresql://localhost:5432/vision_mouse");
            // config.setUsername("vision_user");           // Usuario de PostgreSQL
            // config.setPassword("password123");           // Contraseña de PostgreSQL
            
            // ========== CONFIGURACION DEL POOL (IGUAL PARA TODAS LAS OPCIONES) ==========
            // Configuración de pool (funciona igual con SQLite, MySQL o PostgreSQL)
            config.setMaximumPoolSize(5);      // Máximo 5 conexiones simultáneas
                                               // Si necesita más, espera hasta que se libere
                                               // AUMENTAR: si tienes más de 5 usuarios simultáneos
            
            config.setMinimumIdle(2);          // Mínimo 2 conexiones inactivas
                                               // Mantiene 2 abiertas incluso sin usar
                                               // PARA QUE: Primer request es más rápido
            
            config.setConnectionTimeout(10000); // Timeout: 10 segundos
                                                // Si necesita conexión y todas ocupadas,
                                                // espera máximo 10s antes de error
                                                // AUMENTAR: si hay muchos usuarios simultáneos
            
            config.setIdleTimeout(300000);     // Timeout inactividad: 5 minutos
                                               // Cierra conexión si no la usas en 5 min
                                               // PARA QUE: Liberar recursos
            
            dataSource = new HikariDataSource(config);
            System.out.println("✓ Database pool initialized successfully");
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
        return dataSource != null ? dataSource.getJdbcUrl() : "";
    }

    /**
     * Indica si el motor actual es PostgreSQL.
     */
    public static boolean isPostgreSql() {
        return getJdbcUrl().startsWith("jdbc:postgresql:");
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Database not initialized. Call DatabaseConfig.initialize() first");
        }
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }

    public static boolean isInitialized() {
        return dataSource != null;
    }
}
