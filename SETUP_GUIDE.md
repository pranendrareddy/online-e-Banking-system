# Smart Banking System - Setup Guide

## Prerequisites

### Required Software
1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **MySQL Server 5.7 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Default port: 3306

3. **Apache Tomcat 9.0 or higher** (for web application)
   - Download from: https://tomcat.apache.org/download-90.cgi
   - Extract to a directory (e.g., `C:\apache-tomcat-9.0.xx`)

4. **IDE** (Choose one)
   - **Eclipse IDE for Enterprise Java and Web Developers**
     - Download from: https://www.eclipse.org/downloads/
   - **IntelliJ IDEA Ultimate** (recommended)
     - Download from: https://www.jetbrains.com/idea/download/

5. **Maven** (Optional - IDEs have built-in Maven)
   - Download from: https://maven.apache.org/download.cgi

6. **JavaFX SDK** (for desktop application)
   - Download from: https://gluonhq.com/products/javafx/

---

## Database Setup

### Step 1: Start MySQL Server
```bash
# Windows
net start MySQL80

# Or start via MySQL Workbench or Services
```

### Step 2: Create Database and Tables
1. Open MySQL Workbench or command line client
2. Login as root:
   ```bash
   mysql -u root -p
   ```
3. Execute the schema script:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
   
   **Or** copy and paste the contents of `database/schema.sql` into MySQL Workbench and execute.

### Step 3: (Optional) Load Sample Data
```bash
mysql -u root -p smart_banking < database/sample_data.sql
```

### Step 4: Configure Database Connection
Edit `database.properties` file:
```properties
db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

**Default Admin Credentials:**
- Username: `admin`
- Password: `admin123`

---

## Project Setup

### Option A: Using Eclipse IDE

#### 1. Import Project
- File → Import → Maven → Existing Maven Projects
- Select the `BANKING-SYSTEM` folder
- Click Finish

#### 2. Add JavaFX Libraries (for Desktop App)
- Right-click project → Build Path → Configure Build Path
- Libraries → Add External JARs
- Navigate to JavaFX SDK → lib folder
- Add all .jar files

#### 3. Configure Server (for Web App)
- Window → Show View → Servers
- Right-click → New → Server
- Select Apache Tomcat 9.0
- Browse to Tomcat installation directory
- Add the project to configured server

#### 4. Configure MySQL Driver
- Download MySQL Connector/J: https://dev.mysql.com/downloads/connector/j/
- Add to project Build Path: Right-click project → Build Path → Add External Archives
- Select `mysql-connector-java-8.0.xx.jar`

### Option B: Using IntelliJ IDEA

#### 1. Open Project
- File → Open
- Select the `BANKING-SYSTEM` folder
- IntelliJ will automatically detect Maven project

#### 2. Configure JavaFX
- File → Project Structure → Libraries
- Add → Java
- Navigate to JavaFX SDK → lib folder
- Click OK

#### 3. Configure Tomcat
- Run → Edit Configurations
- Add New → Tomcat Server → Local
- Configure Tomcat Home directory
- Deployment tab → Add → Artifact → smart-banking-system:war exploded

#### 4. Maven Dependencies
- IntelliJ automatically downloads Maven dependencies
- If not, right-click `pom.xml` → Maven → Reload Project

---

## Running the Application

### Desktop Application (JavaFX)

#### Method 1: Using IDE

**Eclipse:**
1. Right-click on `BankingApp.java`
2. Run As → Java Application
3. If errors occur, add VM arguments:
   ```
   --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml
   ```

**IntelliJ IDEA:**
1. Open `BankingApp.java`
2. Right-click → Run 'BankingApp.main()'
3. Configure VM options if needed

#### Method 2: Using Maven
```bash
mvn clean javafx:run
```

#### Method 3: Using Command Line
```bash
# Compile
javac --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls -d bin src/main/java/com/banking/**/*.java

# Run
java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls -cp bin com.banking.desktop.BankingApp
```

### Web Application (JSP/Servlets)

#### Method 1: Using IDE

**Eclipse:**
1. Right-click project → Run As → Run on Server
2. Select Tomcat server
3. Application will open in browser

**IntelliJ IDEA:**
1. Select Tomcat configuration
2. Click Run button
3. Browser will open automatically

#### Method 2: Manual Deployment
```bash
# Build WAR file
mvn clean package

# Copy WAR to Tomcat
copy target\smart-banking-system.war C:\apache-tomcat-9.0.xx\webapps\

# Start Tomcat
C:\apache-tomcat-9.0.xx\bin\startup.bat
```

#### Access URLs:
- **Login Page**: http://localhost:8080/smart-banking-system/login
- **Register Page**: http://localhost:8080/smart-banking-system/register

---

## Testing the Application

### Default Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin123`

**Sample User Accounts** (if sample data loaded):
- Username: `john_doe` | Password: `password123`
- Username: `jane_smith` | Password: `password123`
- Username: `bob_wilson` | Password: `password123`

### Test Scenarios

1. **User Registration**
   - Register a new user
   - Verify account creation
   - Login with new credentials

2. **Account Creation**
   - Login as user
   - Create savings account
   - Verify account appears in dashboard

