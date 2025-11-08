# Smart Banking System

## Overview
A comprehensive banking system with both desktop (JavaFX) and web (JSP/Servlets) interfaces, featuring account management, transactions, and admin dashboard.

## Architecture

### 3-Tier Architecture
```
┌─────────────────────────────────────────┐
│   Presentation Layer                    │
│   - JavaFX Desktop UI                   │
│   - Web UI (JSP/HTML/CSS)               │
└─────────────────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│   Business Logic Layer                  │
│   - Services                            │
│   - Servlets (for Web)                  │
│   - Controllers (for Desktop)           │
└─────────────────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│   Data Access Layer                     │
│   - DAO Classes                         │
│   - JDBC Connection Pool                │
└─────────────────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│   Database (MySQL)                      │
│   - Users, Accounts, Transactions       │
└─────────────────────────────────────────┘
```

## Features

### User Features
- ✅ Account Registration
- ✅ Secure Login (encrypted passwords)
- ✅ Deposit Money
- ✅ Withdraw Money
- ✅ Check Balance
- ✅ View Transaction History
- ✅ Generate Transaction Reports

### Admin Features
- ✅ View All Users
- ✅ View All Accounts
- ✅ View All Transactions
- ✅ Transaction Summary Dashboard
- ✅ Account Management

## Technology Stack

### Desktop Application
- **Java 11+**
- **JavaFX** - UI Framework
- **JDBC** - Database Connectivity
- **MySQL** - Database

### Web Application
- **Java Servlets** - Backend Logic
- **JSP** - Dynamic Pages
- **HTML5/CSS3** - Frontend
- **Apache Tomcat 9+** - Server
- **MySQL** - Database

## Project Structure
```
BANKING-SYSTEM/
├── database/
│   ├── schema.sql
│   ├── ER_DIAGRAM.md
│   └── sample_data.sql
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/banking/
│   │   │   │   ├── model/
│   │   │   │   ├── dao/
│   │   │   │   ├── service/
│   │   │   │   ├── util/
│   │   │   │   ├── servlet/
│   │   │   │   └── desktop/
│   │   │   └── resources/
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       ├── css/
│   │       ├── js/
│   │       └── pages/
├── lib/
├── docs/
├── README.md
└── SETUP_GUIDE.md
```

## Security Features
- **SHA-256 Password Encryption**
- **Session Management**
- **SQL Injection Prevention** (PreparedStatements)
- **Role-Based Access Control** (User/Admin)

## Quick Start
See [SETUP_GUIDE.md](SETUP_GUIDE.md) for detailed setup instructions.
