#!/bin/bash
# Backend Test Execution Script
# Issue: #1904

set -e

echo "🧪 Running Backend Tests..."
echo "============================"

SERVICES_DIR="services"
RESULTS_DIR="test-results"
mkdir -p "$RESULTS_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test results
declare -A test_results
total_passed=0
total_failed=0

# Find all Go services
find_services() {
    find "$SERVICES_DIR" -name "*-go" -type d | sort
}

# Run tests for a single service
run_service_tests() {
    local service_path="$1"
    local service_name=$(basename "$service_path")

    echo ""
    echo "Testing service: $service_name"
    echo "Path: $service_path"

    cd "$service_path"

    # Run tests with coverage
    if go test ./... -v -race -coverprofile=coverage.out -json > test-results.json 2>&1; then
        echo -e "${GREEN}OK Tests passed${NC}"

        # Calculate coverage
        coverage=$(go tool cover -func=coverage.out | grep "total:" | awk '{print $3}' | sed 's/%//')
        echo "Coverage: ${coverage}%"

        # Check coverage threshold
        if (( $(echo "$coverage < 80" | bc -l) )); then
            echo -e "${YELLOW}WARNING  Coverage below 80% threshold${NC}"
            test_results["$service_name"]="coverage_low"
        else
            test_results["$service_name"]="passed"
            ((total_passed++))
        fi
    else
        echo -e "${RED}❌ Tests failed${NC}"
        test_results["$service_name"]="failed"
        ((total_failed++))

        # Show first few failures
        echo "Recent failures:"
        tail -20 test-results.json | jq -r 'select(.Action == "fail") | "\(.Test): \(.Output)"' 2>/dev/null || echo "Failed to parse test output"
    fi

    # Save results
    cp test-results.json "$RESULTS_DIR/${service_name}-results.json" 2>/dev/null || true
    cp coverage.out "$RESULTS_DIR/${service_name}-coverage.out" 2>/dev/null || true

    cd - >/dev/null
}

# Main execution
echo "Found services:"
find_services | while read service; do
    echo "  $(basename "$service")"
done

echo ""
echo "Starting test execution..."

for service in $(find_services); do
    run_service_tests "$service"
done

# Summary
echo ""
echo "============================"
echo "📊 TEST SUMMARY"
echo "============================"
echo ""
echo "Services tested: $(($(find_services | wc -l)))"
echo -e "Passed: ${GREEN}$total_passed${NC}"
echo -e "Failed: ${RED}$total_failed${NC}"

# Detailed results
echo ""
echo "Detailed Results:"
for service in "${!test_results[@]}"; do
    status="${test_results[$service]}"
    case "$status" in
        "passed")
            echo -e "  ${GREEN}OK $service: PASSED${NC}"
            ;;
        "coverage_low")
            echo -e "  ${YELLOW}WARNING  $service: PASSED (low coverage)${NC}"
            ;;
        "failed")
            echo -e "  ${RED}❌ $service: FAILED${NC}"
            ;;
    esac
done

# Overall result
echo ""
if [ "$total_failed" -gt 0 ]; then
    echo -e "${RED}❌ Backend tests: FAILED${NC}"
    echo "Fix failing tests before proceeding"
    exit 1
elif [ "$total_passed" -eq 0 ]; then
    echo -e "${YELLOW}WARNING  No services tested${NC}"
    exit 1
else
    echo -e "${GREEN}OK Backend tests: PASSED${NC}"
    echo "All backend services passed testing"
    exit 0
fi