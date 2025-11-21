# WebSocket Load Test Script
# Requires: Go, realtime-gateway service running

param(
    [string]$ServerURL = "ws://127.0.0.1:18080/ws?token=test",
    [int]$Clients = 10,
    [int]$DurationSeconds = 60,
    [int]$PlayerInputHz = 60,
    [int]$ReportIntervalSeconds = 10
)

Write-Host "=== WebSocket Load Test ===" -ForegroundColor Green
Write-Host ""

# Check if gateway is running
Write-Host "1. Checking gateway availability..." -ForegroundColor Yellow
try {
    $wsTest = Test-NetConnection -ComputerName localhost -Port 18080 -InformationLevel Quiet
    if (-not $wsTest) {
        Write-Host "  ERROR: Gateway is not available on port 18080!" -ForegroundColor Red
        Write-Host "  Please start realtime-gateway service first:" -ForegroundColor Yellow
        Write-Host "    docker-compose up -d realtime-gateway" -ForegroundColor White
        exit 1
    }
    Write-Host "  OK: Gateway is available" -ForegroundColor Green
} catch {
    Write-Host "  ERROR: Failed to check gateway: $_" -ForegroundColor Red
    exit 1
}

# Check if Go is available
Write-Host ""
Write-Host "2. Checking Go installation..." -ForegroundColor Yellow
try {
    $goVersion = go version
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ERROR: Go is not installed or not in PATH!" -ForegroundColor Red
        exit 1
    }
    Write-Host "  OK: $goVersion" -ForegroundColor Green
} catch {
    Write-Host "  ERROR: Failed to check Go: $_" -ForegroundColor Red
    exit 1
}

# Build loadtest tool
Write-Host ""
Write-Host "3. Building loadtest tool..." -ForegroundColor Yellow
$loadtestPath = Join-Path $PSScriptRoot "..\..\services\realtime-gateway-go"
Push-Location $loadtestPath

try {
    go build -o loadtest.exe ./cmd/loadtest
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ERROR: Failed to build loadtest tool!" -ForegroundColor Red
        Pop-Location
        exit 1
    }
    Write-Host "  OK: loadtest.exe built successfully" -ForegroundColor Green
} catch {
    Write-Host "  ERROR: Failed to build loadtest: $_" -ForegroundColor Red
    Pop-Location
    exit 1
}

# Run load test
Write-Host ""
Write-Host "4. Running load test..." -ForegroundColor Yellow
Write-Host "  Server URL: $ServerURL" -ForegroundColor Cyan
Write-Host "  Clients: $Clients" -ForegroundColor Cyan
Write-Host "  Duration: $DurationSeconds seconds" -ForegroundColor Cyan
Write-Host "  PlayerInput Hz: $PlayerInputHz" -ForegroundColor Cyan
Write-Host "  Report Interval: $ReportIntervalSeconds seconds" -ForegroundColor Cyan
Write-Host ""

$durationArg = "$DurationSeconds" + "s"
$reportArg = "$ReportIntervalSeconds" + "s"

try {
    & .\loadtest.exe `
        -url $ServerURL `
        -clients $Clients `
        -duration $durationArg `
        -hz $PlayerInputHz `
        -report $reportArg
} catch {
    Write-Host "  ERROR: Load test failed: $_" -ForegroundColor Red
    Pop-Location
    exit 1
} finally {
    # Cleanup
    if (Test-Path "loadtest.exe") {
        Remove-Item "loadtest.exe" -ErrorAction SilentlyContinue
    }
    Pop-Location
}

Write-Host ""
Write-Host "=== Load test completed ===" -ForegroundColor Green

