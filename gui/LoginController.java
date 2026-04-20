package com.vision.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.vision.auth.AuthenticationService;
import com.vision.auth.User;

/**
 * LoginController - Controla la pantalla de Login
 * 
 * ¿QUÉ HACE?:
 *   - Captura email/contraseña del usuario
 *   - Llama AuthenticationService.login()
 *   - Muestra error o abre HandTrackingApp
 * 
 * FLUJO:
 *   1. Usuario escribe email/contraseña en FXML
 *   2. Usuario presiona botón "Ingresar"
 *   3. handleLogin() se ejecuta
 *   4. Valida con AuthenticationService
 *   5. Si OK → abre app de hand tracking
 *   6. Si NO → muestra error en pantalla
 * 
 * ¿CÓMO FUNCIONA @FXML?:
 *   - FXML carga login.fxml
 *   - Los fx:id del FXML se conectan con @FXML de acá
 *   - Cuando usuario presiona botón, se llama el método onAction
 */
public class LoginController {

    // Campo enlazado con fx:id="emailField" en login.fxml
    @FXML
    private TextField emailField;

    // Campo enlazado con fx:id="passwordField" en login.fxml
    @FXML
    private PasswordField passwordField;

    // Label donde se muestran errores o exito de login.
    @FXML
    private Label errorLabel;

    /**
     * Maneja el botón "Ingresar"
     * Se ejecuta cuando usuario presiona el botón
     */
    @FXML
    private void handleLogin() {
        // 1) Tomar valores actuales del formulario.
        String email = emailField.getText();
        String password = passwordField.getText();

        // 2) Delegar autenticacion al servicio (capa negocio).
        // Intentar login
        User user = AuthenticationService.login(email, password);

        if (user != null) {
            // 3a) Login correcto: feedback visual y consola.
            // LOGIN EXITOSO
            errorLabel.setStyle("-fx-text-fill: #00ff00;");
            errorLabel.setText("¡Bienvenido " + user.getNombre() + "!");
            
            // Aquí iría: abrir HandTrackingApp
            // Por ahora solo mostramos el mensaje
            System.out.println("Usuario autenticado: " + user);
            
        } else {
            // 3b) Login fallido: pedimos causa exacta al servicio.
            // LOGIN FALLIDO
            String error = AuthenticationService.getLoginError(email, password);
            errorLabel.setStyle("-fx-text-fill: #ff6666;");
            errorLabel.setText("" + error);
        }
    }

    /**
     * Maneja el botón "Registrarse"
     * Cambia a la pantalla de registro
     */
    @FXML
    private void handleRegisterSwitch() {
        try {
            // Navega a vista de registro en el mismo Stage.
            AuthenticationApp.showRegister();
        } catch (Exception e) {
            errorLabel.setText("Error al abrir registro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
