# Test API endpoints for services

Write-Host "Testing NECPGAME API Endpoints..." -ForegroundColor Green
Write-Host ""

# Test Character Service - Create Account
Write-Host "1. Testing Character Service API..." -ForegroundColor Yellow
$accountBody = @{
    nickname = "testuser_$(Get-Random)"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8087/api/v1/accounts" -Method POST -Body $accountBody -ContentType "application/json" -ErrorAction SilentlyContinue
if ($response -and $response.id) {
    Write-Host "   ✓ Account created: $($response.id)" -ForegroundColor Green
    $accountId = $response.id
    
    # Test Create Character
    $characterBody = @{
        account_id = $accountId
        name = "TestHero"
        level = 1
    } | ConvertTo-Json
    
    $charResponse = Invoke-RestMethod -Uri "http://localhost:8087/api/v1/characters" -Method POST -Body $characterBody -ContentType "application/json" -ErrorAction SilentlyContinue
    if ($charResponse -and $charResponse.id) {
        Write-Host "   ✓ Character created: $($charResponse.id)" -ForegroundColor Green
        $characterId = $charResponse.id
        
        # Test Get Inventory
        Write-Host "2. Testing Inventory Service API..." -ForegroundColor Yellow
        $invResponse = Invoke-RestMethod -Uri "http://localhost:8085/api/v1/inventory/$characterId" -Method GET -ErrorAction SilentlyContinue
        if ($invResponse -and $invResponse.inventory) {
            Write-Host "   ✓ Inventory retrieved for character" -ForegroundColor Green
        } else {
            Write-Host "   ⚠ Inventory not found (may need DB migration)" -ForegroundColor Yellow
        }
        
        # Test Get Position
        Write-Host "3. Testing Movement Service API..." -ForegroundColor Yellow
        $posResponse = Invoke-RestMethod -Uri "http://localhost:8086/api/v1/movement/$characterId/position" -Method GET -ErrorAction SilentlyContinue
        if ($posResponse) {
            Write-Host "   ✓ Position endpoint accessible" -ForegroundColor Green
        } else {
            Write-Host "   ⚠ Position not found (may need DB migration)" -ForegroundColor Yellow
        }
    } else {
        Write-Host "   ✗ Character creation failed" -ForegroundColor Red
    }
} else {
    Write-Host "   ⚠ Account creation failed (may need DB migration)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "All API tests completed!" -ForegroundColor Green

