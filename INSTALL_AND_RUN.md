# How to Install and Run Smart Banking System

## 🚨 Current Status
Based on your system:
- ✅ Java 21 is installed
- ❌ MySQL is NOT installed
- ❌ Maven is NOT installed

## 🎯 Easiest Way to Run (Recommended)

### Option 1: Use IntelliJ IDEA (Easiest - Everything Built-in)

**Download IntelliJ IDEA Community (FREE):**
- https://www.jetbrains.com/idea/download/

**Steps:**
1. Install IntelliJ IDEA
2. Open IntelliJ → File → Open → Select `BANKING-SYSTEM` folder
3. Wait for IntelliJ to import Maven project (bottom right corner shows progress)
4. IntelliJ will automatically download all dependencies
5. Right-click `BankingApp.java` → Run 'BankingApp.main()'

✅ IntelliJ has built-in Maven - no separate installation needed!

---

### Option 2: Install Prerequisites Manually

If you want to use VS Code/Windsurf:

#### Step 1: Install MySQL (Required - 10 minutes)

1. **Download MySQL Installer:**
   - https://dev.mysql.com/downloads/installer/
   - Choose: `mysql-installer-community-8.0.xx.msi`

2. **Install:**
   - Choose "Developer Default"
   - Set root password (remember it!)
   - Default port: 3306
   - Finish installation

3. **Verify:**
   ```bash
   mysql --version
   ```

4. **Create Database:**
   ```bash
   # Login to MySQL
   mysql -u root -p
   # Enter your password when prompted
   
   # Run these in MySQL console:
   source C:/Users/Prasanna/OneDrive/Documents/BANKING-SYSTEM/database/schema.sql
   source C:/Users/Prasanna/OneDrive/Documents/BANKING-SYSTEM/database/sample_data.sql
   exit
   ```

5. **Update database.properties:**
   ```properties
   db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC
   db.username=root
   db.password=YOUR_MYSQL_PASSWORD_HERE
   ```

#### Step 2: Install Maven (Required for command-line)

1. **Download Maven:**
   - https://maven.apache.org/download.cgi
   - Get: `apache-maven-3.9.x-bin.zip`

2. **Extract:**
   - Extract to: `C:\Program Files\Apache\maven`

3. **Add to PATH:**
   - Windows Search → "Environment Variables"
   - System Variables → New:
     - Name: `MAVEN_HOME`
     - Value: `C:\Program Files\Apache\maven`
   - Edit "Path" → Add: `%MAVEN_HOME%\bin`
   - Click OK

4. **Verify (close and reopen terminal):**
   ```bash
   mvn --version
   ```

#### Step 3: Run the Project

```bash
cd C:\Users\Prasanna\OneDrive\Documents\BANKING-SYSTEM
mvn clean javafx:run
```

---

## ⚡ Quick Alternative: Web Version Only

If you just want to see it working without JavaFX:

### Requirements:
- MySQL (must install)
- Apache Tomcat (or use IDE)

### Steps with IntelliJ:
1. Install MySQL (see above)
2. Open project in IntelliJ
3. Add Tomcat Server configuration
4. Run on Tomcat
5. Open: http://localhost:8080/smart-banking-system/login

---

## 🔧 What Each Tool Does

| Tool | Purpose | Can Skip? |
|------|---------|-----------|
| **Java** | Runs the application | ❌ No - Required |
| **MySQL** | Stores banking data | ❌ No - Required |
| **Maven** | Downloads libraries & builds project | ✅ Yes - if using IntelliJ/Eclipse |
| **JavaFX** | Desktop UI framework | ✅ Yes - if only using web version |
| **Tomcat** | Runs web application | ✅ Yes - if only using desktop version |

---

## 🎮 Ready-to-Use Commands

After installing MySQL and Maven:

### First Time Setup:
```bash
# Create database
mysql -u root -p < database/schema.sql

# Load sample data (optional)
mysql -u root -p smart_banking < database/sample_data.sql

# Edit database.properties with your MySQL password

# Download dependencies
mvn clean install
```

### Run Desktop App:
```bash
mvn clean javafx:run
```

### Build Web App:
```bash
mvn clean package
# Then deploy target/smart-banking-system.war to Tomcat
```

---

## 🆘 Still Having Issues?

### The Absolute Easiest Path:

1. **Install IntelliJ IDEA Community Edition** (Free)
   - https://www.jetbrains.com/idea/download/

2. **Install MySQL** (Required)
   - https://dev.mysql.com/downloads/installer/

3. **Open Project in IntelliJ**
   - File → Open → Select BANKING-SYSTEM folder

4. **Let IntelliJ Handle Everything**
   - It downloads dependencies automatically
   - Built-in Maven support
   - One-click run

**That's it!** IntelliJ does all the hard work for you.

---

## 📞 Quick Checklist

Before running:
- [ ] MySQL installed and running
- [ ] Database 'smart_banking' created (run schema.sql)
- [ ] database.properties has correct password
- [ ] Maven installed OR using IntelliJ/Eclipse
- [ ] Java 11+ installed (you have Java 21 ✓)

---

## 🎯 Default Login After Setup

**Admin:**
- Username: `admin`
- Password: `admin123`

**Test User:**
- Username: `john_doe`
- Password: `password123`

---

## Download Links Summary

1. **MySQL**: https://dev.mysql.com/downloads/installer/
2. **Maven**: https://maven.apache.org/download.cgi
3. **IntelliJ IDEA**: https://www.jetbrains.com/idea/download/
4. **Eclipse**: https://www.eclipse.org/downloads/

**Recommended:** Install IntelliJ IDEA + MySQL only
