package com.vision.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.vision.auth.AuthenticationService;

/**
 * RegisterController - Controla la pantalla de Registro
 * 
 * ¿QUÉ DIFERENCIA HAY CON LoginController?:
 *   - Login: valida usuario existente
 *   - Registro: crea usuario nuevo
 * 
 * VALIDACIONES QUE HACE:
 *   1. Email válido (formato correcto)
 *   2. Nombre no vacío
 *   3. Contraseña ≥ 6 caracteres
 *   4. Contraseña == Confirmar contraseña
 *   5. Email no existe ya
 * 
 * FLUJO:
 *   1. Usuario llena formulario
 *   2. Presiona "Registrarse"
 *   3. handleRegister() valida todo
 *   4. Si OK → guarda en BD → muestra éxito
 *   5. Si NO → muestra error específico
 */
public class RegisterController {

    // Campos vinculados por fx:id desde register.fxml
    @FXML
    private TextField emailField;

    @FXML
    private TextField nombreField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    /**
     * Maneja el botón "Registrarse"
     */
    @FXML
    private void handleRegister() {
        // 1) Leer estado actual del formulario.
        String email = emailField.getText();
        String nombre = nombreField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validación adicional en UI (antes de mandar a servicio)
        // QUE: check local de confirmacion de password.
        // PARA QUE: evitar llamada al servicio cuando no coincide.
        if (!password.equals(confirmPassword)) {
            messageLabel.setStyle("-fx-text-fill: #ff6666;");
            messageLabel.setText("Las contraseñas no coinciden");
            return;
        }

        // 2) Delegar alta de usuario a capa de negocio.
        // Llamar al servicio de registro
        String result = AuthenticationService.register(email, nombre, password);

        // Mostrar resultado
        if (result.contains("✅")) {
            // 3a) Alta correcta: mostrar exito y reset del formulario.
            messageLabel.setStyle("-fx-text-fill: #00ff00;");
            messageLabel.setText(result);
            
            // Limpiar campos
            emailField.clear();
            nombreField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            
            // Aquí iría: volver a login automáticamente después de 2 segundos
            System.out.println(result);
            
        } else {
            // 3b) Alta rechazada: mostrar mensaje de validacion/error.
            messageLabel.setStyle("-fx-text-fill: #ff6666;");
            messageLabel.setText(result);
        }
    }

    /**
     * Maneja el botón "Ya tengo cuenta"
     * Vuelve a login
     */
    @FXML
    private void handleLoginSwitch() {
        try {
            // Navega de vuelta a login.
            AuthenticationApp.showLogin();
        } catch (Exception e) {
            messageLabel.setText("Error al volver a login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
