# Issue: #1585 - Add goleak tests to all Go services
# Adds leak_test.go to services that don't have it

$servicesPath = "services"
$templatePath = "services/realtime-gateway-go/server/leak_test.go"

if (-not (Test-Path $templatePath)) {
    Write-Host "ERROR: Template not found: $templatePath" -ForegroundColor Red
    exit 1
}

$template = Get-Content $templatePath -Raw

$services = Get-ChildItem -Path $servicesPath -Directory | Where-Object { $_.Name -like "*-go" }

$added = 0
$skipped = 0
$errors = 0

foreach ($service in $services) {
    $leakTestFile = Join-Path $service.FullName "server/leak_test.go"
    
    if (Test-Path $leakTestFile) {
        Write-Host "[SKIP] $($service.Name) - already has leak_test.go" -ForegroundColor Yellow
        $skipped++
        continue
    }
    
    $serverDir = Join-Path $service.FullName "server"
    if (-not (Test-Path $serverDir)) {
        Write-Host "[SKIP] $($service.Name) - no server/ directory" -ForegroundColor Yellow
        $skipped++
        continue
    }
    
    # Replace service name in template
    $serviceName = $service.Name
    $customized = $template -replace "realtime-gateway", $serviceName
    $customized = $customized -replace "CRITICAL - WebSocket service!", "CRITICAL - Service goroutine leak detection"
    $customized = $customized -replace "WebSocket connections, game loops", "HTTP handlers, background workers, DB connections"
    
    try {
        $customized | Out-File -FilePath $leakTestFile -Encoding UTF8 -NoNewline
        Write-Host "[OK] $($service.Name) - added leak_test.go" -ForegroundColor Green
        $added++
    } catch {
        Write-Host "[ERROR] $($service.Name) - $($_.Exception.Message)" -ForegroundColor Red
        $errors++
    }
}

Write-Host ""
Write-Host "=== SUMMARY ===" -ForegroundColor Cyan
Write-Host "Added: $added" -ForegroundColor Green
Write-Host "Skipped: $skipped" -ForegroundColor Yellow
Write-Host "Errors: $errors" -ForegroundColor Red
Write-Host "Total: $($services.Count)" -ForegroundColor Cyan

