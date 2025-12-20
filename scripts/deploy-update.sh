#!/bin/bash

# NECP Game Services Deployment Script
# Автоматическое обновление и развертывание сервисов

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
SERVICES_FILE=${SERVICES_FILE:-"docker-compose.yml"}
BACKUP_BEFORE_UPDATE=${BACKUP_BEFORE_UPDATE:-true}
ROLLBACK_ON_FAILURE=${ROLLBACK_ON_FAILURE:-true}
HEALTH_CHECK_TIMEOUT=${HEALTH_CHECK_TIMEOUT:-60}

echo "🚀 NECP Game Services Deployment"
echo "================================"

# Function to backup current state
backup_current_state() {
    echo -e "${BLUE}💾 Creating backup before update...${NC}"

    local backup_dir="backup_$(date +%Y%m%d_%H%M%S)"
    mkdir -p "$backup_dir"

    # Backup docker-compose files
    cp docker-compose.yml "$backup_dir/" 2>/dev/null || true
    cp docker-compose.monitoring.yml "$backup_dir/" 2>/dev/null || true

    # Backup images (optional, can be large)
    echo "Current running containers:"
    docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}" | head -10

    echo "$backup_dir" > ".last_backup"
    echo -e "${GREEN}OK Backup created: $backup_dir${NC}"
}

# Function to check service health
check_service_health() {
    local service=$1
    local port=$2
    local timeout=${3:-30}

    echo -n "Checking $service health... "

    local start_time=$(date +%s)
    while [ $(($(date +%s) - start_time)) -lt $timeout ]; do
        if curl -s --max-time 5 "http://localhost:$port/health" > /dev/null 2>&1; then
            echo -e "${GREEN}OK OK${NC}"
            return 0
        fi
        sleep 2
    done

    echo -e "${RED}❌ FAILED${NC}"
    return 1
}

# Function to update single service
update_service() {
    local service=$1

    echo -e "${BLUE}🔄 Updating $service...${NC}"

    # Stop service
    docker-compose stop "$service"

    # Remove old container
    docker-compose rm -f "$service"

    # Pull new image if using external images
    docker-compose pull "$service" 2>/dev/null || true

    # Rebuild and start
    docker-compose up -d --build "$service"

    echo -e "${GREEN}OK $service updated${NC}"
}

# Function to rollback
rollback_deployment() {
    echo -e "${RED}🔄 Rolling back deployment...${NC}"

    if [ -f ".last_backup" ]; then
        local backup_dir=$(cat .last_backup)

        if [ -d "$backup_dir" ]; then
            echo "Restoring from backup: $backup_dir"

            # Restore docker-compose files
            cp "$backup_dir/docker-compose.yml" . 2>/dev/null || true
            cp "$backup_dir/docker-compose.monitoring.yml" . 2>/dev/null || true

            # Restart all services
            docker-compose down
            docker-compose up -d

            echo -e "${GREEN}OK Rollback completed${NC}"
            return 0
        fi
    fi

    echo -e "${RED}❌ No backup found for rollback${NC}"
    return 1
}

# Function to validate deployment
validate_deployment() {
    echo -e "${BLUE}🔍 Validating deployment...${NC}"

    local failed_services=()

    # Check all services
    local services=(
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

    for service_info in "${services[@]}"; do
        IFS=':' read -r service port <<< "$service_info"
        if ! check_service_health "$service" "$port" 10; then
            failed_services+=("$service")
        fi
    done

    if [ ${#failed_services[@]} -eq 0 ]; then
        echo -e "${GREEN}🎉 All services are healthy!${NC}"
        return 0
    else
        echo -e "${RED}❌ Failed services: ${failed_services[*]}${NC}"
        return 1
    fi
}

# Main deployment process
main() {
    local target_services=("$@")

    # Backup if requested
    if [ "$BACKUP_BEFORE_UPDATE" = true ]; then
        backup_current_state
    fi

    # Update services
    local failed_updates=()

    if [ ${#target_services[@]} -eq 0 ]; then
        echo "No specific services provided, updating all..."
        docker-compose pull
        docker-compose up -d --build
    else
        for service in "${target_services[@]}"; do
            if ! update_service "$service"; then
                failed_updates+=("$service")
            fi
        done
    fi

    # Validate
    echo ""
    if validate_deployment; then
        echo -e "${GREEN}🎉 Deployment successful!${NC}"

        # Cleanup old backups if successful
        if [ -f ".last_backup" ] && [ "$BACKUP_BEFORE_UPDATE" = true ]; then
            local backup_dir=$(cat .last_backup)
            echo "Keeping backup: $backup_dir"
        fi

    else
        echo -e "${RED}❌ Deployment validation failed!${NC}"

        if [ "$ROLLBACK_ON_FAILURE" = true ]; then
            rollback_deployment
        fi

        exit 1
    fi
}

# Parse arguments
if [ $# -eq 0 ]; then
    echo "Usage: $0 [service1 service2 ...]"
    echo "If no services specified, updates all services"
    echo ""
    echo "Environment variables:"
    echo "  BACKUP_BEFORE_UPDATE=true/false (default: true)"
    echo "  ROLLBACK_ON_FAILURE=true/false (default: true)"
    echo "  HEALTH_CHECK_TIMEOUT=seconds (default: 60)"
    exit 1
fi

main "$@"
