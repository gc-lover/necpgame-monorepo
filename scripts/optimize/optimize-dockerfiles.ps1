#!/usr/bin/env pwsh
# Оптимизация Dockerfile для всех сервисов

param(
    [switch]$DryRun = $false
)

$ErrorActionPreference = "Continue"

Write-Host "Optimizing Dockerfiles for all services..." -ForegroundColor Cyan
Write-Host "Dry run: $DryRun" -ForegroundColor $(if ($DryRun) { "Yellow" } else { "Green" })
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

$UPDATED = 0
$SKIPPED = 0

foreach ($service in $SERVICES) {
    $dockerfile = "services\$service\Dockerfile"
    
    if (-not (Test-Path $dockerfile)) {
        Write-Host "SKIP $service (no Dockerfile)" -ForegroundColor Yellow
        $SKIPPED++
        continue
    }

    Write-Host "Checking $service..." -ForegroundColor Cyan

    $content = Get-Content $dockerfile -Raw
    $needsProto = $content -match "COPY proto/|COPY services/"
    
    $serviceName = $service -replace "-go$", ""
    
    if ($DryRun) {
        Write-Host "  Would optimize Dockerfile for $service" -ForegroundColor Yellow
        if ($needsProto) {
            Write-Host "  Uses proto/ directory" -ForegroundColor Yellow
        }
    } else {
        Write-Host "  Checking for optimizations..." -ForegroundColor Cyan
        
        $hasHealthcheck = $content -match "HEALTHCHECK"
        $hasSecurityContext = $content -match "USER"
        $hasTzdata = $content -match "tzdata"
        $hasStaticLink = $content -match "-extldflags"
        $hasGo124 = $content -match "golang:1.24"
        
        if (-not $hasGo124 -or -not $hasHealthcheck -or -not $hasTzdata -or -not $hasStaticLink) {
            Write-Host "  Needs optimization" -ForegroundColor Yellow
            Write-Host "    - Healthcheck: $hasHealthcheck" -ForegroundColor $(if ($hasHealthcheck) { "Green" } else { "Red" })
            Write-Host "    - Security context: $hasSecurityContext" -ForegroundColor $(if ($hasSecurityContext) { "Green" } else { "Yellow" })
            Write-Host "    - Timezone data: $hasTzdata" -ForegroundColor $(if ($hasTzdata) { "Green" } else { "Red" })
            Write-Host "    - Static linking: $hasStaticLink" -ForegroundColor $(if ($hasStaticLink) { "Green" } else { "Red" })
            Write-Host "    - Go 1.24: $hasGo124" -ForegroundColor $(if ($hasGo124) { "Green" } else { "Red" })
            
            Write-Host "  Manual optimization required - see infrastructure/OPTIMIZATION_REPORT.md" -ForegroundColor Yellow
        } else {
            Write-Host "  Already optimized" -ForegroundColor Green
        }
    }
}

Write-Host ""
Write-Host "Summary:" -ForegroundColor Cyan
Write-Host "  Services checked: $($SERVICES.Count)" -ForegroundColor Cyan
Write-Host "  Skipped: $SKIPPED" -ForegroundColor Yellow
Write-Host ""

