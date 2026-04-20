package com.vision.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.vision.database.DatabaseConfig;
import com.vision.database.DatabaseInitializer;
import java.io.IOException;

/**
 * AuthenticationApp - Aplicación JavaFX principal
 * 
 * ¿QUÉ HACE?:
 *   1. Inicializa la BD (crea pool de conexiones)
 *   2. Carga la escena de Login
 *   3. Maneja cambio de escenas (login ↔ registro)
 * 
 * CICLO DE VIDA:
 *   start() → Inicializa BD → Carga login.fxml → Espera usuario
 *   
 * PATRÓN USADO:
 *   - Singleton: solo existe UNA instancia de esta app
 *   - Stage global: guardamos el Stage para cambiar escenas desde controladores
 */
public class AuthenticationApp extends Application {

    // Stage unico compartido para poder cambiar escenas desde metodos static.
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        // Guardamos referencia global del stage principal.
        primaryStage = stage;

        // Inicializar base de datos
        try {
            // QUE: inicializa pool JDBC.
            // PARA QUE: cualquier DAO pueda pedir conexiones.
            DatabaseConfig.initialize();

            // QUE: crea estructura minima de tablas si no existe.
            DatabaseInitializer.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Mostrar pantalla de login
        showLogin();

        // Configurar ventana
        stage.setTitle("VisionMouse - Autenticación");
        stage.setWidth(500);
        stage.setHeight(600);
        stage.setResizable(false);

        // Icono (opcional)
        try {
            stage.getIcons().add(new Image("file:models/icon.png"));
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }

        stage.show();
    }

    /**
     * Cambia a la escena de Login
     */
    public static void showLogin() throws IOException {
        // QUE: carga el archivo FXML de login.
        // COMO: getResource busca dentro del classpath en /fxml/login.fxml.
        FXMLLoader loader = new FXMLLoader(AuthenticationApp.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
    }

    /**
     * Cambia a la escena de Registro
     */
    public static void showRegister() throws IOException {
        // Mismo flujo que showLogin, pero apuntando a la vista de registro.
        FXMLLoader loader = new FXMLLoader(AuthenticationApp.class.getResource("/fxml/register.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
    }

    /**
     * Cierra la aplicación de auth y abre HandTrackingApp
     */
    public static void openHandTrackingApp() {
        try {
            // Cerramos la ventana de autenticacion antes de abrir la app principal.
            primaryStage.close();
            // Aquí se abriría HandTrackingApp
            // new HandTrackingApp().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        // Cerrar pool de conexiones cuando se cierra la app
        DatabaseConfig.close();
        super.stop();
    }

    public static void main(String[] args) {
        // Punto de entrada JavaFX.
        launch(args);
    }
}
