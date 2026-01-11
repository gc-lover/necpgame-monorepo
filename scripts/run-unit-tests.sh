#!/bin/bash

# Run Unit Tests for All Go Services
# This script runs unit tests for all Go microservices in the project

set -e

echo "========================================"
echo "NECPGAME UNIT TESTS"
echo "========================================"
echo "Running unit tests for all Go services..."
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Find all Go service directories
SERVICES_DIR="services"
if [ ! -d "$SERVICES_DIR" ]; then
    echo -e "${RED}ERROR: Services directory not found: $SERVICES_DIR${NC}"
    exit 1
fi

# Counters
TOTAL_SERVICES=0
PASSED_SERVICES=0
FAILED_SERVICES=0
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Function to run tests for a service
run_service_tests() {
    local service_dir=$1
    local service_name=$(basename "$service_dir")

    echo -e "${BLUE}Testing service: $service_name${NC}"

    # Check if go.mod exists
    if [ ! -f "$service_dir/go.mod" ]; then
        echo -e "${YELLOW}⚠️  Skipping $service_name - no go.mod found${NC}"
        return 0
    fi

    # Change to service directory
    cd "$service_dir"

    # Run unit tests with coverage
    if go test -tags=unit -v -coverprofile=coverage.out ./internal/... 2>/dev/null; then
        echo -e "${GREEN}✅ $service_name tests passed${NC}"

        # Parse test results if available
        if [ -f "coverage.out" ]; then
            COVERAGE=$(go tool cover -func=coverage.out | grep total | awk '{print $3}')
            echo -e "${GREEN}   Coverage: $COVERAGE${NC}"
        fi

        ((PASSED_SERVICES++))
        return 0
    else
        echo -e "${RED}❌ $service_name tests failed${NC}"
        ((FAILED_SERVICES++))
        return 1
    fi
}

# Find and test all Go services
echo "Discovering Go services..."
for service_dir in "$SERVICES_DIR"/*-go; do
    if [ -d "$service_dir" ]; then
        ((TOTAL_SERVICES++))
        if run_service_tests "$service_dir"; then
            # Count individual tests if possible
            cd "$service_dir"
            if [ -f "coverage.out" ]; then
                # Simple way to count test functions (not perfect but better than nothing)
                TEST_COUNT=$(grep -c "func Test" internal/**/*.go 2>/dev/null || echo "0")
                ((TOTAL_TESTS += TEST_COUNT))
                ((PASSED_TESTS += TEST_COUNT))
            fi
        fi
        # Return to project root
        cd ../..
    fi
done

echo ""
echo "========================================"
echo "UNIT TESTS SUMMARY"
echo "========================================"
echo "Services tested: $TOTAL_SERVICES"
echo "Services passed: $PASSED_SERVICES"
echo "Services failed: $FAILED_SERVICES"

if [ $TOTAL_TESTS -gt 0 ]; then
    echo "Individual tests: $TOTAL_TESTS"
    echo "Tests passed: $PASSED_TESTS"
    echo "Tests failed: $FAILED_TESTS"
fi

SUCCESS_RATE=$(( PASSED_SERVICES * 100 / TOTAL_SERVICES ))
echo "Success rate: $SUCCESS_RATE%"

echo ""
if [ $FAILED_SERVICES -eq 0 ]; then
    echo -e "${GREEN}🎉 ALL UNIT TESTS PASSED!${NC}"
    echo ""
    echo "Services are ready for integration testing."
    exit 0
else
    echo -e "${RED}❌ SOME UNIT TESTS FAILED!${NC}"
    echo ""
    echo "Please fix the failing tests before proceeding."
    exit 1
fi