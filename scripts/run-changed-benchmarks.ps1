# Issue: Run benchmarks only for changed services
# Запускает бенчмарки только для измененных сервисов

param(
    [string]$BaseBranch = "main",
    [switch]$All = $false,
    [switch]$Quick = $false
)

$ErrorActionPreference = "Continue"

Write-Host "📊 Running benchmarks for changed services..." -ForegroundColor Cyan
Write-Host ""

# Определяем измененные сервисы
$ChangedServices = @()

if ($All) {
    Write-Host "Running benchmarks for ALL services" -ForegroundColor Yellow
    $ChangedServices = Get-ChildItem services -Directory | Where-Object { 
        $_.Name -like "*-go" -or $_.Name -like "*-service-go" 
    } | ForEach-Object { $_.Name }
} else {
    # Получаем измененные файлы
    $ChangedFiles = git diff --name-only $BaseBranch 2>&1
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "⚠️  Could not determine changed files. Running for all services." -ForegroundColor Yellow
        $ChangedServices = Get-ChildItem services -Directory | Where-Object { 
            $_.Name -like "*-go" -or $_.Name -like "*-service-go" 
        } | ForEach-Object { $_.Name }
    } else {
        # Извлекаем уникальные сервисы из путей
        $ChangedServices = $ChangedFiles | Where-Object { 
            $_ -match '^services/([^/]+)' 
        } | ForEach-Object {
            if ($_ -match '^services/([^/]+)') {
                $Matches[1]
            }
        } | Sort-Object -Unique
    }
}

if ($ChangedServices.Count -eq 0) {
    Write-Host "✅ No changed services found" -ForegroundColor Green
    exit 0
}

Write-Host "Changed services: $($ChangedServices.Count)" -ForegroundColor Yellow
foreach ($Service in $ChangedServices) {
    Write-Host "  - $Service" -ForegroundColor Gray
}
Write-Host ""

# Запускаем бенчмарки
$Results = @()
$Success = 0
$Failed = 0
$Skipped = 0

foreach ($Service in $ChangedServices) {
    $ServiceDir = Join-Path "services" $Service
    $Makefile = Join-Path $ServiceDir "Makefile"
    $BenchFile = Join-Path $ServiceDir "server\handlers_bench_test.go"
    
    if (-not (Test-Path $ServiceDir)) {
        $Skipped++
        continue
    }
    
    if (-not (Test-Path $BenchFile)) {
        Write-Host "  ⏭️  $Service - no benchmarks" -ForegroundColor Yellow
        $Skipped++
        continue
    }
    
    Write-Host "  🏃 $Service" -ForegroundColor Cyan -NoNewline
    
    Push-Location $ServiceDir
    
    try {
        if ($Quick) {
            $Result = & make bench-quick 2>&1
        } else {
            $Result = & make bench 2>&1
        }
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host " ✅" -ForegroundColor Green
            $Success++
            $Results += [PSCustomObject]@{
                Service = $Service
                Status = "OK"
            }
        } else {
            Write-Host " ❌" -ForegroundColor Red
            $Failed++
            $Results += [PSCustomObject]@{
                Service = $Service
                Status = "FAILED"
                Error = ($Result | Select-Object -Last 3) -join "`n"
            }
        }
    } catch {
        Write-Host " ❌" -ForegroundColor Red
        $Failed++
        $Results += [PSCustomObject]@{
            Service = $Service
            Status = "ERROR"
            Error = $_.Exception.Message
        }
    } finally {
        Pop-Location
    }
}

Write-Host ""
Write-Host "Summary:" -ForegroundColor Cyan
Write-Host "  Success: $Success" -ForegroundColor Green
Write-Host "  Failed: $Failed" -ForegroundColor Red
Write-Host "  Skipped: $Skipped" -ForegroundColor Yellow

if ($Failed -gt 0) {
    Write-Host ""
    Write-Host "Failed services:" -ForegroundColor Red
    foreach ($Result in $Results | Where-Object { $_.Status -ne "OK" }) {
        Write-Host "  - $($Result.Service): $($Result.Error)" -ForegroundColor Red
    }
    exit 1
}

exit 0

