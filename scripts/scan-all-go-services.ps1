# Scan all Go services for linting errors
Write-Host "Scanning all Go services..." -ForegroundColor Green

$services = Get-ChildItem -Path services -Directory -Filter "*-go" | Where-Object { Test-Path (Join-Path $_.FullName "go.mod") }

$errors = @()
foreach ($service in $services) {
    $serviceName = $service.Name
    Write-Host "`nChecking $serviceName..." -ForegroundColor Yellow
    
    Push-Location $service.FullName
    $vetOutput = go vet ./... 2>&1
    if ($LASTEXITCODE -ne 0) {
        $errors += "$serviceName`: $vetOutput"
        Write-Host "  ❌ Errors found" -ForegroundColor Red
    } else {
        Write-Host "  OK No errors" -ForegroundColor Green
    }
    Pop-Location
}

if ($errors.Count -gt 0) {
    Write-Host "`n=== ERRORS FOUND ===" -ForegroundColor Red
    $errors | ForEach-Object { Write-Host $_ -ForegroundColor Red }
} else {
    Write-Host "`nOK All services passed go vet" -ForegroundColor Green
}
