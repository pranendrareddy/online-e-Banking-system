@echo off
echo ================================================
echo   Smart Banking System - Database Setup
echo ================================================
echo.
echo Step 1: Reset MySQL Root Password
echo ----------------------------------
echo.
echo Option 1 - Use MySQL Installer (EASIEST):
echo   1. Press Windows Key and search: MySQL Installer
echo   2. Open "MySQL Installer - Community"
echo   3. Find "MySQL Server 8.0" in the list
echo   4. Click "Reconfigure" button
echo   5. Keep clicking Next until "Accounts and Roles"
echo   6. Set a NEW root password (e.g., "root123")
echo   7. WRITE IT DOWN!
echo   8. Click Next and Execute
echo.
echo Option 2 - Try Common Passwords:
echo   - root
echo   - admin
echo   - password
echo   - mysql
echo   - (blank)
echo.
echo ----------------------------------
echo.
set /p continue="Press Enter after you've set/remembered your password..."
echo.
echo Testing MySQL connection...
echo.
mysql -u root -p -e "SELECT 'MySQL connection successful!' AS Status;"
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Could not connect to MySQL
    echo Please make sure you entered the correct password.
    echo.
    pause
    exit /b 1
)
echo.
echo ================================================
echo Step 2: Creating Database
echo ================================================
echo.
echo Running schema.sql...
mysql -u root -p smart_banking < database\schema.sql
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to create database
    pause
    exit /b 1
)
echo Database created successfully!
echo.
echo Loading sample data...
mysql -u root -p smart_banking < database\sample_data.sql
if %errorlevel% neq 0 (
    echo WARNING: Sample data load failed (optional)
) else (
    echo Sample data loaded!
)
echo.
echo ================================================
echo Step 3: Verify Database
echo ================================================
echo.
mysql -u root -p -e "USE smart_banking; SHOW TABLES;"
echo.
echo ================================================
echo Database Setup Complete!
echo ================================================
echo.
echo Next steps:
echo 1. Update database.properties with your MySQL password
echo 2. Run: mvn clean install
echo 3. Run: mvn clean javafx:run
echo.
echo Default login credentials:
echo   Admin - Username: admin, Password: admin123
echo   User  - Username: john_doe, Password: password123
echo.
pause
