#!/bin/bash
# Issue: Local monitoring stack
# Запускает весь стек мониторинга локально

set -e

echo "🚀 Starting NECPGAME Monitoring Stack..."
echo ""

# Проверяем Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker not found. Please install Docker first."
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Запускаем стек мониторинга
echo "📊 Starting monitoring services..."
docker-compose up -d prometheus grafana loki tempo pyroscope promtail

echo ""
echo "⏳ Waiting for services to be ready..."
sleep 5

# Проверяем статус
echo ""
echo "📋 Service Status:"
echo ""

services=("prometheus:9090" "grafana:3000" "loki:3100" "tempo:3200" "pyroscope:4040")

for service in "${services[@]}"; do
    name=$(echo $service | cut -d: -f1)
    port=$(echo $service | cut -d: -f2)
    
    if curl -s "http://localhost:$port" > /dev/null 2>&1; then
        echo "  ✅ $name: http://localhost:$port"
    else
        echo "  ⚠️  $name: starting... (check http://localhost:$port)"
    fi
done

echo ""
echo "✅ Monitoring stack started!"
echo ""
echo "📊 Access URLs:"
echo "   Grafana:      http://localhost:3000 (admin/admin)"
echo "   Prometheus:   http://localhost:9090"
echo "   Pyroscope:    http://localhost:4040"
echo "   Loki:         http://localhost:3100"
echo "   Tempo:        http://localhost:3200"
echo ""
echo "📝 Next steps:"
echo "   1. Open Grafana: http://localhost:3000"
echo "   2. Login: admin / admin"
echo "   3. Import dashboards from: infrastructure/observability/grafana/dashboards/"
echo ""
echo "🛑 To stop: docker-compose stop prometheus grafana loki tempo pyroscope promtail"

