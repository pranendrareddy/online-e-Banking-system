# Installing Prerequisites for Smart Banking System

## 1. Install MySQL Server

### Download MySQL
1. Go to: https://dev.mysql.com/downloads/installer/
2. Download **MySQL Installer for Windows** (mysql-installer-web-community)
3. Run the installer

### Installation Steps
1. Choose **"Developer Default"** or **"Server only"**
2. Click **Next** through the installation
3. Configure MySQL Server:
   - **Port**: 3306 (default)
   - **Root Password**: Choose a strong password (remember this!)
   - **Windows Service Name**: MySQL80 or MySQL (note this name)
4. Click **Execute** to install
5. Complete the installation

### Verify Installation
Open Command Prompt and run:
```bash
mysql --version
```

### Add MySQL to PATH (if needed)
If `mysql` command doesn't work:
1. Find MySQL installation (usually `C:\Program Files\MySQL\MySQL Server 8.0\bin`)
2. Add to System PATH:
   - Windows Search → "Environment Variables"
   - Edit "Path" variable
   - Add: `C:\Program Files\MySQL\MySQL Server 8.0\bin`
   - Click OK
   - Restart Command Prompt/PowerShell

---

## 2. Install Maven (Optional but Recommended)

### Download Maven
1. Go to: https://maven.apache.org/download.cgi
2. Download **Binary zip archive** (apache-maven-3.x.x-bin.zip)
3. Extract to: `C:\Program Files\Apache\maven`

### Add Maven to PATH
1. Windows Search → "Environment Variables"
2. Add new System Variable:
   - Name: `MAVEN_HOME`
   - Value: `C:\Program Files\Apache\maven`
3. Edit "Path" variable:
   - Add: `%MAVEN_HOME%\bin`
4. Click OK and restart terminal

### Verify Installation
```bash
mvn --version
```

---

## 3. Quick Alternative: Use IDE's Built-in Tools

If you're using **IntelliJ IDEA** or **Eclipse**, you don't need to install Maven separately:
- Both IDEs have built-in Maven support
- They will automatically download dependencies from pom.xml

---

## After Installing MySQL

### 1. Start MySQL Service
```bash
# Find your MySQL service name
Get-Service -Name "*mysql*"

# Start it (replace MySQL80 with your service name)
net start MySQL80
```

### 2. Create Database
```bash
# Login to MySQL (will prompt for password)
mysql -u root -p

# Then run these commands:
source c:/Users/Prasanna/OneDrive/Documents/BANKING-SYSTEM/database/schema.sql
source c:/Users/Prasanna/OneDrive/Documents/BANKING-SYSTEM/database/sample_data.sql
exit
```

### 3. Configure Database Connection
Edit `database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_ROOT_PASSWORD
```

---

## Running Without Installation (Alternative)

### Use Online IDE or Simpler Database
If you want to test without installing MySQL:
1. Use **H2 Database** (embedded Java database) - requires code modification
2. Use online MySQL sandbox
3. Use Docker: `docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:8.0`

---

## Next Steps After Installation

### Using IntelliJ IDEA:
1. File → Open → Select BANKING-SYSTEM folder
2. Wait for Maven to download dependencies (bottom right corner)
3. Edit `database.properties` with your MySQL password
4. Right-click `BankingApp.java` → Run

### Using Eclipse:
1. File → Import → Maven → Existing Maven Projects
2. Select BANKING-SYSTEM folder
3. Wait for dependencies to download
4. Edit `database.properties` with your MySQL password
5. Right-click `BankingApp.java` → Run As → Java Application

---

## Quick Test Commands

After installation, verify everything:
```bash
# Check Java
java --version

# Check MySQL
mysql --version
mysql -u root -p -e "SHOW DATABASES;"

# Check Maven (if installed)
mvn --version

# Build project (if Maven installed)
mvn clean install
```
