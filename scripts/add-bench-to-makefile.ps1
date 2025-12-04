# Issue: Add bench target to all Makefiles
# Добавляет bench target в Makefile каждого сервиса

$ErrorActionPreference = "Continue"
$ServicesDir = "services"
$Added = 0
$Skipped = 0

Write-Host "Adding bench target to all Makefiles..." -ForegroundColor Cyan
Write-Host ""

$ServiceDirs = Get-ChildItem $ServicesDir -Directory | Where-Object { 
    $_.Name -like "*-go" -or $_.Name -like "*-service-go" 
}

foreach ($ServiceDir in $ServiceDirs) {
    $ServiceName = $ServiceDir.Name
    $Makefile = Join-Path $ServiceDir.FullName "Makefile"
    
    if (-not (Test-Path $Makefile)) {
        $Skipped++
        continue
    }
    
    $Content = Get-Content $Makefile -Raw
    
    # Проверяем, есть ли уже bench target
    if ($Content -match '\.PHONY.*bench|^bench:') {
        Write-Host "  ⏭️  $ServiceName - already has bench" -ForegroundColor Yellow
        $Skipped++
        continue
    }
    
    Write-Host "  ➕ $ServiceName" -ForegroundColor Green
    
    # Определяем SERVICE_NAME из Makefile
    $ServiceNameVar = ""
    if ($Content -match 'SERVICE_NAME\s*:=\s*([^\s]+)') {
        $ServiceNameVar = $Matches[1]
    } else {
        # Пробуем извлечь из пути
        $ServiceNameVar = $ServiceName -replace '-go$', '' -replace '-service-go$', ''
    }
    
    # Добавляем bench targets
    $BenchTargets = @"

.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^$$ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^$$ -bench=. -benchmem -json ./server > ../../.benchmarks/results/${ServiceNameVar}_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^$$ -bench=. -benchmem -benchtime=100ms ./server
"@
    
    # Добавляем в конец файла
    $NewContent = $Content.TrimEnd() + "`n`n" + $BenchTargets
    
    $NewContent | Out-File -FilePath $Makefile -Encoding UTF8 -NoNewline
    $Added++
}

Write-Host ""
Write-Host "Summary:" -ForegroundColor Cyan
Write-Host "  Added: $Added" -ForegroundColor Green
Write-Host "  Skipped: $Skipped" -ForegroundColor Yellow

