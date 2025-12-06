# Apply all Liquibase migrations to PostgreSQL container
# Issue: #50

param(
    [string]$ContainerName = "necpgame-postgres",
    [string]$Database = "necpgame",
    [string]$User = "postgres",
    [string]$Password = "postgres",
    [switch]$UseLiquibase = $false
)

$ErrorActionPreference = "Stop"

Write-Host "🚀 Applying all Liquibase migrations..." -ForegroundColor Cyan
Write-Host ""

# Check if container exists
$containerExists = docker ps -a --filter "name=$ContainerName" --format "{{.Names}}"
if (-not $containerExists) {
    Write-Host "❌ Container '$ContainerName' not found!" -ForegroundColor Red
    Write-Host "💡 Start PostgreSQL container first:" -ForegroundColor Yellow
    Write-Host "   cd infrastructure/docker/postgres" -ForegroundColor Gray
    Write-Host "   docker compose up -d" -ForegroundColor Gray
    exit 1
}

# Check if container is running
$containerRunning = docker ps --filter "name=$ContainerName" --format "{{.Names}}"
if (-not $containerRunning) {
    Write-Host "⚠️  Container '$ContainerName' is not running. Starting..." -ForegroundColor Yellow
    docker start $ContainerName
    Start-Sleep -Seconds 3
}

# Wait for PostgreSQL to be ready
Write-Host "⏳ Waiting for PostgreSQL to be ready..." -ForegroundColor Yellow
$maxAttempts = 30
$attempt = 0
while ($attempt -lt $maxAttempts) {
    $result = docker exec $ContainerName pg_isready -U $User 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ PostgreSQL is ready" -ForegroundColor Green
        break
    }
    $attempt++
    Start-Sleep -Seconds 1
}

if ($attempt -eq $maxAttempts) {
    Write-Host "❌ PostgreSQL failed to start" -ForegroundColor Red
    exit 1
}

if ($UseLiquibase) {
    Write-Host ""
    Write-Host "📦 Using Liquibase container..." -ForegroundColor Cyan
    
    # Check if liquibase container exists
    $liquibaseExists = docker ps -a --filter "name=necpgame-liquibase" --format "{{.Names}}"
    if (-not $liquibaseExists) {
        Write-Host "💡 Starting Liquibase container..." -ForegroundColor Yellow
        $composeFile = "infrastructure/docker/postgres/docker-compose.migrations.yml"
        if (Test-Path $composeFile) {
            docker compose -f $composeFile up -d liquibase
            docker compose -f $composeFile logs -f liquibase
        } else {
            Write-Host "❌ docker-compose.migrations.yml not found" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "🔄 Running Liquibase update..." -ForegroundColor Yellow
        docker compose -f infrastructure/docker/postgres/docker-compose.migrations.yml run --rm liquibase update
    }
} else {
    Write-Host ""
    Write-Host "📝 Applying migrations directly via psql..." -ForegroundColor Cyan
    
    # Get changelog file
    $changelogFile = "infrastructure/liquibase/changelog.yaml"
    if (-not (Test-Path $changelogFile)) {
        Write-Host "❌ Changelog file not found: $changelogFile" -ForegroundColor Red
        exit 1
    }
    
    # Check if Liquibase CLI is available
    $liquibaseCmd = Get-Command liquibase -ErrorAction SilentlyContinue
    if ($liquibaseCmd) {
        Write-Host "✅ Using Liquibase CLI..." -ForegroundColor Green
        Write-Host ""
        
        $env:LIQUIBASE_COMMAND_URL = "jdbc:postgresql://localhost:5432/$Database"
        $env:LIQUIBASE_COMMAND_USERNAME = $User
        $env:LIQUIBASE_COMMAND_PASSWORD = $Password
        $env:LIQUIBASE_COMMAND_CHANGELOG_FILE = $changelogFile
        
        liquibase update
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "✅ All migrations applied successfully!" -ForegroundColor Green
        } else {
            Write-Host ""
            Write-Host "❌ Migration failed!" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "⚠️  Liquibase CLI not found. Install it or use -UseLiquibase flag" -ForegroundColor Yellow
        Write-Host "💡 Install: https://docs.liquibase.com/tools/home.html" -ForegroundColor Gray
        Write-Host ""
        Write-Host "Alternative: Use Docker Compose with Liquibase:" -ForegroundColor Yellow
        Write-Host "   docker compose -f infrastructure/docker/postgres/docker-compose.migrations.yml up liquibase" -ForegroundColor Gray
        exit 1
    }
}

Write-Host ""
Write-Host "📊 Checking migration status..." -ForegroundColor Cyan
docker exec $ContainerName psql -U $User -d $Database -c "
SELECT 
    COUNT(*) as total_changesets,
    MAX(EXECUTEDAT) as last_migration
FROM databasechangelog;
" 2>&1

Write-Host ""
Write-Host "✅ Done!" -ForegroundColor Green

