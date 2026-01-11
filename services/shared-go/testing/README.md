# Automated Testing Framework

## Overview

Enterprise-grade automated testing framework for unit tests, integration tests, and performance benchmarks. Designed for MMOFPS games requiring comprehensive test coverage and performance validation.

## Issue: #2144

## Features

### 1. Unit Testing
- Test suite management
- Test configuration
- Assertion helpers
- Timeout management

### 2. Integration Testing
- Test environment setup
- Database and Redis integration
- Service availability checks
- Test data cleanup

### 3. Performance Benchmarks
- Latency benchmarks
- Throughput benchmarks
- Percentile calculations (P50, P95, P99)
- Concurrent testing

## Usage

### Unit Tests

```go
import "necpgame/services/shared-go/testing"

suite := testing.TestSuite{
    Name: "MyService",
    UnitTests: []testing.UnitTest{
        {
            Name: "TestFunction",
            Test: func(t *testing.T) {
                result := MyFunction()
                testing.AssertEqual(t, "expected", result, "function result")
            },
        },
    },
}

err := testing.RunTestSuite(ctx, suite, testing.DefaultTestConfig())
```

### Integration Tests

```go
// Setup test environment
env, err := testing.SetupIntegrationEnvironment(ctx, dbURL, redisURL, logger)
if err != nil {
    return err
}
defer env.Cleanup()

// Run integration test
suite := testing.TestSuite{
    Name: "MyService",
    IntegrationTests: []testing.IntegrationTest{
        {
            Name: "TestDatabaseIntegration",
            Test: func(ctx context.Context, t *testing.T) error {
                // Use env.DB, env.Redis
                return nil
            },
            Requires: []string{"database", "redis"},
        },
    },
}
```

### Performance Benchmarks

```go
// Latency benchmark
result, err := testing.RunLatencyBenchmark(ctx, "MyOperation", 10*time.Second, func() error {
    return MyOperation()
})

// Throughput benchmark
result, err := testing.RunThroughputBenchmark(ctx, "MyOperation", 10*time.Second, 100, func() error {
    return MyOperation()
})

fmt.Printf("Ops/sec: %.2f, P99: %v\n", result.OpsPerSecond, result.P99Latency)
```

## Best Practices

1. **Unit Tests**: Fast, isolated, no external dependencies
2. **Integration Tests**: Test service interactions, use test databases
3. **Benchmarks**: Measure real performance, set targets
4. **Cleanup**: Always clean up test data
5. **Timeouts**: Set appropriate timeouts for all tests

## Integration

This library can be used in all Go services:

```go
// In service_test.go
func TestMyService(t *testing.T) {
    suite := testing.TestSuite{
        Name: "MyService",
        UnitTests: []testing.UnitTest{
            {
                Name: "TestOperation",
                Test: func(t *testing.T) {
                    // Test code
                },
            },
        },
    }

    err := testing.RunTestSuite(context.Background(), suite, testing.DefaultTestConfig())
    if err != nil {
        t.Fatal(err)
    }
}
```
