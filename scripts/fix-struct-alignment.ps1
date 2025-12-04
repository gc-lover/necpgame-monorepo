# Issue: #1606 - Auto-fix struct alignment using fieldalignment
# Automatically fixes struct field alignment across all Go services
# GAINS: 30-50% memory savings

param(
    [switch]$Fix = $false,
    [string]$Service = ""
)

Write-Host "🔍 Struct Alignment Check/Fix" -ForegroundColor Cyan
Write-Host ""

if (-not (Get-Command fieldalignment -ErrorAction SilentlyContinue)) {
    Write-Host "❌ fieldalignment not found. Installing..." -ForegroundColor Yellow
    go install golang.org/x/tools/go/analysis/passes/fieldalignment/cmd/fieldalignment@latest
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Failed to install fieldalignment" -ForegroundColor Red
        exit 1
    }
}

$servicesDir = "services"
$fixedCount = 0
$checkedCount = 0

if ($Service) {
    $services = @($Service)
} else {
    $services = Get-ChildItem -Path $servicesDir -Directory | Where-Object { 
        Test-Path (Join-Path $_.FullName "*.go")
    } | Select-Object -ExpandProperty Name
}

foreach ($svc in $services) {
    $svcPath = Join-Path $servicesDir $svc
    
    if (-not (Test-Path $svcPath)) {
        continue
    }
    
    # Check if it's a Go service
    $goFiles = Get-ChildItem -Path $svcPath -Filter "*.go" -Recurse | Select-Object -First 1
    if (-not $goFiles) {
        continue
    }
    
    $checkedCount++
    Write-Host "Checking: $svc" -ForegroundColor Gray
    
    Push-Location $svcPath
    
    if ($Fix) {
        # Auto-fix
        fieldalignment -fix ./... 2>&1 | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  OK Fixed: $svc" -ForegroundColor Green
            $fixedCount++
        } else {
            Write-Host "  WARNING  Issues found (may need manual fix): $svc" -ForegroundColor Yellow
        }
    } else {
        # Check only
        $output = fieldalignment ./... 2>&1
        if ($output -match "struct") {
            Write-Host "  WARNING  Alignment issues: $svc" -ForegroundColor Yellow
            Write-Host "     Run: fieldalignment -fix ./..." -ForegroundColor Gray
        } else {
            Write-Host "  OK OK: $svc" -ForegroundColor Green
        }
    }
    
    Pop-Location
}

Write-Host ""
Write-Host "📊 Results:" -ForegroundColor Cyan
Write-Host "  Checked: $checkedCount services"
if ($Fix) {
    Write-Host "  Fixed: $fixedCount services" -ForegroundColor Green
    Write-Host ""
    Write-Host "OK Struct alignment auto-fix complete!" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "💡 To auto-fix, run: .\scripts\fix-struct-alignment.ps1 -Fix" -ForegroundColor Yellow
}

