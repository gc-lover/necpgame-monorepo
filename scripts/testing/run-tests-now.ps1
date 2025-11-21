# Скрипт для запуска UE5 тестов
# Использование: .\run-tests-now.ps1

Write-Host "=== Запуск UE5 тестов синхронизации ===" -ForegroundColor Green
Write-Host ""

# Проверка Gateway
Write-Host "Проверка Gateway..." -ForegroundColor Yellow
$gatewayStatus = docker-compose ps realtime-gateway 2>&1
if ($gatewayStatus -match "Up") {
    Write-Host "✓ Gateway запущен" -ForegroundColor Green
} else {
    Write-Host "⚠ Gateway не запущен. Запускаю..." -ForegroundColor Yellow
    docker-compose up -d realtime-gateway
    Start-Sleep -Seconds 3
}

Write-Host ""
Write-Host "Для запуска тестов:" -ForegroundColor Cyan
Write-Host "1. Откройте проект в Unreal Editor" -ForegroundColor White
Write-Host "2. Перейдите: Window → Test Automation" -ForegroundColor White
Write-Host "3. Найдите: LyraGame.Network.WebSocket" -ForegroundColor White
Write-Host "4. Нажмите: Start Tests" -ForegroundColor White
Write-Host ""
Write-Host "Или используйте командную строку (требует собранный билд):" -ForegroundColor Cyan
Write-Host "cd client\UE5\NECPGAME" -ForegroundColor Gray
Write-Host '& "C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="$PWD\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.*" -ReportOutputPath="$PWD\TestResults"' -ForegroundColor Gray
Write-Host ""



