package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.util.PasswordUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BankingService {
    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public BankingService() {
        this.userDAO = new UserDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // ==================== User Services ====================

    /**
     * Register a new user
     */
    public User registerUser(String username, String password, String fullName, String email, String phone) {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (!PasswordUtil.isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Check if username exists
        if (userDAO.usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email exists
        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create user with hashed password
        String passwordHash = PasswordUtil.hashPassword(password);
        User user = new User(username, passwordHash, fullName, email, phone, "USER");

        if (userDAO.createUser(user)) {
            return user;
        }
        return null;
    }

    /**
     * Login user
     */
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        User user = userDAO.getUserByUsername(username);
        if (user != null && user.isActive()) {
            if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * Get all users (Admin only)
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    // ==================== Account Services ====================

    /**
     * Create a new account
     */
    public Account createAccount(int userId, String accountType, BigDecimal initialBalance) {
        // Validate inputs
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        // Generate unique account number
        String accountNumber;
        do {
            accountNumber = PasswordUtil.generateAccountNumber();
        } while (accountDAO.accountNumberExists(accountNumber));

        Account account = new Account(userId, accountNumber, accountType, initialBalance);
        
        if (accountDAO.createAccount(account)) {
            // Create initial transaction if balance > 0
            if (initialBalance.compareTo(BigDecimal.ZERO) > 0) {
                Transaction transaction = new Transaction(
                    account.getAccountId(),
                    "DEPOSIT",
                    initialBalance,
                    BigDecimal.ZERO,
                    initialBalance,
                    "Initial deposit"
                );
                transactionDAO.createTransaction(transaction);
            }
            return account;
        }
        return null;
    }

    /**
     * Get account by ID
     */
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    /**
     * Get account by account number
     */
    public Account getAccountByNumber(String accountNumber) {
        return accountDAO.getAccountByNumber(accountNumber);
    }

    /**
     * Get accounts by user ID
     */
    public List<Account> getAccountsByUserId(int userId) {
        return accountDAO.getAccountsByUserId(userId);
    }

    /**
     * Get all accounts (Admin only)
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /**
     * Get account balance
     */
    public BigDecimal getBalance(int accountId) {
        Account account = accountDAO.getAccountById(accountId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }

    // ==================== Transaction Services ====================

    /**
     * Deposit money
     */
    public synchronized boolean deposit(int accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new IllegalArgumentException("Account is not active");
        }

        BigDecimal oldBalance = account.getBalance();
        BigDecimal newBalance = oldBalance.add(amount);

        // Update balance
        if (accountDAO.updateBalance(accountId, newBalance)) {
            // Create transaction record
            Transaction transaction = new Transaction(
                accountId,
                "DEPOSIT",
                amount,
                oldBalance,
                newBalance,
                description != null ? description : "Deposit"
            );
            return transactionDAO.createTransaction(transaction);
        }
        return false;
    }

    /**
     * Withdraw money
     */
    public synchronized boolean withdraw(int accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new IllegalArgumentException("Account is not active");
        }

        BigDecimal oldBalance = account.getBalance();
        if (oldBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        BigDecimal newBalance = oldBalance.subtract(amount);

        // Update balance
        if (accountDAO.updateBalance(accountId, newBalance)) {
            // Create transaction record
            Transaction transaction = new Transaction(
                accountId,
                "WITHDRAWAL",
                amount,
                oldBalance,
                newBalance,
                description != null ? description : "Withdrawal"
            );
            return transactionDAO.createTransaction(transaction);
        }
        return false;
    }

    /**
     * Get transaction history
     */
    public List<Transaction> getTransactionHistory(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    /**
     * Get recent transactions
     */
    public List<Transaction> getRecentTransactions(int accountId, int limit) {
        return transactionDAO.getRecentTransactions(accountId, limit);
    }

    /**
     * Get all transactions (Admin only)
     */
    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    /**
     * Get transaction summary
     */
    public Map<String, Object> getTransactionSummary(int accountId) {
        return transactionDAO.getTransactionSummary(accountId);
    }
}
