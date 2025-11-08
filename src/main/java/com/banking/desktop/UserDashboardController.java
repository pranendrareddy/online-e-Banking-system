package com.banking.desktop;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.service.BankingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class UserDashboardController {
    private final User user;
    private final BankingService bankingService;
    private final BorderPane view;
    private Account currentAccount;
    private Label balanceLabel;
    private TableView<Transaction> transactionTable;

    public UserDashboardController(User user) {
        this.user = user;
        this.bankingService = new BankingService();
        this.view = createView();
        loadAccounts();
    }

    private BorderPane createView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // Top - Header
        root.setTop(createHeader());

        // Center - Main Content
        root.setCenter(createMainContent());

        return root;
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #2196F3;");

        Label welcomeLabel = new Label("Welcome, " + user.getFullName());
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        welcomeLabel.setStyle("-fx-text-fill: white;");

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction(e -> BankingApp.showLoginScreen());

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(welcomeLabel, spacer, logoutButton);

        header.getChildren().add(headerBox);
        return header;
    }

    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Account Info Section
        VBox accountSection = createAccountSection();

        // Action Buttons
        HBox actionButtons = createActionButtons();

        // Transaction History
        VBox transactionSection = createTransactionSection();

        content.getChildren().addAll(accountSection, actionButtons, transactionSection);
        return content;
    }

    private VBox createAccountSection() {
        VBox section = new VBox(10);
        section.getStyleClass().add("section-box");

        Label titleLabel = new Label("Account Information");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        balanceLabel = new Label("Balance: $0.00");
        balanceLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        balanceLabel.setStyle("-fx-text-fill: #4CAF50;");

        ComboBox<Account> accountComboBox = new ComboBox<>();
        accountComboBox.setPrefWidth(300);
        accountComboBox.setPromptText("Select Account");
        accountComboBox.setConverter(new javafx.util.StringConverter<Account>() {
            @Override
            public String toString(Account account) {
                return account != null ? account.getAccountNumber() + " (" + account.getAccountType() + ")" : "";
            }

            @Override
            public Account fromString(String string) {
                return null;
            }
        });

        accountComboBox.setOnAction(e -> {
            currentAccount = accountComboBox.getValue();
            if (currentAccount != null) {
                updateAccountInfo();
            }
        });

        Button createAccountButton = new Button("Create New Account");
        createAccountButton.getStyleClass().add("secondary-button");
        createAccountButton.setOnAction(e -> showCreateAccountDialog());

        HBox accountBox = new HBox(10);
        accountBox.setAlignment(Pos.CENTER_LEFT);
        accountBox.getChildren().addAll(accountComboBox, createAccountButton);

        section.getChildren().addAll(titleLabel, balanceLabel, accountBox);

        // Store reference for loading accounts
        accountComboBox.setId("accountComboBox");
        section.setUserData(accountComboBox);

        return section;
    }

    private HBox createActionButtons() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button depositButton = new Button("Deposit");
        depositButton.setPrefWidth(150);
        depositButton.getStyleClass().add("primary-button");
        depositButton.setOnAction(e -> showDepositDialog());

        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setPrefWidth(150);
        withdrawButton.getStyleClass().add("primary-button");
        withdrawButton.setOnAction(e -> showWithdrawDialog());

        Button refreshButton = new Button("Refresh");
        refreshButton.setPrefWidth(150);
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setOnAction(e -> updateAccountInfo());

        buttonBox.getChildren().addAll(depositButton, withdrawButton, refreshButton);
        return buttonBox;
    }

    private VBox createTransactionSection() {
        VBox section = new VBox(10);
        section.getStyleClass().add("section-box");
        VBox.setVgrow(section, Priority.ALWAYS);

        Label titleLabel = new Label("Transaction History");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        transactionTable = new TableView<>();
        transactionTable.setPrefHeight(300);

        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        idCol.setPrefWidth(50);

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        typeCol.setPrefWidth(100);

        TableColumn<Transaction, BigDecimal> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(100);

        TableColumn<Transaction, BigDecimal> balanceAfterCol = new TableColumn<>("Balance After");
        balanceAfterCol.setCellValueFactory(new PropertyValueFactory<>("balanceAfter"));
        balanceAfterCol.setPrefWidth(120);

        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        TableColumn<Transaction, Timestamp> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateCol.setPrefWidth(180);

        transactionTable.getColumns().addAll(idCol, typeCol, amountCol, balanceAfterCol, descCol, dateCol);

        section.getChildren().addAll(titleLabel, transactionTable);
        return section;
    }

    private void loadAccounts() {
        List<Account> accounts = bankingService.getAccountsByUserId(user.getUserId());
        ComboBox<Account> comboBox = (ComboBox<Account>) ((VBox) view.getCenter()).getChildren().get(0).getUserData();
        comboBox.setItems(FXCollections.observableArrayList(accounts));
        
        if (!accounts.isEmpty()) {
            comboBox.setValue(accounts.get(0));
            currentAccount = accounts.get(0);
            updateAccountInfo();
        }
    }

    private void updateAccountInfo() {
        if (currentAccount != null) {
            currentAccount = bankingService.getAccountById(currentAccount.getAccountId());
            balanceLabel.setText(String.format("Balance: $%.2f", currentAccount.getBalance()));
            loadTransactions();
        }
    }

    private void loadTransactions() {
        if (currentAccount != null) {
            List<Transaction> transactions = bankingService.getTransactionHistory(currentAccount.getAccountId());
            ObservableList<Transaction> transactionList = FXCollections.observableArrayList(transactions);
            transactionTable.setItems(transactionList);
        }
    }

    private void showCreateAccountDialog() {
        Dialog<Account> dialog = new Dialog<>();
        dialog.setTitle("Create New Account");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("SAVINGS", "CURRENT", "FIXED_DEPOSIT");
        typeComboBox.setValue("SAVINGS");

        TextField balanceField = new TextField("0");

        grid.add(new Label("Account Type:"), 0, 0);
        grid.add(typeComboBox, 1, 0);
        grid.add(new Label("Initial Balance:"), 0, 1);
        grid.add(balanceField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    BigDecimal balance = new BigDecimal(balanceField.getText());
                    Account account = bankingService.createAccount(user.getUserId(), typeComboBox.getValue(), balance);
                    return account;
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        Optional<Account> result = dialog.showAndWait();
        result.ifPresent(account -> {
            if (account != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
                loadAccounts();
            }
        });
    }

    private void showDepositDialog() {
        if (currentAccount == null) {
            showAlert(Alert.AlertType.WARNING, "No Account", "Please select an account first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Deposit Money");
        dialog.setHeaderText("Deposit to Account: " + currentAccount.getAccountNumber());
        dialog.setContentText("Enter amount:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amount -> {
            try {
                BigDecimal depositAmount = new BigDecimal(amount);
                bankingService.deposit(currentAccount.getAccountId(), depositAmount, "Deposit via Desktop App");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Deposit successful!");
                updateAccountInfo();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });
    }

    private void showWithdrawDialog() {
        if (currentAccount == null) {
            showAlert(Alert.AlertType.WARNING, "No Account", "Please select an account first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Withdraw Money");
        dialog.setHeaderText("Withdraw from Account: " + currentAccount.getAccountNumber());
        dialog.setContentText("Enter amount:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amount -> {
            try {
                BigDecimal withdrawAmount = new BigDecimal(amount);
                bankingService.withdraw(currentAccount.getAccountId(), withdrawAmount, "Withdrawal via Desktop App");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Withdrawal successful!");
                updateAccountInfo();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
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

    public BorderPane getView() {
        return view;
    }
}
