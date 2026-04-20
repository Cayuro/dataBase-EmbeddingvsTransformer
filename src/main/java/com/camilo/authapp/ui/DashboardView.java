package com.camilo.authapp.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView extends VBox {
    public DashboardView(String fullName, Runnable onLogout) {
        setSpacing(18);
        setPadding(new Insets(40));
        getStyleClass().add("auth-root");
        setAlignment(Pos.CENTER);

        Label title = new Label("Bienvenido");
        title.getStyleClass().add("title");

        Label message = new Label(fullName + " inició sesión correctamente.");
        message.setWrapText(true);
        message.getStyleClass().add("subtitle");

        Button logoutButton = new Button("Cerrar sesión");
        logoutButton.getStyleClass().add("primary-button");
        logoutButton.setOnAction(event -> onLogout.run());

        getChildren().addAll(title, message, logoutButton);
    }
}