#!/usr/bin/env pwsh
# Тестирование запуска сервиса

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName,
    
    [int]$TimeoutSeconds = 30
)

$ErrorActionPreference = "Continue"

Write-Host "Testing run for: $ServiceName" -ForegroundColor Cyan
Write-Host ""

$serviceDir = "services\$ServiceName"
$dockerfile = "$serviceDir\Dockerfile"

if (-not (Test-Path $dockerfile)) {
    Write-Host "ERROR: Dockerfile not found: $dockerfile" -ForegroundColor Red
    exit 1
}

$dockerfileContent = Get-Content $dockerfile -Raw
$needsProto = $dockerfileContent -match "COPY proto/|COPY services/"

if ($needsProto) {
    $context = "."
    $dockerfilePath = "$serviceDir/Dockerfile"
} else {
    $context = $serviceDir
    $dockerfilePath = "$serviceDir/Dockerfile"
}

$imageTag = "necpgame-$ServiceName`:test"
$containerName = "test-$ServiceName"

Write-Host "Building image..." -ForegroundColor Cyan
$buildOutput = & docker build -t $imageTag -f $dockerfilePath $context 2>&1
$buildExitCode = $LASTEXITCODE

if ($buildExitCode -ne 0) {
    Write-Host "❌ Build failed!" -ForegroundColor Red
    $errorLines = ($buildOutput | Select-String -Pattern "ERROR|error|failed" | Select-Object -Last 5)
    if ($errorLines) {
        $errorLines | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
    }
    exit 1
}

Write-Host "OK Build successful" -ForegroundColor Green

$exposeMatch = $dockerfileContent | Select-String -Pattern "EXPOSE\s+(\d+)/tcp"
if ($exposeMatch) {
    $httpPort = $exposeMatch.Matches[0].Groups[1].Value
    Write-Host "HTTP port: $httpPort" -ForegroundColor Cyan
} else {
    $httpPort = "8080"
    Write-Host "Using default HTTP port: $httpPort" -ForegroundColor Yellow
}

$metricsMatch = $dockerfileContent | Select-String -Pattern "EXPOSE\s+\d+/tcp\s+(\d+)"
if ($metricsMatch) {
    $metricsPort = $metricsMatch.Matches[0].Groups[1].Value
    Write-Host "Metrics port: $metricsPort" -ForegroundColor Cyan
} else {
    $metricsPort = "9090"
    Write-Host "Using default metrics port: $metricsPort" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Starting container..." -ForegroundColor Cyan

docker rm -f $containerName 2>&1 | Out-Null

$envVars = @(
    "-e", "LOG_LEVEL=info",
    "-e", "DATABASE_URL=postgresql://postgres:postgres@host.docker.internal:5432/necpgame?sslmode=disable",
    "-e", "REDIS_URL=redis://host.docker.internal:6379/0"
)

if ($ServiceName -eq "character-service-go") {
    $envVars += "-e", "ADDR=0.0.0.0:8087"
    $envVars += "-e", "METRICS_ADDR=:9092"
} elseif ($ServiceName -eq "matchmaking-go") {
    $envVars += "-e", "METRICS_ADDR=:9090"
}

$runArgs = @("run", "-d", "--name", $containerName, "-p", "${httpPort}:${httpPort}", "-p", "${metricsPort}:${metricsPort}")
$runArgs += $envVars
$runArgs += $imageTag

& docker $runArgs | Out-Null

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Failed to start container" -ForegroundColor Red
    docker rmi $imageTag 2>&1 | Out-Null
    exit 1
}

Write-Host "OK Container started" -ForegroundColor Green
Write-Host "Container name: $containerName" -ForegroundColor Cyan
Write-Host "Waiting for service to initialize..." -ForegroundColor Cyan

Start-Sleep -Seconds 5

$logs = docker logs $containerName 2>&1 | Select-Object -Last 10
if ($logs) {
    Write-Host ""
    Write-Host "Last 10 log lines:" -ForegroundColor Cyan
    $logs | ForEach-Object { Write-Host "  $_" -ForegroundColor Gray }
}

$healthStatus = docker inspect --format='{{.State.Health.Status}}' $containerName 2>&1
if ($healthStatus -ne "no healthcheck" -and $healthStatus -ne "") {
    Write-Host ""
    Write-Host "Health check status: $healthStatus" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "Testing metrics endpoint..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:${metricsPort}/metrics" -TimeoutSec 5 -UseBasicParsing 2>&1
    if ($response.StatusCode -eq 200) {
        Write-Host "OK Metrics endpoint is responding" -ForegroundColor Green
    }
} catch {
    Write-Host "WARNING  Metrics endpoint not responding (may need more time or dependencies)" -ForegroundColor Yellow
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Container is running. You can:" -ForegroundColor Yellow
Write-Host "  - View logs: docker logs -f $containerName" -ForegroundColor Yellow
Write-Host "  - Stop container: docker stop $containerName" -ForegroundColor Yellow
Write-Host "  - Remove container: docker rm -f $containerName" -ForegroundColor Yellow
Write-Host ""
Write-Host "To clean up:" -ForegroundColor Yellow
Write-Host "  docker rm -f $containerName && docker rmi $imageTag" -ForegroundColor Yellow

exit 0

