# Issue: #1588 - Add resilience patterns to all Go services
# Adds resilience.go to services that don't have it

$servicesPath = "services"
$templatePath = "services/leaderboard-service-go/server/resilience.go"

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
    $resilienceFile = Join-Path $service.FullName "server/resilience.go"
    
    if (Test-Path $resilienceFile) {
        Write-Host "[SKIP] $($service.Name) - already has resilience.go" -ForegroundColor Yellow
        $skipped++
        continue
    }
    
    $serverDir = Join-Path $service.FullName "server"
    if (-not (Test-Path $serverDir)) {
        Write-Host "[SKIP] $($service.Name) - no server/ directory" -ForegroundColor Yellow
        $skipped++
        continue
    }
    
    try {
        # Adjust package name if needed (some services might have different package names)
        $adjustedTemplate = $template
        
        # Write resilience.go
        Set-Content -Path $resilienceFile -Value $adjustedTemplate -Encoding UTF8
        Write-Host "[OK] $($service.Name) - added resilience.go" -ForegroundColor Green
        $added++
    } catch {
        Write-Host "[ERROR] $($service.Name) - $($_.Exception.Message)" -ForegroundColor Red
        $errors++
    }
}

Write-Host "`n=== SUMMARY ===" -ForegroundColor Cyan
Write-Host "Added: $added" -ForegroundColor Green
Write-Host "Skipped: $skipped" -ForegroundColor Yellow
Write-Host "Errors: $errors" -ForegroundColor Red
Write-Host "Total services: $($services.Count)" -ForegroundColor Cyan

