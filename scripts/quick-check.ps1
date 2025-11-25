$ErrorActionPreference = "SilentlyContinue"

$maxLines = 600
$failed = @()
$passed = 0
$excluded = 0

Write-Host "`n🔍 Quick File Size Check (Go files in services only)`n" -ForegroundColor Cyan
Write-Host "Max lines per file: $maxLines`n"

Get-ChildItem -Path "services" -Recurse -Include *.go -File | ForEach-Object {
    $relPath = $_.FullName.Replace((Get-Location).Path + "\", "")
    
    if ($_.Name -like "*.gen.go" -or $_.Name -like "*.pb.go" -or $relPath -like "*vendor*") {
        $excluded++
        return
    }
    
    $lines = (Get-Content $_.FullName | Measure-Object -Line).Lines
    
    if ($lines -gt $maxLines) {
        $failed += [PSCustomObject]@{
            File = $relPath
            Lines = $lines
            Exceeds = ($lines - $maxLines)
        }
        Write-Host "❌ $relPath - $lines lines (exceeds by $($lines - $maxLines))" -ForegroundColor Red
    } else {
        $passed++
        Write-Host "✅ $relPath - $lines lines" -ForegroundColor Green
    }
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "📊 Summary:" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "✅ Passed: $passed files" -ForegroundColor Green
Write-Host "❌ Failed: $($failed.Count) files" -ForegroundColor Red
Write-Host "⏭️  Excluded: $excluded files" -ForegroundColor Yellow

if ($failed.Count -gt 0) {
    Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Red
    Write-Host "❌ Files Exceeding $maxLines Lines:" -ForegroundColor Red
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Red
    $failed | Sort-Object -Property Exceeds -Descending | Format-Table -AutoSize
    Write-Host "`nThese files need to be split into smaller files!"
} else {
    Write-Host "`n✅ All files pass the size check!" -ForegroundColor Green
}

