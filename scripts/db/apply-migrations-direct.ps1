# Apply migrations directly via psql (without Liquibase)
# Issue: #50

$ErrorActionPreference = "Stop"

$ContainerName = "necpgame-postgres"
$Database = "necpgame"
$User = "postgres"
$MigrationsDir = "infrastructure/liquibase/migrations"

Write-Host "🚀 Applying migrations directly via psql..." -ForegroundColor Cyan
Write-Host ""

# Check container
if (-not (docker ps --filter "name=$ContainerName" --format "{{.Names}}")) {
    Write-Host "❌ Container '$ContainerName' not running!" -ForegroundColor Red
    exit 1
}

# Get all SQL migration files in order
$migrationFiles = @()

# Schema migrations (from changelog order)
$schemaFiles = Get-Content "infrastructure/liquibase/changelog.yaml" | 
    Where-Object { $_ -match "file:\s+migrations/(.+\.sql)" } | 
    ForEach-Object { 
        if ($_ -match "file:\s+migrations/(.+\.sql)") {
            $file = "infrastructure/liquibase/migrations/$($matches[1])"
            if (Test-Path $file) { $file }
        }
    }

# Content migrations
$questFiles = Get-ChildItem "$MigrationsDir/data/quests" -Filter "*.sql" -Recurse | Sort-Object Name
$npcFiles = Get-ChildItem "$MigrationsDir/data/npcs" -Filter "*.sql" -Recurse | Sort-Object Name
$dialogueFiles = Get-ChildItem "$MigrationsDir/data/dialogues" -Filter "*.sql" -Recurse | Sort-Object Name

$migrationFiles = $schemaFiles + $questFiles.FullName + $npcFiles.FullName + $dialogueFiles.FullName

Write-Host "📊 Found $($migrationFiles.Count) migration files" -ForegroundColor Yellow
Write-Host "  Schema: $($schemaFiles.Count)" -ForegroundColor Gray
Write-Host "  Quests: $($questFiles.Count)" -ForegroundColor Gray
Write-Host "  NPCs: $($npcFiles.Count)" -ForegroundColor Gray
Write-Host "  Dialogues: $($dialogueFiles.Count)" -ForegroundColor Gray
Write-Host ""

$success = 0
$failed = 0
$skipped = 0
$failedFiles = @()

foreach ($file in $migrationFiles) {
    $fileName = Split-Path $file -Leaf
    
    # Check if already applied (simple check - skip if table exists)
    $isContentMigration = $file -match "data/(quests|npcs|dialogues)"
    
    if ($isContentMigration) {
        # For content migrations, check if data exists
        if ($file -match "quests") {
            $checkQuery = "SELECT COUNT(*) FROM gameplay.quest_definitions LIMIT 1;"
        } elseif ($file -match "npcs") {
            $checkQuery = "SELECT COUNT(*) FROM narrative.npc_definitions LIMIT 1;"
        } else {
            $checkQuery = "SELECT COUNT(*) FROM narrative.dialogue_nodes LIMIT 1;"
        }
        
        $tableExists = docker exec $ContainerName psql -U $User -d $Database -t -c $checkQuery 2>&1
        if ($tableExists -match "ERROR" -or $tableExists -match "does not exist") {
            Write-Host "WARNING  Table not created yet, skipping content migration: $fileName" -ForegroundColor Yellow
            $skipped++
            continue
        }
    }
    
    Write-Host "📝 Applying: $fileName" -ForegroundColor Cyan -NoNewline
    
    $sqlContent = Get-Content $file -Raw -Encoding UTF8
    $result = $sqlContent | docker exec -i $ContainerName psql -U $User -d $Database 2>&1
    
    if ($LASTEXITCODE -eq 0 -and $result -notmatch "ERROR") {
        Write-Host " OK" -ForegroundColor Green
        $success++
    } else {
        if ($result -match "already exists" -or $result -match "duplicate key") {
            Write-Host " ⏭️  (already applied)" -ForegroundColor Yellow
            $skipped++
        } else {
            Write-Host " ❌" -ForegroundColor Red
            $errorMsg = ($result | Select-Object -Last 2) -join " "
            Write-Host "   Error: $errorMsg" -ForegroundColor Red
            $failed++
            $failedFiles += @{
                File = $fileName
                Path = $file
                Error = $errorMsg
            }
        }
    }
}

Write-Host ""
Write-Host ""
Write-Host "=" * 80 -ForegroundColor Cyan
Write-Host "📊 SUMMARY" -ForegroundColor Cyan
Write-Host "=" * 80 -ForegroundColor Cyan
Write-Host "OK Success: $success" -ForegroundColor Green
Write-Host "⏭️  Skipped: $skipped" -ForegroundColor Yellow
Write-Host "❌ Failed: $failed" -ForegroundColor $(if ($failed -eq 0) { "Green" } else { "Red" })
Write-Host ""

# Show failed migrations details
if ($failed -gt 0) {
    Write-Host "=" * 80 -ForegroundColor Red
    Write-Host "❌ FAILED MIGRATIONS" -ForegroundColor Red
    Write-Host "=" * 80 -ForegroundColor Red
    foreach ($failedFile in $failedFiles) {
        Write-Host ""
        Write-Host "📄 File: $($failedFile.File)" -ForegroundColor Yellow
        Write-Host "   Path: $($failedFile.Path)" -ForegroundColor Gray
        Write-Host "   Error: $($failedFile.Error)" -ForegroundColor Red
    }
    Write-Host ""
}

if ($failed -eq 0) {
    Write-Host "OK All migrations completed!" -ForegroundColor Green
    
    # Check results
    Write-Host ""
    Write-Host "📊 Database status:" -ForegroundColor Cyan
    docker exec $ContainerName psql -U $User -d $Database -c "
    SELECT 
        'quests' as type, COUNT(*) as count FROM gameplay.quest_definitions
    UNION ALL
    SELECT 
        'npcs', COUNT(*) FROM narrative.npc_definitions
    UNION ALL
    SELECT 
        'dialogues', COUNT(*) FROM narrative.dialogue_nodes;
    " 2>&1
} else {
    Write-Host "❌ Some migrations failed!" -ForegroundColor Red
    exit 1
}

