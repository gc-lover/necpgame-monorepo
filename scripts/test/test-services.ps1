# Test script for services

Write-Host "Testing NECPGAME Services..." -ForegroundColor Green
Write-Host ""

# Test Inventory Service
Write-Host "1. Testing Inventory Service..." -ForegroundColor Yellow
$inventoryHealth = curl -s http://localhost:8085/health 2>$null
if ($inventoryHealth -match "healthy") {
    Write-Host "   ✓ Inventory Service is healthy" -ForegroundColor Green
} else {
    Write-Host "   ✗ Inventory Service health check failed" -ForegroundColor Red
}

# Test Character Service
Write-Host "2. Testing Character Service..." -ForegroundColor Yellow
$characterHealth = curl -s http://localhost:8087/health 2>$null
if ($characterHealth -match "healthy") {
    Write-Host "   ✓ Character Service is healthy" -ForegroundColor Green
} else {
    Write-Host "   ✗ Character Service health check failed" -ForegroundColor Red
}

# Test Movement Service
Write-Host "3. Testing Movement Service..." -ForegroundColor Yellow
$movementHealth = curl -s http://localhost:8086/health 2>$null
if ($movementHealth -match "healthy") {
    Write-Host "   ✓ Movement Service is healthy" -ForegroundColor Green
} else {
    Write-Host "   ✗ Movement Service health check failed" -ForegroundColor Red
}

# Test Metrics
Write-Host "4. Testing Metrics endpoints..." -ForegroundColor Yellow
$inventoryMetrics = curl -s http://localhost:9094/metrics 2>$null
$characterMetrics = curl -s http://localhost:9096/metrics 2>$null
if ($inventoryMetrics -and $characterMetrics) {
    Write-Host "   ✓ Metrics endpoints are accessible" -ForegroundColor Green
} else {
    Write-Host "   ✗ Metrics endpoints check failed" -ForegroundColor Red
}

Write-Host ""
Write-Host "Service Status:" -ForegroundColor Cyan
docker-compose ps inventory-service character-service movement-service 2>$null | Select-String "Up" | ForEach-Object {
    Write-Host "   $_" -ForegroundColor Green
}

Write-Host ""
Write-Host "All tests completed!" -ForegroundColor Green

