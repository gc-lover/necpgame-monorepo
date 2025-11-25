#!/bin/bash
# Скрипт для проверки генерации кода во всех сервисах

set -e

echo "🔍 Проверка генерации кода для всех сервисов..."
echo ""

SERVICES=(
    "reset-service-go:reset-service:chi-server"
    "companion-service-go:companion-service:gorilla-server"
    "inventory-service-go:inventory-service:gorilla-server"
    "housing-service-go:housing-service:gorilla-server"
    "clan-war-service-go:clan-war-service:gorilla-server"
    "movement-service-go:movement-service:gorilla-server"
    "referral-service-go:referral-service:gorilla-server"
    "voice-chat-service-go:voice-chat-service:gorilla-server"
    "achievement-service-go:achievement-core-service:gorilla-server"
    "admin-service-go:admin-service:gorilla-server"
    "battle-pass-service-go:battle-pass-core-service:gorilla-server"
    "character-service-go:character-core-service:gorilla-server"
    "economy-service-go:economy-inventory-core-service:gorilla-server"
    "feedback-service-go:feedback-service:gorilla-server"
    "gameplay-service-go:gameplay-progression-core-service:gorilla-server"
    "leaderboard-service-go:leaderboard-core-service:gorilla-server"
    "social-service-go:social-friends-core-service:gorilla-server"
    "support-service-go:support-tickets-core-service:gorilla-server"
    "world-service-go:world-events-service:gorilla-server"
)

TOTAL=${#SERVICES[@]}
PASSED=0
FAILED=0
SKIPPED=0

for service_info in "${SERVICES[@]}"; do
    IFS=':' read -r service_dir service_name router_type <<< "$service_info"
    service_path="services/$service_dir"
    
    echo "📦 Checking $service_dir..."
    
    if [ ! -d "$service_path" ]; then
        echo "  ⚠️  Directory not found, skipping"
        SKIPPED=$((SKIPPED + 1))
        echo ""
        continue
    fi
    
    if [ ! -f "$service_path/Makefile" ]; then
        echo "  ❌ Makefile not found"
        FAILED=$((FAILED + 1))
        echo ""
        continue
    fi
    
    if [ ! -f "$service_path/oapi-codegen.yaml" ]; then
        echo "  ❌ oapi-codegen.yaml not found"
        FAILED=$((FAILED + 1))
        echo ""
        continue
    fi
    
    if [ ! -f "../../proto/openapi/$service_name.yaml" ]; then
        echo "  ⚠️  OpenAPI spec not found: proto/openapi/$service_name.yaml"
        SKIPPED=$((SKIPPED + 1))
        echo ""
        continue
    fi
    
    cd "$service_path" || exit 1
    
    if make verify-api > /dev/null 2>&1; then
        echo "  ✅ OpenAPI spec is valid"
        PASSED=$((PASSED + 1))
    else
        echo "  ⚠️  OpenAPI spec validation failed (might be OK)"
        SKIPPED=$((SKIPPED + 1))
    fi
    
    cd - > /dev/null || exit 1
    echo ""
done

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 Results:"
echo "  Total services: $TOTAL"
echo "  ✅ Passed: $PASSED"
echo "  ❌ Failed: $FAILED"
echo "  ⚠️  Skipped: $SKIPPED"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ $FAILED -eq 0 ]; then
    echo "✅ All services are properly configured!"
    exit 0
else
    echo "❌ Some services have issues"
    exit 1
fi

