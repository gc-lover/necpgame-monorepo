# Issue: Run benchmarks after build
# Запускает бенчмарки после успешной сборки сервиса

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName,
    [switch]$Quick = $false
)

$ErrorActionPreference = "Continue"

$ServiceDir = Join-Path "services" $ServiceName

if (-not (Test-Path $ServiceDir)) {
    Write-Host "❌ Service not found: $ServiceName" -ForegroundColor Red
    exit 1
}

$BenchFile = Join-Path $ServiceDir "server\handlers_bench_test.go"

if (-not (Test-Path $BenchFile)) {
    Write-Host "⏭️  No benchmarks for $ServiceName" -ForegroundColor Yellow
    exit 0
}

Write-Host "📊 Running benchmarks for $ServiceName..." -ForegroundColor Cyan

Push-Location $ServiceDir

try {
    if (Test-Path "Makefile") {
        if ($Quick) {
            & make bench-quick
        } else {
            & make bench
        }
    } else {
        if ($Quick) {
            go test -run=^$ -bench=. -benchmem -benchtime=100ms ./server
        } else {
            go test -run=^$ -bench=. -benchmem ./server
        }
    }
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "OK Benchmarks passed" -ForegroundColor Green
        exit 0
    } else {
        Write-Host "❌ Benchmarks failed" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Error running benchmarks: $_" -ForegroundColor Red
    exit 1
} finally {
    Pop-Location
}

