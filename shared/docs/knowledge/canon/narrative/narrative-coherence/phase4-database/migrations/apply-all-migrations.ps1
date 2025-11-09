# Apply All Migrations Script (Windows)
# Version: 1.0.0
# Date: 2025-11-07 00:28

# Configuration
$DB_NAME = if ($env:DB_NAME) { $env:DB_NAME } else { "necpgame" }
$DB_USER = if ($env:DB_USER) { $env:DB_USER } else { "postgres" }
$DB_HOST = if ($env:DB_HOST) { $env:DB_HOST } else { "localhost" }
$DB_PORT = if ($env:DB_PORT) { $env:DB_PORT } else { "5432" }
$DB_PASSWORD = $env:DB_PASSWORD

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "NECPGAME - Quest System Migrations" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Database: $DB_NAME"
Write-Host "Host: ${DB_HOST}:${DB_PORT}"
Write-Host "User: $DB_USER"
Write-Host "=========================================" -ForegroundColor Cyan

# Функция для применения миграции
function Apply-Migration {
    param([string]$File)
    
    Write-Host ""
    Write-Host "Applying: $File" -ForegroundColor Yellow
    
    $env:PGPASSWORD = $DB_PASSWORD
    psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f $File
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Success: $File" -ForegroundColor Green
    } else {
        Write-Host "❌ Failed: $File" -ForegroundColor Red
        exit 1
    }
}

# Применяем миграции в порядке
Write-Host ""
Write-Host "Step 1/5: Expanding quests table..." -ForegroundColor Cyan
Apply-Migration "001-expand-quests-table.sql"

Write-Host ""
Write-Host "Step 2/5: Creating quest branches..." -ForegroundColor Cyan
Apply-Migration "002-create-quest-branches.sql"

Write-Host ""
Write-Host "Step 3/5: Creating dialogue system..." -ForegroundColor Cyan
Apply-Migration "003-create-dialogue-system.sql"

Write-Host ""
Write-Host "Step 4/5: Creating player systems..." -ForegroundColor Cyan
Apply-Migration "004-create-player-systems.sql"

Write-Host ""
Write-Host "Step 5/5: Creating world state system..." -ForegroundColor Cyan
Apply-Migration "005-create-world-state-system.sql"

Write-Host ""
Write-Host "=========================================" -ForegroundColor Green
Write-Host "✅ ALL MIGRATIONS APPLIED SUCCESSFULLY!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:"
Write-Host "1. Verify tables: psql -d $DB_NAME -c '\dt quest*'"
Write-Host "2. Check indexes: psql -d $DB_NAME -c '\di quest*'"
Write-Host "3. Import quest data: run import scripts"
Write-Host ""

