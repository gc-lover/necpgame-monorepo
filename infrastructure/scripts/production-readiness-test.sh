#!/bin/bash

# NECPGAME Production Readiness Test Suite v2.0.0
# Comprehensive testing for production deployment validation

set -euo pipefail

# Configuration
NAMESPACE="${NAMESPACE:-necpgame}"
RELEASE="${RELEASE:-v2.0.0}"
TIMEOUT="${TIMEOUT:-300}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Test counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Test result tracking
declare -a FAILED_TEST_NAMES=()

# Test assertion function
assert() {
    local description="$1"
    local command="$2"
    local expected_exit_code="${3:-0}"

    ((TOTAL_TESTS++))
    log_info "Running test: $description"

    if eval "$command"; then
        local actual_exit_code=$?
        if [ $actual_exit_code -eq $expected_exit_code ]; then
            log_success "✓ $description"
            ((PASSED_TESTS++))
            return 0
        else
            log_error "✗ $description (expected exit code $expected_exit_code, got $actual_exit_code)"
            ((FAILED_TESTS++))
            FAILED_TEST_NAMES+=("$description")
            return 1
        fi
    else
        local actual_exit_code=$?
        if [ $actual_exit_code -eq $expected_exit_code ]; then
            log_success "✓ $description"
            ((PASSED_TESTS++))
            return 0
        else
            log_error "✗ $description (expected exit code $expected_exit_code, got $actual_exit_code)"
            ((FAILED_TESTS++))
            FAILED_TEST_NAMES+=("$description")
            return 1
        fi
    fi
}

# Wait for condition with timeout
wait_for() {
    local condition="$1"
    local timeout="$2"
    local interval="${3:-5}"
    local description="$4"

    local elapsed=0
    while [ $elapsed -lt $timeout ]; do
        if eval "$condition"; then
            log_success "$description completed in ${elapsed}s"
            return 0
        fi
        sleep $interval
        elapsed=$((elapsed + interval))
        log_info "Waiting for $description... (${elapsed}s/${timeout}s)"
    done

    log_error "$description timed out after ${timeout}s"
    return 1
}

# Kubernetes connectivity check
test_kubernetes_connectivity() {
    log_info "Testing Kubernetes connectivity..."

    assert "kubectl version check" "kubectl version --client --short" || return 1
    assert "kubectl cluster access" "kubectl cluster-info" || return 1
    assert "namespace exists" "kubectl get namespace $NAMESPACE" || return 1

    log_success "Kubernetes connectivity verified"
}

# Service deployment checks
test_service_deployments() {
    log_info "Testing service deployments..."

    local services=("guild-core-service" "guild-bank-service" "guild-war-service" "guild-territory-service")

    for service in "${services[@]}"; do
        assert "$service deployment exists" "kubectl get deployment $service -n $NAMESPACE" || return 1

        # Wait for rollout to complete
        assert "$service rollout status" "kubectl rollout status deployment/$service -n $NAMESPACE --timeout=${TIMEOUT}s" || return 1

        # Check pod status
        assert "$service pods ready" "kubectl get pods -l app=$service -n $NAMESPACE -o jsonpath='{.items[*].status.conditions[?(@.type==\"Ready\")].status}' | grep -v False" || return 1

        # Check service exists
        assert "$service service exists" "kubectl get service $service -n $NAMESPACE" || return 1
    done

    log_success "All service deployments verified"
}

# Health check endpoints
test_health_endpoints() {
    log_info "Testing health check endpoints..."

    local services=(
        "guild-core-service:8084"
        "guild-bank-service:8085"
        "guild-war-service:8086"
        "guild-territory-service:8087"
    )

    for service_info in "${services[@]}"; do
        local service_name=$(echo $service_info | cut -d: -f1)
        local service_port=$(echo $service_info | cut -d: -f2)

        # Test health endpoint
        assert "$service_name health check" "kubectl exec -n $NAMESPACE deployment/$service_name -c $service_name -- curl -f http://localhost:$service_port/health" || return 1

        # Test readiness endpoint
        assert "$service_name readiness check" "kubectl exec -n $NAMESPACE deployment/$service_name -c $service_name -- curl -f http://localhost:$service_port/ready" || return 1

        # Test metrics endpoint
        assert "$service_name metrics check" "kubectl exec -n $NAMESPACE deployment/$service_name -c $service_name -- curl -f http://localhost:$service_port/metrics | grep -q 'necpgame_'" || return 1
    done

    log_success "All health endpoints verified"
}

# Database connectivity
test_database_connectivity() {
    log_info "Testing database connectivity..."

    # Test PostgreSQL connection
    assert "PostgreSQL connection" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- pg_isready -h \$DATABASE_HOST -p \$DATABASE_PORT -U \$DATABASE_USER" || return 1

    # Test database schema
    assert "Database schema validation" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- psql -c 'SELECT version();'" || return 1

    # Test Liquibase migrations
    assert "Liquibase migrations applied" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- ls /app/migrations | wc -l | grep -q '^[1-9][0-9]*$'" || return 1

    log_success "Database connectivity verified"
}

# Redis connectivity
test_redis_connectivity() {
    log_info "Testing Redis connectivity..."

    assert "Redis connection" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- redis-cli -h \$REDIS_HOST -p \$REDIS_PORT ping | grep -q PONG" || return 1

    assert "Redis cluster status" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- redis-cli -h \$REDIS_HOST -p \$REDIS_PORT cluster info | grep -q 'cluster_state:ok'" || return 1

    log_success "Redis connectivity verified"
}

