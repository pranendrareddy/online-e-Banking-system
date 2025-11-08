@echo off
echo ========================================
echo MySQL Password Reset Helper
echo ========================================
echo.
echo If you forgot your MySQL root password, follow these steps:
echo.
echo 1. Stop MySQL Service:
echo    - Open Services (Win + R, type: services.msc)
echo    - Find "MySQL" or "MySQL80" service
echo    - Right-click and select "Stop"
echo.
echo 2. Start MySQL in safe mode (skip grant tables):
echo    - Open Command Prompt as Administrator
echo    - Run: mysqld --skip-grant-tables
echo.
echo 3. In a NEW Command Prompt, run:
echo    mysql -u root
echo    FLUSH PRIVILEGES;
echo    ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';
echo    EXIT;
echo.
echo 4. Stop the safe mode MySQL (Ctrl+C in first window)
echo.
echo 5. Restart MySQL Service normally
echo.
echo OR use MySQL Installer to reconfigure root password:
echo    - Search for "MySQL Installer" in Windows
echo    - Reconfigure MySQL Server
echo    - Set new root password
echo.
pause
