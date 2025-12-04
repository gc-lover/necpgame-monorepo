# Fix go.mod/go.sum for all services
# Runs go mod tidy on all services that have go.mod

Write-Host ""
Write-Host "Fixing go.mod/go.sum for all services..." -ForegroundColor Cyan
Write-Host ""

$Services = Get-ChildItem "services\*-go" -Directory | Where-Object {
    $_.Name -notmatch '(database-partition-manager|database-view-refresher|workqueue-service)'
}

$Fixed = 0
$Failed = 0

foreach ($Service in $Services) {
    $ServicePath = $Service.FullName
    $ServiceName = $Service.Name
    
    if (Test-Path "$ServicePath\go.mod") {
        Write-Host "Processing: $ServiceName" -ForegroundColor Gray -NoNewline
        
        Push-Location $ServicePath
        $Output = & go mod tidy 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host " - OK" -ForegroundColor Green
            $Fixed++
        } else {
            Write-Host " - FAILED" -ForegroundColor Red
            Write-Host $Output
            $Failed++
        }
        
        Pop-Location
    }
}

Write-Host ""
Write-Host "Fixed: $Fixed" -ForegroundColor Green
Write-Host "Failed: $Failed" -ForegroundColor $(if ($Failed -gt 0) { "Red" } else { "Green" })
Write-Host ""

