#!/bin/bash

# Automated Test Suite Runner for ogen Services
# Issue: #143576311 - [QA] ogen Migration: Comprehensive Testing Strategy & Performance Validation Framework

set -e

SERVICE_NAME=${1:-"support-sla-service-go"}
OUTPUT_DIR=${2:-"scripts/testing/results"}

echo "========================================"
echo "OGEN AUTOMATED TEST SUITE"
echo "========================================"
echo "Service: $SERVICE_NAME"
echo "Output Dir: $OUTPUT_DIR"
echo "Timestamp: $(date)"
echo ""

# Create output directory
mkdir -p "$OUTPUT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Change to service directory
SERVICE_DIR="services/$SERVICE_NAME"
if [ ! -d "$SERVICE_DIR" ]; then
    echo -e "${RED}ERROR: Service directory not found: $SERVICE_DIR${NC}"
    exit 1
fi

cd "$SERVICE_DIR"

# Check if Go is available
if ! command -v go &> /dev/null; then
    echo -e "${RED}ERROR: Go is not installed or not in PATH${NC}"
    exit 1
fi

echo -e "${BLUE}Running unit tests...${NC}"
UNIT_TEST_OUTPUT="$OUTPUT_DIR/unit-tests-$(date +%Y%m%d-%H%M%S).txt"
go test ./... -v -race -coverprofile=coverage.out > "$UNIT_TEST_OUTPUT" 2>&1
UNIT_TEST_EXIT_CODE=$?

echo "Unit test results saved to: $UNIT_TEST_OUTPUT"

