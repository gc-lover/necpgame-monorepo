# ogen Migration Testing Framework

**Issue:** #143576311 - [QA] ogen Migration: Comprehensive Testing Strategy & Performance Validation Framework

This framework provides comprehensive testing, performance validation, and monitoring capabilities for ogen (OpenAPI code generation) services in the NECPGAME project.

## Overview

The ogen migration testing framework ensures that:
- ogen-generated handlers maintain API compatibility
- Services meet performance targets (<50ms latency, <1% error rate)
- Automated testing prevents regressions
- Production monitoring provides real-time insights

## Framework Components

### 1. Testing Strategy (`ogen-migration-testing-strategy.md`)
Comprehensive documentation of testing approach, objectives, and validation criteria.

### 2. Performance Validation (`performance-validation/`)
Automated performance testing and validation framework.

### 3. Automated Test Suite (`run-test-suite.sh`)
Complete test suite including unit tests, benchmarks, and static analysis.

### 4. Load Testing (`load-test/`)
Load testing scenarios and tools for production-like stress testing.

### 5. Monitoring & Alerting (`monitoring/`)
Prometheus, Grafana, and Alertmanager configurations for production monitoring.

## Quick Start

### Prerequisites
- Go 1.19+
- Python 3.8+
- Docker & Docker Compose
- curl, hey (optional for load testing)

### Basic Testing Workflow

```bash
# 1. Run automated test suite
cd scripts/testing
./run-test-suite.sh support-sla-service-go

# 2. Run performance validation
cd performance-validation
python3 performance-validator.py --url http://localhost:8080 --service support-sla

# 3. Run load testing
cd ../load-test
./run-load-tests.sh support-sla http://localhost:8080

# 4. Set up monitoring (optional)
cd ../monitoring
./setup-monitoring.sh
```

## Detailed Usage

### Automated Test Suite

The test suite runs comprehensive validation including:

```bash
# Run all tests for a service
./run-test-suite.sh [service-name]

# Example for support SLA service
./run-test-suite.sh support-sla-service-go
```

**Test Coverage:**
- Unit tests for all ogen-generated handlers
- Integration tests for API endpoints
- Performance benchmarks
- Race condition detection
- Static analysis (go vet, golint)
- Code coverage reporting

### Performance Validation

Validates service performance against enterprise targets:

```bash
# Run performance validation
python3 scripts/testing/performance-validation/performance-validator.py \
    --url http://localhost:8080 \
    --service support-sla

# Run benchmarks
./scripts/testing/performance-validation/run-benchmarks.sh support-sla http://localhost:8080
```

**Performance Targets:**
- Health checks: <10ms P95
- API endpoints: <50ms P95
- Error rate: <1%
- Memory usage: <512MB
- CPU usage: <70%

### Load Testing

Simulates production traffic patterns:

```bash
# Run load testing scenarios
python3 scripts/testing/load-test/load-test-scenarios.py \
    --url http://localhost:8080 \
    --service support-sla

# Or use the wrapper script
./scripts/testing/load-test/run-load-tests.sh support-sla http://localhost:8080
```

**Load Scenarios:**
- Warm-up phase
- Normal load (1000 req, 20 concurrent)
- Peak load (5000 req, 50 concurrent)
- Stress test (50000 req, 100 concurrent)
- Mixed endpoints testing

### Monitoring Setup

Set up production monitoring stack:

```bash
# Start monitoring stack
./scripts/testing/monitoring/setup-monitoring.sh

# Access points:
# - Prometheus: http://localhost:9090
# - Grafana: http://localhost:3000 (admin/admin)
# - Alertmanager: http://localhost:9093
```

## Service-Specific Configurations

### Support SLA Service

**Endpoints Tested:**
- `GET /api/v1/sla/health` - Health monitoring
- `GET /api/v1/sla/ticket/{id}/status` - SLA status
- `GET /api/v1/sla/policies` - SLA policies
- `GET /api/v1/sla/analytics/summary` - Analytics
- `GET /api/v1/sla/alerts/active` - Active alerts

**Performance Targets:**
- Health check: <10ms
- SLA operations: <50ms
- Analytics: <100ms

### Gameplay Service

**Endpoints Tested:**
- `GET /api/v1/gameplay/affixes` - Affix listing
- `POST /api/v1/gameplay/instances/{id}/affixes` - Affix application
- Complex combat calculations

**Performance Targets:**
- CRUD operations: <50ms
- Complex calculations: <200ms

## CI/CD Integration

### GitHub Actions Example

```yaml
# .github/workflows/ogen-testing.yml
name: ogen Migration Testing

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Run test suite
        run: ./scripts/testing/run-test-suite.sh ${{ matrix.service }}

      - name: Performance validation
        run: |
          ./scripts/testing/performance-validation/run-benchmarks.sh ${{ matrix.service }}

      - name: Load testing
        run: ./scripts/testing/load-test/run-load-tests.sh ${{ matrix.service }}

  deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to staging
        # Deployment logic here
```

## Alerting & Monitoring

### Prometheus Alerts

Critical alerts trigger notifications for:
- High latency (>50ms P95)
- High error rates (>5%)
- Service unavailability
- Resource exhaustion
- Performance regressions

### Grafana Dashboards

Pre-configured dashboards show:
- Response time percentiles
- Request rates and error rates
- Resource usage (CPU, memory)
- Database connection metrics
- Custom ogen service metrics

## Troubleshooting

### Common Issues

**Test Suite Failures:**
```bash
# Check detailed logs
cat scripts/testing/results/unit-tests-*.txt
cat scripts/testing/results/benchmarks-*.txt
```

**Performance Validation Issues:**
```bash
# Check service logs
docker logs support-sla-service-go

# Verify service is running
curl http://localhost:8080/api/v1/sla/health
```

**Load Testing Problems:**
```bash
# Check system resources
top
free -h

# Reduce concurrency if needed
python3 load-test-scenarios.py --concurrency 10
```

### Performance Optimization

**Common Bottlenecks:**
1. Database connection pooling
2. Memory allocation in handlers
3. Context timeouts
4. Serialization overhead

**Optimization Checklist:**
- [ ] Database connection pool configured
- [ ] Context timeouts implemented
- [ ] Structured logging enabled
- [ ] Memory pooling for large responses

## Validation Criteria

### Success Criteria
- [ ] All unit tests pass (>90% coverage)
- [ ] Performance targets met (<50ms P95)
- [ ] Error rate <1% under load
- [ ] Load tests pass all scenarios
- [ ] Monitoring alerts configured
- [ ] Documentation complete

### Rollback Triggers
- Performance degradation >20%
- Error rate increase >5%
- Service unavailability >1 minute
- Critical security vulnerabilities

## Maintenance

### Regular Tasks
- Weekly performance regression testing
- Monthly load testing with production data
- Alert rule tuning based on production metrics
- Documentation updates for new endpoints

### Updating Test Configurations
1. Modify `performance-targets.yaml` for new targets
2. Update `ogen-alerts.yml` for new alert rules
3. Refresh Grafana dashboards for new metrics
4. Update this README with new procedures

## Support

### Getting Help
1. Check this README for common issues
2. Review detailed logs in `scripts/testing/results/`
3. Check service-specific documentation
4. Contact the Backend team for framework issues

### Contributing
- Follow SOLID principles for new test code
- Add performance targets for new endpoints
- Update monitoring configurations
- Maintain comprehensive documentation

---

**Framework Version:** 1.0.0
**Last Updated:** 2025-01-05
**Maintained by:** Backend Team
**Issue:** #143576311


