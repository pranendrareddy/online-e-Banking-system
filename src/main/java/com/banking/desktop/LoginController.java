package com.banking.desktop;

import com.banking.model.User;
import com.banking.service.BankingService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginController {
    private final BankingService bankingService;
    private final VBox view;

    public LoginController() {
        this.bankingService = new BankingService();
        this.view = createView();
    }

    private VBox createView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root-pane");

        // Title
        Label titleLabel = new Label("Smart Banking System");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.getStyleClass().add("title-label");

        Label subtitleLabel = new Label("Login to your account");
        subtitleLabel.setFont(Font.font("System", 14));
        subtitleLabel.getStyleClass().add("subtitle-label");

        // Login Form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(250);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(250);

        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 0, 1);
        formGrid.add(passwordLabel, 0, 2);
        formGrid.add(passwordField, 0, 3);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(120);
        loginButton.getStyleClass().add("primary-button");

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(120);
        registerButton.getStyleClass().add("secondary-button");

        buttonBox.getChildren().addAll(loginButton, registerButton);

        // Status Label
        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");

        // Event Handlers
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter username and password");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            User user = bankingService.login(username, password);
            if (user != null) {
                if ("ADMIN".equals(user.getRole())) {
                    BankingApp.showAdminDashboard(user);
                } else {
                    BankingApp.showUserDashboard(user);
                }
            } else {
                statusLabel.setText("Invalid username or password");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        registerButton.setOnAction(e -> showRegistrationDialog());

        // Add enter key support
        passwordField.setOnAction(e -> loginButton.fire());

        root.getChildren().addAll(titleLabel, subtitleLabel, formGrid, buttonBox, statusLabel);
        return root;
    }

    private void showRegistrationDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Register New User");
        dialog.setHeaderText("Create a new account");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Full Name:"), 0, 2);
        grid.add(fullNameField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                try {
                    User user = bankingService.registerUser(
                        usernameField.getText(),
                        passwordField.getText(),
                        fullNameField.getText(),
                        emailField.getText(),
                        phoneField.getText()
                    );
                    return user;
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Registration Failed", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Account created successfully! You can now login.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
