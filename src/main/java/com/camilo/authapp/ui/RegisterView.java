package com.camilo.authapp.ui;

import com.camilo.authapp.service.AuthResult;
import com.camilo.authapp.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class RegisterView extends VBox {
    public RegisterView(AuthService authService, Runnable onBackToLogin, Consumer<com.camilo.authapp.model.User> onRegisterSuccess) {
        setSpacing(14);
        setPadding(new Insets(40));
        getStyleClass().add("auth-root");
        setAlignment(Pos.CENTER);

        Label title = new Label("Registro");
        title.getStyleClass().add("title");

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Nombre completo");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuario");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmar contraseña");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");
        messageLabel.setWrapText(true);

        Button registerButton = new Button("Registrar");
        registerButton.getStyleClass().add("primary-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(event -> {
            AuthResult result = authService.register(
                    usernameField.getText(),
                    fullNameField.getText(),
                    passwordField.getText(),
                    confirmPasswordField.getText()
            );
            messageLabel.setText(result.message());
            if (result.success()) {
                onRegisterSuccess.accept(result.user());
            }
        });

        Button backButton = new Button("Volver al login");
        backButton.getStyleClass().add("secondary-button");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(event -> onBackToLogin.run());

        getChildren().addAll(title, fullNameField, usernameField, passwordField, confirmPasswordField, registerButton, backButton, messageLabel);
    }
}