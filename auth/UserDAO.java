package com.vision.auth;

import com.vision.database.DatabaseConfig;
import java.sql.*;

/**
 * UserDAO - Data Access Object (Acceso a la base de datos)
 * 
 * ¿QUÉ SIGNIFICA DAO?:
 *   - Patrón de diseño que SEPARA la lógica de negocios de la BD
 *   - Todo lo relacionado con INSERT/SELECT/UPDATE va aquí
 * 
 * ¿QUÉ HACE ESTA CLASE?:
 *   - findByEmail() → busca usuario por email
 *   - save() → guarda un usuario nuevo
 *   - Traduce objetos User ↔ filas en BD
 * 
 * EJEMPLO DE FLUJO:
 *   1. Usuario ingresa email "juan@gmail.com"
 *   2. LoginController llama userDAO.findByEmail("juan@gmail.com")
 *   3. UserDAO ejecuta SELECT en BD
 *   4. Si existe, retorna objeto User
 *   5. Si no existe, retorna null
 */
public class UserDAO {

    /**
     * Busca un usuario por email en la BD
     * 
     * @param email El email a buscar
     * @return User si existe, null si no
     */
    public static User findByEmail(String email) {
        // QUE: consulta parametrizada para buscar por email.
        // PARA QUE: evitar SQL injection y mejorar legibilidad.
        String query = "SELECT id, email, nombre, password_hash, created_at FROM users WHERE email = ?";

        // QUE: abrimos conexion + PreparedStatement.
        // COMO: try-with-resources garantiza cierre automatico.
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Posicion 1 corresponde al primer '?' de la consulta.
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            // Si hay fila, mapeamos columnas SQL a objeto User.
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setNombre(rs.getString("nombre"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return user;
            }

        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
        }

        return null;
    }

    /**
     * Guarda un usuario nuevo en la BD
     * 
     * @param user El usuario a guardar (sin ID, se genera automáticamente)
     * @return true si se guardó exitosamente, false si fallo
     */
    public static boolean save(User user) {
        // QUE: inserta nuevo usuario con 3 campos funcionales.
        // PARA QUE: id y created_at se generan en la BD.
        String query = "INSERT INTO users (email, nombre, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Mapeo en orden de placeholders de la consulta.
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getNombre());
            pstmt.setString(3, user.getPasswordHash());

            // executeUpdate retorna numero de filas afectadas.
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("User saved: " + user.getEmail());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }

        return false;
    }

    /**
     * Verifica si un email ya existe en la BD
     * (Usado en registro para evitar duplicados)
     * 
     * @param email El email a verificar
     * @return true si existe, false si no
     */
    public static boolean emailExists(String email) {
        // Reutiliza el metodo de busqueda para mantener una sola fuente de verdad.
        return findByEmail(email) != null;
    }
}
