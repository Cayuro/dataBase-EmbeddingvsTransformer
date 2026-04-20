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

public class LoginView extends VBox {
    public LoginView(AuthService authService, Runnable onOpenRegister, Consumer<com.camilo.authapp.model.User> onLoginSuccess) {
        setSpacing(14);
        setPadding(new Insets(40));
        getStyleClass().add("auth-root");
        setAlignment(Pos.CENTER);

        Label title = new Label("Iniciar sesión");
        title.getStyleClass().add("title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuario");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message");
        messageLabel.setWrapText(true);

        Button loginButton = new Button("Entrar");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(event -> {
            AuthResult result = authService.login(usernameField.getText(), passwordField.getText());
            messageLabel.setText(result.message());
            if (result.success()) {
                onLoginSuccess.accept(result.user());
            }
        });

        Button openRegisterButton = new Button("Crear cuenta");
        openRegisterButton.getStyleClass().add("secondary-button");
        openRegisterButton.setMaxWidth(Double.MAX_VALUE);
        openRegisterButton.setOnAction(event -> onOpenRegister.run());

        getChildren().addAll(title, usernameField, passwordField, loginButton, openRegisterButton, messageLabel);
    }
}