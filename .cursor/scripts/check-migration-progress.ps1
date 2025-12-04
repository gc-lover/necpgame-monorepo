# Check ogen Migration Progress
# Scans all services and reports migration status

param(
    [Parameter(Mandatory=$false)]
    [switch]$Detailed
)

Write-Host "`n📊 ogen Migration Progress Report" -ForegroundColor Cyan
Write-Host "Generated: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray
Write-Host ""

# Count all Go services
$AllServices = Get-ChildItem "services\*-go" -Directory | Where-Object { 
    $_.Name -notmatch '(database-partition-manager|database-view-refresher|workqueue-service)' 
}

$TotalServices = $AllServices.Count

# Check which are migrated
$MigratedServices = @()
$InProgressServices = @()
$NotStartedServices = @()

foreach ($Service in $AllServices) {
    $ServiceName = $Service.Name
    $ServicePath = $Service.FullName
    
    # Check for ogen indicators
    $HasOgenMakefile = $false
    $HasMigrationSummary = $false
    $HasOgenGenerated = $false
    
    if (Test-Path "$ServicePath\Makefile") {
        $Makefile = Get-Content "$ServicePath\Makefile" -Raw
        $HasOgenMakefile = $Makefile -match 'ogen --target'
    }
    
    if (Test-Path "$ServicePath\MIGRATION_SUMMARY.md") {
        $HasMigrationSummary = $true
    }
    
    if (Test-Path "$ServicePath\pkg\api\oas_json_gen.go") {
        $HasOgenGenerated = $true
    }
    
    # Classify
    if ($HasMigrationSummary -or ($HasOgenMakefile -and $HasOgenGenerated)) {
        $MigratedServices += $ServiceName
    }
    elseif ($HasOgenMakefile -or $HasOgenGenerated) {
        $InProgressServices += $ServiceName
    }
    else {
        $NotStartedServices += $ServiceName
    }
}

# Calculate percentages
$MigratedCount = $MigratedServices.Count
$InProgressCount = $InProgressServices.Count
$NotStartedCount = $NotStartedServices.Count

$MigratedPercent = [math]::Round(($MigratedCount / $TotalServices) * 100, 1)
$InProgressPercent = [math]::Round(($InProgressCount / $TotalServices) * 100, 1)
$NotStartedPercent = [math]::Round(($NotStartedCount / $TotalServices) * 100, 1)

# Display summary
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
Write-Host ""
Write-Host "Migrated:    $MigratedCount / $TotalServices ($MigratedPercent%)" -ForegroundColor Green
Write-Host "In Progress: $InProgressCount / $TotalServices ($InProgressPercent%)" -ForegroundColor Yellow
Write-Host "Not Started: $NotStartedCount / $TotalServices ($NotStartedPercent%)" -ForegroundColor Red
Write-Host ""

# Progress bar
$BarLength = 40
$MigratedBar = [math]::Floor(($MigratedCount / $TotalServices) * $BarLength)
$InProgressBar = [math]::Floor(($InProgressCount / $TotalServices) * $BarLength)
$NotStartedBar = $BarLength - $MigratedBar - $InProgressBar

Write-Host "Progress: [" -NoNewline
Write-Host ("#" * $MigratedBar) -NoNewline -ForegroundColor Green
Write-Host ("=" * $InProgressBar) -NoNewline -ForegroundColor Yellow
Write-Host ("-" * $NotStartedBar) -NoNewline -ForegroundColor DarkGray
Write-Host "]" -ForegroundColor White
Write-Host ""

# Detailed breakdown
if ($Detailed) {
    if ($MigratedServices.Count -gt 0) {
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
        Write-Host "OK MIGRATED ($MigratedCount)" -ForegroundColor Green
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
        foreach ($Service in $MigratedServices | Sort-Object) {
            Write-Host "  • $Service" -ForegroundColor Green
        }
        Write-Host ""
    }
    
    if ($InProgressServices.Count -gt 0) {
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
        Write-Host "🚧 IN PROGRESS ($InProgressCount)" -ForegroundColor Yellow
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
        foreach ($Service in $InProgressServices | Sort-Object) {
            Write-Host "  • $Service" -ForegroundColor Yellow
        }
        Write-Host ""
    }
    
    if ($NotStartedServices.Count -gt 0) {
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
        Write-Host "❌ NOT STARTED ($NotStartedCount)" -ForegroundColor Red
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
        
        # Group by priority
        $CombatServices = $NotStartedServices | Where-Object { $_ -match '^combat-' }
        $MovementServices = $NotStartedServices | Where-Object { $_ -match '^(movement|world)' }
        $OtherServices = $NotStartedServices | Where-Object { $_ -notmatch '^(combat-|movement|world)' }
        
        if ($CombatServices.Count -gt 0) {
            Write-Host "  🔴 HIGH PRIORITY - Combat ($($CombatServices.Count)):" -ForegroundColor Red
            foreach ($Service in $CombatServices | Sort-Object) {
                Write-Host "    • $Service" -ForegroundColor Gray
            }
        }
        
        if ($MovementServices.Count -gt 0) {
            Write-Host "  🔴 HIGH PRIORITY - Movement & World ($($MovementServices.Count)):" -ForegroundColor Red
            foreach ($Service in $MovementServices | Sort-Object) {
                Write-Host "    • $Service" -ForegroundColor Gray
            }
        }
        
        if ($OtherServices.Count -gt 0) {
            Write-Host "  🟡 MEDIUM/LOW - Other ($($OtherServices.Count)):" -ForegroundColor Yellow
            foreach ($Service in $OtherServices | Sort-Object) {
                Write-Host "    • $Service" -ForegroundColor Gray
            }
        }
        
        Write-Host ""
    }
}

# Recommendations
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
Write-Host "NEXT STEPS" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray

if ($NotStartedCount -gt 0) {
    Write-Host ""
    Write-Host "🎯 Focus on HIGH PRIORITY services first:" -ForegroundColor Yellow
    Write-Host "   • Combat services (Issue #1595)" -ForegroundColor Gray
    Write-Host "   • Movement & World services (Issue #1596)" -ForegroundColor Gray
    Write-Host ""
    Write-Host "🚀 Batch migrate combat services:" -ForegroundColor Cyan
    Write-Host "   .\.cursor\scripts\batch-migrate-combat-services.ps1" -ForegroundColor Gray
    Write-Host ""
}

if ($InProgressCount -gt 0) {
    Write-Host ""
    Write-Host "WARNING  Complete in-progress migrations:" -ForegroundColor Yellow
    Write-Host "   1. Update handlers" -ForegroundColor Gray
    Write-Host "   2. Build & test" -ForegroundColor Gray
    Write-Host "   3. Add MIGRATION_SUMMARY.md" -ForegroundColor Gray
    Write-Host ""
}

Write-Host "Documentation:" -ForegroundColor Cyan
Write-Host "   .\.cursor\ogen\02-MIGRATION-STEPS.md" -ForegroundColor Gray
Write-Host "   services\combat-combos-service-ogen-go\ (reference)" -ForegroundColor Gray
Write-Host ""

