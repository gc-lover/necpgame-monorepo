#!/bin/bash
# Скрипт для добавления зависимости oapi-codegen/runtime во все сервисы

set -e

echo "🔧 Adding oapi-codegen/runtime dependency to all services..."
echo ""

SERVICES=(
    "reset-service-go"
    "companion-service-go"
    "inventory-service-go"
    "housing-service-go"
    "clan-war-service-go"
    "movement-service-go"
    "referral-service-go"
    "voice-chat-service-go"
    "achievement-service-go"
    "admin-service-go"
    "battle-pass-service-go"
    "character-service-go"
    "economy-service-go"
    "feedback-service-go"
    "gameplay-service-go"
    "leaderboard-service-go"
    "social-service-go"
    "support-service-go"
    "world-service-go"
)

TOTAL=0
ADDED=0
SKIPPED=0

for service in "${SERVICES[@]}"; do
    service_path="services/$service"
    go_mod="$service_path/go.mod"
    
    if [ ! -f "$go_mod" ]; then
        echo "⚠️  $service: go.mod not found, skipping"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi
    
    TOTAL=$((TOTAL + 1))
    
    if grep -q "github.com/oapi-codegen/runtime" "$go_mod"; then
        echo "✅ $service: dependency already exists"
        SKIPPED=$((SKIPPED + 1))
    else
        echo "➕ $service: adding dependency..."
        cd "$service_path" || exit 1
        go get github.com/oapi-codegen/runtime@v1.1.2
        go mod tidy
        cd - > /dev/null || exit 1
        ADDED=$((ADDED + 1))
        echo "   ✅ Added"
    fi
    echo ""
done

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 Results:"
echo "  Total services: $TOTAL"
echo "  ✅ Already had dependency: $SKIPPED"
echo "  ➕ Added dependency: $ADDED"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ $ADDED -gt 0 ]; then
    echo "✅ Successfully added dependency to $ADDED service(s)!"
fi

exit 0

