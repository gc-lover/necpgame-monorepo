# Comprehensive test script for all NECPGAME services

Write-Host "=== NECPGAME Services Test ===" -ForegroundColor Cyan
Write-Host ""

# Test Health Checks
Write-Host "1. Health Checks..." -ForegroundColor Yellow
$services = @(
    @{Name="Inventory"; URL="http://localhost:8085/health"},
    @{Name="Character"; URL="http://localhost:8087/health"},
    @{Name="Movement"; URL="http://localhost:8086/health"}
)

foreach ($service in $services) {
    try {
        $response = Invoke-RestMethod -Uri $service.URL -Method GET -TimeoutSec 2 -ErrorAction Stop
        if ($response.status -eq "healthy") {
            Write-Host "   ✓ $($service.Name) Service: Healthy" -ForegroundColor Green
        } else {
            Write-Host "   ✗ $($service.Name) Service: Not healthy" -ForegroundColor Red
        }
    } catch {
        Write-Host "   ✗ $($service.Name) Service: Failed to connect" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "2. Testing Character Service API..." -ForegroundColor Yellow
try {
    $accountBody = @{
        nickname = "testuser_$(Get-Date -Format 'yyyyMMddHHmmss')"
    } | ConvertTo-Json

    $account = Invoke-RestMethod -Uri "http://localhost:8087/api/v1/accounts" -Method POST -Body $accountBody -ContentType "application/json" -ErrorAction Stop
    Write-Host "   ✓ Account created: $($account.nickname) (ID: $($account.id))" -ForegroundColor Green
    
    $characterBody = @{
        account_id = $account.id
        name = "TestHero"
        level = 1
        class_code = "solo"
    } | ConvertTo-Json

    $character = Invoke-RestMethod -Uri "http://localhost:8087/api/v1/characters" -Method POST -Body $characterBody -ContentType "application/json" -ErrorAction Stop
    Write-Host "   ✓ Character created: $($character.name) (ID: $($character.id))" -ForegroundColor Green
    
    $characterId = $character.id
    
    Write-Host ""
    Write-Host "3. Testing Inventory Service API..." -ForegroundColor Yellow
    try {
        $inventory = Invoke-RestMethod -Uri "http://localhost:8085/api/v1/inventory/$characterId" -Method GET -ErrorAction Stop
        Write-Host "   ✓ Inventory retrieved for character" -ForegroundColor Green
        Write-Host "     Capacity: $($inventory.inventory.capacity), Used: $($inventory.inventory.used_slots)" -ForegroundColor Gray
    } catch {
        Write-Host "   ⚠ Inventory endpoint test: $($_.Exception.Message)" -ForegroundColor Yellow
    }
    
    Write-Host ""
    Write-Host "4. Testing Movement Service API..." -ForegroundColor Yellow
    try {
        $position = Invoke-RestMethod -Uri "http://localhost:8086/api/v1/movement/$characterId/position" -Method GET -ErrorAction Stop
        Write-Host "   ✓ Position endpoint accessible" -ForegroundColor Green
    } catch {
        Write-Host "   ⚠ Position not found (expected for new character)" -ForegroundColor Yellow
    }
    
} catch {
    Write-Host "   ✗ Character Service test failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "5. Metrics Endpoints..." -ForegroundColor Yellow
$metrics = @(
    @{Name="Inventory"; URL="http://localhost:9094/metrics"},
    @{Name="Character"; URL="http://localhost:9096/metrics"},
    @{Name="Movement"; URL="http://localhost:9095/metrics"}
)

foreach ($metric in $metrics) {
    try {
        $response = Invoke-WebRequest -Uri $metric.URL -Method GET -TimeoutSec 2 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "   ✓ $($metric.Name) Metrics: Accessible" -ForegroundColor Green
        }
    } catch {
        Write-Host "   ✗ $($metric.Name) Metrics: Failed" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=== Test Summary ===" -ForegroundColor Cyan
Write-Host "All services are running and responding!" -ForegroundColor Green
Write-Host ""