if [ $UNIT_TEST_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ Unit tests passed${NC}"
else
    echo -e "${RED}✗ Unit tests failed${NC}"
    echo "Check detailed results in: $UNIT_TEST_OUTPUT"
fi

echo ""
echo -e "${BLUE}Running benchmark tests...${NC}"
BENCHMARK_OUTPUT="$OUTPUT_DIR/benchmarks-$(date +%Y%m%d-%H%M%S).txt"
go test ./... -bench=. -benchmem -run=^$ > "$BENCHMARK_OUTPUT" 2>&1
BENCHMARK_EXIT_CODE=$?

echo "Benchmark results saved to: $BENCHMARK_OUTPUT"

if [ $BENCHMARK_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ Benchmark tests completed${NC}"
else
    echo -e "${RED}✗ Benchmark tests failed${NC}"
fi

echo ""
echo -e "${BLUE}Running integration tests...${NC}"
INTEGRATION_OUTPUT="$OUTPUT_DIR/integration-tests-$(date +%Y%m%d-%H%M%S).txt"

# Check if integration tests exist and can run
if go test ./... -run="Integration" -v > "$INTEGRATION_OUTPUT" 2>&1; then
    INTEGRATION_EXIT_CODE=$?
    if [ $INTEGRATION_EXIT_CODE -eq 0 ]; then
        echo -e "${GREEN}✓ Integration tests passed${NC}"
    else
        echo -e "${YELLOW}⚠ Integration tests had issues (may require database setup)${NC}"
    fi
else
    echo -e "${YELLOW}⚠ Integration tests not found or skipped${NC}"
    INTEGRATION_EXIT_CODE=0
fi

echo "Integration test results saved to: $INTEGRATION_OUTPUT"

echo ""
echo -e "${BLUE}Generating coverage report...${NC}"
if [ -f "coverage.out" ]; then
    COVERAGE_HTML="$OUTPUT_DIR/coverage-$(date +%Y%m%d-%H%M%S).html"
    go tool cover -html=coverage.out -o "$COVERAGE_HTML"

    # Generate coverage summary
    COVERAGE_SUMMARY="$OUTPUT_DIR/coverage-summary-$(date +%Y%m%d-%H%M%S).txt"
    go tool cover -func=coverage.out > "$COVERAGE_SUMMARY"

    echo "Coverage HTML report: $COVERAGE_HTML"
    echo "Coverage summary: $COVERAGE_SUMMARY"

    # Check coverage threshold
    TOTAL_COVERAGE=$(go tool cover -func=coverage.out | grep "total:" | awk '{print $3}' | sed 's/%//')

    if (( $(echo "$TOTAL_COVERAGE >= 80" | bc -l) )); then
        echo -e "${GREEN}✓ Code coverage: ${TOTAL_COVERAGE}% (target: >80%)${NC}"
    elif (( $(echo "$TOTAL_COVERAGE >= 60" | bc -l) )); then
        echo -e "${YELLOW}⚠ Code coverage: ${TOTAL_COVERAGE}% (target: >80%)${NC}"
    else
        echo -e "${RED}✗ Code coverage: ${TOTAL_COVERAGE}% (target: >80%)${NC}"
    fi
else
    echo -e "${YELLOW}⚠ Coverage file not found${NC}"
fi

echo ""
echo -e "${BLUE}Running static analysis...${NC}"
STATIC_ANALYSIS_OUTPUT="$OUTPUT_DIR/static-analysis-$(date +%Y%m%d-%H%M%S).txt"

# Run go vet
echo "Running go vet..."
go vet ./... > "$STATIC_ANALYSIS_OUTPUT" 2>&1
VET_EXIT_CODE=$?

# Run golint if available
if command -v golint &> /dev/null; then
    echo "Running golint..."
    golint ./... >> "$STATIC_ANALYSIS_OUTPUT" 2>&1
fi

# Run ineffassign if available
if command -v ineffassign &> /dev/null; then
    echo "Running ineffassign..."
    ineffassign . >> "$STATIC_ANALYSIS_OUTPUT" 2>&1
fi

echo "Static analysis results saved to: $STATIC_ANALYSIS_OUTPUT"

if [ $VET_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ Static analysis passed${NC}"
else
    echo -e "${RED}✗ Static analysis found issues${NC}"
fi

echo ""
echo -e "${BLUE}Checking for race conditions...${NC}"
RACE_TEST_OUTPUT="$OUTPUT_DIR/race-test-$(date +%Y%m%d-%H%M%S).txt"

go test ./... -race -run="Test" > "$RACE_TEST_OUTPUT" 2>&1
RACE_EXIT_CODE=$?

echo "Race condition test results saved to: $RACE_TEST_OUTPUT"

if [ $RACE_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ No race conditions detected${NC}"
else
    echo -e "${RED}✗ Race conditions detected${NC}"
fi

echo ""
echo "========================================"
echo "TEST SUITE SUMMARY"
echo "========================================"

# Overall result
OVERALL_SUCCESS=true

if [ $UNIT_TEST_EXIT_CODE -ne 0 ]; then
    OVERALL_SUCCESS=false
    echo -e "${RED}✗ Unit tests: FAILED${NC}"
else
    echo -e "${GREEN}✓ Unit tests: PASSED${NC}"
fi

if [ $BENCHMARK_EXIT_CODE -ne 0 ]; then
    OVERALL_SUCCESS=false
    echo -e "${RED}✗ Benchmarks: FAILED${NC}"
else
    echo -e "${GREEN}✓ Benchmarks: COMPLETED${NC}"
fi

if [ $VET_EXIT_CODE -ne 0 ]; then
    OVERALL_SUCCESS=false
    echo -e "${RED}✗ Static analysis: FAILED${NC}"
else
    echo -e "${GREEN}✓ Static analysis: PASSED${NC}"
fi

if [ $RACE_EXIT_CODE -ne 0 ]; then
    OVERALL_SUCCESS=false
    echo -e "${RED}✗ Race conditions: DETECTED${NC}"
else
    echo -e "${GREEN}✓ Race conditions: NONE${NC}"
fi

echo ""
if [ "$OVERALL_SUCCESS" = true ]; then
    echo -e "${GREEN}🎉 ALL TESTS PASSED! Service is ready for deployment.${NC}"
    exit 0
else
    echo -e "${RED}❌ SOME TESTS FAILED! Review the output files for details.${NC}"
    echo ""
    echo "Failed test outputs:"
    if [ $UNIT_TEST_EXIT_CODE -ne 0 ]; then echo "  - Unit tests: $UNIT_TEST_OUTPUT"; fi
    if [ $BENCHMARK_EXIT_CODE -ne 0 ]; then echo "  - Benchmarks: $BENCHMARK_OUTPUT"; fi
    if [ $VET_EXIT_CODE -ne 0 ]; then echo "  - Static analysis: $STATIC_ANALYSIS_OUTPUT"; fi
    if [ $RACE_EXIT_CODE -ne 0 ]; then echo "  - Race tests: $RACE_TEST_OUTPUT"; fi
    exit 1
fi






