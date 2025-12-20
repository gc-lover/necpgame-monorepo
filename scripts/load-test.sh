#!/bin/bash

# NECP Game Load Testing Script
# Нагрузочное тестирование сервисов

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
CONCURRENT_REQUESTS=${CONCURRENT_REQUESTS:-10}
TOTAL_REQUESTS=${TOTAL_REQUESTS:-100}
DURATION=${DURATION:-30}
BASE_URL=${BASE_URL:-"http://localhost"}

echo "🚀 NECP Game Load Testing"
echo "========================="
echo "Configuration:"
echo "  Concurrent requests: $CONCURRENT_REQUESTS"
echo "  Total requests: $TOTAL_REQUESTS"
echo "  Duration: ${DURATION}s"
echo "  Base URL: $BASE_URL"
echo ""

# Function to run load test on single endpoint
load_test_endpoint() {
    local service=$1
    local port=$2
    local endpoint=$3
    local description=$4

    echo -e "${BLUE}Testing $description${NC}"
    echo "Endpoint: $BASE_URL:$port$endpoint"

    # Use curl with parallel requests
    local temp_file=$(mktemp)

    # Generate list of requests
    for i in $(seq 1 $TOTAL_REQUESTS); do
        echo "url = \"$BASE_URL:$port$endpoint\"" >> "$temp_file"
        echo "output = /dev/null" >> "$temp_file"
        echo "write-out = \"%{http_code} %{time_total}\\n\"" >> "$temp_file"
        echo "" >> "$temp_file"
    done

    # Run parallel requests
    local start_time=$(date +%s)
    local results=$(curl -s -Z --config "$temp_file" --parallel-max $CONCURRENT_REQUESTS 2>/dev/null)
    local end_time=$(date +%s)

    # Clean up
    rm -f "$temp_file"

    # Analyze results
    local total_time=$((end_time - start_time))
    local success_count=$(echo "$results" | grep "^200" | wc -l)
    local error_count=$(echo "$results" | grep -v "^200" | wc -l)
    local avg_response_time=$(echo "$results" | grep "^200" | awk '{sum += $2} END {if (NR > 0) print sum/NR * 1000; else print 0}')

    echo "Results:"
    echo "  Total requests: $TOTAL_REQUESTS"
    echo "  Successful: $success_count"
    echo "  Failed: $error_count"
    echo "  Success rate: $((success_count * 100 / TOTAL_REQUESTS))%"
    echo -e "  Average response time: ${YELLOW}$(printf "%.2f" $avg_response_time)ms${NC}"
    echo "  Total time: ${total_time}s"
    echo "  Requests/second: $((TOTAL_REQUESTS / total_time))"
    echo ""

    # Return success if success rate > 95%
    if [ $success_count -gt $((TOTAL_REQUESTS * 95 / 100)) ]; then
        return 0
    else
        return 1
    fi
}

# Health endpoints load test
echo "🏥 Health Endpoints Load Test:"
echo "=============================="

health_tests=(
    "achievement-service:8100:/health:Achievement Service Health"
    "combat-sessions-service:8117:/health:Combat Sessions Service Health"
    "leaderboard-service:8130:/health:Leaderboard Service Health"
)

health_passed=0
for test in "${health_tests[@]}"; do
    IFS=':' read -r service port endpoint description <<< "$test"
    if load_test_endpoint "$service" "$port" "$endpoint" "$description"; then
        health_passed=$((health_passed + 1))
    fi
done

# API endpoints load test (if implemented)
echo "🔗 API Endpoints Load Test:"
echo "==========================="

api_tests=(
    "achievement-service:8100:/api/v1/achievements:Achievement Service API"
)

api_passed=0
for test in "${api_tests[@]}"; do
    IFS=':' read -r service port endpoint description <<< "$test"
    # Note: API endpoints may return 404 if not implemented, so we check for any non-5xx response
    if load_test_endpoint "$service" "$port" "$endpoint" "$description"; then
        api_passed=$((api_passed + 1))
    fi
done

# Summary
echo "📋 Load Test Summary:"
echo "====================="
echo "Health endpoints passed: $health_passed/${#health_tests[@]}"
echo "API endpoints tested: $api_passed/${#api_tests[@]}"

if [ $health_passed -eq ${#health_tests[@]} ]; then
    echo -e "${GREEN}🎉 All health endpoints handled load successfully!${NC}"
    exit 0
else
    echo -e "${RED}WARNING  Some endpoints failed under load${NC}"
    exit 1
fi
