# Issue: #50
# PowerShell script to import multiple quest YAML files in batch

param(
    [string]$QuestDir = "knowledge/canon/lore/timeline-author/quests/america/las-vegas/2020-2029",
    [string]$ApiUrl = "http://localhost:8083/api/v1/gameplay/quests/content/reload",
    [string]$AuthToken = ""
)

if (-not (Test-Path $QuestDir)) {
    Write-Host "Error: Quest directory not found: $QuestDir" -ForegroundColor Red
    exit 1
}

# Find all quest YAML files
$questFiles = Get-ChildItem -Path $QuestDir -Filter "quest-*.yaml" | Sort-Object Name

if ($questFiles.Count -eq 0) {
    Write-Host "No quest files found in: $QuestDir" -ForegroundColor Yellow
    exit 0
}

Write-Host "Found $($questFiles.Count) quest files to import" -ForegroundColor Green
Write-Host ""

$successCount = 0
$failCount = 0
$failedFiles = @()

foreach ($questFile in $questFiles) {
    Write-Host "Importing: $($questFile.Name)" -ForegroundColor Cyan
    
    try {
        & "$PSScriptRoot\import-quest.ps1" -QuestFile $questFile.FullName -ApiUrl $ApiUrl -AuthToken $AuthToken
        if ($LASTEXITCODE -eq 0) {
            $successCount++
            Write-Host "OK Success: $($questFile.Name)" -ForegroundColor Green
        } else {
            $failCount++
            $failedFiles += $questFile.Name
            Write-Host "❌ Failed: $($questFile.Name)" -ForegroundColor Red
        }
    } catch {
        $failCount++
        $failedFiles += $questFile.Name
        Write-Host "❌ Error importing $($questFile.Name): $($_.Exception.Message)" -ForegroundColor Red
    }
    
    Write-Host ""
    Start-Sleep -Milliseconds 500  # Small delay between requests
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Import Summary:" -ForegroundColor Cyan
Write-Host "  Total: $($questFiles.Count)" -ForegroundColor White
Write-Host "  Success: $successCount" -ForegroundColor Green
Write-Host "  Failed: $failCount" -ForegroundColor Red

if ($failedFiles.Count -gt 0) {
    Write-Host ""
    Write-Host "Failed files:" -ForegroundColor Red
    foreach ($file in $failedFiles) {
        Write-Host "  - $file" -ForegroundColor Red
    }
}

