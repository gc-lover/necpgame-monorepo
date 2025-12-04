# Script to build all Go services
# Usage: .\scripts\build-all-services.ps1

$servicesPath = "services"
$results = @()

Get-ChildItem -Path $servicesPath -Directory | ForEach-Object {
    $service = $_.Name
    $servicePath = Join-Path $servicesPath $service
    
    if (Test-Path (Join-Path $servicePath "go.mod")) {
        Write-Host "Building $service..."
        
        Push-Location $servicePath
        
        $buildOutput = go build -o "bin/$service.exe" . 2>&1
        $buildSuccess = $LASTEXITCODE -eq 0
        
        if ($buildSuccess) {
            Write-Host "  ✓ Success" -ForegroundColor Green
            $results += [PSCustomObject]@{
                Service = $service
                Status = "Success"
            }
        } else {
            Write-Host "  ✗ Failed" -ForegroundColor Red
            $results += [PSCustomObject]@{
                Service = $service
                Status = "Failed"
                Error = ($buildOutput | Select-Object -First 3) -join "`n"
            }
        }
        
        Pop-Location
    }
}

Write-Host "`n=== Build Summary ===" -ForegroundColor Cyan
$successCount = ($results | Where-Object { $_.Status -eq "Success" }).Count
$failedCount = ($results | Where-Object { $_.Status -eq "Failed" }).Count
Write-Host "Success: $successCount" -ForegroundColor Green
Write-Host "Failed: $failedCount" -ForegroundColor Red

if ($failedCount -gt 0) {
    Write-Host "`nFailed services:" -ForegroundColor Yellow
    $results | Where-Object { $_.Status -eq "Failed" } | ForEach-Object {
        Write-Host "  - $($_.Service)" -ForegroundColor Red
    }
}

