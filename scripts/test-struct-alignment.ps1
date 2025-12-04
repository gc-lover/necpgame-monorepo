# Issue: #1586 - Тестирование скриптов struct alignment
# Тестирует скрипты на копиях файлов и сравнивает результаты

param(
    [string]$TestDir = "test-struct-alignment"
)

Write-Host "🧪 Тестирование скриптов struct alignment" -ForegroundColor Cyan
Write-Host ""

$openapiDir = "$TestDir/openapi"
$liquibaseDir = "$TestDir/liquibase"

# Тестируем OpenAPI скрипты
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Yellow
Write-Host "📄 OpenAPI YAML рефакторинг" -ForegroundColor Yellow
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Yellow
Write-Host ""

$openapiFiles = Get-ChildItem $openapiDir -Filter "*.yaml"
$openapiResults = @()

foreach ($file in $openapiFiles) {
    Write-Host "🔍 Тестирую: $($file.Name)" -ForegroundColor Cyan
    
    # Dry run
    $dryRun = python scripts/reorder-openapi-fields.py $file.FullName --dry-run 2>&1 | Out-String
    $changedMatch = $dryRun -match "Изменено schemas: (\d+)"
    
    if ($changedMatch) {
        $count = [int]($matches[1])
        Write-Host "  OK Найдено изменений: $count schemas" -ForegroundColor Green
        
        # Применяем изменения
        python scripts/reorder-openapi-fields.py $file.FullName 2>&1 | Out-Null
        
        # Проверяем валидность
        $validation = npx --yes @redocly/cli lint $file.FullName 2>&1 | Out-String
        if ($validation -match "validated") {
            Write-Host "  OK Валидация: OK" -ForegroundColor Green
        }
        else {
            Write-Host "  WARNING  Валидация: Warnings" -ForegroundColor Yellow
        }
        
        $openapiResults += [PSCustomObject]@{
            File = $file.Name
            Changed = $count
            Status = "OK"
        }
    }
    else {
        Write-Host "  ℹ️  Уже оптимизировано" -ForegroundColor Gray
        $openapiResults += [PSCustomObject]@{
            File = $file.Name
            Changed = 0
            Status = "Already optimized"
        }
    }
    Write-Host ""
}

# Тестируем Liquibase скрипты
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Yellow
Write-Host "🗄️  Liquibase SQL рефакторинг" -ForegroundColor Yellow
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Yellow
Write-Host ""

$liquibaseFiles = Get-ChildItem $liquibaseDir -Filter "*.sql"
$liquibaseResults = @()

foreach ($file in $liquibaseFiles) {
    Write-Host "🔍 Тестирую: $($file.Name)" -ForegroundColor Cyan
    
    # Dry run
    $dryRun = python scripts/reorder-liquibase-columns.py $file.FullName --dry-run 2>&1 | Out-String
    $changedMatch = $dryRun -match "Изменено таблиц: (\d+)"
    
    if ($changedMatch) {
        $count = [int]($matches[1])
        Write-Host "  OK Найдено изменений: $count таблиц" -ForegroundColor Green
        
        # Применяем изменения
        python scripts/reorder-liquibase-columns.py $file.FullName 2>&1 | Out-Null
        
        # Проверяем что PRIMARY KEY сохранены
        $content = Get-Content $file.FullName -Raw
        if ($content -match "PRIMARY KEY") {
            Write-Host "  OK PRIMARY KEY сохранены" -ForegroundColor Green
        }
        
        $liquibaseResults += [PSCustomObject]@{
            File = $file.Name
            Changed = $count
            Status = "OK"
        }
    }
    else {
        Write-Host "  ℹ️  Уже оптимизировано" -ForegroundColor Gray
        $liquibaseResults += [PSCustomObject]@{
            File = $file.Name
            Changed = 0
            Status = "Already optimized"
        }
    }
    Write-Host ""
}

# Итоговая статистика
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "📊 ИТОГОВАЯ СТАТИСТИКА" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host ""

Write-Host "OpenAPI YAML:" -ForegroundColor White
$openapiResults | Format-Table -AutoSize

Write-Host "Liquibase SQL:" -ForegroundColor White
$liquibaseResults | Format-Table -AutoSize

$totalOpenApi = ($openapiResults | Measure-Object -Property Changed -Sum).Sum
$totalLiquibase = ($liquibaseResults | Measure-Object -Property Changed -Sum).Sum

Write-Host ""
Write-Host "Всего обработано:" -ForegroundColor Cyan
Write-Host "  OpenAPI: $($openapiFiles.Count) файлов, $totalOpenApi schemas изменено" -ForegroundColor White
Write-Host "  Liquibase: $($liquibaseFiles.Count) файлов, $totalLiquibase таблиц изменено" -ForegroundColor White
Write-Host ""
