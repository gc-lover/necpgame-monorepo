#!/bin/bash
# Comprehensive QA Test Execution Script
# Issue: #2258 - Enterprise-Grade Domain Services QA Testing
# Agent: QA - Automated testing orchestration with comprehensive coverage

set -euo pipefail

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
TEST_ENV="${TEST_ENV:-staging}"
PARALLEL_WORKERS="${PARALLEL_WORKERS:-4}"
REPORT_DIR="${REPORT_DIR:-tests/reports/$(date +%Y%m%d_%H%M%S)}"
LOG_FILE="${LOG_FILE:-$REPORT_DIR/qa-test-execution.log}"

# Test service endpoints
QUEST_SERVICE_URL="${QUEST_SERVICE_URL:-http://quest-service.$TEST_ENV.necpgame.internal}"
COMBAT_SERVICE_URL="${COMBAT_SERVICE_URL:-http://combat-service.$TEST_ENV.necpgame.internal}"
ECONOMY_SERVICE_URL="${ECONOMY_SERVICE_URL:-http://economy-service.$TEST_ENV.necpgame.internal}"
SOCIAL_SERVICE_URL="${SOCIAL_SERVICE_URL:-http://social-service.$TEST_ENV.necpgame.internal}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

# Logging functions
log_info() {
    echo -e "${BLUE}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

log_success() {
    echo -e "${GREEN}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

log_warning() {
    echo -e "${YELLOW}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

log_error() {
    echo -e "${RED}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

log_header() {
    echo -e "${PURPLE}========================================${NC}" | tee -a "$LOG_FILE"
    echo -e "${PURPLE}$1${NC}" | tee -a "$LOG_FILE"
    echo -e "${PURPLE}========================================${NC}" | tee -a "$LOG_FILE"
}

# Test results tracking
declare -A TEST_RESULTS
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0
SKIPPED_TESTS=0

# Pre-test validation
pre_test_validation() {
    log_header "PRE-TEST VALIDATION"

    # Create report directory
    mkdir -p "$REPORT_DIR"

    # Validate environment
    if [[ "$TEST_ENV" != "staging" && "$TEST_ENV" != "production" && "$TEST_ENV" != "local" ]]; then
        log_error "Invalid TEST_ENV: $TEST_ENV"
        exit 1
    fi

    # Check required tools
    local required_tools=("python3" "pip" "curl" "docker" "kubectl")
    for tool in "${required_tools[@]}"; do
        if ! command -v "$tool" &> /dev/null; then
            log_error "Required tool not found: $tool"
            exit 1
        fi
    done

    # Check service availability
    log_info "Checking service availability..."

    local services=(
        "$QUEST_SERVICE_URL/health:Quest Service"
        "$COMBAT_SERVICE_URL/health:Combat Service"
        "$ECONOMY_SERVICE_URL/health:Economy Service"
        "$SOCIAL_SERVICE_URL/health:Social Service"
    )

    for service_info in "${services[@]}"; do
        local url="${service_info%:*}"
        local name="${service_info#*:}"

        if curl -f -s --max-time 10 "$url" > /dev/null 2>&1; then
            log_success "$name is available"
        else
            log_warning "$name is not available - tests may be skipped"
        fi
    done

    # Install test dependencies
    log_info "Installing test dependencies..."
    cd "$PROJECT_ROOT"
    pip install -r tests/requirements.txt

    # Validate test data
    if [[ ! -f "tests/test_data/seed_data.json" ]]; then
        log_warning "Test data file not found - generating..."
        python3 tests/scripts/generate-test-data.py
    fi

    log_success "Pre-test validation completed"
}

# Unit tests
run_unit_tests() {
    log_header "UNIT TESTS"

    cd "$PROJECT_ROOT"

    # Quest service unit tests
    log_info "Running Quest Service unit tests..."
    if python3 -m pytest tests/functional/unit/services/quest-service/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/unit-quest-service.xml" \
                     --cov=services/quest_service \
                     --cov-report=xml:"$REPORT_DIR/coverage-unit-quest.xml" \
                     --cov-report=html:"$REPORT_DIR/coverage-unit-html" \
                     --maxfail=5; then
        log_success "Quest Service unit tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Quest Service unit tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Combat service unit tests
    log_info "Running Combat Service unit tests..."
    if python3 -m pytest tests/functional/unit/services/combat-service/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/unit-combat-service.xml" \
                     --cov=services/combat_service \
                     --cov-report=xml:"$REPORT_DIR/coverage-unit-combat.xml"; then
        log_success "Combat Service unit tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Combat Service unit tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Economy service unit tests
    log_info "Running Economy Service unit tests..."
    if python3 -m pytest tests/functional/unit/services/economy-service/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/unit-economy-service.xml" \
                     --cov=services/economy_service \
                     --cov-report=xml:"$REPORT_DIR/coverage-unit-economy.xml"; then
        log_success "Economy Service unit tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Economy Service unit tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Social service unit tests
    log_info "Running Social Service unit tests..."
    if python3 -m pytest tests/functional/unit/services/social-service/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/unit-social-service.xml" \
                     --cov=services/social_service \
                     --cov-report=xml:"$REPORT_DIR/coverage-unit-social.xml"; then
        log_success "Social Service unit tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Social Service unit tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    log_success "Unit tests completed"
}

# API tests
run_api_tests() {
    log_header "API TESTS"

    cd "$PROJECT_ROOT"

    # Quest service API tests
    log_info "Running Quest Service API tests..."
    if TEST_ENV="$TEST_ENV" QUEST_SERVICE_URL="$QUEST_SERVICE_URL" \
       python3 -m pytest tests/functional/api/quest-service/ \
                      -v --tb=short \
                      --junitxml="$REPORT_DIR/api-quest-service.xml" \
                      --maxfail=3; then
        log_success "Quest Service API tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Quest Service API tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Combat service API tests
    log_info "Running Combat Service API tests..."
    if TEST_ENV="$TEST_ENV" COMBAT_SERVICE_URL="$COMBAT_SERVICE_URL" \
       python3 -m pytest tests/functional/api/combat-service/ \
                      -v --tb=short \
                      --junitxml="$REPORT_DIR/api-combat-service.xml"; then
        log_success "Combat Service API tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Combat Service API tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Economy service API tests
    log_info "Running Economy Service API tests..."
    if TEST_ENV="$TEST_ENV" ECONOMY_SERVICE_URL="$ECONOMY_SERVICE_URL" \
       python3 -m pytest tests/functional/api/economy-service/ \
                      -v --tb=short \
                      --junitxml="$REPORT_DIR/api-economy-service.xml"; then
        log_success "Economy Service API tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Economy Service API tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    log_success "API tests completed"
}

# Performance tests
run_performance_tests() {
    log_header "PERFORMANCE TESTS"

    cd "$PROJECT_ROOT"

    # Load tests
    log_info "Running load tests..."
    if locust -f tests/performance/load-tests/test_quest_service_load.py \
             --host="$QUEST_SERVICE_URL" \
             --users=100 \
             --spawn-rate=5 \
             --run-time=2m \
             --csv="$REPORT_DIR/performance-load" \
             --loglevel=WARNING; then
        log_success "Load tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Load tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Stress tests
    log_info "Running stress tests..."
    if artillery run tests/performance/stress-tests/stress-config.yml \
               --output "$REPORT_DIR/performance-stress.json" \
               --quiet; then
        log_success "Stress tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Stress tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    log_success "Performance tests completed"
}

# Security tests
run_security_tests() {
    log_header "SECURITY TESTS"

    cd "$PROJECT_ROOT"

    # Authentication tests
    log_info "Running authentication tests..."
    if python3 -m pytest tests/security/authentication/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/security-auth.xml"; then
        log_success "Authentication tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Authentication tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Authorization tests
    log_info "Running authorization tests..."
    if python3 -m pytest tests/security/authorization/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/security-authz.xml"; then
        log_success "Authorization tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Authorization tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Vulnerability tests
    log_info "Running vulnerability tests..."
    if python3 -m pytest tests/security/vulnerabilities/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/security-vuln.xml"; then
        log_success "Vulnerability tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Vulnerability tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    log_success "Security tests completed"
}

# Integration tests
run_integration_tests() {
    log_header "INTEGRATION TESTS"

    cd "$PROJECT_ROOT"

    # Cross-service integration
    log_info "Running cross-service integration tests..."
    if python3 -m pytest tests/integration/cross_service/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/integration-cross-service.xml"; then
        log_success "Cross-service integration tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Cross-service integration tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Database integration
    log_info "Running database integration tests..."
    if python3 -m pytest tests/integration/database/ \
                     -v --tb=short \
                     --junitxml="$REPORT_DIR/integration-database.xml"; then
        log_success "Database integration tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Database integration tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    log_success "Integration tests completed"
}

# Chaos engineering tests
run_chaos_tests() {
    log_header "CHAOS ENGINEERING TESTS"

    cd "$PROJECT_ROOT"

    # Pod failure chaos
    log_info "Running pod failure chaos tests..."
    if kubectl apply -f tests/chaos/pod-kill-experiment.yaml && \
       sleep 120 && \
       kubectl delete -f tests/chaos/pod-kill-experiment.yaml && \
       python3 tests/scripts/validate-chaos-resilience.py \
               --experiment pod-kill \
               --report "$REPORT_DIR/chaos-pod-kill.json"; then
        log_success "Pod failure chaos tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Pod failure chaos tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    # Network latency chaos
    log_info "Running network latency chaos tests..."
    if kubectl apply -f tests/chaos/network-delay-experiment.yaml && \
       sleep 180 && \
       kubectl delete -f tests/chaos/network-delay-experiment.yaml && \
       python3 tests/scripts/validate-chaos-resilience.py \
               --experiment network-delay \
               --report "$REPORT_DIR/chaos-network-delay.json"; then
        log_success "Network latency chaos tests passed"
        ((PASSED_TESTS++))
    else
        log_error "Network latency chaos tests failed"
        ((FAILED_TESTS++))
    fi
    ((TOTAL_TESTS++))

    log_success "Chaos engineering tests completed"
}

# Generate comprehensive report
generate_report() {
    log_header "GENERATING COMPREHENSIVE REPORT"

    cd "$PROJECT_ROOT"

    # Generate HTML report
    log_info "Generating HTML test report..."
    python3 tests/scripts/generate-report.py \
           --input-dir "$REPORT_DIR" \
           --output "$REPORT_DIR/comprehensive-test-report.html" \
           --format html

    # Generate JUnit summary
    log_info "Generating JUnit summary..."
    python3 tests/scripts/junit-summary.py \
           --input-dir "$REPORT_DIR" \
           --output "$REPORT_DIR/test-summary.json"

    # Generate coverage report
    log_info "Generating coverage report..."
    coverage combine "$REPORT_DIR"/coverage-*.xml 2>/dev/null || true
    coverage html -d "$REPORT_DIR/coverage-full-html" 2>/dev/null || true
    coverage xml -o "$REPORT_DIR/coverage-full.xml" 2>/dev/null || true

    # Generate performance analysis
    log_info "Generating performance analysis..."
    python3 tests/scripts/analyze-performance.py \
           --input-dir "$REPORT_DIR" \
           --output "$REPORT_DIR/performance-analysis.json"

    # Generate security assessment
    log_info "Generating security assessment..."
    python3 tests/scripts/assess-security.py \
           --input-dir "$REPORT_DIR" \
           --output "$REPORT_DIR/security-assessment.json"

    log_success "Comprehensive report generated: $REPORT_DIR/comprehensive-test-report.html"
}

# Final results and notifications
finalize_results() {
    log_header "TEST EXECUTION SUMMARY"

    local success_rate=0
    if [[ $TOTAL_TESTS -gt 0 ]]; then
        success_rate=$((PASSED_TESTS * 100 / TOTAL_TESTS))
    fi

    echo "Total Tests: $TOTAL_TESTS" | tee -a "$LOG_FILE"
    echo "Passed: $PASSED_TESTS" | tee -a "$LOG_FILE"
    echo "Failed: $FAILED_TESTS" | tee -a "$LOG_FILE"
    echo "Skipped: $SKIPPED_TESTS" | tee -a "$LOG_FILE"
    echo "Success Rate: ${success_rate}%" | tee -a "$LOG_FILE"

    # Quality gates
    local quality_passed=true

    if [[ $success_rate -lt 95 ]]; then
        log_error "❌ QUALITY GATE FAILED: Success rate < 95% (${success_rate}%)"
        quality_passed=false
    fi

    if [[ $FAILED_TESTS -gt 0 ]]; then
        log_error "❌ QUALITY GATE FAILED: $FAILED_TESTS tests failed"
        quality_passed=false
    fi

    # Coverage check
    if [[ -f "$REPORT_DIR/coverage-full.xml" ]]; then
        local coverage
        coverage=$(python3 -c "
import xml.etree.ElementTree as ET
try:
    root = ET.parse('$REPORT_DIR/coverage-full.xml').getroot()
    print(int(float(root.attrib.get('line-rate', 0)) * 100))
except:
    print(0)
")
        if [[ $coverage -lt 90 ]]; then
            log_error "❌ QUALITY GATE FAILED: Code coverage < 90% (${coverage}%)"
            quality_passed=false
        fi
    fi

    # Performance check
    if [[ -f "$REPORT_DIR/performance-analysis.json" ]]; then
        local p95_response_time
        p95_response_time=$(python3 -c "
import json
try:
    with open('$REPORT_DIR/performance-analysis.json') as f:
        data = json.load(f)
        print(int(data.get('p95_response_time', 999)))
except:
    print(999)
")
        if [[ $p95_response_time -gt 200 ]]; then
            log_error "❌ QUALITY GATE FAILED: P95 response time > 200ms (${p95_response_time}ms)"
            quality_passed=false
        fi
    fi

    if [[ "$quality_passed" == "true" ]]; then
        log_success "✅ ALL QUALITY GATES PASSED"
        echo "🎉 QA Testing Successful! Ready for production deployment." | tee -a "$LOG_FILE"

        # Send success notification
        send_notification "✅ QA Tests Passed" \
            "Environment: $TEST_ENV\nSuccess Rate: ${success_rate}%\nReport: $REPORT_DIR/comprehensive-test-report.html" \
            "good"
    else
        log_error "❌ QUALITY GATES FAILED"
        echo "🚫 QA Testing Failed! Deployment blocked." | tee -a "$LOG_FILE"

        # Send failure notification
        send_notification "❌ QA Tests Failed" \
            "Environment: $TEST_ENV\nSuccess Rate: ${success_rate}%\nFailed Tests: $FAILED_TESTS\nReport: $REPORT_DIR/comprehensive-test-report.html" \
            "danger"
    fi

    # Archive logs
    log_info "Archiving test results..."
    mkdir -p "/var/log/necp-game/qa-archive"
    cp -r "$REPORT_DIR" "/var/log/necp-game/qa-archive/$(basename "$REPORT_DIR")"

    return $((quality_passed ? 0 : 1))
}

# Notification function
send_notification() {
    local title="$1"
    local message="$2"
    local color="${3:-good}"

    # Slack webhook notification (if configured)
    if [[ -n "${SLACK_WEBHOOK_URL:-}" ]]; then
        curl -s -X POST "$SLACK_WEBHOOK_URL" \
            -H 'Content-Type: application/json' \
            -d "{\"text\":\"${title}\",\"attachments\":[{\"text\":\"${message}\",\"color\":\"${color}\"}]}" \
            || log_warning "Failed to send Slack notification"
    fi

    # Email notification (if configured)
    if [[ -n "${EMAIL_RECIPIENTS:-}" ]]; then
        echo "Subject: ${title}
${message}

Report: ${REPORT_DIR}/comprehensive-test-report.html
Log: ${LOG_FILE}" | \
        sendmail "$EMAIL_RECIPIENTS" || log_warning "Failed to send email notification"
    fi
}

# Main execution
main() {
    log_header "NECPGAME COMPREHENSIVE QA TEST SUITE"
    log_info "Environment: $TEST_ENV"
    log_info "Parallel Workers: $PARALLEL_WORKERS"
    log_info "Report Directory: $REPORT_DIR"

    local start_time
    start_time=$(date +%s)

    # Execute test phases
    pre_test_validation
    run_unit_tests
    run_api_tests
    run_performance_tests
    run_security_tests
    run_integration_tests
    run_chaos_tests

    # Generate reports
    generate_report

    # Finalize and check quality gates
    finalize_results
    local exit_code=$?

    local end_time
    end_time=$(date +%s)
    local duration=$((end_time - start_time))

    log_info "QA Test Suite completed in ${duration}s"
    log_info "Full report available at: $REPORT_DIR/comprehensive-test-report.html"

    return $exit_code
}

# Handle script arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --env|--environment)
            TEST_ENV="$2"
            shift 2
            ;;
        --workers)
            PARALLEL_WORKERS="$2"
            shift 2
            ;;
        --report-dir)
            REPORT_DIR="$2"
            shift 2
            ;;
        --quest-service)
            QUEST_SERVICE_URL="$2"
            shift 2
            ;;
        --combat-service)
            COMBAT_SERVICE_URL="$2"
            shift 2
            ;;
        --economy-service)
            ECONOMY_SERVICE_URL="$2"
            shift 2
            ;;
        --social-service)
            SOCIAL_SERVICE_URL="$2"
            shift 2
            ;;
        --skip-unit)
            SKIP_UNIT=true
            shift
            ;;
        --skip-api)
            SKIP_API=true
            shift
            ;;
        --skip-performance)
            SKIP_PERFORMANCE=true
            shift
            ;;
        --skip-security)
            SKIP_SECURITY=true
            shift
            ;;
        --skip-integration)
            SKIP_INTEGRATION=true
            shift
            ;;
        --skip-chaos)
            SKIP_CHAOS=true
            shift
            ;;
        --help)
            echo "Usage: $0 [OPTIONS]"
            echo "Options:"
            echo "  --env ENVIRONMENT          Target environment (staging|production|local)"
            echo "  --workers NUM             Number of parallel test workers"
            echo "  --report-dir DIR          Directory for test reports"
            echo "  --quest-service URL       Quest service endpoint"
            echo "  --combat-service URL      Combat service endpoint"
            echo "  --economy-service URL     Economy service endpoint"
            echo "  --social-service URL      Social service endpoint"
            echo "  --skip-unit              Skip unit tests"
            echo "  --skip-api               Skip API tests"
            echo "  --skip-performance       Skip performance tests"
            echo "  --skip-security          Skip security tests"
            echo "  --skip-integration       Skip integration tests"
            echo "  --skip-chaos             Skip chaos engineering tests"
            echo "  --help                   Show this help message"
            exit 0
            ;;
        *)
            log_error "Unknown option: $1"
            exit 1
            ;;
    esac
done

# Run main function
main "$@"