3. **Deposit Money**
   - Select an account
   - Make a deposit
   - Check updated balance

4. **Withdraw Money**
   - Select an account with balance
   - Make a withdrawal
   - Verify balance update

5. **Transaction History**
   - View transaction history
   - Verify all transactions are logged

6. **Admin Dashboard**
   - Login as admin
   - View all users
   - View all accounts
   - View all transactions
   - Check statistics

---

## Project Structure

```
BANKING-SYSTEM/
│
├── database/                    # Database scripts
│   ├── schema.sql              # Database schema
│   ├── ER_DIAGRAM.md           # ER diagram documentation
│   └── sample_data.sql         # Sample test data
│
├── src/main/java/com/banking/
│   ├── model/                  # Domain models
│   │   ├── User.java
│   │   ├── Account.java
│   │   └── Transaction.java
│   │
│   ├── dao/                    # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── AccountDAO.java
│   │   └── TransactionDAO.java
│   │
│   ├── service/                # Business logic
│   │   └── BankingService.java
│   │
│   ├── util/                   # Utility classes
│   │   ├── DatabaseConnection.java
│   │   └── PasswordUtil.java
│   │
│   ├── servlet/                # Web servlets
│   │   ├── LoginServlet.java
│   │   ├── RegisterServlet.java
│   │   ├── UserDashboardServlet.java
│   │   ├── AdminDashboardServlet.java
│   │   ├── TransactionServlet.java
│   │   └── AccountServlet.java
│   │
│   └── desktop/                # JavaFX desktop app
│       ├── BankingApp.java
│       ├── LoginController.java
│       ├── UserDashboardController.java
│       └── AdminDashboardController.java
│
├── src/main/webapp/            # Web application
│   ├── WEB-INF/
│   │   └── web.xml
│   ├── pages/                  # JSP pages
│   │   ├── login.jsp
│   │   ├── register.jsp
│   │   ├── user-dashboard.jsp
│   │   ├── admin-dashboard.jsp
│   │   ├── transactions.jsp
│   │   └── error.jsp
│   └── css/
│       └── style.css
│
├── src/main/resources/
│   └── styles/
│       └── banking-style.css   # JavaFX CSS
│
├── pom.xml                     # Maven configuration
├── database.properties          # Database configuration
├── README.md                   # Project overview
└── SETUP_GUIDE.md             # This file
```

---

## Troubleshooting

### Common Issues

**1. MySQL Connection Failed**
- Verify MySQL service is running
- Check database credentials in `database.properties`
- Ensure MySQL port 3306 is not blocked

**2. JavaFX Module Not Found**
- Add VM arguments: `--module-path PATH_TO_JAVAFX_SDK --add-modules javafx.controls,javafx.fxml`
- Verify JavaFX SDK is correctly configured

**3. Servlet Classes Not Found**
- Ensure servlet-api.jar is in classpath
- Verify Tomcat is properly configured
- Clean and rebuild project

**4. Port 8080 Already in Use**
- Stop other applications using port 8080
- Change Tomcat port in `server.xml`

**5. Database Tables Not Created**
- Manually execute `schema.sql`
- Check MySQL permissions
- Verify database exists: `SHOW DATABASES;`

### Support Commands

```bash
# Check Java version
java -version

# Check Maven
mvn -version

# Check MySQL
mysql --version

# Test database connection
mysql -u root -p -e "USE smart_banking; SHOW TABLES;"

# Build project
mvn clean install

# Run tests
mvn test
```

---

## Security Notes

1. **Change Default Passwords**
   - Change admin password after first login
   - Use strong passwords in production

2. **Database Security**
   - Create dedicated MySQL user for the application
   - Don't use root user in production
   - Restrict database access

3. **HTTPS Configuration**
   - Configure SSL/TLS for production deployment
   - Use secure session management

4. **Input Validation**
   - All inputs are validated server-side
   - SQL injection prevention using PreparedStatements
   - Password strength requirements enforced

---

## Additional Features

### Password Encryption
- SHA-256 algorithm
- Implemented in `PasswordUtil.java`

### Session Management
- 30-minute timeout
- Secure session attributes
- Automatic logout on browser close

### Role-Based Access Control
- USER: Limited to own accounts
- ADMIN: Full system access

### Transaction Reports
- View complete transaction history
- Export functionality (future enhancement)

---

## Deployment to Production

### Web Application Deployment

1. **Build WAR file:**
   ```bash
   mvn clean package
   ```

2. **Deploy to production Tomcat:**
   - Copy `target/smart-banking-system.war` to production server
   - Place in Tomcat `webapps` directory
   - Configure production database settings
   - Enable HTTPS

3. **Configure firewall:**
   - Open port 8080 or 443 (HTTPS)
   - Restrict database port 3306

### Desktop Application Deployment

1. **Create executable JAR:**
   ```bash
   mvn clean package
   ```

2. **Bundle with JavaFX:**
   - Use jpackage tool (JDK 14+)
   - Create installer for Windows/Mac/Linux

---

## License

This project is created for educational purposes.

---

## Contact & Support

For issues, questions, or contributions, please refer to project documentation or contact the development team.

**Happy Banking! 🏦**
