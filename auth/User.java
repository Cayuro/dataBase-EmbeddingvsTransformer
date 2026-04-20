package com.vision.auth;

import java.time.LocalDateTime;

/**
 * Modelo de Usuario - Contiene datos del usuario
 * 
 * ¿QUÉ ES?: Una clase que representa UN usuario en el sistema.
 * ¿QUÉ CONTIENE?: ID, email, nombre, contraseña hasheada, fecha de creación.
 * ¿POR QUÉ?: Necesitamos guardar esta info en la base de datos.
 */
public class User {
    // Clave primaria de BD.
    private int id;
    // Identificador unico funcional para login.
    private String email;
    // Nombre visible del usuario.
    private String nombre;
    // Hash BCrypt de la password (nunca password en claro).
    private String passwordHash;      // NUNCA la contraseña en texto plano
    // Marca temporal de alta del usuario.
    private LocalDateTime createdAt;

    // Constructor vacío (para BD)
    public User() {}

    // Constructor completo (para crear nuevos usuarios)
    public User(String email, String nombre, String passwordHash) {
        // Asignaciones directas desde capa de servicio.
        this.email = email;
        this.nombre = nombre;
        this.passwordHash = passwordHash;
        // Fecha de creacion por defecto al construir objeto nuevo.
        this.createdAt = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
