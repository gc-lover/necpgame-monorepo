#!/usr/bin/env pwsh
# Проверка соответствия портов между Dockerfile, main.go и docker-compose.yml

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName
)

$ErrorActionPreference = "Continue"

Write-Host "Verifying ports for: $ServiceName" -ForegroundColor Cyan
Write-Host ""

$serviceDir = "services\$ServiceName"

# Проверяем main.go
$mainGo = "$serviceDir\main.go"
if (Test-Path $mainGo) {
    $content = Get-Content $mainGo -Raw
    
    $httpPortMatch = $content | Select-String -Pattern 'getEnv\("ADDR".*"(\d+)"\)'
    $metricsPortMatch = $content | Select-String -Pattern 'getEnv\("METRICS_ADDR".*":(\d+)"\)'
    
    if ($httpPortMatch) {
        $httpPort = $httpPortMatch.Matches[0].Groups[1].Value
        Write-Host "main.go HTTP port: $httpPort" -ForegroundColor Cyan
    } else {
        Write-Host "WARNING: Could not find HTTP port in main.go" -ForegroundColor Yellow
    }
    
    if ($metricsPortMatch) {
        $metricsPort = $metricsPortMatch.Matches[0].Groups[1].Value
        Write-Host "main.go Metrics port: $metricsPort" -ForegroundColor Cyan
    } else {
        Write-Host "WARNING: Could not find metrics port in main.go" -ForegroundColor Yellow
    }
}

# Проверяем Dockerfile
$dockerfile = "$serviceDir\Dockerfile"
if (Test-Path $dockerfile) {
    $content = Get-Content $dockerfile -Raw
    
    $exposeMatch = $content | Select-String -Pattern 'EXPOSE\s+(\d+)/tcp\s+(\d+)'
    $healthcheckMatch = $content | Select-String -Pattern 'HEALTHCHECK.*localhost:(\d+)'
    
    if ($exposeMatch) {
        $dockerHttpPort = $exposeMatch.Matches[0].Groups[1].Value
        $dockerMetricsPort = $exposeMatch.Matches[0].Groups[2].Value
        Write-Host "Dockerfile EXPOSE: $dockerHttpPort/tcp $dockerMetricsPort" -ForegroundColor Cyan
        
        if ($httpPort -and $dockerHttpPort -ne $httpPort) {
            Write-Host "ERROR: HTTP port mismatch! main.go=$httpPort, Dockerfile=$dockerHttpPort" -ForegroundColor Red
        }
        
        if ($metricsPort -and $dockerMetricsPort -ne $metricsPort) {
            Write-Host "ERROR: Metrics port mismatch! main.go=$metricsPort, Dockerfile=$dockerMetricsPort" -ForegroundColor Red
        }
    }
    
    if ($healthcheckMatch) {
        $healthcheckPort = $healthcheckMatch.Matches[0].Groups[1].Value
        Write-Host "Dockerfile HEALTHCHECK port: $healthcheckPort" -ForegroundColor Cyan
        
        if ($metricsPort -and $healthcheckPort -ne $metricsPort) {
            Write-Host "ERROR: Health check port mismatch! main.go=$metricsPort, Dockerfile HEALTHCHECK=$healthcheckPort" -ForegroundColor Red
        }
    }
}

# Проверяем docker-compose.yml
$dockerCompose = "docker-compose.yml"
if (Test-Path $dockerCompose) {
    $content = Get-Content $dockerCompose -Raw
    $serviceNameShort = $ServiceName -replace "-go$", ""
    
    $composeMatch = $content | Select-String -Pattern "$serviceNameShort:`n.*ADDR=.*:(\d+).*METRICS_ADDR=:(\d+)" -AllMatches
    
    if ($composeMatch) {
        $composeHttpPort = $composeMatch.Matches[0].Groups[1].Value
        $composeMetricsPort = $composeMatch.Matches[0].Groups[2].Value
        Write-Host "docker-compose.yml ADDR port: $composeHttpPort" -ForegroundColor Cyan
        Write-Host "docker-compose.yml METRICS_ADDR port: $composeMetricsPort" -ForegroundColor Cyan
    }
}

Write-Host ""
Write-Host "Verification complete" -ForegroundColor Cyan

