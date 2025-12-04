# Script to run tests for all Go services
# Usage: .\scripts\run-all-tests.ps1

$servicesPath = "services"
$results = @()
$totalServices = 0
$successCount = 0
$failedCount = 0

Write-Host "Running tests for all services...`n" -ForegroundColor Cyan

Get-ChildItem -Path $servicesPath -Directory | ForEach-Object {
    $service = $_.Name
    $servicePath = Join-Path $servicesPath $service
    
    if (Test-Path (Join-Path $servicePath "go.mod")) {
        $totalServices++
        Write-Host "Testing $service..." -NoNewline
        
        Push-Location $servicePath
        
        # Run tests with timeout
        $testOutput = go test ./... -v 2>&1
        $testSuccess = $LASTEXITCODE -eq 0
        
        if ($testSuccess) {
            Write-Host " ✓ PASSED" -ForegroundColor Green
            $successCount++
            $results += [PSCustomObject]@{
                Service = $service
                Status = "PASSED"
                Output = ""
            }
        } else {
            Write-Host " ✗ FAILED" -ForegroundColor Red
            $failedCount++
            $errorLines = ($testOutput | Select-String -Pattern "FAIL|error|Error" | Select-Object -First 5) -join "`n"
            $results += [PSCustomObject]@{
                Service = $service
                Status = "FAILED"
                Output = $errorLines
            }
        }
        
        Pop-Location
    }
}

Write-Host "`n=== Test Summary ===" -ForegroundColor Cyan
Write-Host "Total services: $totalServices" -ForegroundColor White
Write-Host "Passed: $successCount" -ForegroundColor Green
Write-Host "Failed: $failedCount" -ForegroundColor Red

if ($failedCount -gt 0) {
    Write-Host "`nFailed services:" -ForegroundColor Yellow
    $results | Where-Object { $_.Status -eq "FAILED" } | ForEach-Object {
        Write-Host "  - $($_.Service)" -ForegroundColor Red
        if ($_.Output) {
            Write-Host "    $($_.Output)" -ForegroundColor Gray
        }
    }
}

# Return exit code based on results
if ($failedCount -gt 0) {
    exit 1
} else {
    exit 0
}

