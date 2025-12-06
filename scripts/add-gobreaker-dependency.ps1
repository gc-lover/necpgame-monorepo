# Issue: #1588 - Add gobreaker dependency to services with resilience.go
# Adds github.com/sony/gobreaker v1.0.0 to go.mod if missing

$servicesPath = "services"

$services = Get-ChildItem -Path $servicesPath -Directory | Where-Object { $_.Name -like "*-go" }

$added = 0
$skipped = 0
$errors = 0

foreach ($service in $services) {
    $resilienceFile = Join-Path $service.FullName "server/resilience.go"
    $goModFile = Join-Path $service.FullName "go.mod"
    
    if (-not (Test-Path $resilienceFile)) {
        continue  # Skip services without resilience.go
    }
    
    if (-not (Test-Path $goModFile)) {
        Write-Host "[SKIP] $($service.Name) - no go.mod" -ForegroundColor Yellow
        $skipped++
        continue
    }
    
    $goModContent = Get-Content $goModFile -Raw
    
    if ($goModContent -match "github.com/sony/gobreaker") {
        Write-Host "[SKIP] $($service.Name) - already has gobreaker" -ForegroundColor Yellow
        $skipped++
        continue
    }
    
    try {
        # Add gobreaker dependency
        $newLine = "`tgithub.com/sony/gobreaker v1.0.0 // Issue: #1588 - Circuit breaker"
        
        # Find the require block
        if ($goModContent -match "(require\s*\([^)]*)") {
            # Add before closing parenthesis
            $goModContent = $goModContent -replace "(require\s*\([^)]*)(\))", "`$1`n$newLine`n`t`$2"
        } else {
            # Add require block if it doesn't exist
            if ($goModContent -notmatch "require") {
                $goModContent = $goModContent + "`n`nrequire (`n$newLine`n)`n"
            }
        }
        
        Set-Content -Path $goModFile -Value $goModContent -Encoding UTF8
        Write-Host "[OK] $($service.Name) - added gobreaker dependency" -ForegroundColor Green
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

