#!/bin/bash
# Валидация Dockerfile для всех сервисов

set -e

echo "🔍 Валидация Dockerfile для всех Go сервисов..."

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

MISSING=0
INVALID=0

for service in "${SERVICES[@]}"; do
    dockerfile="services/$service/Dockerfile"
    if [ ! -f "$dockerfile" ]; then
        echo "❌ Отсутствует: $dockerfile"
        MISSING=$((MISSING + 1))
    else
        if ! docker build --dry-run -f "$dockerfile" "services/$service" > /dev/null 2>&1; then
            echo "⚠️  Проблема с синтаксисом: $dockerfile"
            INVALID=$((INVALID + 1))
        else
            echo "✅ $service"
        fi
    fi
done

if [ $MISSING -eq 0 ] && [ $INVALID -eq 0 ]; then
    echo "✅ Все Dockerfile валидны!"
    exit 0
else
    echo "❌ Найдено проблем: отсутствует $MISSING, невалидных $INVALID"
    exit 1
fi


