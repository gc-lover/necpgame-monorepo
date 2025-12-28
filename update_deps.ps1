# Update Go dependencies for all services
Get-ChildItem -Path services -Directory | Where-Object { Test-Path "$($_.FullName)\go.mod" } | ForEach-Object {
    Write-Host "Updating $($_.Name)"
    Push-Location $_.FullName
    try {
        go mod tidy
        Write-Host "✓ Updated $($_.Name)"
    } catch {
        Write-Host "✗ Failed to update $($_.Name): $($_.Exception.Message)"
    }
    Pop-Location
}
