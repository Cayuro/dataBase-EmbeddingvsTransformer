package com.vision.auth;

/**
 * AuthenticationService - Lógica de autenticación
 * 
 * ¿QUÉ DIFERENCIA HAY CON UserDAO?:
 *   - UserDAO: habla con la BD (INSERT, SELECT)
 *   - AuthenticationService: lógica de negocio (validaciones, hash, etc)
 * 
 * ¿RESPONSABILIDADES?:
 *   1. register() → Valida entrada → hashea contraseña → llama UserDAO.save()
 *   2. login() → Busca usuario → verifica contraseña → retorna usuario o error
 *   3. Mensajes de error claros para el front-end
 * 
 * ARQUITECTURA (LIMPIA):
 *   JavaFX Controller
 *        ↓ (llama)
 *   AuthenticationService
 *        ↓ (llama)
 *   UserDAO
 *        ↓ (query)
 *   Base de Datos
 */
public class AuthenticationService {

    /**
     * Registra un usuario nuevo
     * 
     * @param email Email del usuario
     * @param nombre Nombre del usuario
     * @param password Contraseña en texto plano
     * @return Mensaje de resultado (error o éxito)
     */
    public static String register(String email, String nombre, String password) {
        // QUE: punto de entrada del caso de uso de registro.
        // PARA QUE: controlar validaciones antes de tocar BD.
        // COMO: cortar temprano con mensajes claros.

        // Validación 1: campos vacíos
        if (email == null || email.trim().isEmpty()) {
            return "Email no puede estar vacío";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Nombre no puede estar vacío";
        }
        if (password == null || password.isEmpty()) {
            return "Contraseña no puede estar vacía";
        }

        // Validación 2: email válido (simple regex)
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Email no válido";
        }

        // Validación 3: contraseña mínimo 6 caracteres
        if (password.length() < 6) {
            return "Contraseña debe tener al menos 6 caracteres";
        }

        // Validación 4: email no existe ya
        if (UserDAO.emailExists(email)) {
            return "Email ya existe en el sistema";
        }

        // QUE: transformar password plano a hash BCrypt.
        // PARA QUE: proteger credenciales ante fuga de BD.
        String passwordHash = PasswordUtils.hashPassword(password);

        // QUE: crear entidad de dominio para persistencia.
        User newUser = new User(email, nombre, passwordHash);

        // Guardar en BD
        if (UserDAO.save(newUser)) {
            return "Registro exitoso. Bienvenido " + nombre + "!";
        } else {
            return "Error al guardar usuario en base de datos";
        }
    }

    /**
     * Intenta hacer login
     * 
     * @param email Email del usuario
     * @param password Contraseña en texto plano
     * @return User si es correcto, null si falla
     */
    public static User login(String email, String password) {
        // QUE: caso de uso de autenticacion.
        // PARA QUE: devolver User solo si credenciales son correctas.
        // Validación: campos vacíos
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        // QUE: buscar usuario existente por email.
        // PARA QUE: obtener hash guardado y compararlo.
        User user = UserDAO.findByEmail(email);

        if (user == null) {
            System.err.println("Usuario no encontrado: " + email);
            return null;
        }

        // QUE: validar password en forma segura contra hash.
        // COMO: BCrypt.verify compara sin revelar password real.
        if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            System.out.println("Login exitoso: " + user.getNombre());
            return user;
        }

        System.err.println("Contraseña incorrecta");
        return null;
    }

    /**
     * Obtiene mensaje de error legible del login
     * (para mostrar en la UI)
     */
    public static String getLoginError(String email, String password) {
        // QUE: generar causa exacta para mostrar en pantalla.
        // PARA QUE: UX clara cuando el login falla.
        if (email == null || email.trim().isEmpty()) {
            return "Email no puede estar vacío";
        }
        if (password == null || password.isEmpty()) {
            return "Contraseña no puede estar vacía";
        }

        User user = UserDAO.findByEmail(email);
        if (user == null) {
            return "Usuario no encontrado";
        }

        if (!PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            return "Contraseña incorrecta";
        }

        return "Error desconocido";
    }
}
