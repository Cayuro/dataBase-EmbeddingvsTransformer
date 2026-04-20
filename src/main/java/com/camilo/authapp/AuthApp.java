package com.camilo.authapp;

import com.camilo.authapp.config.DatabaseConfig;
import com.camilo.authapp.data.DatabaseInitializer;
import com.camilo.authapp.data.MySqlConnectionFactory;
import com.camilo.authapp.data.MySqlUserRepository;
import com.camilo.authapp.service.AuthService;
import com.camilo.authapp.service.PasswordHasher;
import com.camilo.authapp.ui.DashboardView;
import com.camilo.authapp.ui.LoginView;
import com.camilo.authapp.ui.RegisterView;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuthApp extends Application {
    private AuthService authService;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        try {
            DatabaseConfig databaseConfig = DatabaseConfig.fromEnvironment();
            MySqlConnectionFactory connectionFactory = new MySqlConnectionFactory(databaseConfig);
            MySqlUserRepository userRepository = new MySqlUserRepository(connectionFactory);
            this.authService = new AuthService(userRepository, new PasswordHasher());

            new DatabaseInitializer(connectionFactory).initialize();

            showLoginView();
            primaryStage.setTitle("Login y Registro JavaFX");
            primaryStage.show();
        } catch (Exception exception) {
            showStartupError(exception);
        }
    }

    private void showLoginView() {
        LoginView loginView = new LoginView(
                authService,
                this::showRegisterView,
                user -> showDashboardView(user.fullName())
        );
        configureScene(loginView, 460, 640);
    }

    private void showRegisterView() {
        RegisterView registerView = new RegisterView(
                authService,
                this::showLoginView,
                user -> showDashboardView(user.fullName())
        );
        configureScene(registerView, 460, 720);
    }

    private void showDashboardView(String fullName) {
        DashboardView dashboardView = new DashboardView(fullName, this::showLoginView);
        configureScene(dashboardView, 460, 420);
    }

    private void configureScene(javafx.scene.Parent root, int width, int height) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showStartupError(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio");
        alert.setHeaderText("No se pudo conectar a MySQL");
        alert.setContentText(exception.getMessage());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}