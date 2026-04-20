package com.vision.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * PasswordUtils - Seguridad de contraseñas
 * 
 * ¿QUÉ HACE?: Hashea y verifica contraseñas con Bcrypt.
 * ¿POR QUÉ NO GUARDAR EN TEXTO PLANO?
 *   - Si alguien accede a la BD, NO ve las contraseñas reales
 *   - Bcrypt es IMPOSIBLE de revertir (one-way hash)
 *   - Cada hash incluye "salt" (componente aleatorio)
 * 
 * EJEMPLO:
 *   "micontraseña123" → "$2a$10$xyzabcdef..." (jamás verás de nuevo "micontraseña123")
 */
public class PasswordUtils {

    /**
     * Hashea una contraseña en texto plano
     * 
     * @param password La contraseña sin hash
     * @return String con el hash Bcrypt
     */
    public static String hashPassword(String password) {
        // QUE: genera hash BCrypt con costo 12.
        // PARA QUE: subir dificultad de ataques por fuerza bruta.
        // COMO: hashToString incluye version, costo, salt y hash final.
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Verifica si una contraseña coincide con su hash
     * 
     * @param password Contraseña ingresada por usuario
     * @param hash El hash guardado en BD
     * @return true si coinciden, false si no
     */
    public static boolean verifyPassword(String password, String hash) {
        // QUE: compara password ingresada contra hash persistido.
        // PARA QUE: autenticar sin guardar/recuperar password real.
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }
}
