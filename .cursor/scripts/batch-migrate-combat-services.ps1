# Batch Migration Script for Combat Services
# Migrates all combat services from oapi-codegen to ogen

param(
    [Parameter(Mandatory=$false)]
    [switch]$DryRun
)

$ErrorActionPreference = "Stop"

Write-Host "`n🎯 Batch ogen Migration - Combat Services" -ForegroundColor Cyan
Write-Host "Issue: #1595" -ForegroundColor Yellow
if ($DryRun) {
    Write-Host "Mode: DRY RUN" -ForegroundColor Yellow
}
Write-Host ""

# Combat services to migrate (18 total, excluding already migrated)
$CombatServices = @(
    # Already migrated
    # "combat-actions-service-go",
    # "combat-combos-service-go",  # has -ogen version
    
    # To migrate
    "combat-ai-service-go",
    "combat-damage-service-go",
    "combat-extended-mechanics-service-go",
    "combat-hacking-service-go",
    "combat-sessions-service-go",
    "combat-turns-service-go",
    "combat-implants-core-service-go",
    "combat-implants-maintenance-service-go",
    "combat-implants-stats-service-go",
    "combat-sandevistan-service-go",
    "projectile-core-service-go",
    "hacking-core-service-go",
    "gameplay-weapon-special-mechanics-service-go",
    "weapon-progression-service-go",
    "weapon-resource-service-go"
)

Write-Host "📊 Services to migrate: $($CombatServices.Count)" -ForegroundColor Cyan
Write-Host ""

$SuccessCount = 0
$SkipCount = 0
$FailCount = 0

foreach ($Service in $CombatServices) {
    Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
    Write-Host "🔄 Processing: $Service" -ForegroundColor Cyan
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
    
    try {
        # Check if already migrated
        if (Test-Path "services\$Service\MIGRATION_SUMMARY.md") {
            Write-Host "⏭️  Already migrated - SKIP" -ForegroundColor Yellow
            $SkipCount++
            continue
        }
        
        # Run migration script
        if ($DryRun) {
            & .\.cursor\scripts\migrate-service-to-ogen.ps1 -ServiceName $Service -DryRun
        } else {
            & .\.cursor\scripts\migrate-service-to-ogen.ps1 -ServiceName $Service
        }
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ $Service - SUCCESS" -ForegroundColor Green
            $SuccessCount++
        } else {
            Write-Host "❌ $Service - FAILED" -ForegroundColor Red
            $FailCount++
        }
    }
    catch {
        Write-Host "❌ $Service - ERROR: $_" -ForegroundColor Red
        $FailCount++
    }
    
    Write-Host ""
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
Write-Host "📊 Migration Summary" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
Write-Host ""
Write-Host "✅ Success:     $SuccessCount" -ForegroundColor Green
Write-Host "⏭️  Skipped:     $SkipCount" -ForegroundColor Yellow
Write-Host "❌ Failed:      $FailCount" -ForegroundColor Red
Write-Host "📦 Total:       $($CombatServices.Count)" -ForegroundColor Cyan
Write-Host ""

if ($SuccessCount -gt 0) {
    Write-Host "🎉 Migration batch completed!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 Next steps for each service:" -ForegroundColor Cyan
    Write-Host "  1. Update handlers to implement ogen interfaces" -ForegroundColor Yellow
    Write-Host "  2. Build & test" -ForegroundColor Yellow
    Write-Host "  3. Run benchmarks" -ForegroundColor Yellow
    Write-Host "  4. Create MIGRATION_SUMMARY.md" -ForegroundColor Yellow
    Write-Host ""
}

if ($FailCount -gt 0) {
    Write-Host "⚠️  Some migrations failed. Check logs above." -ForegroundColor Red
    exit 1
}


