#!/bin/bash

# Ogen Service Performance Benchmarks
# Issue: #143576311 - [QA] ogen Migration: Comprehensive Testing Strategy & Performance Validation Framework

set -e

SERVICE_NAME=${1:-"support-sla"}
BASE_URL=${2:-"http://localhost:8080"}
OUTPUT_DIR=${3:-"scripts/testing/performance-validation/results"}

echo "========================================"
echo "OGEN SERVICE PERFORMANCE BENCHMARKS"
echo "========================================"
echo "Service: $SERVICE_NAME"
echo "Base URL: $BASE_URL"
echo "Output Dir: $OUTPUT_DIR"
echo "Timestamp: $(date)"
echo ""

# Create output directory
mkdir -p "$OUTPUT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if service is running
echo "Checking if service is running..."
if ! curl -f -s "$BASE_URL/api/v1/sla/health" > /dev/null 2>&1; then
    echo -e "${RED}ERROR: Service is not running or not accessible at $BASE_URL${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Service is running${NC}"
echo ""

# Run performance validation
echo "Running performance validation..."
VALIDATION_OUTPUT="$OUTPUT_DIR/performance-validation-$(date +%Y%m%d-%H%M%S).txt"

python3 scripts/testing/performance-validation/performance-validator.py \
    --url "$BASE_URL" \
    --service "$SERVICE_NAME" \
    > "$VALIDATION_OUTPUT" 2>&1

VALIDATION_EXIT_CODE=$?

# Display results
echo ""
echo "PERFORMANCE VALIDATION RESULTS:"
echo "========================================"
cat "$VALIDATION_OUTPUT"

echo ""
echo "Detailed results saved to: $VALIDATION_OUTPUT"

# Check validation results
if [ $VALIDATION_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ All performance targets met!${NC}"

    # Run additional benchmarks
    echo ""
    echo "Running additional benchmarks..."
    run_additional_benchmarks "$SERVICE_NAME" "$BASE_URL" "$OUTPUT_DIR"
else
    echo -e "${RED}✗ Performance validation failed!${NC}"
    echo "Check the detailed results above for specific issues."
fi

exit $VALIDATION_EXIT_CODE

function run_additional_benchmarks() {
    local service=$1
    local base_url=$2
    local output_dir=$3

    echo "Memory usage benchmark..."
    memory_output="$output_dir/memory-benchmark-$(date +%Y%m%d-%H%M%S).txt"

    # Simple memory monitoring (requires pidof or similar)
    if command -v pidof > /dev/null; then
        SERVICE_PID=$(pidof "$SERVICE_NAME-service-go" | head -1)
        if [ -n "$SERVICE_PID" ]; then
            echo "Monitoring memory usage for PID: $SERVICE_PID"
            ps -p "$SERVICE_PID" -o pid,ppid,cmd,%mem,%cpu --headers > "$memory_output"
            echo "Memory usage saved to: $memory_output"
        else
            echo "Could not find service process PID"
        fi
    fi

    echo "Load testing with hey..."
    if command -v hey > /dev/null; then
        load_output="$output_dir/load-test-$(date +%Y%m%d-%H%M%S).txt"

        echo "Warm-up phase (100 requests)..."
        hey -n 100 -c 5 "$base_url/api/v1/sla/health" > /dev/null 2>&1

        echo "Load test phase (1000 requests, 20 concurrent)..."
        hey -n 1000 -c 20 "$base_url/api/v1/sla/health" > "$load_output"

        echo "Load test results saved to: $load_output"
        echo ""
        echo "LOAD TEST SUMMARY:"
        tail -10 "$load_output"
    else
        echo "hey tool not found. Install with: go install github.com/rakyll/hey@latest"
    fi

    echo "Database connection benchmark..."
    db_output="$output_dir/db-connections-$(date +%Y%m%d-%H%M%S).txt"

    # Test database connectivity through API
    echo "Testing database connections through health endpoint..."
    for i in {1..10}; do
        start=$(date +%s%N)
        curl -s "$base_url/api/v1/sla/health" > /dev/null
        end=$(date +%s%N)
        duration=$(( (end - start) / 1000000 ))  # Convert to milliseconds
        echo "Request $i: ${duration}ms" >> "$db_output"
    done

    echo "Database connection tests saved to: $db_output"
    echo ""
    echo "DATABASE CONNECTION SUMMARY:"
    awk '{sum+=$2; count++} END {print "Average:", sum/count, "ms"}' "$db_output"
}

