# Online E-Banking System

A comprehensive banking management system with both desktop (JavaFX) and web interfaces for managing accounts, transactions, and user operations.

## 🌟 Features

### User Management
- User registration and authentication
- Role-based access control (Admin/User)
- Secure password storage using SHA-256 hashing
- Profile management

### Account Operations
- Create and manage multiple bank accounts
- Support for different account types (Savings, Current)
- Real-time balance tracking
- Account status management

### Transaction Management
- Deposit and withdrawal operations
- Transaction history and tracking
- Balance verification before withdrawals
- Detailed transaction records with timestamps

### Dual Interface
- **Desktop Application**: JavaFX-based GUI for bank staff
- **Web Application**: JSP/Servlet-based interface for online banking

## 🛠️ Technology Stack

### Backend
- **Language**: Java 11
- **Build Tool**: Apache Maven
- **Database**: MySQL 8.0
- **Desktop Framework**: JavaFX 17.0.2

### Web Technologies
- **Servlet API**: 4.0.1
- **JSP**: 2.3.3
- **JSTL**: 1.2

### Dependencies
- MySQL Connector Java 8.0.33
- JUnit 4.13.2 (Testing)

## 📋 Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6+
- MySQL Server 8.0+
- Apache Tomcat 9.0+ (for web deployment)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/pranendrareddy/online-e-Banking-system.git
cd BANKING-SYSTEM
```

### 2. Database Setup

#### Automated Setup (Windows)
```bash
# Run the database setup script
.\setup-database.bat
```

#### Manual Setup
```bash
# Login to MySQL
mysql -u root -p

# Run the database schema
source database/schema.sql
```

### 3. Configure Database Connection
Edit `database.properties` with your MySQL credentials:
```properties
db.url=jdbc:mysql://localhost:3306/banking_system
db.username=your_username
db.password=your_password
```

### 4. Build the Project
```bash
mvn clean install
```

## 🖥️ Running the Application

### Desktop Application
```bash
# Using the batch file (Windows)
.\run-desktop.bat

# Or using Maven
mvn javafx:run
```

### Web Application
1. Deploy the WAR file to Tomcat:
```bash
# Copy the WAR file
cp target/smart-banking-system.war %CATALINA_HOME%/webapps/

# Start Tomcat
%CATALINA_HOME%/bin/startup.bat
```

2. Access the application:
```
http://localhost:8080/smart-banking-system
```

## 📁 Project Structure

```
BANKING-SYSTEM/
├── src/
│   └── main/
│       ├── java/          # Java source files
│       ├── resources/     # Configuration files
│       └── webapp/        # Web application files (JSP, CSS, JS)
├── database/              # Database schema and ER diagrams
├── lib/                   # External libraries
├── target/               # Compiled files and WAR
├── pom.xml              # Maven configuration
├── database.properties  # Database configuration
└── README.md           # This file
```

## 🗄️ Database Schema

### Main Tables
- **users**: User account information and authentication
- **accounts**: Bank account details and balances
- **transactions**: Transaction history and records

See `database/ER_DIAGRAM.md` for detailed entity relationships.

## 🔐 Security Features

- Password hashing using SHA-256
- Role-based access control (RBAC)
- SQL injection prevention using prepared statements
- Session management for web interface
- Account balance validation before transactions

## 👥 Default Users

After database setup, default admin account:
- **Username**: admin
- **Password**: admin123

*Note: Change default credentials after first login*

## 🧪 Testing

Run unit tests:
```bash
mvn test
```

## 📝 Business Rules

1. Users must have unique username and email
2. Account numbers are auto-generated (10 digits)
3. Account balance cannot be negative
4. Transaction amounts must be positive
5. Withdrawals cannot exceed current balance
6. Admin users have full system access
7. Regular users can only access their own accounts

## 🛠️ Maintenance Scripts

- `setup-database.bat` - Initial database setup
- `reset-mysql-password.bat` - Reset MySQL root password
- `reset-and-setup.ps1` - Complete system reset and setup
- `quick-setup.ps1` - Quick environment setup

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Pranendra Reddy**
- GitHub: [@pranendrareddy](https://github.com/pranendrareddy)

## 📞 Support

For support and queries, please open an issue in the GitHub repository.

## 🔄 Version History

- **v1.0.0** - Initial release
  - Desktop application with JavaFX
  - Web application with JSP/Servlets
  - Core banking operations
  - User and account management

---

**Note**: This is an educational project for learning banking system architecture and Java enterprise development.
