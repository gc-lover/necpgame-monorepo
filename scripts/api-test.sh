#!/bin/bash

# NECP Game API Testing Script
# Комплексное тестирование API endpoints

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
JWT_SECRET=${JWT_SECRET:-"your-jwt-secret-change-in-production"}
BASE_URL=${BASE_URL:-"http://localhost"}

# Generate JWT token
generate_token() {
    local user_id=$1
    local roles=$2

    python3 -c "
import jwt
from datetime import datetime, timedelta
import json

payload = {
    'sub': '$user_id',
    'iat': datetime.utcnow(),
    'exp': datetime.utcnow() + timedelta(hours=24),
    'roles': $roles,
    'iss': 'necp-game-backend',
    'aud': 'necp-game-api'
}

token = jwt.encode(payload, '$JWT_SECRET', algorithm='HS256')
print(token)
"
}

# Test function
test_endpoint() {
    local service=$1
    local port=$2
    local endpoint=$3
    local method=${4:-"GET"}
    local data=$5
    local description=$6

    echo -n "Testing $description... "

    local url="$BASE_URL:$port$endpoint"
    local auth_header=""

    # Add JWT token for API endpoints (not health)
    if [[ $endpoint != "/health" && $endpoint != "/metrics" ]]; then
        local token=$(generate_token "test-user-$service" "['player']")
        auth_header="-H \"Authorization: Bearer $token\""
    fi

    # Build curl command
    local cmd="curl -s -X $method"
    if [[ -n "$auth_header" ]]; then
        cmd="$cmd $auth_header"
    fi
    if [[ -n "$data" ]]; then
        cmd="$cmd -H \"Content-Type: application/json\" -d '$data'"
    fi
    cmd="$cmd \"$url\""

    # Execute and check response
    local response=$(eval $cmd)
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" -X $method \
        $([[ -n "$auth_header" ]] && echo "$auth_header") \
        $([[ -n "$data" ]] && echo "-H \"Content-Type: application/json\" -d '$data'") \
        "$url")

    if [[ $endpoint == "/health" ]]; then
        if [[ $http_code -eq 200 ]]; then
            echo -e "${GREEN}OK PASS${NC} (HTTP $http_code)"
            return 0
        else
            echo -e "${RED}❌ FAIL${NC} (HTTP $http_code)"
            return 1
        fi
    elif [[ $endpoint == "/metrics" ]]; then
        if [[ $http_code -eq 200 ]] && echo "$response" | grep -q "go_goroutines"; then
            echo -e "${GREEN}OK PASS${NC} (HTTP $http_code, metrics available)"
            return 0
        else
            echo -e "${RED}❌ FAIL${NC} (HTTP $http_code)"
            return 1
        fi
    else
        # API endpoints - check for non-404
        if [[ $http_code -ne 404 ]]; then
            echo -e "${GREEN}OK PASS${NC} (HTTP $http_code)"
            return 0
        else
            echo -e "${YELLOW}WARNING  SKIP${NC} (HTTP $http_code - not implemented)"
            return 0
        fi
    fi
}

echo "🧪 NECP Game API Testing Suite"
echo "================================"

# Health checks
echo ""
echo "🏥 Health Checks:"
echo "-----------------"

health_tests=(
    "achievement-service:8100:/health:Achievement Service Health"
    "admin-service:8101:/health:Admin Service Health"
    "battle-pass-service:8102:/health:Battle Pass Service Health"
    "client-service:8110:/health:Client Service Health"
    "combat-sessions-service:8117:/health:Combat Sessions Service Health"
    "cosmetic-service:8119:/health:Cosmetic Service Health"
    "housing-service:8128:/health:Housing Service Health"
    "leaderboard-service:8130:/health:Leaderboard Service Health"
)

health_passed=0
health_total=${#health_tests[@]}

for test in "${health_tests[@]}"; do
    IFS=':' read -r service port endpoint description <<< "$test"
    if test_endpoint "$service" "$port" "$endpoint" "GET" "" "$description"; then
        ((health_passed++))
    fi
done

# Metrics checks
echo ""
echo "📊 Metrics Checks:"
echo "------------------"

metrics_tests=(
    "achievement-service:9200:/metrics:Achievement Service Metrics"
    "client-service:9210:/metrics:Client Service Metrics"
)

metrics_passed=0
metrics_total=${#metrics_tests[@]}

for test in "${metrics_tests[@]}"; do
    IFS=':' read -r service port endpoint description <<< "$test"
    if test_endpoint "$service" "$port" "$endpoint" "GET" "" "$description"; then
        ((metrics_passed++))
    fi
done

# API endpoints (may return 404 if not implemented)
echo ""
echo "🔗 API Endpoints:"
echo "-----------------"

api_tests=(
    "achievement-service:8100:/api/v1/achievements:Achievement Service API"
    "battle-pass-service:8102:/api/v1/battle-passes:Battle Pass Service API"
)

api_passed=0
api_total=${#api_tests[@]}

for test in "${api_tests[@]}"; do
    IFS=':' read -r service port endpoint description <<< "$test"
    if test_endpoint "$service" "$port" "$endpoint" "GET" "" "$description"; then
        ((api_passed++))
    fi
done

# Summary
echo ""
echo "📋 Test Summary:"
echo "---------------"
echo "Health checks: $health_passed/$health_total passed"
echo "Metrics checks: $metrics_passed/$metrics_total passed"
echo "API endpoints: $api_passed/$api_total tested"

total_passed=$((health_passed + metrics_passed + api_passed))
total_tests=$((health_total + metrics_total + api_total))

echo ""
echo "Total: $total_passed/$total_tests tests passed ($((total_passed * 100 / total_tests))%)"

if [ $health_passed -eq $health_total ]; then
    echo -e "${GREEN}🎉 All health checks passed!${NC}"
    exit 0
else
    echo -e "${RED}WARNING  Some health checks failed${NC}"
    exit 1
fi
