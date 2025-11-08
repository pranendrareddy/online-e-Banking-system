-- Smart Banking System Database Schema
-- MySQL 5.7+

-- Drop existing database if exists
DROP DATABASE IF EXISTS smart_banking;

-- Create database
CREATE DATABASE smart_banking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE smart_banking;

-- Create USERS table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB;

-- Create ACCOUNTS table
CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    account_number VARCHAR(10) UNIQUE NOT NULL,
    account_type ENUM('SAVINGS', 'CURRENT', 'FIXED_DEPOSIT') DEFAULT 'SAVINGS',
    balance DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE', 'FROZEN') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_account_number (account_number),
    INDEX idx_user_id (user_id),
    CHECK (balance >= 0)
) ENGINE=InnoDB;

-- Create TRANSACTIONS table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    balance_before DECIMAL(15, 2) NOT NULL,
    balance_after DECIMAL(15, 2) NOT NULL,
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('COMPLETED', 'PENDING', 'FAILED') DEFAULT 'COMPLETED',
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    INDEX idx_account_id (account_id),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_transaction_type (transaction_type),
    CHECK (amount > 0)
) ENGINE=InnoDB;

-- Create view for account summary
CREATE VIEW account_summary AS
SELECT 
    a.account_id,
    a.account_number,
    a.account_type,
    a.balance,
    u.username,
    u.full_name,
    u.email,
    COUNT(t.transaction_id) as total_transactions,
    SUM(CASE WHEN t.transaction_type IN ('DEPOSIT', 'TRANSFER_IN') THEN t.amount ELSE 0 END) as total_deposits,
    SUM(CASE WHEN t.transaction_type IN ('WITHDRAWAL', 'TRANSFER_OUT') THEN t.amount ELSE 0 END) as total_withdrawals,
    a.created_at,
    a.status
FROM accounts a
JOIN users u ON a.user_id = u.user_id
LEFT JOIN transactions t ON a.account_id = t.account_id
GROUP BY a.account_id;

-- Create stored procedure for deposit
DELIMITER //
CREATE PROCEDURE sp_deposit(
    IN p_account_id INT,
    IN p_amount DECIMAL(15, 2),
    IN p_description VARCHAR(255)
)
BEGIN
    DECLARE v_current_balance DECIMAL(15, 2);
    DECLARE v_new_balance DECIMAL(15, 2);
    
    START TRANSACTION;
    
    -- Get current balance with lock
    SELECT balance INTO v_current_balance
    FROM accounts
    WHERE account_id = p_account_id FOR UPDATE;
    
    -- Calculate new balance
    SET v_new_balance = v_current_balance + p_amount;
    
    -- Update account balance
    UPDATE accounts
    SET balance = v_new_balance
    WHERE account_id = p_account_id;
    
    -- Insert transaction record
    INSERT INTO transactions (account_id, transaction_type, amount, balance_before, balance_after, description)
    VALUES (p_account_id, 'DEPOSIT', p_amount, v_current_balance, v_new_balance, p_description);
    
    COMMIT;
END //
DELIMITER ;

-- Create stored procedure for withdrawal
DELIMITER //
CREATE PROCEDURE sp_withdraw(
    IN p_account_id INT,
    IN p_amount DECIMAL(15, 2),
    IN p_description VARCHAR(255)
)
BEGIN
    DECLARE v_current_balance DECIMAL(15, 2);
    DECLARE v_new_balance DECIMAL(15, 2);
    DECLARE v_insufficient_funds CONDITION FOR SQLSTATE '45000';
    
    START TRANSACTION;
    
    -- Get current balance with lock
    SELECT balance INTO v_current_balance
    FROM accounts
    WHERE account_id = p_account_id FOR UPDATE;
    
    -- Check if sufficient funds
    IF v_current_balance < p_amount THEN
        SIGNAL v_insufficient_funds
        SET MESSAGE_TEXT = 'Insufficient funds for withdrawal';
    END IF;
    
    -- Calculate new balance
    SET v_new_balance = v_current_balance - p_amount;
    
    -- Update account balance
    UPDATE accounts
    SET balance = v_new_balance
    WHERE account_id = p_account_id;
    
    -- Insert transaction record
    INSERT INTO transactions (account_id, transaction_type, amount, balance_before, balance_after, description)
    VALUES (p_account_id, 'WITHDRAWAL', p_amount, v_current_balance, v_new_balance, p_description);
    
    COMMIT;
END //
DELIMITER ;

-- Insert default admin user (password: admin123)
-- Password hash is SHA-256 of "admin123"
INSERT INTO users (username, password_hash, full_name, email, phone, role)
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 
        'System Administrator', 'admin@smartbanking.com', '1234567890', 'ADMIN');

-- Display success message
SELECT 'Database schema created successfully!' as Message;
