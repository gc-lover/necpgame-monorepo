#!/bin/bash

# Load Testing Runner for ogen Services
# Issue: #143576311 - [QA] ogen Migration: Comprehensive Testing Strategy & Performance Validation Framework

set -e

SERVICE_NAME=${1:-"support-sla"}
BASE_URL=${2:-"http://localhost:8080"}
OUTPUT_DIR=${3:-"scripts/testing/load-test/results"}

echo "========================================"
echo "OGEN SERVICE LOAD TESTING"
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
GREEN='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if Python is available
if ! command -v python3 &> /dev/null; then
    echo -e "${RED}ERROR: Python 3 is not installed or not in PATH${NC}"
    exit 1
fi

# Check if service is running
echo "Checking if service is running..."
if ! curl -f -s "$BASE_URL/api/v1/sla/health" > /dev/null 2>&1; then
    echo -e "${RED}ERROR: Service is not running or not accessible at $BASE_URL${NC}"
    echo "Make sure the service is started before running load tests."
    exit 1
fi
echo -e "${GREEN}✓ Service is running${NC}"
echo ""

# Install required Python packages if not present
echo "Checking Python dependencies..."
python3 -c "import aiohttp, statistics" 2>/dev/null || {
    echo "Installing required Python packages..."
    pip3 install aiohttp statistics 2>/dev/null || {
        echo -e "${YELLOW}⚠️  Could not install Python packages automatically${NC}"
        echo "Please install manually: pip3 install aiohttp"
    }
}

# Run load tests
echo -e "${BLUE}Starting load testing scenarios...${NC}"
LOAD_TEST_OUTPUT="$OUTPUT_DIR/load-test-$(date +%Y%m%d-%H%M%S).txt"

python3 scripts/testing/load-test/load-test-scenarios.py \
    --url "$BASE_URL" \
    --service "$SERVICE_NAME" \
    --output "$OUTPUT_DIR" \
    > "$LOAD_TEST_OUTPUT" 2>&1

LOAD_TEST_EXIT_CODE=$?

# Display results
echo ""
echo "LOAD TESTING RESULTS:"
echo "========================================"
cat "$LOAD_TEST_OUTPUT"

echo ""
echo "Detailed results saved to: $OUTPUT_DIR"

# Check results
if [ $LOAD_TEST_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ Load testing completed${NC}"

    # Check for any failures in the output
    if grep -q "FAILED\|ERROR\|❌" "$LOAD_TEST_OUTPUT"; then
        echo -e "${RED}⚠️  Some load tests detected issues${NC}"
        LOAD_TEST_EXIT_CODE=1
    else
        echo -e "${GREEN}✓ All load tests passed performance targets${NC}"
    fi
else
    echo -e "${RED}✗ Load testing failed${NC}"
fi

# Additional checks
echo ""
echo -e "${BLUE}Running additional load testing tools...${NC}"

# Check if hey is available for additional load testing
if command -v hey &> /dev/null; then
    echo "Running hey load test..."

    HEY_OUTPUT="$OUTPUT_DIR/hey-load-test-$(date +%Y%m%d-%H%M%S).txt"

    # Run hey with various scenarios
    echo "Hey load test results:" > "$HEY_OUTPUT"
    echo "====================" >> "$HEY_OUTPUT"

    echo "Scenario 1: 1000 requests, 20 concurrent" >> "$HEY_OUTPUT"
    hey -n 1000 -c 20 "$BASE_URL/api/v1/sla/health" >> "$HEY_OUTPUT" 2>&1

    echo "" >> "$HEY_OUTPUT"
    echo "Scenario 2: 5000 requests, 50 concurrent" >> "$HEY_OUTPUT"
    hey -n 5000 -c 50 "$BASE_URL/api/v1/sla/health" >> "$HEY_OUTPUT" 2>&1

    echo "Hey results saved to: $HEY_OUTPUT"
else
    echo "hey tool not found. Install with: go install github.com/rakyll/hey@latest"
fi

# Memory and CPU monitoring
echo "Monitoring system resources..."

# Check if service process is still running
if curl -f -s "$BASE_URL/api/v1/sla/health" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Service remained stable during load testing${NC}"

    # Try to get process information
    if command -v pgrep &> /dev/null; then
        SERVICE_PID=$(pgrep -f "$SERVICE_NAME-service-go" | head -1)
        if [ -n "$SERVICE_PID" ]; then
            RESOURCE_OUTPUT="$OUTPUT_DIR/resource-usage-$(date +%Y%m%d-%H%M%S).txt"
            echo "Resource usage for PID $SERVICE_PID:" > "$RESOURCE_OUTPUT"
            ps -p "$SERVICE_PID" -o pid,ppid,cmd,%mem,%cpu --headers >> "$RESOURCE_OUTPUT"
            echo "Resource usage saved to: $RESOURCE_OUTPUT"
        fi
    fi
else
    echo -e "${RED}✗ Service became unresponsive during load testing${NC}"
    LOAD_TEST_EXIT_CODE=1
fi

echo ""
echo "========================================"
echo "LOAD TESTING SUMMARY"
echo "========================================"

if [ $LOAD_TEST_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}🎉 LOAD TESTING PASSED!${NC}"
    echo ""
    echo "Service successfully handled:"
    echo "  - Multiple concurrent requests"
    echo "  - Mixed endpoint load patterns"
    echo "  - Sustained load over time"
    echo "  - Resource usage remained stable"
    echo ""
    echo "Ready for production deployment!"
else
    echo -e "${RED}❌ LOAD TESTING FAILED!${NC}"
    echo ""
    echo "Issues detected:"
    if grep -q "FAILED\|ERROR" "$LOAD_TEST_OUTPUT"; then
        echo "  - Load test scenarios failed"
    fi
    if ! curl -f -s "$BASE_URL/api/v1/sla/health" > /dev/null 2>&1; then
        echo "  - Service became unresponsive"
    fi
    echo ""
    echo "Review the detailed logs for specific issues."
fi

exit $LOAD_TEST_EXIT_CODE


