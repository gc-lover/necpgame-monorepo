# Simple ogen Migration Progress Check

Write-Host ""
Write-Host "ogen Migration Progress" -ForegroundColor Cyan
Write-Host "========================" -ForegroundColor Cyan
Write-Host ""

# Count all services
$AllServices = Get-ChildItem "services\*-go" -Directory | Where-Object {
    $_.Name -notmatch '(database-partition-manager|database-view-refresher|workqueue-service)'
}

$TotalServices = $AllServices.Count
$MigratedCount = 0
$InProgressCount = 0

foreach ($Service in $AllServices) {
    $ServicePath = $Service.FullName
    
    # Check migration status
    if (Test-Path "$ServicePath\MIGRATION_SUMMARY.md") {
        $MigratedCount++
    }
    elseif ((Test-Path "$ServicePath\pkg\api\oas_json_gen.go") -or ((Test-Path "$ServicePath\Makefile") -and ((Get-Content "$ServicePath\Makefile" -Raw) -match 'ogen --target'))) {
        $InProgressCount++
    }
}

$NotStartedCount = $TotalServices - $MigratedCount - $InProgressCount

Write-Host "Total Services: $TotalServices" -ForegroundColor White
Write-Host "Migrated:       $MigratedCount" -ForegroundColor Green
Write-Host "In Progress:    $InProgressCount" -ForegroundColor Yellow
Write-Host "Not Started:    $NotStartedCount" -ForegroundColor Red
Write-Host ""

$Percent = [math]::Round(($MigratedCount / $TotalServices) * 100, 1)
Write-Host "Progress: $Percent percent complete" -ForegroundColor Cyan
Write-Host ""


