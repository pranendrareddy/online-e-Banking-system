# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Prerequisites Checklist
- [ ] Java JDK 11+ installed
- [ ] MySQL Server running
- [ ] IDE (Eclipse/IntelliJ) installed
- [ ] Apache Tomcat 9+ (for web app)

---

## Step 1: Database Setup (2 minutes)

```bash
# Login to MySQL
mysql -u root -p

# Execute schema
mysql -u root -p < database/schema.sql

# (Optional) Load sample data
mysql -u root -p smart_banking < database/sample_data.sql
```

**Or use MySQL Workbench:**
1. Open `database/schema.sql`
2. Execute all statements
3. Optionally execute `database/sample_data.sql`

---

## Step 2: Configure Database Connection

Edit `database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_PASSWORD_HERE
```

---

## Step 3: Choose Your Version

### 🖥️ Option A: Desktop Application (JavaFX)

**Eclipse:**
```
1. Import project (Maven → Existing Maven Projects)
2. Right-click BankingApp.java → Run As → Java Application
3. Add VM arguments if needed:
   --module-path "PATH/TO/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
```

**IntelliJ IDEA:**
```
1. Open project folder
2. Maven → Reload Project
3. Run BankingApp.java
```

**Maven Command:**
```bash
mvn clean javafx:run
```

### 🌐 Option B: Web Application (JSP/Servlets)

**Eclipse:**
```
1. Import project
2. Configure Tomcat server
3. Right-click project → Run As → Run on Server
4. Open browser: http://localhost:8080/smart-banking-system/login
```

**IntelliJ IDEA:**
```
1. Open project
2. Configure Tomcat (Run → Edit Configurations)
3. Run Tomcat configuration
```

**Manual Deployment:**
```bash
# Build WAR
mvn clean package

# Copy to Tomcat
copy target\smart-banking-system.war C:\tomcat\webapps\

# Start Tomcat
C:\tomcat\bin\startup.bat

# Access: http://localhost:8080/smart-banking-system/login
```

---

## Step 4: Login and Test

### Default Admin Account
```
Username: admin
Password: admin123
```

### Sample User Accounts (if loaded sample data)
```
Username: john_doe
Password: password123
```

---

## 🎯 Quick Test Scenarios

### As User:
1. ✅ Register new account
2. ✅ Login
3. ✅ Create bank account (Savings/Current)
4. ✅ Deposit money
5. ✅ Withdraw money
6. ✅ View transaction history

### As Admin:
1. ✅ Login as admin
2. ✅ View all users
3. ✅ View all accounts
4. ✅ View all transactions
5. ✅ Check system statistics

---

## 📁 Required Libraries

### Desktop App:
- JavaFX SDK 17+
- MySQL Connector/J 8.0.33

### Web App:
- Servlet API 4.0.1
- JSP API 2.3.3
- MySQL Connector/J 8.0.33
- JSTL 1.2

*All libraries included in `pom.xml` - Maven will download automatically*

---

## 🐛 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| JavaFX not found | Add VM arguments with module path |
| MySQL connection error | Check credentials in database.properties |
| Port 8080 in use | Stop other services or change Tomcat port |
| Servlet errors | Ensure Tomcat is properly configured |
| Build failures | Run `mvn clean install` |

---

## 📚 Need More Help?

See detailed **SETUP_GUIDE.md** for:
- Complete installation instructions
- Detailed troubleshooting
- Production deployment guide
- Security configuration

---

## 🎉 You're Ready!

The system is now ready to use. Happy Banking! 🏦
