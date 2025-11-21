# Simple synchronization test for metrics verification
# Requires: docker-compose, realtime-gateway

Write-Host "=== Basic Synchronization Test ===" -ForegroundColor Green
Write-Host ""

# Check service availability
Write-Host "1. Checking service availability..." -ForegroundColor Yellow

$gatewayStatus = docker-compose ps realtime-gateway --format json | ConvertFrom-Json
if ($gatewayStatus.State -ne "running") {
    Write-Host "  ERROR: realtime-gateway is not running!" -ForegroundColor Red
    exit 1
}
Write-Host "  OK: realtime-gateway is running" -ForegroundColor Green

# Check metrics
Write-Host ""
Write-Host "2. Checking metrics..." -ForegroundColor Yellow

try {
    $metrics = Invoke-WebRequest -Uri "http://localhost:9093/metrics" -UseBasicParsing
    if ($metrics.StatusCode -eq 200) {
        Write-Host "  OK: Metrics available at http://localhost:9093/metrics" -ForegroundColor Green
        
        # Check for required metrics
        $metricsContent = $metrics.Content
        $requiredMetrics = @(
            "player_input_received_total",
            "player_input_forwarded_total",
            "gamestate_received_total",
            "gamestate_broadcasted_total",
            "gamestate_broadcast_duration_seconds",
            "active_clients",
            "active_server_connection",
            "message_size_bytes"
        )
        
        Write-Host ""
        Write-Host "  Checking required metrics:" -ForegroundColor Cyan
        foreach ($metric in $requiredMetrics) {
            if ($metricsContent -match $metric) {
                Write-Host "    OK: $metric" -ForegroundColor Green
            } else {
                Write-Host "    MISSING: $metric" -ForegroundColor Red
            }
        }
    } else {
        Write-Host "  ERROR: Metrics unavailable (code: $($metrics.StatusCode))" -ForegroundColor Red
    }
} catch {
    Write-Host "  ERROR: Failed to get metrics: $_" -ForegroundColor Red
}

# Check WebSocket endpoint
Write-Host ""
Write-Host "3. Checking WebSocket endpoint..." -ForegroundColor Yellow

try {
    $wsTest = Test-NetConnection -ComputerName localhost -Port 18080 -InformationLevel Quiet
    if ($wsTest) {
        Write-Host "  OK: WebSocket endpoint available at ws://localhost:18080" -ForegroundColor Green
    } else {
        Write-Host "  WARNING: WebSocket endpoint unavailable" -ForegroundColor Yellow
    }
} catch {
    Write-Host "  WARNING: Failed to check WebSocket endpoint: $_" -ForegroundColor Yellow
}

# Current metric values
Write-Host ""
Write-Host "4. Current metric values:" -ForegroundColor Yellow

try {
    $metrics = Invoke-WebRequest -Uri "http://localhost:9093/metrics" -UseBasicParsing
    $metricsContent = $metrics.Content
    
    $metricsToShow = @{
        "active_clients" = "Active Clients"
        "active_server_connection" = "Server Connection"
        "player_input_received_total" = "PlayerInput Received"
        "player_input_forwarded_total" = "PlayerInput Forwarded"
        "gamestate_received_total" = "GameState Received"
        "gamestate_broadcasted_total" = "GameState Broadcasted"
    }
    
    foreach ($metricName in $metricsToShow.Keys) {
        $pattern = "$metricName\s+(\d+(?:\.\d+)?)"
        if ($metricsContent -match $pattern) {
            $value = $matches[1]
            $label = $metricsToShow[$metricName]
            Write-Host "  $label : $value" -ForegroundColor Cyan
        }
    }
} catch {
    Write-Host "  ERROR: Failed to get metric values" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Test completed ===" -ForegroundColor Green
Write-Host ""
Write-Host "For full synchronization test:" -ForegroundColor Yellow
Write-Host "  1. Start UE5 Dedicated Server" -ForegroundColor White
Write-Host "  2. Start one or more UE5 clients" -ForegroundColor White
Write-Host "  3. Check metrics in Prometheus: http://localhost:9090" -ForegroundColor White
Write-Host "  4. Check metrics in Grafana: http://localhost:3000" -ForegroundColor White
Write-Host ""

