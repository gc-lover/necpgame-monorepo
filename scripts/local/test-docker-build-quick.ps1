#!/usr/bin/env pwsh
# Quick test Docker build for problematic services

$ErrorActionPreference = "Continue"

Write-Host "Quick testing Docker build for services..." -ForegroundColor Cyan
Write-Host ""

$SERVICES_TO_TEST = @(
    "achievement-service-go",
    "admin-service-go",
    "character-service-go",
    "economy-service-go",
    "gameplay-service-go",
    "inventory-service-go",
    "social-service-go",
    "support-service-go"
)

$BUILD_ERRORS = 0
$BUILD_SUCCESS = 0
$ERRORS = @()

foreach ($service in $SERVICES_TO_TEST) {
    $dockerfile = "services\$service\Dockerfile"
    if (-not (Test-Path $dockerfile)) {
        Write-Host "SKIP $service (no Dockerfile)" -ForegroundColor Yellow
        continue
    }

    Write-Host "Building $service..." -ForegroundColor Cyan
    
    $dockerfileContent = Get-Content $dockerfile -Raw
    $needsRootContext = ($dockerfileContent -match "COPY services/|COPY proto/")
    
    if ($needsRootContext) {
        $buildContext = "."
        $dockerfilePath = $dockerfile
    } else {
        $buildContext = "services\$service"
        $dockerfilePath = "Dockerfile"
    }
    
    $imageTag = "necpgame-${service}:test"
    
    $buildOutput = & docker build -t $imageTag -f $dockerfilePath $buildContext 2>&1
    $buildExitCode = $LASTEXITCODE
    
    if ($buildExitCode -eq 0) {
        Write-Host "OK $service" -ForegroundColor Green
        $BUILD_SUCCESS++
        docker rmi $imageTag 2>&1 | Out-Null
    } else {
        Write-Host "ERROR $service" -ForegroundColor Red
        $BUILD_ERRORS++
        
        $errorLines = ($buildOutput | Select-String -Pattern "ERROR|error|failed|already declared" | Select-Object -Last 5)
        if ($errorLines) {
            Write-Host "   Errors:" -ForegroundColor Red
            $errorLines | ForEach-Object { Write-Host "   $_" -ForegroundColor Red }
        }
        
        $ERRORS += @{
            Service = $service
            Error = ($buildOutput | Out-String)
        }
        Write-Host ""
    }
}

Write-Host ""
Write-Host "Results: Success: $BUILD_SUCCESS, Errors: $BUILD_ERRORS" -ForegroundColor Cyan

if ($BUILD_ERRORS -gt 0) {
    Write-Host ""
    Write-Host "Services with errors:" -ForegroundColor Yellow
    foreach ($error in $ERRORS) {
        Write-Host "  - $($error.Service)" -ForegroundColor Yellow
    }
}

exit $BUILD_ERRORS

