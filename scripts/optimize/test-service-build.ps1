#!/usr/bin/env pwsh
# Тестирование сборки и базовой проверки сервиса

param(
    [Parameter(Mandatory = $true)]
    [string]$ServiceName,
    
    [switch]$TestRun = $false
)

$ErrorActionPreference = "Continue"

Write-Host "Testing service: $ServiceName" -ForegroundColor Cyan
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
}
else {
    $context = $serviceDir
    $dockerfilePath = "$serviceDir/Dockerfile"
}

$imageTag = "necpgame-$ServiceName`:test"

Write-Host "Building image: $imageTag" -ForegroundColor Cyan
Write-Host "Context: $context" -ForegroundColor Cyan
Write-Host "Dockerfile: $dockerfilePath" -ForegroundColor Cyan
Write-Host ""

$buildStart = Get-Date

$buildOutput = & docker build -t $imageTag -f $dockerfilePath $context 2>&1
$buildExitCode = $LASTEXITCODE

$buildDuration = (Get-Date) - $buildStart

if ($buildExitCode -eq 0) {
    Write-Host "OK Build successful!" -ForegroundColor Green
    Write-Host "Build time: $($buildDuration.TotalSeconds) seconds" -ForegroundColor Cyan
    
    $imageInfo = docker images $imageTag --format "{{.Size}}"
    Write-Host "Image size: $imageInfo" -ForegroundColor Cyan
    
    if ($TestRun) {
        Write-Host ""
        Write-Host "Testing image..." -ForegroundColor Cyan
        
        $healthCheckExists = $dockerfileContent -match "HEALTHCHECK"
        if ($healthCheckExists) {
            Write-Host "OK Health check configured" -ForegroundColor Green
        }
        else {
            Write-Host "WARNING  No health check configured" -ForegroundColor Yellow
        }
        
        $securityContext = $dockerfileContent -match "USER\s+\w+"
        if ($securityContext) {
            Write-Host "OK Security context (non-root user) configured" -ForegroundColor Green
        }
        else {
            Write-Host "WARNING  No security context configured" -ForegroundColor Yellow
        }
        
        Write-Host ""
        Write-Host "You can test run with:" -ForegroundColor Yellow
        Write-Host "  docker run --rm -it $imageTag" -ForegroundColor Yellow
    }
    
    Write-Host ""
    Write-Host "Cleaning up test image..." -ForegroundColor Cyan
    docker rmi $imageTag 2>&1 | Out-Null
    
    exit 0
}
else {
    Write-Host "❌ Build failed!" -ForegroundColor Red
    Write-Host "Build time: $($buildDuration.TotalSeconds) seconds" -ForegroundColor Red
    Write-Host ""
    
    $errorLines = ($buildOutput | Select-String -Pattern "ERROR|error|failed" | Select-Object -Last 10)
    if ($errorLines) {
        Write-Host "Errors:" -ForegroundColor Red
        $errorLines | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
    }
    
    exit 1
}

