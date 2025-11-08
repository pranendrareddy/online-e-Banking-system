package com.banking.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    private int accountId;
    private int userId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private Timestamp createdAt;
    private Timestamp lastUpdated;
    private String status;

    // Constructors
    public Account() {
    }

    public Account(int userId, String accountNumber, String accountType, BigDecimal balance) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", status='" + status + '\'' +
                '}';
    }
}
