# Fix remaining common errors in tests

$servicesPath = "services"

# Fix unused uuid imports
Get-ChildItem -Path $servicesPath -Directory | ForEach-Object {
    $service = $_.Name
    $benchFile = Join-Path $servicesPath "$service/server/handlers_bench_test.go"
    
    if (Test-Path $benchFile) {
        $content = Get-Content $benchFile -Raw
        
        # Remove unused uuid import if uuid is not used
        if ($content -match 'github.com/google/uuid' -and $content -notmatch 'uuid\.') {
            $content = $content -replace '\s+"github.com/google/uuid"\s*\n', "`n"
            Set-Content -Path $benchFile -Value $content -NoNewline
            Write-Host "Removed unused uuid import from $service"
        }
    }
}

Write-Host "Done!"

