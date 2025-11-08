# MySQL Password Reset and Database Setup
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  MySQL Password Reset & Database Setup" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Choose an option:" -ForegroundColor Yellow
Write-Host "1. I know my MySQL password (just need to update config)" -ForegroundColor White
Write-Host "2. I forgot my password (reset it using MySQL Installer)" -ForegroundColor White
Write-Host ""
$choice = Read-Host "Enter 1 or 2"

if ($choice -eq "2") {
    Write-Host ""
    Write-Host "To reset your MySQL password:" -ForegroundColor Yellow
    Write-Host "-------------------------------------" -ForegroundColor Yellow
    Write-Host "1. Press Windows Key" -ForegroundColor White
    Write-Host "2. Search: 'MySQL Installer'" -ForegroundColor White
    Write-Host "3. Open 'MySQL Installer - Community'" -ForegroundColor White
    Write-Host "4. Find 'MySQL Server 8.0' in the list" -ForegroundColor White
    Write-Host "5. Click 'Reconfigure' button" -ForegroundColor White
    Write-Host "6. Click 'Next' until 'Accounts and Roles'" -ForegroundColor White
    Write-Host "7. Set NEW root password (example: root123)" -ForegroundColor White
    Write-Host "8. Click 'Next' and 'Execute'" -ForegroundColor White
    Write-Host ""
    Write-Host "After resetting, run this script again and choose option 1" -ForegroundColor Green
    Write-Host ""
    pause
    exit
}

Write-Host ""
Write-Host "Enter your MySQL root password:" -ForegroundColor Yellow
$password = Read-Host -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

# Test MySQL connection
Write-Host ""
Write-Host "Testing MySQL connection..." -ForegroundColor Cyan
$env:MYSQL_PWD = $plainPassword
$testResult = mysql -u root -e "SELECT 1" 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Password is incorrect!" -ForegroundColor Red
    Write-Host "The password you entered doesn't work." -ForegroundColor Red
    Write-Host ""
    Write-Host "Please:" -ForegroundColor Yellow
    Write-Host "1. Make sure you're entering the correct password" -ForegroundColor White
    Write-Host "2. Or choose option 2 to reset it" -ForegroundColor White
    Write-Host ""
    $env:MYSQL_PWD = $null
    pause
    exit 1
}

Write-Host "✓ Password is correct!" -ForegroundColor Green
Write-Host ""

# Create database
Write-Host "Creating database 'smart_banking'..." -ForegroundColor Cyan
mysql -u root -e "CREATE DATABASE IF NOT EXISTS smart_banking" 2>&1 | Out-Null

Write-Host "Loading schema..." -ForegroundColor Cyan
Get-Content "database\schema.sql" | mysql -u root smart_banking 2>&1

Write-Host "Loading sample data..." -ForegroundColor Cyan
Get-Content "database\sample_data.sql" | mysql -u root smart_banking 2>&1

Write-Host "✓ Database created successfully!" -ForegroundColor Green
Write-Host ""

# Update database.properties
Write-Host "Updating database.properties..." -ForegroundColor Cyan
$configContent = @"
db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC
db.username=root
db.password=$plainPassword
"@
Set-Content -Path "database.properties" -Value $configContent -Force
Write-Host "✓ Configuration updated!" -ForegroundColor Green
Write-Host ""

# Clear password
$env:MYSQL_PWD = $null

# Build project
Write-Host "Building project (downloading dependencies)..." -ForegroundColor Cyan
Write-Host "This may take 2-3 minutes on first run..." -ForegroundColor Yellow
mvn clean install

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Green
    Write-Host "  ✓ Setup Complete! Ready to Run!" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "To start the application:" -ForegroundColor Cyan
    Write-Host "  mvn clean javafx:run" -ForegroundColor White
    Write-Host ""
    Write-Host "Default Login:" -ForegroundColor Cyan
    Write-Host "  Admin: username=admin, password=admin123" -ForegroundColor White
    Write-Host "  User:  username=john_doe, password=password123" -ForegroundColor White
    Write-Host ""
    
    $launch = Read-Host "Launch application now? (Y/N)"
    if ($launch -eq "Y" -or $launch -eq "y") {
        Write-Host ""
        Write-Host "Starting Smart Banking System..." -ForegroundColor Cyan
        mvn clean javafx:run
    }
} else {
    Write-Host ""
    Write-Host "Build completed with some warnings (this is normal)" -ForegroundColor Yellow
    Write-Host "You can still run the application with:" -ForegroundColor Yellow
    Write-Host "  mvn clean javafx:run" -ForegroundColor White
}
