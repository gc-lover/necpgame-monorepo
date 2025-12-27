# NECPGAME Integration Tests

Comprehensive integration test suite for NECPGAME microservices.

## Overview

This test suite validates:
- Service-to-service communication
- API compatibility and contracts
- Performance requirements (MMOFPS standards)
- Fault tolerance and resilience
- Load balancing and scaling

## Test Categories

### Service Integration Tests
- **Analytics Service**: Health checks, API endpoints, concurrent access
- **Easter Eggs Service**: Authentication-protected endpoints
- **World Events Service**: Event management and participation
- **Guild Service**: Social features and voice channels
- **Database Connectivity**: Migration validation and data integrity

### Performance Validation Tests
- **Latency Requirements**: P95 <50ms, P99 <100ms for hot paths
- **Concurrent Load**: 1000+ simultaneous users support
- **Memory Efficiency**: Zero allocations in hot paths
- **Throughput**: 10k+ RPS sustained performance

### Service Mesh Tests
- **API Gateway Routing**: Cross-service communication
- **Circuit Breaker**: Fault tolerance and recovery
- **Load Balancing**: Request distribution validation
- **Metrics Collection**: Prometheus integration
- **Event Streaming**: Kafka message processing

## Running Tests

### Prerequisites
- PostgreSQL database running (Docker container `necpgame-postgres-1`)
- Redis cache available
- All services built and running

### Execute Tests
```bash
# Run all integration tests
cd tests/integration
go test -v ./...

# Run specific test suite
go test -v -run TestServiceIntegrationSuite

# Run performance tests
go test -v -run TestPerformanceSuite

# Run service mesh tests
go test -v -run TestServiceMeshSuite
```

### Individual Tests
```bash
# Health checks
go test -v -run TestAnalyticsServiceHealth

# Latency validation
go test -v -run TestAnalyticsLatencyValidation

# Concurrent load
go test -v -run TestConcurrentAnalyticsRequests

# Service discovery
go test -v -run TestServiceDiscovery
```

## Performance Benchmarks

### Latency Targets (MMOFPS Requirements)
- **Health Endpoints**: P95 <1ms, P99 <5ms
- **Analytics APIs**: P95 <50ms, P99 <100ms
- **Combat Hot Paths**: P95 <25ms, P99 <50ms
- **Database Queries**: P95 <100ms, P99 <200ms

### Throughput Targets
- **Concurrent Users**: 10,000+ simultaneous connections
- **Events/Second**: 100k+ event processing capacity
- **API Requests**: 50k+ RPS sustained load

### Memory Efficiency
- **Per-Player Data**: <50KB active memory
- **Cache Hit Rate**: >95% for hot data
- **Zero Allocations**: Hot path operations optimized

## Test Configuration

Services are configured for local development:

```go
services := []ServiceConfig{
    {"analytics-dashboard", "http://localhost:8091", 5*time.Second, true},
    {"cyberspace-easter-eggs", "http://localhost:8080", 5*time.Second, false},
    {"world-events", "http://localhost:8070", 5*time.Second, false},
    {"guild-service", "http://localhost:8060", 5*time.Second, false},
}
```

## CI/CD Integration

Tests run automatically in CI pipeline:
- Unit tests for individual services
- Integration tests for service communication
- Performance benchmarks with regression detection
- Load testing with configurable parameters

## Troubleshooting

### Common Issues

**Services Not Available**
```
Error: service analytics-dashboard not reachable
```
- Ensure services are built: `make build` in each service directory
- Start services: `./service-name` or use Docker Compose
- Check ports are not in use by other applications

**Database Connection Failed**
```
Error: Database connection failed
```
- Verify PostgreSQL container is running: `docker ps`
- Check connection parameters in service config
- Run migrations: `python scripts/migrations/apply-migrations.py`

**High Latency**
```
P95 latency 75ms exceeds requirement of <50ms
```
- Check system resources (CPU, memory)
- Verify database and Redis performance
- Review service logs for bottlenecks
- Consider scaling service instances

### Debug Mode
```bash
# Run with verbose output
go test -v -args -verbose

# Run single test with debug
go test -v -run TestAnalyticsServiceHealth -args -debug

# Check service logs
tail -f service-logs/analytics.log
```

## Test Architecture

### Test Utilities (`utils.go`)
- Service health checking and discovery
- Latency measurement and percentile calculation
- Concurrent load testing framework
- MMOFPS performance validation helpers

### Test Suites
- **Main Suite**: Service integration and API validation
- **Performance Suite**: Load testing and latency benchmarks
- **Service Mesh Suite**: Cross-service communication and routing

### Test Data
- Mock data for development testing
- Performance test data sets
- Integration test scenarios
- Chaos testing configurations

## Contributing

When adding new tests:
1. Follow existing naming conventions
2. Add proper error handling and logging
3. Include performance assertions where applicable
4. Update this README with new test descriptions
5. Ensure tests run in CI/CD pipeline

## Performance Monitoring

Tests integrate with monitoring:
- Prometheus metrics collection
- Grafana dashboards for test results
- Alerting on performance regressions
- Historical performance tracking

## Future Enhancements

- Chaos testing with service failures
- Multi-region deployment testing
- End-to-end user journey tests
- AI/ML model validation tests
- Blockchain integration tests
