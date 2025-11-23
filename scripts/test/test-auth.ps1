# Test script for JWT authentication in character-service

Write-Host "Testing JWT Authentication in Character Service..." -ForegroundColor Cyan
Write-Host ""

# Test 1: Health check (should work without auth)
Write-Host "1. Testing health endpoint (no auth required)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8087/health" -Method GET -ErrorAction Stop
    if ($response.status -eq "healthy") {
        Write-Host "   ✓ Health check passed" -ForegroundColor Green
    }
} catch {
    Write-Host "   ✗ Health check failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "2. Testing API endpoint without token (should fail)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8087/api/v1/accounts" -Method POST -Body (@{nickname="testuser"} | ConvertTo-Json) -ContentType "application/json" -ErrorAction Stop
    Write-Host "   ✗ Request succeeded without token (should have failed)" -ForegroundColor Red
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode -eq 401) {
        Write-Host "   ✓ Request correctly rejected with 401 Unauthorized" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Request failed with status $statusCode: $($_.Exception.Message)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "3. Testing API endpoint with invalid token (should fail)..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer invalid-token-12345"
        "Content-Type" = "application/json"
    }
    $response = Invoke-RestMethod -Uri "http://localhost:8087/api/v1/accounts" -Method POST -Body (@{nickname="testuser"} | ConvertTo-Json) -Headers $headers -ErrorAction Stop
    Write-Host "   ✗ Request succeeded with invalid token (should have failed)" -ForegroundColor Red
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode -eq 401) {
        Write-Host "   ✓ Request correctly rejected with 401 Unauthorized" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Request failed with status $statusCode: $($_.Exception.Message)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "Note: To test with a real JWT token from Keycloak:" -ForegroundColor Cyan
Write-Host "  1. Get a token from Keycloak (http://localhost:8080)" -ForegroundColor Gray
Write-Host "  2. Use it in Authorization header: Bearer <token>" -ForegroundColor Gray
Write-Host "  3. Test with: curl -X POST http://localhost:8087/api/v1/accounts \`" -ForegroundColor Gray
Write-Host "     -H 'Authorization: Bearer <token>' \`" -ForegroundColor Gray
Write-Host "     -H 'Content-Type: application/json' \`" -ForegroundColor Gray
Write-Host "     -d '{\"nickname\":\"testuser\"}'" -ForegroundColor Gray
Write-Host ""
Write-Host "All tests completed!" -ForegroundColor Green













