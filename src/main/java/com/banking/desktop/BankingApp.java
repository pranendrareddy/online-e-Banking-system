package com.banking.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class BankingApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Smart Banking System");
        showLoginScreen();
    }

    public static void showLoginScreen() {
        try {
            LoginController controller = new LoginController();
            Scene scene = new Scene(controller.getView(), 400, 500);
            scene.getStylesheets().add(BankingApp.class.getResource("/styles/banking-style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showUserDashboard(com.banking.model.User user) {
        try {
            UserDashboardController controller = new UserDashboardController(user);
            Scene scene = new Scene(controller.getView(), 800, 600);
            scene.getStylesheets().add(BankingApp.class.getResource("/styles/banking-style.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAdminDashboard(com.banking.model.User admin) {
        try {
            AdminDashboardController controller = new AdminDashboardController(admin);
            Scene scene = new Scene(controller.getView(), 900, 650);
            scene.getStylesheets().add(BankingApp.class.getResource("/styles/banking-style.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
