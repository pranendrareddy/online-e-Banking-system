-- Sample Data for Testing
-- Password for all users: password123
-- SHA-256 hash: ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f

USE smart_banking;

-- Insert sample users
INSERT INTO users (username, password_hash, full_name, email, phone, role) VALUES
('john_doe', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'John Doe', 'john@email.com', '9876543210', 'USER'),
('jane_smith', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Jane Smith', 'jane@email.com', '9876543211', 'USER'),
('bob_wilson', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Bob Wilson', 'bob@email.com', '9876543212', 'USER');

-- Insert sample accounts
INSERT INTO accounts (user_id, account_number, account_type, balance) VALUES
(2, '1001234567', 'SAVINGS', 50000.00),
(2, '1001234568', 'CURRENT', 75000.00),
(3, '1002345678', 'SAVINGS', 100000.00),
(4, '1003456789', 'SAVINGS', 25000.00);

-- Insert sample transactions
INSERT INTO transactions (account_id, transaction_type, amount, balance_before, balance_after, description) VALUES
(1, 'DEPOSIT', 10000.00, 40000.00, 50000.00, 'Initial deposit'),
(1, 'WITHDRAWAL', 5000.00, 50000.00, 45000.00, 'ATM withdrawal'),
(1, 'DEPOSIT', 10000.00, 45000.00, 55000.00, 'Salary credit'),
(2, 'DEPOSIT', 75000.00, 0.00, 75000.00, 'Opening deposit'),
(3, 'DEPOSIT', 100000.00, 0.00, 100000.00, 'Fixed deposit'),
(4, 'DEPOSIT', 25000.00, 0.00, 25000.00, 'Initial deposit');

-- Update account balances to match last transaction
UPDATE accounts a
JOIN (
    SELECT account_id, balance_after
    FROM transactions
    WHERE (account_id, transaction_date) IN (
        SELECT account_id, MAX(transaction_date)
        FROM transactions
        GROUP BY account_id
    )
) t ON a.account_id = t.account_id
SET a.balance = t.balance_after;

SELECT 'Sample data inserted successfully!' as Message;
