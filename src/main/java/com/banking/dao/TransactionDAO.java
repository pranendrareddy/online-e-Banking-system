package com.banking.dao;

import com.banking.model.Transaction;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Create a new transaction
     */
    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, balance_before, balance_after, description, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getTransactionType());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setBigDecimal(4, transaction.getBalanceBefore());
            stmt.setBigDecimal(5, transaction.getBalanceAfter());
            stmt.setString(6, transaction.getDescription());
            stmt.setString(7, transaction.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        transaction.setTransactionId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractTransactionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting transaction: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get transactions by account ID
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Get recent transactions by account ID
     */
    public List<Transaction> getRecentTransactions(int accountId, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent transactions: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Get transactions by type
     */
    public List<Transaction> getTransactionsByType(int accountId, String type) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND transaction_type = ? ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions by type: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Get transaction summary for an account
     */
    public java.util.Map<String, Object> getTransactionSummary(int accountId) {
        java.util.Map<String, Object> summary = new java.util.HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) as total_count, " +
                     "SUM(CASE WHEN transaction_type IN ('DEPOSIT', 'TRANSFER_IN') THEN amount ELSE 0 END) as total_deposits, " +
                     "SUM(CASE WHEN transaction_type IN ('WITHDRAWAL', 'TRANSFER_OUT') THEN amount ELSE 0 END) as total_withdrawals " +
                     "FROM transactions WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                summary.put("totalCount", rs.getInt("total_count"));
                summary.put("totalDeposits", rs.getBigDecimal("total_deposits"));
                summary.put("totalWithdrawals", rs.getBigDecimal("total_withdrawals"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting transaction summary: " + e.getMessage());
        }
        return summary;
    }

    /**
     * Extract Transaction object from ResultSet
     */
    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setBalanceBefore(rs.getBigDecimal("balance_before"));
        transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));
        transaction.setDescription(rs.getString("description"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
        transaction.setStatus(rs.getString("status"));
        return transaction;
    }
}
