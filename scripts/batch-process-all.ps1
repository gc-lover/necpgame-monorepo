# Issue: #1586 - Batch processing всех файлов для struct alignment
# Обрабатывает все OpenAPI и Liquibase файлы

param(
    [string]$TestDir = "test-struct-alignment",
    [switch]$DryRun = $false
)

# Получаем абсолютный путь к рабочей директории
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$workDir = if ($scriptPath) { Split-Path -Parent $scriptPath } else { Get-Location }
$testPath = Join-Path $workDir $TestDir

Write-Host "Batch processing all files for struct alignment" -ForegroundColor Cyan
Write-Host "Working directory: $workDir" -ForegroundColor Gray
Write-Host "Test directory: $testPath" -ForegroundColor Gray
Write-Host ""

$openapiDir = Join-Path $testPath "openapi"
$liquibaseDir = Join-Path $testPath "liquibase"

$openapiFiles = @()
$liquibaseFiles = @()

if (Test-Path $openapiDir) {
    $openapiFiles = Get-ChildItem $openapiDir -Filter "*.yaml" -ErrorAction SilentlyContinue
}
if (Test-Path $liquibaseDir) {
    $liquibaseFiles = Get-ChildItem $liquibaseDir -Filter "*.sql" -ErrorAction SilentlyContinue
}

$openapiResults = @()
$liquibaseResults = @()

$openapiTotal = 0
$liquibaseTotal = 0

# Обработка OpenAPI файлов
Write-Host "=========================================" -ForegroundColor Yellow
Write-Host "OpenAPI YAML refactoring ($($openapiFiles.Count) files)" -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Yellow
Write-Host ""

$processed = 0
foreach ($file in $openapiFiles) {
    $processed++
    Write-Progress -Activity "Processing OpenAPI" -Status "$($file.Name)" -PercentComplete (($processed / $openapiFiles.Count) * 100)
    
    $scriptPath = Join-Path $workDir "scripts\reorder-openapi-fields.py"
    $dryRunFlag = if ($DryRun) { "--dry-run" } else { "" }
    $output = python $scriptPath $file.FullName $dryRunFlag 2>&1 | Out-String
    
    # Парсим вывод - ищем "Changed schemas: N"
    if ($output -match "Changed\s+schemas?:\s*(\d+)") {
        $count = [int]($matches[1])
        if ($count -gt 0) {
            $openapiTotal += $count
            $openapiResults += [PSCustomObject]@{
                File = $file.Name
                Changed = $count
                Status = "OK"
            }
        }
    }
    elseif ($output -match "уже оптимизированы|Already optimized") {
        $openapiResults += [PSCustomObject]@{
            File = $file.Name
            Changed = 0
            Status = "Already optimized"
        }
    }
    else {
        $openapiResults += [PSCustomObject]@{
            File = $file.Name
            Changed = 0
            Status = "No changes"
        }
    }
}

Write-Progress -Activity "Processing OpenAPI" -Completed

# Обработка Liquibase файлов
Write-Host ""
Write-Host "=========================================" -ForegroundColor Yellow
Write-Host "Liquibase SQL refactoring ($($liquibaseFiles.Count) files)" -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Yellow
Write-Host ""

$processed = 0
foreach ($file in $liquibaseFiles) {
    $processed++
    Write-Progress -Activity "Processing Liquibase" -Status "$($file.Name)" -PercentComplete (($processed / $liquibaseFiles.Count) * 100)
    
    $scriptPath = Join-Path $workDir "scripts\reorder-liquibase-columns.py"
    $dryRunFlag = if ($DryRun) { "--dry-run" } else { "" }
    $output = python $scriptPath $file.FullName $dryRunFlag 2>&1 | Out-String
    
    # Парсим вывод - ищем "Changed tables: N"
    if ($output -match "Changed\s+tables?:\s*(\d+)") {
        $count = [int]($matches[1])
        if ($count -gt 0) {
            $liquibaseTotal += $count
            $liquibaseResults += [PSCustomObject]@{
                File = $file.Name
                Changed = $count
                Status = "OK"
            }
        }
    }
    elseif ($output -match "уже оптимизированы|Already optimized") {
        $liquibaseResults += [PSCustomObject]@{
            File = $file.Name
            Changed = 0
            Status = "Already optimized"
        }
    }
    else {
        $liquibaseResults += [PSCustomObject]@{
            File = $file.Name
            Changed = 0
            Status = "No changes"
        }
    }
}

Write-Progress -Activity "Processing Liquibase" -Completed

# Итоговая статистика
Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "FINAL STATISTICS" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

$openapiChanged = ($openapiResults | Where-Object { $_.Changed -gt 0 }).Count
$liquibaseChanged = ($liquibaseResults | Where-Object { $_.Changed -gt 0 }).Count

Write-Host "OpenAPI YAML:" -ForegroundColor White
Write-Host "  Total files: $($openapiFiles.Count)" -ForegroundColor Gray
Write-Host "  Changed files: $openapiChanged" -ForegroundColor Green
Write-Host "  Total schemas changed: $openapiTotal" -ForegroundColor Green
Write-Host ""

Write-Host "Liquibase SQL:" -ForegroundColor White
Write-Host "  Total files: $($liquibaseFiles.Count)" -ForegroundColor Gray
Write-Host "  Changed files: $liquibaseChanged" -ForegroundColor Green
Write-Host "  Total tables changed: $liquibaseTotal" -ForegroundColor Green
Write-Host ""

# Сохраняем детальные результаты
if ($openapiResults.Count -gt 0) {
    $openapiResults | Export-Csv -Path (Join-Path $testPath "openapi-results.csv") -NoTypeInformation -Encoding UTF8
}
if ($liquibaseResults.Count -gt 0) {
    $liquibaseResults | Export-Csv -Path (Join-Path $testPath "liquibase-results.csv") -NoTypeInformation -Encoding UTF8
}

Write-Host "Detailed results saved:" -ForegroundColor Green
Write-Host "  - $(Join-Path $testPath 'openapi-results.csv')" -ForegroundColor Gray
Write-Host "  - $(Join-Path $testPath 'liquibase-results.csv')" -ForegroundColor Gray
Write-Host ""

if ($DryRun) {
    Write-Host "DRY RUN mode - changes not applied" -ForegroundColor Yellow
    Write-Host "Run without -DryRun to apply changes" -ForegroundColor Cyan
}

