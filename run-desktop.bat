@echo off
echo ================================================
echo   Smart Banking System - Desktop Application
echo ================================================
echo.

REM Check if MySQL is running
echo [1/4] Checking MySQL connection...
mysql -u root -e "SELECT 1" 2>nul
if %errorlevel% neq 0 (
    echo ERROR: MySQL is not installed or not running!
    echo.
    echo Please install MySQL first:
    echo 1. Download from: https://dev.mysql.com/downloads/installer/
    echo 2. Install and set root password
    echo 3. Run: mysql -u root -p ^< database/schema.sql
    echo.
    pause
    exit /b 1
)
echo    MySQL is running ✓
echo.

REM Check database exists
echo [2/4] Checking database...
mysql -u root -e "USE smart_banking" 2>nul
if %errorlevel% neq 0 (
    echo    Database 'smart_banking' not found!
    echo    Creating database...
    mysql -u root -p < database\schema.sql
    if %errorlevel% neq 0 (
        echo    ERROR: Failed to create database
        pause
        exit /b 1
    )
    echo    Database created ✓
) else (
    echo    Database exists ✓
)
echo.

REM Download dependencies (requires internet)
echo [3/4] Downloading dependencies...
echo    This requires Maven. Installing Maven wrapper...
if not exist mvnw.cmd (
    echo    ERROR: Maven is required to download dependencies.
    echo.
    echo    Please either:
    echo    1. Install Maven from: https://maven.apache.org/download.cgi
    echo    2. Or open this project in IntelliJ IDEA or Eclipse
    echo.
    pause
    exit /b 1
)

REM Run with Maven wrapper
echo [4/4] Starting application...
call mvnw.cmd clean javafx:run

pause
