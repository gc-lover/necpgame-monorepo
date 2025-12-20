#!/bin/bash

# NECP Game Full System Check Suite
# Запуск полного набора проверок системы

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo "🔬 NECP Game Full System Check Suite"
echo "===================================="

# Configuration
REPORT_DIR="system_reports"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
SUMMARY_FILE="$REPORT_DIR/system_check_summary_$TIMESTAMP.md"

mkdir -p "$REPORT_DIR"

# Global status tracking
declare -A CHECK_RESULTS
TOTAL_CHECKS=0
PASSED_CHECKS=0

# Function to run a check and track results
run_check() {
    local check_name=$1
    local check_command=$2
    local description=${3:-$check_name}

    echo -e "${BLUE}🔍 Running $description...${NC}"

    ((TOTAL_CHECKS++))

    if eval "$check_command"; then
        echo -e "${GREEN}OK $description PASSED${NC}"
        CHECK_RESULTS["$check_name"]="PASS"
        ((PASSED_CHECKS++))
        return 0
    else
        echo -e "${RED}❌ $description FAILED${NC}"
        CHECK_RESULTS["$check_name"]="FAIL"
        return 1
    fi
}

# Function to generate summary report
generate_summary() {
    local success_rate=$((PASSED_CHECKS * 100 / TOTAL_CHECKS))

    cat > "$SUMMARY_FILE" << EOF
# NECP Game System Check Summary

**Report Date:** $(date)
**Success Rate:** $success_rate% ($PASSED_CHECKS/$TOTAL_CHECKS checks passed)

## Check Results

EOF

    for check in "${!CHECK_RESULTS[@]}"; do
        local status=${CHECK_RESULTS[$check]}
        local icon="OK"
        if [ "$status" = "FAIL" ]; then
            icon="❌"
        fi
        echo "- $icon $check: $status" >> "$SUMMARY_FILE"
    done

    cat >> "$SUMMARY_FILE" << EOF

## System Status

### Infrastructure
- **Docker:** $(docker --version 2>/dev/null | head -1 || echo "Not available")
- **Docker Compose:** $(docker-compose --version 2>/dev/null || echo "Not available")
- **Git:** $(git --version 2>/dev/null | head -1 || echo "Not available")

### Services
- **Total Services:** 27
- **Expected Healthy:** 27
- **Current Status:** Check individual service reports

### Monitoring
- **Prometheus:** $(curl -s http://localhost:9090/-/healthy 2>/dev/null && echo "Running" || echo "Not available")
- **Grafana:** $(curl -s http://localhost:3000/api/health 2>/dev/null && echo "Running" || echo "Not available")
- **Loki:** $(curl -s http://localhost:3100/ready 2>/dev/null && echo "Running" || echo "Not available")

## Recommendations

EOF

    if [ $success_rate -eq 100 ]; then
        echo "- OK All checks passed! System is healthy." >> "$SUMMARY_FILE"
    else
        echo "- WARNING  Some checks failed. Review detailed reports." >> "$SUMMARY_FILE"
        echo "- 🔧 Run individual checks for more details." >> "$SUMMARY_FILE"
    fi

    echo "" >> "$SUMMARY_FILE"
    echo "## Next Steps" >> "$SUMMARY_FILE"
    echo "" >> "$SUMMARY_FILE"
    echo "1. Review detailed check reports in $REPORT_DIR/" >> "$SUMMARY_FILE"
    echo "2. Address any failed checks" >> "$SUMMARY_FILE"
    echo "3. Run performance analysis: \`./scripts/performance-analysis.sh\`" >> "$SUMMARY_FILE"
    echo "4. Run security audit: \`./scripts/security-audit.sh\`" >> "$SUMMARY_FILE"
    echo "5. Generate release notes: \`./scripts/generate-release-notes.sh\`" >> "$SUMMARY_FILE"

    echo -e "${GREEN}📋 Summary report saved to: $SUMMARY_FILE${NC}"
}

# Main check suite
echo "Running comprehensive system checks..."
echo ""

# 1. System Health Check
run_check "system_health" "./scripts/system-check.sh > $REPORT_DIR/system_health_$TIMESTAMP.log 2>&1" "System Health Check"

# 2. API Testing
run_check "api_tests" "./scripts/api-test.sh > $REPORT_DIR/api_tests_$TIMESTAMP.log 2>&1" "API Endpoint Tests"

# 3. Load Testing (light version)
run_check "load_tests" "CONCURRENT_REQUESTS=2 TOTAL_REQUESTS=20 DURATION=10 ./scripts/load-test.sh > $REPORT_DIR/load_tests_$TIMESTAMP.log 2>&1" "Light Load Tests"

# 4. Security Audit
run_check "security_audit" "./scripts/security-audit.sh > $REPORT_DIR/security_audit_$TIMESTAMP.log 2>&1" "Security Audit"

# 5. Performance Analysis
run_check "performance_analysis" "./scripts/performance-analysis.sh > $REPORT_DIR/performance_$TIMESTAMP.log 2>&1" "Performance Analysis"

# 6. Database Backup Check
run_check "backup_check" "./scripts/backup-databases.sh > $REPORT_DIR/backup_$TIMESTAMP.log 2>&1" "Database Backup"

echo ""
echo "📊 Check Results:"
echo "================="

for check in "${!CHECK_RESULTS[@]}"; do
    status=${CHECK_RESULTS[$check]}
    if [ "$status" = "PASS" ]; then
        echo -e "${GREEN}OK $check${NC}"
    else
        echo -e "${RED}❌ $check${NC}"
    fi
done

echo ""
echo "📈 Summary: $PASSED_CHECKS/$TOTAL_CHECKS checks passed ($((PASSED_CHECKS * 100 / TOTAL_CHECKS))%)"

# Generate detailed summary report
generate_summary

echo ""
echo "📁 Detailed reports saved to: $REPORT_DIR/"
echo "📋 Summary report: $SUMMARY_FILE"

if [ $PASSED_CHECKS -eq $TOTAL_CHECKS ]; then
    echo ""
    echo -e "${GREEN}🎉 ALL CHECKS PASSED! System is fully operational.${NC}"
    exit 0
else
    echo ""
    echo -e "${YELLOW}WARNING  SOME CHECKS FAILED. Review reports for details.${NC}"
    exit 1
fi
