#!/bin/bash
# Тестовая сборка Docker образов для всех сервисов

set -e

echo "🐳 Тестовая сборка Docker образов..."
echo ""

SERVICES=(
    "character-service-go"
    "inventory-service-go"
    "movement-service-go"
    "social-service-go"
    "achievement-service-go"
    "economy-service-go"
    "support-service-go"
    "reset-service-go"
    "gameplay-service-go"
    "admin-service-go"
    "clan-war-service-go"
    "companion-service-go"
    "voice-chat-service-go"
    "housing-service-go"
    "realtime-gateway-go"
    "ws-lobby-go"
    "matchmaking-go"
)

BUILD_ERRORS=0
BUILD_SUCCESS=0

for service in "${SERVICES[@]}"; do
    dockerfile="services/$service/Dockerfile"
    if [ ! -f "$dockerfile" ]; then
        echo "⏭️  Пропущен $service (нет Dockerfile)"
        continue
    fi

    echo "🔨 Сборка $service..."
    if docker build -q -t "necpgame-$service:test" -f "$dockerfile" "services/$service" > /dev/null 2>&1; then
        echo "OK $service собран успешно"
        BUILD_SUCCESS=$((BUILD_SUCCESS + 1))
        docker rmi "necpgame-$service:test" > /dev/null 2>&1 || true
    else
        echo "❌ Ошибка сборки $service"
        BUILD_ERRORS=$((BUILD_ERRORS + 1))
    fi
done

echo ""
echo "=============================================="
echo "Результаты:"
echo "  OK Успешно: $BUILD_SUCCESS"
echo "  ❌ Ошибок: $BUILD_ERRORS"
echo ""

if [ $BUILD_ERRORS -eq 0 ]; then
    echo "OK Все Docker образы собираются успешно!"
    exit 0
else
    echo "❌ Найдено $BUILD_ERRORS ошибок сборки"
    exit 1
fi


