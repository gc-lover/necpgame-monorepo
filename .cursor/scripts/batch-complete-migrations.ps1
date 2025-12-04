# Batch complete ogen migrations for all services with generated code

Write-Host ""
Write-Host "Batch Completing ogen Migrations" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
Write-Host ""

$Services = Get-ChildItem "services\*-go" -Directory | Where-Object {
    $_.Name -notmatch '(database-partition-manager|database-view-refresher|workqueue-service|combat-combos-service-ogen-go)'
}

$Completed = 0
$Skipped = 0
$Failed = 0

foreach ($Service in $Services) {
    $ServiceName = $Service.Name
    $ServicePath = $Service.FullName
    
    # Skip if already has MIGRATION_SUMMARY.md
    if (Test-Path "$ServicePath\MIGRATION_SUMMARY.md") {
        Write-Host "SKIP: $ServiceName - Already migrated" -ForegroundColor Yellow
        $Skipped++
        continue
    }
    
    # Skip if no ogen code
    if (-not (Test-Path "$ServicePath\pkg\api\oas_json_gen.go")) {
        continue
    }
    
    Write-Host ""
    Write-Host "----------------------------------------" -ForegroundColor DarkGray
    Write-Host "Processing: $ServiceName" -ForegroundColor Cyan
    Write-Host "----------------------------------------" -ForegroundColor DarkGray
    
    try {
        $ScriptPath = Join-Path $PSScriptRoot "complete-ogen-migration.ps1"
        if (-not (Test-Path $ScriptPath)) {
            $ScriptPath = Join-Path (Get-Location) ".cursor\scripts\complete-ogen-migration.ps1"
        }
        & $ScriptPath -ServiceName $ServiceName
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "OK: $ServiceName - COMPLETE" -ForegroundColor Green
            $Completed++
        } else {
            Write-Host "FAIL: $ServiceName - FAILED" -ForegroundColor Red
            $Failed++
        }
    }
    catch {
        Write-Host "ERROR: $ServiceName - $_" -ForegroundColor Red
        $Failed++
    }
}

Write-Host ""
Write-Host "----------------------------------------" -ForegroundColor DarkGray
Write-Host "Summary" -ForegroundColor Cyan
Write-Host "----------------------------------------" -ForegroundColor DarkGray
Write-Host ""
Write-Host "OK Completed: $Completed" -ForegroundColor Green
Write-Host "⏭️  Skipped:   $Skipped" -ForegroundColor Yellow
$FailedColor = if ($Failed -gt 0) { "Red" } else { "Green" }
Write-Host "❌ Failed:    $Failed" -ForegroundColor $FailedColor
Write-Host ""

