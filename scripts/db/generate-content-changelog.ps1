# Generate changelog entries for content migrations (quests, NPCs, dialogues)
# Issue: #50

$migrationsDir = "infrastructure/liquibase/migrations/data"
$changelogFile = "infrastructure/liquibase/changelog-content.yaml"

Write-Host "[NOTE] Generating content migrations changelog..." -ForegroundColor Cyan

# Find all content migration files
$questFiles = Get-ChildItem -Path "$migrationsDir/quests" -Filter "*.sql" -Recurse | Sort-Object Name
$npcFiles = Get-ChildItem -Path "$migrationsDir/npcs" -Filter "*.sql" -Recurse | Sort-Object Name
$dialogueFiles = Get-ChildItem -Path "$migrationsDir/dialogues" -Filter "*.sql" -Recurse | Sort-Object Name

Write-Host "  Found $($questFiles.Count) quest migrations" -ForegroundColor Gray
Write-Host "  Found $($npcFiles.Count) NPC migrations" -ForegroundColor Gray
Write-Host "  Found $($dialogueFiles.Count) dialogue migrations" -ForegroundColor Gray

# Generate YAML content
$yamlLines = @("databaseChangeLog:")
$yamlLines += "  # Content migrations - generated automatically"
$yamlLines += "  # Issue: #50"
$yamlLines += ""

# Add quest migrations
if ($questFiles.Count -gt 0) {
    $yamlLines += "  # Quest migrations"
    foreach ($file in $questFiles) {
        $relativePath = $file.FullName -replace [regex]::Escape($PWD.Path + '\'), '' -replace '\\', '/'
        $yamlLines += "  - include:"
        $yamlLines += "      file: $relativePath"
    }
    $yamlLines += ""
}

# Add NPC migrations
if ($npcFiles.Count -gt 0) {
    $yamlLines += "  # NPC migrations"
    foreach ($file in $npcFiles) {
        $relativePath = $file.FullName -replace [regex]::Escape($PWD.Path + '\'), '' -replace '\\', '/'
        $yamlLines += "  - include:"
        $yamlLines += "      file: $relativePath"
    }
    $yamlLines += ""
}

# Add dialogue migrations
if ($dialogueFiles.Count -gt 0) {
    $yamlLines += "  # Dialogue migrations"
    foreach ($file in $dialogueFiles) {
        $relativePath = $file.FullName -replace [regex]::Escape($PWD.Path + '\'), '' -replace '\\', '/'
        $yamlLines += "  - include:"
        $yamlLines += "      file: $relativePath"
    }
    $yamlLines += ""
}

# Write to file
$yamlContent = $yamlLines -join "`n"
$yamlContent | Out-File -FilePath $changelogFile -Encoding UTF8 -NoNewline

Write-Host "[OK] Generated: $changelogFile" -ForegroundColor Green
Write-Host "   Total migrations: $($questFiles.Count + $npcFiles.Count + $dialogueFiles.Count)" -ForegroundColor Gray
Write-Host ""
Write-Host "[IDEA] Add to main changelog.yaml:" -ForegroundColor Yellow
Write-Host "   - include:" -ForegroundColor Gray
Write-Host "       file: changelog-content.yaml" -ForegroundColor Gray

