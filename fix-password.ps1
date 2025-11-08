# Simple MySQL Password Fix
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  Fix MySQL Password" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Option 1: Reset Password Using MySQL Installer" -ForegroundColor Yellow
Write-Host "-----------------------------------------------" -ForegroundColor Yellow
Write-Host "1. Press Windows Key and search: 'MySQL Installer'" -ForegroundColor White
Write-Host "2. Open 'MySQL Installer - Community'" -ForegroundColor White
Write-Host "3. Find 'MySQL Server 8.0' -> Click 'Reconfigure'" -ForegroundColor White
Write-Host "4. Click Next until 'Accounts and Roles'" -ForegroundColor White
Write-Host "5. Set password to: root123" -ForegroundColor White
Write-Host "6. Click Execute" -ForegroundColor White
Write-Host ""

Write-Host "Option 2: Enter Current Password" -ForegroundColor Yellow
Write-Host "-----------------------------------------------" -ForegroundColor Yellow
Write-Host ""

$continue = Read-Host "Did you reset password using Option 1? (Y/N)"

if ($continue -eq "N" -or $continue -eq "n") {
    Write-Host ""
    Write-Host "Please reset your password using MySQL Installer first." -ForegroundColor Yellow
    Write-Host "Then run this script again." -ForegroundColor Yellow
    Write-Host ""
    pause
    exit
}

Write-Host ""
Write-Host "Enter your NEW MySQL root password: " -ForegroundColor Cyan -NoNewline
$password = Read-Host -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

Write-Host "Testing connection..." -ForegroundColor Cyan
$env:MYSQL_PWD = $plainPassword
$null = mysql -u root -e "SELECT 1" 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Password still incorrect!" -ForegroundColor Red
    Write-Host "Please verify you reset it correctly." -ForegroundColor Red
    $env:MYSQL_PWD = $null
    pause
    exit 1
}

Write-Host "✓ Password works!" -ForegroundColor Green
Write-Host ""

# Create database
Write-Host "Setting up database..." -ForegroundColor Cyan
mysql -u root -e "CREATE DATABASE IF NOT EXISTS smart_banking" 2>&1 | Out-Null
Get-Content "database\schema.sql" | mysql -u root smart_banking 2>&1 | Out-Null
Get-Content "database\sample_data.sql" | mysql -u root smart_banking 2>&1 | Out-Null
Write-Host "✓ Database created!" -ForegroundColor Green
Write-Host ""

# Update config
Write-Host "Updating database.properties..." -ForegroundColor Cyan
$config = "db.url=jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC`ndb.username=root`ndb.password=$plainPassword"
Set-Content -Path "database.properties" -Value $config -Force
Write-Host "✓ Configuration updated!" -ForegroundColor Green
Write-Host ""

$env:MYSQL_PWD = $null

Write-Host "============================================" -ForegroundColor Green
Write-Host "  ✓ Password Fixed!" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Write-Host "Now run these commands:" -ForegroundColor Cyan
Write-Host "  mvn clean install" -ForegroundColor White
Write-Host "  mvn clean javafx:run" -ForegroundColor White
Write-Host ""
pause