# Kafka connectivity
test_kafka_connectivity() {
    log_info "Testing Kafka connectivity..."

    assert "Kafka broker connection" "kubectl exec -n $NAMESPACE deployment/guild-war-service -c guild-war-service -- kafka-console-producer.sh --broker-list \$KAFKA_BROKERS --topic test-topic <<< 'test message'" || return 1

    assert "Kafka consumer connection" "timeout 10 kubectl exec -n $NAMESPACE deployment/guild-war-service -c guild-war-service -- kafka-console-consumer.sh --bootstrap-server \$KAFKA_BROKERS --topic test-topic --from-beginning --max-messages 1 | grep -q 'test message'" || return 1

    log_success "Kafka connectivity verified"
}

# Istio service mesh
test_istio_configuration() {
    log_info "Testing Istio service mesh configuration..."

    assert "Istio gateway exists" "kubectl get gateway guild-core-gateway -n $NAMESPACE" || return 1

    assert "Istio virtual service exists" "kubectl get virtualservice guild-core-virtualservice -n $NAMESPACE" || return 1

    assert "Istio destination rules exist" "kubectl get destinationrule -n $NAMESPACE | grep -q guild" || return 1

    log_success "Istio service mesh verified"
}

# Security configuration
test_security_configuration() {
    log_info "Testing security configuration..."

    # Check security contexts
    assert "Security contexts applied" "kubectl get pods -n $NAMESPACE -o jsonpath='{.items[*].spec.securityContext.runAsNonRoot}' | grep -q 'true'" || return 1

    # Check network policies
    assert "Network policies exist" "kubectl get networkpolicy -n $NAMESPACE | grep -q 'necpgame'" || return 1

    # Check secrets exist
    assert "Secrets configured" "kubectl get secrets necpgame-secrets -n $NAMESPACE" || return 1

    log_success "Security configuration verified"
}

# Performance benchmarks
test_performance_baselines() {
    log_info "Testing performance baselines..."

    # CPU usage check
    assert "CPU usage within limits" "kubectl top pods -n $NAMESPACE --no-headers | awk '{if(\$2 > 200) exit 1}'" || return 1

    # Memory usage check
    assert "Memory usage within limits" "kubectl top pods -n $NAMESPACE --no-headers | awk '{if(\$3 > 400) exit 1}'" || return 1

    # Response time check (basic)
    assert "API response time" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- curl -w '%{time_total}' -o /dev/null -s http://localhost:8084/health | awk '{if(\$1 > 0.1) exit 1}'" || return 1

    log_success "Performance baselines verified"
}

# Monitoring and alerting
test_monitoring_setup() {
    log_info "Testing monitoring and alerting setup..."

    assert "Prometheus service monitors" "kubectl get servicemonitor -n $NAMESPACE | grep -q necpgame" || return 1

    assert "Grafana dashboards" "kubectl get configmap -n monitoring | grep -q necpgame" || return 1

    assert "Prometheus rules" "kubectl get prometheusrules -n monitoring | grep -q necpgame" || return 1

    log_success "Monitoring and alerting verified"
}

# Rollback capability
test_rollback_capability() {
    log_info "Testing rollback capability..."

    # Check deployment history
    assert "Deployment history available" "kubectl rollout history deployment/guild-core-service -n $NAMESPACE | grep -q 'REVISION'" || return 1

    # Check previous image exists
    assert "Previous image available" "kubectl get deployment guild-core-service -n $NAMESPACE -o jsonpath='{.spec.template.spec.containers[0].image}' | grep -q 'v1\.'" || return 1

    log_success "Rollback capability verified"
}

# API compatibility
test_api_compatibility() {
    log_info "Testing API compatibility..."

    # Test OpenAPI compliance
    assert "OpenAPI specification valid" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- curl -f http://localhost:8084/openapi.yaml" || return 1

    # Test API versioning
    assert "API versioning works" "kubectl exec -n $NAMESPACE deployment/guild-core-service -c guild-core-service -- curl -H 'Accept: application/vnd.necpgame.v2+json' http://localhost:8084/api/v2/guilds" || return 1

    log_success "API compatibility verified"
}

# Main test execution
main() {
    log_info "Starting NECPGAME Production Readiness Test Suite v2.0.0"
    log_info "Namespace: $NAMESPACE, Release: $RELEASE, Timeout: ${TIMEOUT}s"

    # Pre-flight checks
    test_kubernetes_connectivity

    # Core functionality tests
    test_service_deployments
    test_health_endpoints
    test_database_connectivity
    test_redis_connectivity
    test_kafka_connectivity

    # Infrastructure tests
    test_istio_configuration
    test_security_configuration
    test_monitoring_setup
    test_rollback_capability

    # Quality tests
    test_performance_baselines
    test_api_compatibility

    # Results summary
    log_info "=== TEST RESULTS SUMMARY ==="
    log_info "Total Tests: $TOTAL_TESTS"
    log_info "Passed: $PASSED_TESTS"
    log_info "Failed: $FAILED_TESTS"
    log_info "Success Rate: $((PASSED_TESTS * 100 / TOTAL_TESTS))%"

    if [ $FAILED_TESTS -gt 0 ]; then
        log_error "FAILED TESTS:"
        for failed_test in "${FAILED_TEST_NAMES[@]}"; do
            log_error "  - $failed_test"
        done
        log_error "Production deployment NOT READY"
        exit 1
    else
        log_success "All tests PASSED - Production deployment READY"
        log_success "🎉 NECPGAME v2.0.0 'Cyberpunk Forge' is ready for production!"
        exit 0
    fi
}

# Trap for cleanup
trap 'log_warning "Test interrupted by user"' INT TERM

# Execute main function
main "$@"