# Final comprehensive test

Write-Host "=== FINAL TEST: All Services ===" -ForegroundColor Cyan
Write-Host ""

# 1. Health Checks
Write-Host "1. Health Checks..." -ForegroundColor Yellow
$services = @(
    @{Name="Inventory"; URL="http://localhost:8085/health"},
    @{Name="Character"; URL="http://localhost:8087/health"},
    @{Name="Movement"; URL="http://localhost:8086/health"}
)

$allHealthy = $true
foreach ($service in $services) {
    try {
        $response = Invoke-RestMethod -Uri $service.URL -Method GET -TimeoutSec 2 -ErrorAction Stop
        if ($response.status -eq "healthy") {
            Write-Host "   ✓ $($service.Name): Healthy" -ForegroundColor Green
        } else {
            Write-Host "   ✗ $($service.Name): Not healthy" -ForegroundColor Red
            $allHealthy = $false
        }
    } catch {
        Write-Host "   ✗ $($service.Name): Connection failed" -ForegroundColor Red
        $allHealthy = $false
    }
}

Write-Host ""
Write-Host "2. Database Tables..." -ForegroundColor Yellow
$tables = docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "\dt mvp_core.*" 2>$null | Select-String "character|inventory|position"
if ($tables) {
    Write-Host "   ✓ All required tables exist" -ForegroundColor Green
    Write-Host "     Tables found: $($tables.Count)" -ForegroundColor Gray
} else {
    Write-Host "   ✗ Tables check failed" -ForegroundColor Red
}

Write-Host ""
Write-Host "3. Docker Containers..." -ForegroundColor Yellow
$containers = docker-compose ps --format "{{.Service}}|{{.Status}}" 2>$null | Select-String "inventory|character|movement|postgres|redis"
foreach ($container in $containers) {
    if ($container -match "Up") {
        Write-Host "   ✓ $($container -replace '\|.*', '')" -ForegroundColor Green
    } else {
        Write-Host "   ✗ $($container -replace '\|.*', '')" -ForegroundColor Red
    }
}

Write-Host ""
if ($allHealthy) {
    Write-Host "=== ALL SERVICES WORKING! ===" -ForegroundColor Green
} else {
    Write-Host "=== SOME ISSUES DETECTED ===" -ForegroundColor Yellow
}

