# Issue: Pre-commit hook for benchmarks
# Запускает быстрые бенчмарки для измененных сервисов перед коммитом

$ErrorActionPreference = "Continue"

# Получаем staged файлы
$StagedFiles = git diff --cached --name-only 2>&1

if ($LASTEXITCODE -ne 0 -or $StagedFiles.Count -eq 0) {
    exit 0
}

# Извлекаем измененные сервисы
$ChangedServices = $StagedFiles | Where-Object { 
    $_ -match '^services/([^/]+)/server/.*\.go$' 
} | ForEach-Object {
    if ($_ -match '^services/([^/]+)') {
        $Matches[1]
    }
} | Sort-Object -Unique

if ($ChangedServices.Count -eq 0) {
    exit 0
}

Write-Host ""
Write-Host "📊 Running quick benchmarks for changed services..." -ForegroundColor Cyan
Write-Host ""

$Failed = 0

foreach ($Service in $ChangedServices) {
    $ServiceDir = Join-Path "services" $Service
    $BenchFile = Join-Path $ServiceDir "server\handlers_bench_test.go"
    
    if (-not (Test-Path $BenchFile)) {
        continue
    }
    
    Write-Host "  🏃 $Service" -ForegroundColor Cyan -NoNewline
    
    Push-Location $ServiceDir
    
    try {
        if (Test-Path "Makefile") {
            $Result = & make bench-quick 2>&1
        } else {
            $Result = go test -run=^$ -bench=. -benchmem -benchtime=100ms ./server 2>&1
        }
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host " OK" -ForegroundColor Green
        } else {
            Write-Host " ❌" -ForegroundColor Red
            $Failed++
        }
    } catch {
        Write-Host " ❌" -ForegroundColor Red
        $Failed++
    } finally {
        Pop-Location
    }
}

if ($Failed -gt 0) {
    Write-Host ""
    Write-Host "WARNING  Some benchmarks failed. Commit anyway? (y/N)" -ForegroundColor Yellow
    $Response = Read-Host
    if ($Response -ne "y" -and $Response -ne "Y") {
        exit 1
    }
}

exit 0

