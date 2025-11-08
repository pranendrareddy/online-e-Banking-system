# Complete Manual Setup - Step by Step

## Current Status
✅ Java 21 installed  
✅ MySQL installed (but password issue)  
❌ Maven not installed  
❌ Database not created  

---

## STEP 1: Fix MySQL Password (5 minutes)

### Method 1: Use MySQL Installer (Easiest)
1. Press **Windows Key** and search: `MySQL Installer`
2. Open **MySQL Installer - Community**
3. Find **MySQL Server 8.0.x** in the list
4. Click **Reconfigure** button
5. Click **Next** until you reach "Authentication Method"
6. Choose **"Use Strong Password Encryption"**
7. Click **Next** to "Accounts and Roles"
8. Set **new root password** (e.g., `root123`)
9. **WRITE IT DOWN!** You'll need it later
10. Click **Next** → **Execute** → **Finish**

### Method 2: Try Default Passwords
Try logging in with these:
```bash
# No password
mysql -u root

# Or try common defaults:
mysql -u root -proot
mysql -u root -padmin
mysql -u root -pmysql
```

### Verify MySQL is Working
Once you have the correct password:
```bash
mysql -u root -p
# Enter your password
# You should see: mysql>
```
Type `exit` to leave MySQL for now.

---

## STEP 2: Download and Install Maven (10 minutes)

### 2.1 Download Maven
1. Open browser: https://maven.apache.org/download.cgi
2. Find **Binary zip archive** 
3. Download: `apache-maven-3.9.9-bin.zip` (or latest version)
4. Save to Downloads folder

### 2.2 Extract Maven
1. Go to your **Downloads** folder
2. Right-click `apache-maven-3.9.9-bin.zip`
3. Select **Extract All**
4. Extract to: `C:\Program Files\Apache`
5. You should have: `C:\Program Files\Apache\apache-maven-3.9.9`

### 2.3 Set Environment Variables

**Add MAVEN_HOME:**
1. Press **Windows Key** and search: `environment variables`
2. Click **"Edit the system environment variables"**
3. Click **"Environment Variables"** button
4. Under **System Variables**, click **"New"**
5. Variable name: `MAVEN_HOME`
6. Variable value: `C:\Program Files\Apache\apache-maven-3.9.9`
7. Click **OK**

**Add Maven to PATH:**
1. In **System Variables**, find and select **Path**
2. Click **"Edit"**
3. Click **"New"**
4. Add: `%MAVEN_HOME%\bin`
5. Click **OK** on all windows

### 2.4 Verify Maven Installation
**IMPORTANT: Close and reopen PowerShell/Terminal**

```bash
mvn --version
```

You should see:
```
Apache Maven 3.9.9
Maven home: C:\Program Files\Apache\apache-maven-3.9.9
Java version: 21.0.7
```

---

## STEP 3: Create Database (5 minutes)

### 3.1 Login to MySQL
```bash
mysql -u root -p
```
Enter your password when prompted.

### 3.2 Create Database and Tables

In the MySQL console, run:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS smart_banking;
USE smart_banking;

-- Create users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Create accounts table
CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type ENUM('SAVINGS', 'CURRENT', 'FIXED_DEPOSIT') NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'FROZEN', 'CLOSED') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create transactions table
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    balance_before DECIMAL(15, 2) NOT NULL,
    balance_after DECIMAL(15, 2) NOT NULL,
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('COMPLETED', 'PENDING', 'FAILED') DEFAULT 'COMPLETED',
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password_hash, full_name, email, role) 
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'System Administrator', 'admin@smartbank.com', 'ADMIN');

-- Insert sample user (password: password123)
INSERT INTO users (username, password_hash, full_name, email, phone) 
VALUES ('john_doe', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'John Doe', 'john@example.com', '1234567890');

-- Insert sample account
INSERT INTO accounts (user_id, account_number, account_type, balance) 
VALUES (2, 'ACC1000000001', 'SAVINGS', 5000.00);

-- Verify
SELECT 'Database created successfully!' AS Status;
```

Type `exit` when done.

### 3.3 Alternative: Use SQL File

Or simply run:
```bash
mysql -u root -p smart_banking < database/schema.sql
mysql -u root -p smart_banking < database/sample_data.sql
```

---

## STEP 4: Configure Database Connection (2 minutes)

1. Open file: `database.properties`
2. Update with your MySQL password:

```properties
db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE
```

**Example:** If your password is `root123`:
```properties
db.password=root123
```

---

## STEP 5: Download Project Dependencies (5 minutes)

```bash
cd C:\Users\Prasanna\OneDrive\Documents\BANKING-SYSTEM
mvn clean install
```

This will:
- Download MySQL Connector
- Download JavaFX libraries
- Download Servlet APIs
- Compile the project

**Wait for:** `BUILD SUCCESS`

---

## STEP 6: Run the Desktop Application (2 minutes)

```bash
mvn clean javafx:run
```

The desktop application should launch!

**Default Login:**
- Username: `admin`
- Password: `admin123`

Or test user:
- Username: `john_doe`
- Password: `password123`

---

## STEP 7: (Optional) Run Web Application

### Install Apache Tomcat
1. Download: https://tomcat.apache.org/download-90.cgi
2. Get: **32-bit/64-bit Windows Service Installer** (.exe)
3. Install with defaults
4. Tomcat will run on: http://localhost:8080

### Deploy Web App
```bash
# Build WAR file
mvn clean package

# Copy to Tomcat
copy target\smart-banking-system.war "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\"

# Wait 30 seconds for deployment

# Open browser:
# http://localhost:8080/smart-banking-system/login
```

---

## Troubleshooting

### "mvn: command not found" after installation
- **Close and reopen** PowerShell/Terminal
- Verify PATH: `echo %MAVEN_HOME%`
- Should show: `C:\Program Files\Apache\apache-maven-3.9.9`

### "MySQL connection failed"
- Check MySQL is running: `Get-Service -Name "*mysql*"`
- Start it: `net start MySQL80` (or your service name)
- Verify password in `database.properties`

### "JavaFX runtime components missing"
Maven should download them automatically. If not:
```bash
mvn clean install -U
```

### Build fails
```bash
# Clean everything and retry
mvn clean
mvn install
```

---

## Quick Commands Reference

```bash
# Check Java
java --version

# Check Maven (after installation)
mvn --version

# Check MySQL
mysql --version
mysql -u root -p

# Build project
cd C:\Users\Prasanna\OneDrive\Documents\BANKING-SYSTEM
mvn clean install

# Run desktop app
mvn clean javafx:run

# Build web app
mvn clean package
```

---

## Checklist

Before running, verify:
- [x] MySQL installed and running
- [x] MySQL root password known
- [x] Database 'smart_banking' created
- [x] database.properties updated with password
- [x] Maven installed and in PATH
- [x] `mvn --version` works
- [x] `mvn clean install` completed successfully

---

## Time Estimate

- Step 1 (MySQL password): 5 min
- Step 2 (Maven install): 10 min  
- Step 3 (Database setup): 5 min
- Step 4 (Config): 2 min
- Step 5 (Dependencies): 5 min
- Step 6 (Run): 2 min

**Total: ~30 minutes**

---

## Next Steps

Once everything is running:
1. Test login with admin/john_doe
2. Create new user account
3. Create bank account
4. Test deposit/withdrawal
5. View transaction history
6. Try admin dashboard

**Good luck! 🚀**
