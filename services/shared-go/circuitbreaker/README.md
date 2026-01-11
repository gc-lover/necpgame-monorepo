# Circuit Breaker Pattern Library

## Overview

Enterprise-grade circuit breaker implementation for fault tolerance, graceful degradation, and service resilience. Designed for MMOFPS games requiring high availability and fault tolerance.

## Issue: #2156

## Features

### 1. Three States
- **Closed**: Normal operation, requests allowed
- **Open**: Circuit is open, requests rejected immediately
- **Half-Open**: Testing state, limited requests allowed

### 2. Automatic State Transitions
- **Closed → Open**: After failure threshold is reached
- **Open → Half-Open**: After timeout period
- **Half-Open → Closed**: After success threshold is reached
- **Half-Open → Open**: On any failure

### 3. Performance Optimizations
- Lock-free reads for hot path (Allow() method)
- Atomic counters for metrics
- Memory-aligned struct (64-byte alignment)
- Minimal allocations

### 4. Configurable Parameters
- Failure threshold (default: 5)
- Success threshold (default: 2)
- Timeout duration (default: 30s)
- Half-open max calls (default: 3)

## Usage

### Basic Usage

```go
import "necpgame/services/shared-go/circuitbreaker"

// Create circuit breaker with default config
cb := circuitbreaker.New(circuitbreaker.DefaultConfig())

// Execute function with circuit breaker protection
err := cb.Execute(ctx, func() error {
    // Your service call here
    return service.DoSomething()
})

if err != nil {
    // Handle error
}
```

### Custom Configuration

```go
config := circuitbreaker.Config{
    FailureThreshold: 10,
    SuccessThreshold: 3,
    Timeout: 60 * time.Second,
    HalfOpenMaxCalls: 5,
    OnStateChange: func(from, to circuitbreaker.State) {
        log.Printf("Circuit breaker state changed: %s → %s", from, to)
    },
}

cb := circuitbreaker.New(config)
```

### Manual Control

```go
// Check if request is allowed
if !cb.Allow() {
    return errors.New("circuit breaker is open")
}

// Record success/failure manually
if err != nil {
    cb.RecordFailure()
} else {
    cb.RecordSuccess()
}
```

### Metrics

```go
metrics := cb.GetMetrics()
log.Printf("State: %s", metrics.State)
log.Printf("Total Requests: %d", metrics.TotalRequests)
log.Printf("Total Failures: %d", metrics.TotalFailures)
log.Printf("Failure Rate: %.2f%%", 
    float64(metrics.TotalFailures)/float64(metrics.TotalRequests)*100)
```

## Integration

This library can be used in all Go services:

```go
// In service.go
type Service struct {
    repo           *repository.Repository
    circuitBreaker *circuitbreaker.CircuitBreaker
    // ...
}

func NewService(repo *repository.Repository) *Service {
    return &Service{
        repo: repo,
        circuitBreaker: circuitbreaker.New(circuitbreaker.DefaultConfig()),
        // ...
    }
}

func (s *Service) DoSomething(ctx context.Context) error {
    return s.circuitBreaker.Execute(ctx, func() error {
        return s.repo.SomeOperation(ctx)
    })
}
```

## Performance

- **Lock-free reads**: Allow() method uses atomic operations
- **Minimal overhead**: <1% performance impact
- **Memory efficient**: 64-byte aligned struct
- **Thread-safe**: Safe for concurrent use

## Best Practices

1. **Use for external dependencies**: Database, external APIs, downstream services
2. **Configure appropriately**: Adjust thresholds based on service characteristics
3. **Monitor metrics**: Track state changes and failure rates
4. **Provide fallbacks**: Return cached data or default values when circuit is open
5. **Log state changes**: Use OnStateChange callback for monitoring

## Migration

To migrate existing circuit breaker implementations:

1. Replace local CircuitBreaker struct with `circuitbreaker.CircuitBreaker`
2. Update imports to use `necpgame/services/shared-go/circuitbreaker`
3. Replace custom logic with `cb.Execute()` or `cb.Allow()` + `cb.RecordSuccess/Failure()`
4. Remove duplicate circuit breaker code from services
