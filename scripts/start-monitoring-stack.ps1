# Issue: Local monitoring stack
# Запускает весь стек мониторинга локально (PowerShell)

Write-Host "🚀 Starting NECPGAME Monitoring Stack..." -ForegroundColor Cyan
Write-Host ""

# Проверяем Docker
try {
    docker info | Out-Null
} catch {
    Write-Host "❌ Docker is not running. Please start Docker first." -ForegroundColor Red
    exit 1
}

# Запускаем стек мониторинга
Write-Host "📊 Starting monitoring services..." -ForegroundColor Yellow
docker-compose up -d prometheus grafana loki tempo pyroscope promtail

Write-Host ""
Write-Host "⏳ Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Проверяем статус
Write-Host ""
Write-Host "📋 Service Status:" -ForegroundColor Cyan
Write-Host ""

$services = @(
    @{Name="prometheus"; Port=9090},
    @{Name="grafana"; Port=3000},
    @{Name="loki"; Port=3100},
    @{Name="tempo"; Port=3200},
    @{Name="pyroscope"; Port=4040}
)

foreach ($service in $services) {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:$($service.Port)" -TimeoutSec 2 -UseBasicParsing -ErrorAction SilentlyContinue
        Write-Host "  OK $($service.Name): http://localhost:$($service.Port)" -ForegroundColor Green
    } catch {
        Write-Host "  WARNING  $($service.Name): starting... (check http://localhost:$($service.Port))" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "OK Monitoring stack started!" -ForegroundColor Green
Write-Host ""
Write-Host "📊 Access URLs:" -ForegroundColor Cyan
Write-Host "   Grafana:      http://localhost:3000 (admin/admin)"
Write-Host "   Prometheus:   http://localhost:9090"
Write-Host "   Pyroscope:    http://localhost:4040"
Write-Host "   Loki:         http://localhost:3100"
Write-Host "   Tempo:        http://localhost:3200"
Write-Host ""
Write-Host "📝 Next steps:" -ForegroundColor Yellow
Write-Host "   1. Open Grafana: http://localhost:3000"
Write-Host "   2. Login: admin / admin"
Write-Host "   3. Import dashboards from: infrastructure/observability/grafana/dashboards/"
Write-Host ""
Write-Host "🛑 To stop: docker-compose stop prometheus grafana loki tempo pyroscope promtail" -ForegroundColor Gray

