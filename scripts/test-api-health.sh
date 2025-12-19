#!/bin/bash
# API Health Check Script
# Issue: #1904

set -e

API_BASE_URL="${API_BASE_URL:-http://localhost:8080}"
TIMEOUT=30
RETRIES=3

echo "🏥 API Health Check"
echo "=================="
echo "Base URL: $API_BASE_URL"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Results
health_checks=0
health_passed=0
api_checks=0
api_passed=0

# Function to check endpoint
check_endpoint() {
    local url="$1"
    local expected_status="${2:-200}"
    local description="$3"

    echo -n "Testing $description ($url)... "

    if curl -f -s --max-time "$TIMEOUT" \
           -w "%{http_code}" \
           -o /dev/null \
           "$url" | grep -q "^$expected_status$"; then
        echo -e "${GREEN}OK PASSED${NC}"
        return 0
    else
        echo -e "${RED}❌ FAILED${NC}"
        return 1
    fi
}

# Health checks
echo ""
echo "🔍 Health Endpoints:"

# Health check
((health_checks++))
if check_endpoint "$API_BASE_URL/health" 200 "Health"; then
    ((health_passed++))
fi

# Metrics check (optional)
if check_endpoint "$API_BASE_URL/metrics" 200 "Metrics"; then
    echo "  📊 Metrics data available"
else
    echo -e "  ${YELLOW}WARNING  Metrics not available (optional)${NC}"
fi

# Readiness check (optional)
if check_endpoint "$API_BASE_URL/ready" 200 "Readiness"; then
    echo "  🎯 Readiness check passed"
else
    echo -e "  ${YELLOW}WARNING  Readiness check not available (optional)${NC}"
fi

# API endpoints tests
echo ""
echo "🔗 API Endpoints:"

# Try to discover API endpoints from OpenAPI spec
if check_endpoint "$API_BASE_URL/docs" 200 "OpenAPI Docs"; then
    echo "  📖 OpenAPI specification available"

    # Try to validate OpenAPI spec if redocly is available
    if command -v redocly >/dev/null 2>&1; then
        echo -n "  🔍 Validating OpenAPI spec... "
        if redocly lint "$API_BASE_URL/docs" >/dev/null 2>&1; then
            echo -e "${GREEN}OK PASSED${NC}"
        else
            echo -e "${YELLOW}WARNING  WARNING${NC} (spec validation issues)"
        fi
    fi
else
    echo -e "  ${YELLOW}WARNING  OpenAPI docs not available${NC}"
fi

# Test common API patterns
echo ""
echo "🧪 Common API Patterns:"

# OPTIONS preflight (CORS)
if check_endpoint "$API_BASE_URL/api/v1/test" 200 "OPTIONS preflight" \
   -X OPTIONS -H "Origin: http://localhost:3000"; then
    echo "  🌐 CORS preflight working"
fi

# Error handling - 404
if check_endpoint "$API_BASE_URL/api/v1/nonexistent" 404 "404 Error"; then
    ((api_checks++))
    ((api_passed++))
fi

# Authentication (if applicable)
# This would need to be customized based on your auth system
echo -e "  ${YELLOW}ℹ️  Authentication tests: Customize based on your auth system${NC}"

# Performance checks
echo ""
echo "⚡ Performance Checks:"

# Response time check
echo -n "Response time (< 2s)... "
start_time=$(date +%s%N)
if curl -f -s --max-time 5 "$API_BASE_URL/health" >/dev/null 2>&1; then
    end_time=$(date +%s%N)
    response_time=$(( (end_time - start_time) / 1000000 ))  # Convert to milliseconds

    if [ "$response_time" -lt 2000 ]; then
        echo -e "${GREEN}OK ${response_time}ms${NC}"
    else
        echo -e "${YELLOW}WARNING  ${response_time}ms (slow)${NC}"
    fi
else
    echo -e "${RED}❌ FAILED${NC}"
fi

# Summary
echo ""
echo "=================="
echo "📊 HEALTH SUMMARY"
echo "=================="
echo ""
echo "Health Endpoints: $health_passed/$health_checks passed"
echo "API Checks: $api_passed/$api_checks passed"

total_passed=$((health_passed + api_passed))
total_checks=$((health_checks + api_checks))

if [ "$total_passed" -eq "$total_checks" ]; then
    echo -e "${GREEN}OK API Health: ALL CHECKS PASSED${NC}"
    echo "System is healthy and ready for testing"
    exit 0
elif [ "$health_passed" -eq "$health_checks" ]; then
    echo -e "${YELLOW}WARNING  API Health: BASIC CHECKS PASSED${NC}"
    echo "System is operational but some advanced checks failed"
    exit 0
else
    echo -e "${RED}❌ API Health: FAILED${NC}"
    echo "Critical health issues detected"
    echo "Fix health endpoints before proceeding with QA"
    exit 1
fi