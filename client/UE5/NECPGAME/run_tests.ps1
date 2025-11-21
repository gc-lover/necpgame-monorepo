$ErrorActionPreference = "Stop"

$ProjectPath = Join-Path $PSScriptRoot "NECPGAME.uproject"
$UEPath = "C:\Program Files\Epic Games\UE_5.7"
$RunUAT = Join-Path $UEPath "Engine\Build\BatchFiles\RunUAT.bat"
$TestResultsPath = Join-Path $PSScriptRoot "TestResults"

Write-Host "=== Запуск Automation тестов ===" -ForegroundColor Green

& $RunUAT RunUnreal `
    -Project=$ProjectPath `
    -build=$UEPath `
    -EditorTestList="LyraGame.Network.WebSocket.*" `
    -ReportOutputPath=$TestResultsPath

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n=== Automation тесты завершены успешно ===" -ForegroundColor Green
} else {
    Write-Host "`n=== Automation тесты завершились с ошибками ===" -ForegroundColor Red
    Write-Host "Код выхода: $LASTEXITCODE" -ForegroundColor Yellow
}

Write-Host "`n=== Запуск Gauntlet теста ===" -ForegroundColor Green

& $RunUAT RunUnreal `
    -Project=$ProjectPath `
    -build=$UEPath `
    -Test="LyraTest.WebSocketSyncTest" `
    -Gauntlet `
    -ReportOutputPath=$TestResultsPath

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n=== Gauntlet тест завершен успешно ===" -ForegroundColor Green
} else {
    Write-Host "`n=== Gauntlet тест завершился с ошибками ===" -ForegroundColor Red
    Write-Host "Код выхода: $LASTEXITCODE" -ForegroundColor Yellow
}

Write-Host "`n=== Результаты тестов сохранены в: $TestResultsPath ===" -ForegroundColor Cyan



