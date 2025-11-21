# Скрипт для проверки подключения UE5 Dedicated Server к Gateway

Write-Host "=== Проверка подключения UE5 Server к Gateway ===" -ForegroundColor Green
Write-Host ""

# Проверка Gateway
Write-Host "1. Проверка Gateway..." -ForegroundColor Yellow
try {
    $metrics = Invoke-WebRequest -Uri "http://localhost:9093/metrics" -UseBasicParsing
    if ($metrics.StatusCode -eq 200) {
        $metricsContent = $metrics.Content
        
        # Проверка активного подключения сервера
        if ($metricsContent -match 'active_server_connection\s+(\d+)') {
            $serverConnection = $matches[1]
            if ($serverConnection -eq "1") {
                Write-Host "  OK Server подключен к Gateway!" -ForegroundColor Green
            } else {
                Write-Host "  WARNING  Server НЕ подключен (active_server_connection = $serverConnection)" -ForegroundColor Yellow
                Write-Host "     Убедитесь, что UE5 Server запущен в PIE режиме" -ForegroundColor White
            }
        } else {
            Write-Host "  WARNING  Метрика active_server_connection не найдена" -ForegroundColor Yellow
        }
        
        # Показать активные соединения
        if ($metricsContent -match 'active_clients\s+(\d+)') {
            $activeClients = $matches[1]
            Write-Host "  Active Clients: $activeClients" -ForegroundColor Cyan
        }
        
        # Показать PlayerInput
        if ($metricsContent -match 'player_input_received_total\s+(\d+)') {
            $playerInput = $matches[1]
            Write-Host "  PlayerInput Received: $playerInput" -ForegroundColor Cyan
        }
        
        # Показать GameState
        if ($metricsContent -match 'gamestate_received_total\s+(\d+)') {
            $gameState = $matches[1]
            Write-Host "  GameState Received: $gameState" -ForegroundColor Cyan
        }
    }
} catch {
    Write-Host "  ❌ Ошибка при проверке Gateway: $_" -ForegroundColor Red
}

# Проверка WebSocket соединения
Write-Host ""
Write-Host "2. Проверка WebSocket порта..." -ForegroundColor Yellow
try {
    $wsTest = Test-NetConnection -ComputerName localhost -Port 18080 -InformationLevel Quiet
    if ($wsTest) {
        Write-Host "  OK WebSocket порт 18080 доступен" -ForegroundColor Green
    } else {
        Write-Host "  ❌ WebSocket порт 18080 недоступен" -ForegroundColor Red
    }
} catch {
    Write-Host "  WARNING  Не удалось проверить WebSocket порт" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== Инструкции ===" -ForegroundColor Green
Write-Host ""
Write-Host "Для проверки подключения в UE5 Editor:" -ForegroundColor Yellow
Write-Host "  1. Откройте Output Log (Window → Output Log)" -ForegroundColor White
Write-Host "  2. Ищите сообщение:" -ForegroundColor White
Write-Host "     'LyraServerGatewayConnection: Connected to gateway at 127.0.0.1:18080'" -ForegroundColor Cyan
Write-Host ""
Write-Host "Если сервер подключен, запустите нагрузочный тест:" -ForegroundColor Yellow
Write-Host "  cd services\realtime-gateway-go" -ForegroundColor White
Write-Host "  go build -o loadtest.exe ./cmd/loadtest" -ForegroundColor White
Write-Host "  .\loadtest.exe -url 'ws://127.0.0.1:18080/ws?token=test' -clients 10 -duration 60s" -ForegroundColor White
Write-Host ""

