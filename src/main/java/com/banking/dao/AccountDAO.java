package com.banking.dao;

import com.banking.model.Account;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    /**
     * Create a new account
     */
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (user_id, account_number, account_type, balance, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, account.getUserId());
            stmt.setString(2, account.getAccountNumber());
            stmt.setString(3, account.getAccountType());
            stmt.setBigDecimal(4, account.getBalance());
            stmt.setString(5, account.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        account.setAccountId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get account by ID
     */
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAccountFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get account by account number
     */
    public Account getAccountByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAccountFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get accounts by user ID
     */
    public List<Account> getAccountsByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                accounts.add(extractAccountFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting accounts: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Get all accounts
     */
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                accounts.add(extractAccountFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all accounts: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Update account balance
     */
    public boolean updateBalance(int accountId, java.math.BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ?, last_updated = CURRENT_TIMESTAMP WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, accountId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update account status
     */
    public boolean updateStatus(int accountId, String status) {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, accountId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete account
     */
    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
        }
        return false;
    }

    /**
     * Check if account number exists
     */
    public boolean accountNumberExists(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking account number: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extract Account object from ResultSet
     */
    private Account extractAccountFromResultSet(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setCreatedAt(rs.getTimestamp("created_at"));
        account.setLastUpdated(rs.getTimestamp("last_updated"));
        account.setStatus(rs.getString("status"));
        return account;
    }
}
