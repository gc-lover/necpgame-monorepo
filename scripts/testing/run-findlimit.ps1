# PowerShell script для запуска автоматического поиска предела Gateway

param(
    [string]$Url = "ws://127.0.0.1:18080/ws?token=test",
    [int]$StartClients = 10,
    [int]$MaxClients = 500,
    [int]$StepSize = 20,
    [int]$TestDurationSeconds = 20,
    [int]$PlayerInputHz = 60,
    [double]$ErrorThreshold = 1.0,
    [int]$CooldownSeconds = 5
)

$ErrorActionPreference = "Stop"

Write-Host "=== Gateway Limit Finder Script ===" -ForegroundColor Cyan
Write-Host ""

# Проверяем, запущен ли Gateway
Write-Host "Checking Gateway availability..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://127.0.0.1:9093/metrics" -Method Get -TimeoutSec 5 -ErrorAction Stop
    Write-Host "✅ Gateway is running" -ForegroundColor Green
} catch {
    Write-Host "❌ Gateway is not available at http://127.0.0.1:9093" -ForegroundColor Red
    Write-Host "Please start Gateway first: docker-compose up -d realtime-gateway" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Переходим в директорию с исходниками
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent (Split-Path -Parent $scriptDir)
$findlimitPath = Join-Path $projectRoot "services\realtime-gateway-go\findlimit.exe"

# Проверяем, существует ли исполняемый файл
if (-not (Test-Path $findlimitPath)) {
    Write-Host "Building findlimit tool..." -ForegroundColor Yellow
    
    $goPath = Join-Path $projectRoot "services\realtime-gateway-go"
    Push-Location $goPath
    
    try {
        go build -o findlimit.exe ./cmd/findlimit
        if ($LASTEXITCODE -ne 0) {
            Write-Host "❌ Failed to build findlimit tool" -ForegroundColor Red
            exit 1
        }
        Write-Host "✅ findlimit tool built successfully" -ForegroundColor Green
    } finally {
        Pop-Location
    }
}

Write-Host ""
Write-Host "Starting limit search..." -ForegroundColor Cyan
Write-Host ""

# Запускаем findlimit
$findlimitPath = Join-Path $projectRoot "services\realtime-gateway-go\findlimit.exe"

$arguments = @(
    "-url", $Url,
    "-start", $StartClients,
    "-max", $MaxClients,
    "-step", $StepSize,
    "-duration", "${TestDurationSeconds}s",
    "-hz", $PlayerInputHz,
    "-error-threshold", $ErrorThreshold,
    "-cooldown", "${CooldownSeconds}s"
)

& $findlimitPath $arguments

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "❌ Limit search failed with exit code: $LASTEXITCODE" -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host ""
Write-Host "✅ Limit search completed" -ForegroundColor Green

