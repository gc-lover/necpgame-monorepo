# Trading Contracts Integration Tests

Comprehensive integration test suite for the Trading Contracts service, covering functional testing, performance validation, API specification compliance, and external system integration.

## Overview

This test suite validates the complete Trading Contracts system including:

- **Functional Tests**: Contract creation, order matching, position management
- **Performance Tests**: Response times, throughput, memory usage, caching effectiveness
- **API Specification Tests**: OpenAPI compliance, schema validation, error handling
- **Integration Tests**: Database, Redis, message queues, external APIs

## Test Structure

```
scripts/tests/integration/trading-contracts/
├── test_contract_trading_integration.py    # Functional integration tests
├── test_contract_performance.py            # Performance and load tests
├── test_contract_api_spec.py               # API specification compliance
├── test_contract_integration.py            # External system integration
├── run_tests.py                            # Test runner script
└── README.md                               # This file
```

## Prerequisites

### System Requirements

- Python 3.8+
- PostgreSQL 13+
- Redis 6+
- Trading Contracts service running on port 8088

### Python Dependencies

```bash
pip install requests psycopg2-binary redis pyyaml jsonschema pytest
```

### Environment Setup

1. **Start PostgreSQL**:
```bash
# Using Docker
docker run -d --name test-postgres \
  -e POSTGRES_DB=test_trading \
  -e POSTGRES_USER=test \
  -e POSTGRES_PASSWORD=test \
  -p 5432:5432 postgres:13

# Or using local installation
createdb test_trading
```

2. **Start Redis**:
```bash
# Using Docker
docker run -d --name test-redis -p 6379:6379 redis:6

# Or using local installation
redis-server
```

3. **Start Trading Contracts Service**:
```bash
cd services/trading-contracts-service-go
export DATABASE_URL="postgresql://test:test@localhost:5432/test_trading?sslmode=disable"
export REDIS_URL="redis://localhost:6379"
go run cmd/server/main.go
```

## Running Tests

### Run All Tests

```bash
cd scripts/tests/integration/trading-contracts
python run_tests.py
```

### Run Single Test

```bash
# Run only functional tests
python run_tests.py --single-test test_contract_trading_integration.py

# Run only performance tests
python run_tests.py --single-test test_contract_performance.py
```

### Run with Custom Output

```bash
# Save results to specific file
python run_tests.py --output my_test_results.json

# Verbose output
python run_tests.py --verbose
```

### Run Individual Test Files Directly

```bash
# Functional tests
python test_contract_trading_integration.py

# Performance tests
python test_contract_performance.py

# API spec tests
python test_contract_api_spec.py

# Integration tests
python test_contract_integration.py
```

## Test Configuration

Configure test environment using environment variables:

```bash
export TEST_SERVICE_URL="http://localhost:8088"        # Trading Contracts service URL
export TEST_DATABASE_URL="postgresql://test:test@localhost:5432/test_trading"
export TEST_REDIS_URL="redis://localhost:6379"
export TEST_ENV="integration_test"                     # Test environment identifier
```

## Test Categories

### 1. Functional Integration Tests (`test_contract_trading_integration.py`)

Tests core trading functionality:

- ✅ Contract creation (SPOT, FUTURE, OPTION contracts)
- ✅ Order types (LIMIT, MARKET, STOP orders)
- ✅ Order book operations
- ✅ Contract retrieval and updates
- ✅ Position tracking
- ✅ Error handling and validation
- ✅ Concurrent operations
- ✅ System resilience

**Key Test Scenarios:**
- Create buy/sell contracts with different parameters
- Test order matching and execution
- Verify position calculations
- Test contract cancellation
- Validate data persistence

### 2. Performance Tests (`test_contract_performance.py`)

Validates system performance under load:

- 📊 Response time benchmarks (< 100ms P95 target)
- ⚡ Throughput testing (500+ req/sec target)
- 🧠 Memory usage monitoring
- 💾 Database query performance
- 🔄 Caching effectiveness (Redis L1/L2)

**Performance Metrics:**
- Average response time: < 50ms
- P95 response time: < 100ms
- Error rate: < 1%
- Cache hit rate: > 95%

### 3. API Specification Tests (`test_contract_api_spec.py`)

Ensures OpenAPI specification compliance:

- 📋 Endpoint existence validation
- 🔍 Request/response schema validation
- 🚨 HTTP status code correctness
- 📝 Required parameter enforcement
- ⚠️ Error response format validation

**Compliance Checks:**
- All OpenAPI paths exist
- Request/response schemas match spec
- Proper HTTP status codes returned
- Error responses follow specification

