# Script to check all Go services for compilation and basic functionality
# Usage: .\scripts\check-all-services.ps1

$ErrorActionPreference = "Continue"
$servicesPath = "services"
$results = @()
$failedServices = @()

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Checking all Go services..." -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Get all service directories
$serviceDirs = Get-ChildItem -Path $servicesPath -Directory | Where-Object { 
    $_.Name -like "*-go" -or 
    (Test-Path (Join-Path $_.FullName "main.go")) -or
    (Test-Path (Join-Path $_.FullName "go.mod"))
} | Sort-Object Name

$totalServices = $serviceDirs.Count
$currentService = 0

foreach ($serviceDir in $serviceDirs) {
    $currentService++
    $serviceName = $serviceDir.Name
    $servicePath = $serviceDir.FullName
    
    Write-Host "[$currentService/$totalServices] Checking: $serviceName" -ForegroundColor Yellow
    
    # Check if it's a Go service
    $mainGo = Join-Path $servicePath "main.go"
    $goMod = Join-Path $servicePath "go.mod"
    
    if (-not (Test-Path $mainGo) -and -not (Test-Path $goMod)) {
        Write-Host "  ⚠ Skipping (no main.go or go.mod)" -ForegroundColor Gray
        continue
    }
    
    # Check if go.mod exists
    if (-not (Test-Path $goMod)) {
        Write-Host "  ❌ FAILED: No go.mod file" -ForegroundColor Red
        $results += [PSCustomObject]@{
            Service = $serviceName
            Status  = "FAILED"
            Error   = "No go.mod file"
        }
        $failedServices += $serviceName
        continue
    }
    
    # Try to build
    Push-Location $servicePath
    try {
        $buildOutput = & go build -o test-build.exe . 2>&1
        $buildExitCode = $LASTEXITCODE
        
        if ($buildExitCode -eq 0) {
            Write-Host "  OK BUILD OK" -ForegroundColor Green
            
            # Clean up test build
            if (Test-Path "test-build.exe") {
                Remove-Item "test-build.exe" -Force -ErrorAction SilentlyContinue
            }
            
            $results += [PSCustomObject]@{
                Service = $serviceName
                Status  = "OK"
                Error   = ""
            }
        }
        else {
            Write-Host "  ❌ BUILD FAILED" -ForegroundColor Red
            $errorMsg = ($buildOutput | Out-String).Trim()
            if ($errorMsg.Length -gt 200) {
                $errorMsg = $errorMsg.Substring(0, 200) + "..."
            }
            Write-Host "  Error: $errorMsg" -ForegroundColor Red
            
            $results += [PSCustomObject]@{
                Service = $serviceName
                Status  = "FAILED"
                Error   = $errorMsg
            }
            $failedServices += $serviceName
        }
    }
    catch {
        Write-Host "  ❌ EXCEPTION: $_" -ForegroundColor Red
        $results += [PSCustomObject]@{
            Service = $serviceName
            Status  = "FAILED"
            Error   = $_.Exception.Message
        }
        $failedServices += $serviceName
    }
    finally {
        Pop-Location
    }
    
    Write-Host ""
}

# Summary
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Summary" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

$okCount = ($results | Where-Object { $_.Status -eq "OK" }).Count
$failedCount = ($results | Where-Object { $_.Status -eq "FAILED" }).Count

Write-Host "Total services checked: $totalServices" -ForegroundColor White
Write-Host "OK OK: $okCount" -ForegroundColor Green
Write-Host "❌ FAILED: $failedCount" -ForegroundColor Red
Write-Host ""

if ($failedCount -gt 0) {
    Write-Host "Failed services:" -ForegroundColor Red
    foreach ($failed in $failedServices) {
        Write-Host "  - $failed" -ForegroundColor Red
    }
    Write-Host ""
    
    # Show detailed errors
    Write-Host "Detailed errors:" -ForegroundColor Yellow
    foreach ($result in $results | Where-Object { $_.Status -eq "FAILED" }) {
        Write-Host "`n$($result.Service):" -ForegroundColor Yellow
        Write-Host "  $($result.Error)" -ForegroundColor Red
    }
    
    exit 1
}
else {
    Write-Host "All services compile successfully! OK" -ForegroundColor Green
    exit 0
}


