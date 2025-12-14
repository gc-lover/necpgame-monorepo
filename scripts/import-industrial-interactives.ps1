# Issue: #1840
# PowerShell script to import industrial interactive objects to database

param(
    [string]$DB_HOST = "localhost",
    [string]$DB_PORT = "5432",
    [string]$DB_NAME = "necpgame",
    [string]$DB_USER = "postgres",
    [string]$DB_PASSWORD = "postgres"
)

$SCRIPT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path
$SQL_FILE = Join-Path $SCRIPT_DIR "import-industrial-interactives.sql"

# Check if SQL file exists
if (-not (Test-Path $SQL_FILE)) {
    Write-Host "❌ Error: SQL file not found: $SQL_FILE" -ForegroundColor Red
    exit 1
}

Write-Host "🚀 Starting industrial interactives import..." -ForegroundColor Green
Write-Host "📊 Database: $DB_HOST`:$DB_PORT/$DB_NAME" -ForegroundColor Cyan
Write-Host "📁 SQL file: $SQL_FILE" -ForegroundColor Cyan

# Set environment variable for password
$env:PGPASSWORD = $DB_PASSWORD

# Check database connection
Write-Host "🔍 Testing database connection..." -ForegroundColor Yellow
try {
    $connectionTest = & psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT 1;" 2>$null
    if ($LASTEXITCODE -ne 0) {
        throw "Connection test failed"
    }
    Write-Host "OK Database connection successful" -ForegroundColor Green
}
catch {
    Write-Host "❌ Database connection failed" -ForegroundColor Red
    Write-Host "   Please check your database configuration:" -ForegroundColor Yellow
    Write-Host "   - DB_HOST: $DB_HOST" -ForegroundColor Yellow
    Write-Host "   - DB_PORT: $DB_PORT" -ForegroundColor Yellow
    Write-Host "   - DB_NAME: $DB_NAME" -ForegroundColor Yellow
    Write-Host "   - DB_USER: $DB_USER" -ForegroundColor Yellow
    exit 1
}

# Run the import
Write-Host "📤 Executing import script..." -ForegroundColor Yellow
try {
    & psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f $SQL_FILE
    if ($LASTEXITCODE -ne 0) {
        throw "Import failed"
    }
    Write-Host "OK Import completed successfully!" -ForegroundColor Green
}
catch {
    Write-Host "❌ Import failed" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🎉 Industrial interactives import completed successfully!" -ForegroundColor Green
Write-Host "📋 Check the output above for import statistics" -ForegroundColor Cyan
Write-Host ""
Write-Host "🔍 To verify the import, you can run:" -ForegroundColor Cyan
Write-Host "& psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c \"SELECT object_id, name, category, threat_level FROM gameplay.interactive_objects WHERE object_id IN ('electrical_panel', 'valve_system', 'conveyor_system', 'crane_manipulator'); \"" -ForegroundColor Cyan