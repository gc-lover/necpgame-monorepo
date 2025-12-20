# Quick check for Go files in services exceeding 1000 lines

$MAX_LINES = 1000
$failed = @()
$checked = 0

Write-Host "Checking Go files in services..." -ForegroundColor Cyan
Write-Host "Max lines per file: $MAX_LINES" -ForegroundColor Cyan
Write-Host ""

Get-ChildItem -Path "$PSScriptRoot\..\services" -Filter "*.go" -Recurse | ForEach-Object {
    if ($_.Name -notlike "*.gen.go" -and $_.Name -notlike "*.pb.go") {
        $checked++
        $lines = (Get-Content $_.FullName | Measure-Object -Line).Lines
        
        if ($lines -gt $MAX_LINES) {
            $failed += [PSCustomObject]@{
                File = $_.FullName.Replace("$PSScriptRoot\..\", "")
                Lines = $lines
            }
            Write-Host "FAIL: $($_.FullName) : $lines lines" -ForegroundColor Red
        }
    }
}

Write-Host ""
Write-Host "================================================================" -ForegroundColor Gray
Write-Host "Summary:" -ForegroundColor Cyan
Write-Host "================================================================" -ForegroundColor Gray
Write-Host "Total checked: $checked files" -ForegroundColor White
Write-Host "Failed: $($failed.Count) files" -ForegroundColor $(if ($failed.Count -gt 0) { "Red" } else { "Green" })
Write-Host "================================================================" -ForegroundColor Gray
Write-Host ""

if ($failed.Count -gt 0) {
    Write-Host "Files exceeding $MAX_LINES lines:" -ForegroundColor Red
    $failed | Format-Table -AutoSize
    exit 1
} else {
    Write-Host "All Go files pass the size check!" -ForegroundColor Green
    exit 0
}