### 4. External Integration Tests (`test_contract_integration.py`)

Tests integration with external systems:

- 🗄️ PostgreSQL database operations
- 🔴 Redis caching and pub/sub
- 📨 Message queue connectivity
- 🌐 External API integrations
- 🔗 Service dependencies

**Integration Points:**
- Database CRUD operations
- Redis cache operations
- Message queue pub/sub
- Market data API calls
- Risk management integration

## Test Results

### Output Files

Tests generate two output files:

1. **`integration_test_results_{timestamp}.json`** - Detailed JSON results
2. **`integration_test_results_{timestamp}.txt`** - Human-readable report

### Result Interpretation

#### ✅ PASS Criteria
- **Overall**: ≥ 80% tests pass
- **Performance**: P95 < 100ms, error rate < 1%
- **API Compliance**: All endpoints return valid responses
- **Integration**: All required dependencies accessible

#### ⚠️ WARNING Conditions
- 60-79% pass rate
- P95 response time 100-500ms
- Some optional dependencies unavailable

#### ❌ FAIL Conditions
- < 60% tests pass
- P95 response time > 500ms
- Critical dependencies unavailable
- API specification violations

## Troubleshooting

### Common Issues

#### Service Not Available
```
❌ Trading Contracts Service: Unavailable
```
**Solution:**
```bash
# Check if service is running
curl http://localhost:8088/health

# Start service if needed
cd services/trading-contracts-service-go
go run cmd/server/main.go
```

#### Database Connection Failed
```
❌ PostgreSQL: Unavailable
```
**Solution:**
```bash
# Check PostgreSQL status
docker ps | grep postgres

# Verify connection
psql postgresql://test:test@localhost:5432/test_trading -c "SELECT 1;"
```

#### Redis Connection Failed
```
❌ Redis: Unavailable
```
**Solution:**
```bash
# Check Redis status
docker ps | grep redis

# Test connection
redis-cli ping
```

#### Test Timeouts
```
⏰ Test TIMEOUT
```
**Solution:**
- Increase timeout in `run_tests.py`
- Check system resources (CPU, memory)
- Verify service performance

### Debug Mode

Run tests with verbose output:

```bash
python run_tests.py --verbose
```

Check individual test output:

```bash
python -m pytest test_contract_trading_integration.py -v -s
```

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Integration Tests
on: [push, pull_request]

jobs:
  integration-tests:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:13
        env:
          POSTGRES_DB: test_trading
          POSTGRES_USER: test
          POSTGRES_PASSWORD: test
      redis:
        image: redis:6

    steps:
    - uses: actions/checkout@v2
    - uses: actions/setup-go@v2
      with:
        go-version: 1.19

    - name: Setup PostgreSQL
      run: |
        sudo apt-get update
        sudo apt-get install postgresql-client
        createdb test_trading

    - name: Start Trading Contracts Service
      run: |
        cd services/trading-contracts-service-go
        go mod tidy
        go build -o server cmd/server/main.go
        ./server &
        sleep 10

    - name: Run Integration Tests
      run: |
        cd scripts/tests/integration/trading-contracts
        pip install requests psycopg2-binary redis
        python run_tests.py

    - name: Upload Test Results
      uses: actions/upload-artifact@v2
      with:
        name: test-results
        path: scripts/tests/integration/trading-contracts/integration_test_results_*.*
```

## Contributing

### Adding New Tests

1. **Functional Tests**: Add to `test_contract_trading_integration.py`
2. **Performance Tests**: Add to `test_contract_performance.py`
3. **API Tests**: Add to `test_contract_api_spec.py`
4. **Integration Tests**: Add to `test_contract_integration.py`

### Test Naming Convention

- Test functions: `test_<feature>_<scenario>`
- Test classes: `<Feature>Test`
- Test methods: `test_<specific_behavior>`

### Assertions

Use descriptive assertion messages:

```python
assert response.status_code == 201, f"Expected 201, got {response.status_code}: {response.text}"
```

## Issue Tracking

**Issue:** #2202 - Тестирование системы контрактов и сделок

**Status:** ✅ COMPLETED - Comprehensive test suite implemented

**Related Issues:**
- #2191 - Trading Contracts Service Implementation
- #2278 - BazaarBot Economic Simulation

## Next Steps

- [ ] Add chaos engineering tests
- [ ] Implement distributed testing across multiple nodes
- [ ] Add load testing with realistic user patterns
- [ ] Integrate with monitoring dashboards
- [ ] Add automated performance regression detection