#!/usr/bin/env pwsh
# Test Docker build for all services

$ErrorActionPreference = "Continue"

Write-Host "Testing Docker build for all services..." -ForegroundColor Cyan
Write-Host ""

$SERVICES = @(
    "achievement-service-go",
    "admin-service-go",
    "battle-pass-service-go",
    "character-service-go",
    "clan-war-service-go",
    "companion-service-go",
    "economy-service-go",
    "feedback-service-go",
    "gameplay-service-go",
    "housing-service-go",
    "inventory-service-go",
    "leaderboard-service-go",
    "maintenance-service-go",
    "matchmaking-go",
    "movement-service-go",
    "progression-paragon-service-go",
    "realtime-gateway-go",
    "referral-service-go",
    "reset-service-go",
    "social-service-go",
    "support-service-go",
    "voice-chat-service-go",
    "world-service-go",
    "ws-lobby-go"
)

$BUILD_ERRORS = 0
$BUILD_SUCCESS = 0
$ERRORS = @()

foreach ($service in $SERVICES) {
    $dockerfile = "services\$service\Dockerfile"
    if (-not (Test-Path $dockerfile)) {
        Write-Host "SKIP $service (no Dockerfile)" -ForegroundColor Yellow
        continue
    }

    Write-Host "Building $service..." -ForegroundColor Cyan
    
    # Check if Dockerfile needs root context (contains "COPY services/" or "COPY proto/")
    $dockerfileContent = Get-Content $dockerfile -Raw
    $needsRootContext = ($dockerfileContent -match "COPY services/|COPY proto/")
    
    if ($needsRootContext) {
        # Use root context for services that need proto/ directory
        $buildContext = "."
        $dockerfilePath = $dockerfile
    } else {
        # Use service directory context
        $buildContext = "services\$service"
        $dockerfilePath = "Dockerfile"
    }
    
    $imageTag = "necpgame-${service}:test"
    
    $buildOutput = & docker build -t $imageTag -f $dockerfilePath $buildContext 2>&1
    $buildExitCode = $LASTEXITCODE
    
    if ($buildExitCode -eq 0) {
        Write-Host "OK $service built successfully" -ForegroundColor Green
        $BUILD_SUCCESS++
        
        docker rmi $imageTag 2>&1 | Out-Null
    } else {
        Write-Host "ERROR building $service" -ForegroundColor Red
        $BUILD_ERRORS++
        $ERRORS += @{
            Service = $service
            Error = ($buildOutput | Out-String)
        }
        
        $errorLines = ($buildOutput | Select-Object -Last 10) -join "`n"
        Write-Host "   Last error lines:" -ForegroundColor Red
        Write-Host $errorLines -ForegroundColor Red
        Write-Host ""
    }
}

Write-Host ""
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "Results:" -ForegroundColor Cyan
Write-Host "  Success: $BUILD_SUCCESS" -ForegroundColor Green
Write-Host "  Errors: $BUILD_ERRORS" -ForegroundColor $(if ($BUILD_ERRORS -gt 0) { "Red" } else { "Green" })
Write-Host ""

if ($BUILD_ERRORS -eq 0) {
    Write-Host "All Docker images build successfully!" -ForegroundColor Green
    exit 0
} else {
    Write-Host "Found $BUILD_ERRORS build errors" -ForegroundColor Red
    Write-Host ""
    Write-Host "Services with errors:" -ForegroundColor Yellow
    foreach ($error in $ERRORS) {
        Write-Host "  - $($error.Service)" -ForegroundColor Yellow
    }
    exit 1
}
