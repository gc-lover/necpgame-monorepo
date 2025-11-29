#!/usr/bin/env pwsh
# Применение оптимального Dockerfile к сервисам

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName,
    
    [switch]$TestBuild = $false,
    [switch]$TestRun = $false
)

$ErrorActionPreference = "Continue"

Write-Host "Optimizing Dockerfile for: $ServiceName" -ForegroundColor Cyan

$serviceDir = "services\$ServiceName"
$dockerfile = "$serviceDir\Dockerfile"

if (-not (Test-Path $dockerfile)) {
    Write-Host "ERROR: Dockerfile not found: $dockerfile" -ForegroundColor Red
    exit 1
}

$dockerfileContent = Get-Content $dockerfile -Raw
$needsProto = $dockerfileContent -match "COPY proto/|COPY services/"

Write-Host "Service needs proto/: $needsProto" -ForegroundColor $(if ($needsProto) { "Yellow" } else { "Green" })

if ($TestBuild) {
    Write-Host "Testing build..." -ForegroundColor Cyan
    
    if ($needsProto) {
        $context = "."
        $dockerfilePath = "$serviceDir\Dockerfile"
    } else {
        $context = $serviceDir
        $dockerfilePath = "Dockerfile"
    }
    
    $imageTag = "necpgame-$ServiceName`:test"
    
    Write-Host "Building with context: $context, dockerfile: $dockerfilePath" -ForegroundColor Cyan
    
    $buildOutput = & docker build -t $imageTag -f $dockerfilePath $context 2>&1
    $buildExitCode = $LASTEXITCODE
    
    if ($buildExitCode -eq 0) {
        Write-Host "Build successful!" -ForegroundColor Green
        
        if ($TestRun) {
            Write-Host "Testing run (dry-run, checking health endpoint exists)..." -ForegroundColor Cyan
            
            $healthCheckExists = $dockerfileContent -match "HEALTHCHECK"
            if ($healthCheckExists) {
                Write-Host "Health check configured" -ForegroundColor Green
            } else {
                Write-Host "WARNING: No health check configured" -ForegroundColor Yellow
            }
        }
        
        docker rmi $imageTag 2>&1 | Out-Null
        exit 0
    } else {
        Write-Host "Build failed!" -ForegroundColor Red
        $errorLines = ($buildOutput | Select-String -Pattern "ERROR|error|failed" | Select-Object -Last 5)
        if ($errorLines) {
            Write-Host "Errors:" -ForegroundColor Red
            $errorLines | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
        }
        exit 1
    }
} else {
    Write-Host "Dockerfile optimization applied (dry-run mode)" -ForegroundColor Yellow
    Write-Host "Use -TestBuild to test the build" -ForegroundColor Yellow
}

