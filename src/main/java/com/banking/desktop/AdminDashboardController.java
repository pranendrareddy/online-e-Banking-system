package com.banking.desktop;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.service.BankingService;
import javafx.collections.FXCollections;
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

public class AdminDashboardController {
    private final User admin;
    private final BankingService bankingService;
    private final BorderPane view;
    private TabPane tabPane;

    public AdminDashboardController(User admin) {
        this.admin = admin;
        this.bankingService = new BankingService();
        this.view = createView();
    }

    private BorderPane createView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // Top - Header
        root.setTop(createHeader());

        // Center - Tabs
        tabPane = new TabPane();
        Tab overviewTab = new Tab("Dashboard", createOverviewTab());
        Tab usersTab = new Tab("Users", createUsersTab());
        Tab accountsTab = new Tab("Accounts", createAccountsTab());
        Tab transactionsTab = new Tab("Transactions", createTransactionsTab());

        overviewTab.setClosable(false);
        usersTab.setClosable(false);
        accountsTab.setClosable(false);
        transactionsTab.setClosable(false);

        tabPane.getTabs().addAll(overviewTab, usersTab, accountsTab, transactionsTab);
        root.setCenter(tabPane);

        return root;
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #673AB7;");

        Label welcomeLabel = new Label("Admin Dashboard - " + admin.getFullName());
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

    private VBox createOverviewTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("System Overview");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Statistics Grid
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);

        List<User> users = bankingService.getAllUsers();
        List<Account> accounts = bankingService.getAllAccounts();
        List<Transaction> transactions = bankingService.getAllTransactions();

        int totalUsers = users.size();
        int totalAccounts = accounts.size();
        int totalTransactions = transactions.size();
        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        VBox usersBox = createStatBox("Total Users", String.valueOf(totalUsers), "#4CAF50");
        VBox accountsBox = createStatBox("Total Accounts", String.valueOf(totalAccounts), "#2196F3");
        VBox transactionsBox = createStatBox("Total Transactions", String.valueOf(totalTransactions), "#FF9800");
        VBox balanceBox = createStatBox("Total Balance", String.format("$%.2f", totalBalance), "#9C27B0");

        statsGrid.add(usersBox, 0, 0);
        statsGrid.add(accountsBox, 1, 0);
        statsGrid.add(transactionsBox, 0, 1);
        statsGrid.add(balanceBox, 1, 1);

        Button refreshButton = new Button("Refresh Data");
        refreshButton.getStyleClass().add("primary-button");
        refreshButton.setOnAction(e -> refreshAllData());

        content.getChildren().addAll(titleLabel, statsGrid, refreshButton);
        return content;
    }

    private VBox createStatBox(String title, String value, String color) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");
        box.setPrefSize(200, 120);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: white;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        titleLabel.setStyle("-fx-text-fill: white;");

        box.getChildren().addAll(valueLabel, titleLabel);
        return box;
    }

    private VBox createUsersTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("All Users");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        TableView<User> userTable = new TableView<>();
        userTable.setPrefHeight(450);

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        idCol.setPrefWidth(50);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(120);

        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(150);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(180);

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(120);

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(80);

        TableColumn<User, Timestamp> dateCol = new TableColumn<>("Created At");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        dateCol.setPrefWidth(150);

        userTable.getColumns().addAll(idCol, usernameCol, nameCol, emailCol, phoneCol, roleCol, dateCol);

        List<User> users = bankingService.getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(users));

        content.getChildren().addAll(titleLabel, userTable);
        return content;
    }

    private VBox createAccountsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("All Accounts");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        TableView<Account> accountTable = new TableView<>();
        accountTable.setPrefHeight(450);

        TableColumn<Account, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        idCol.setPrefWidth(50);

        TableColumn<Account, String> numberCol = new TableColumn<>("Account Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        numberCol.setPrefWidth(120);

        TableColumn<Account, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        typeCol.setPrefWidth(100);

        TableColumn<Account, BigDecimal> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balanceCol.setPrefWidth(120);

        TableColumn<Account, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);

        TableColumn<Account, Timestamp> dateCol = new TableColumn<>("Created At");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        dateCol.setPrefWidth(150);

        accountTable.getColumns().addAll(idCol, numberCol, typeCol, balanceCol, statusCol, dateCol);

        List<Account> accounts = bankingService.getAllAccounts();
        accountTable.setItems(FXCollections.observableArrayList(accounts));

        content.getChildren().addAll(titleLabel, accountTable);
        return content;
    }

    private VBox createTransactionsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("All Transactions");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        TableView<Transaction> transactionTable = new TableView<>();
        transactionTable.setPrefHeight(450);

        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        idCol.setPrefWidth(50);

        TableColumn<Transaction, Integer> accountIdCol = new TableColumn<>("Account ID");
        accountIdCol.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        accountIdCol.setPrefWidth(90);

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
        descCol.setPrefWidth(180);

        TableColumn<Transaction, Timestamp> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateCol.setPrefWidth(150);

        transactionTable.getColumns().addAll(idCol, accountIdCol, typeCol, amountCol, balanceAfterCol, descCol, dateCol);

        List<Transaction> transactions = bankingService.getAllTransactions();
        transactionTable.setItems(FXCollections.observableArrayList(transactions));

        content.getChildren().addAll(titleLabel, transactionTable);
        return content;
    }

    private void refreshAllData() {
        // Refresh overview tab
        VBox overviewContent = createOverviewTab();
        ((Tab) tabPane.getTabs().get(0)).setContent(overviewContent);

        // Refresh users tab
        VBox usersContent = createUsersTab();
        ((Tab) tabPane.getTabs().get(1)).setContent(usersContent);

        // Refresh accounts tab
        VBox accountsContent = createAccountsTab();
        ((Tab) tabPane.getTabs().get(2)).setContent(accountsContent);

        // Refresh transactions tab
        VBox transactionsContent = createTransactionsTab();
        ((Tab) tabPane.getTabs().get(3)).setContent(transactionsContent);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Data refreshed successfully!");
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
