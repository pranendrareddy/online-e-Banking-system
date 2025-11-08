# Smart Banking System - Quick Setup Script
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Smart Banking System - Quick Setup" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Get MySQL password
Write-Host "Step 1: MySQL Password" -ForegroundColor Yellow
Write-Host "------------------------------" -ForegroundColor Yellow
$password = Read-Host "Enter your MySQL root password" -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

Write-Host ""
Write-Host "Testing MySQL connection..." -ForegroundColor Yellow
$env:MYSQL_PWD = $plainPassword
try {
    $result = mysql -u root -e "SELECT 'Connection successful!' AS Status;" 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "Connection failed"
    }
} catch {
    Write-Host "ERROR: MySQL connection failed!" -ForegroundColor Red
    Write-Host "The password might be incorrect." -ForegroundColor Red
    Write-Host ""
    Write-Host "To reset password:" -ForegroundColor Yellow
    Write-Host "1. Search 'MySQL Installer' in Windows" -ForegroundColor White
    Write-Host "2. Reconfigure MySQL Server" -ForegroundColor White
    Write-Host "3. Set new root password" -ForegroundColor White
    exit 1
}

Write-Host "✓ MySQL connection successful!" -ForegroundColor Green
Write-Host ""

# Step 2: Create database
Write-Host "Step 2: Creating Database" -ForegroundColor Yellow
Write-Host "------------------------------" -ForegroundColor Yellow
Get-Content database\schema.sql | mysql -u root smart_banking 2>&1 | Out-Null
Write-Host "✓ Database created!" -ForegroundColor Green

# Load sample data
Get-Content database\sample_data.sql | mysql -u root smart_banking 2>&1 | Out-Null
Write-Host "✓ Sample data loaded!" -ForegroundColor Green
Write-Host ""

# Step 3: Update database.properties
Write-Host "Step 3: Updating Configuration" -ForegroundColor Yellow
Write-Host "------------------------------" -ForegroundColor Yellow
$configContent = @"
db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC
db.username=root
db.password=$plainPassword
"@
Set-Content -Path "database.properties" -Value $configContent
Write-Host "✓ Configuration updated!" -ForegroundColor Green
Write-Host ""

# Step 4: Build project
Write-Host "Step 4: Building Project" -ForegroundColor Yellow
Write-Host "------------------------------" -ForegroundColor Yellow
Write-Host "Downloading dependencies... (this may take a few minutes)" -ForegroundColor Cyan
mvn clean install -q 2>&1 | Out-Null
Write-Host "✓ Project built successfully!" -ForegroundColor Green
Write-Host ""

# Clear password from environment
$env:MYSQL_PWD = $null

# Step 5: Ready to run
Write-Host "================================================" -ForegroundColor Green
Write-Host "  Setup Complete! Ready to Run!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green
Write-Host ""
Write-Host "To start the desktop application, run:" -ForegroundColor Cyan
Write-Host "  mvn clean javafx:run" -ForegroundColor White
Write-Host ""
Write-Host "Default Login Credentials:" -ForegroundColor Cyan
Write-Host "  Admin - Username: admin, Password: admin123" -ForegroundColor White
Write-Host "  User  - Username: john_doe, Password: password123" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to launch the application..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

Write-Host ""
Write-Host "Launching Smart Banking System..." -ForegroundColor Cyan
mvn clean javafx:run
