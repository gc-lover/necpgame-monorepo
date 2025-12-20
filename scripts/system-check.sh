#!/bin/bash

# NECP Game Services System Check Script
# Проверяет состояние всех сервисов и инфраструктуры

set -e

echo "🔍 NECP Game Services - System Health Check"
echo "=========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to check service health
check_service() {
    local service=$1
    local port=$2
    local endpoint=${3:-/health}

    if curl -s --max-time 5 "http://localhost:$port$endpoint" > /dev/null 2>&1; then
        echo -e "${GREEN}OK $service (port $port)${NC}"
        return 0
    else
        echo -e "${RED}❌ $service (port $port)${NC}"
        return 1
    fi
}

# Function to check Docker container status
check_container() {
    local container=$1
    local status=$(docker ps --filter "name=$container" --format "{{.Status}}" | head -1)

    if [[ $status == *"Up"* ]]; then
        echo -e "${GREEN}OK $container: $status${NC}"
        return 0
    else
        echo -e "${RED}❌ $container: $status${NC}"
        return 1
    fi
}

echo ""
echo "🐳 Docker Infrastructure Status:"
echo "--------------------------------"

# Check infrastructure
check_container "necpgame-postgres"
check_container "necpgame-redis"
check_container "necpgame-keycloak"

echo ""
echo "🎮 Application Services Health:"
echo "-------------------------------"

# Check application services
services=(
    "achievement-service:8100"
    "admin-service:8101"
    "battle-pass-service:8102"
    "character-engram-compatibility-service:8103"
    "character-engram-core-service:8104"
    "client-service:8110"
    "combat-damage-service:8127"
    "combat-hacking-service:8128"
    "combat-sessions-service:8117"
    "cosmetic-service:8119"
    "housing-service:8128"
    "leaderboard-service:8130"
    "progression-experience-service:8135"
    "projectile-core-service:8091"
    "referral-service:8097"
    "reset-service:8144"
    "social-player-orders-service:8097"
    "stock-analytics-tools-service:8155"
    "stock-dividends-service:8156"
    "stock-events-service:8157"
    "stock-futures-service:8158"
    "stock-indices-service:8159"
    "stock-margin-service:8160"
    "stock-options-service:8161"
    "stock-protection-service:8162"
    "support-service:8163"
)

healthy_count=0
total_count=${#services[@]}

for service_info in "${services[@]}"; do
    IFS=':' read -r service port <<< "$service_info"
    if check_service "$service" "$port"; then
        ((healthy_count++))
    fi
done

echo ""
echo "📊 Summary:"
echo "-----------"
echo "Total services: $total_count"
echo "Healthy services: $healthy_count"
echo "Health percentage: $((healthy_count * 100 / total_count))%"

if [ $healthy_count -eq $total_count ]; then
    echo -e "${GREEN}🎉 All services are healthy!${NC}"
    exit 0
else
    echo -e "${RED}WARNING  Some services are unhealthy${NC}"
    exit 1
fi
