# Resilience Patterns Template

**Issue:** #1588

## Circuit Breaker

**File:** `server/circuit_breaker.go`

```go
// Issue: #1588 - Circuit Breaker for DB connections
package server

import (
	"context"
	"time"

	"github.com/sony/gobreaker"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	circuitBreakerState = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "circuit_breaker_state",
			Help: "Circuit breaker state (0=closed, 1=open, 2=half-open)",
		},
		[]string{"name"},
	)
)

// DBCircuitBreaker wraps DB operations with circuit breaker
type DBCircuitBreaker struct {
	cb *gobreaker.CircuitBreaker
}

// NewDBCircuitBreaker creates a new circuit breaker for DB
func NewDBCircuitBreaker(name string) *DBCircuitBreaker {
	cb := gobreaker.NewCircuitBreaker(gobreaker.Settings{
		Name:        name,
		MaxRequests: 3,              // Max requests in half-open
		Interval:    10 * time.Second, // Reset failure count
		Timeout:     30 * time.Second, // Try recover after 30s
		
		ReadyToTrip: func(counts gobreaker.Counts) bool {
			if counts.Requests < 3 {
				return false
			}
			failureRatio := float64(counts.TotalFailures) / float64(counts.Requests)
			return failureRatio >= 0.6 // Open if 60%+ failures
		},
		
		OnStateChange: func(name string, from, to gobreaker.State) {
			// Update Prometheus metric
			stateValue := 0.0
			switch to {
			case gobreaker.StateOpen:
				stateValue = 1.0
			case gobreaker.StateHalfOpen:
				stateValue = 2.0
			}
			circuitBreakerState.WithLabelValues(name).Set(stateValue)
		},
	})
	
	return &DBCircuitBreaker{cb: cb}
}

// Execute runs operation with circuit breaker
func (dbcb *DBCircuitBreaker) Execute(fn func() (interface{}, error)) (interface{}, error) {
	return dbcb.cb.Execute(fn)
}
```

## Load Shedding

**File:** `server/load_shedder.go`

```go
// Issue: #1588 - Load Shedding middleware
package server

import (
	"encoding/json"
	"net/http"
	"sync/atomic"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	requestsShedded = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "requests_shedded_total",
			Help: "Total requests shedded due to overload",
		},
	)
)

// LoadShedder drops requests when overloaded
type LoadShedder struct {
	maxConcurrent int32
	current       atomic.Int32
}

// NewLoadShedder creates a new load shedder
func NewLoadShedder(maxConcurrent int) *LoadShedder {
	return &LoadShedder{
		maxConcurrent: int32(maxConcurrent),
	}
}

// Allow checks if request can be processed
func (ls *LoadShedder) Allow() bool {
	current := ls.current.Load()
	if current >= ls.maxConcurrent {
		requestsShedded.Inc()
		return false // Reject
	}
	
	ls.current.Add(1)
	return true
}

// Done releases a slot
func (ls *LoadShedder) Done() {
	ls.current.Add(-1)
}

// Middleware wraps HTTP handler with load shedding
func (ls *LoadShedder) Middleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !ls.Allow() {
			w.WriteHeader(http.StatusServiceUnavailable)
			json.NewEncoder(w).Encode(map[string]string{
				"error": "service overloaded, try again later",
			})
			return
		}
		defer ls.Done()
		
		next.ServeHTTP(w, r)
	})
}
```

## Retry with Exponential Backoff

**File:** `server/retry.go`

```go
// Issue: #1588 - Retry with Exponential Backoff
package server

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	retriesTotal = promauto.NewHistogram(
		prometheus.HistogramOpts{
			Name:    "operation_retries",
			Help:    "Number of retries per operation",
			Buckets: []float64{0, 1, 2, 3, 5, 10},
		},
	)
)

// RetryWithBackoff retries operation with exponential backoff
func RetryWithBackoff(ctx context.Context, fn func() error, maxRetries int) error {
	backoff := 100 * time.Millisecond
	maxBackoff := 10 * time.Second
	
	var retries int
	for retry := 0; retry < maxRetries; retry++ {
		err := fn()
		if err == nil {
			retriesTotal.Observe(float64(retry))
			return nil // Success
		}
		
		// Check if retryable
		if !isRetryable(err) {
			return err // Don't retry
		}
		
		if retry < maxRetries-1 {
			// Check context cancellation
			select {
			case <-ctx.Done():
				return ctx.Err()
			case <-time.After(backoff):
				// Continue retry
			}
			
			backoff *= 2
			if backoff > maxBackoff {
				backoff = maxBackoff
			}
			retries++
		}
	}
	
	retriesTotal.Observe(float64(retries))
	return fmt.Errorf("failed after %d retries", maxRetries)
}

// isRetryable checks if error is retryable
func isRetryable(err error) bool {
	// Retry on network errors, timeouts, 5xx
	if errors.Is(err, context.DeadlineExceeded) {
		return true
	}
	
	// Add more retryable error checks as needed
	return false
}
```

## Feature Flags

**File:** `server/feature_flags.go`

```go
// Issue: #1588 - Feature Flags for graceful degradation
package server

import (
	"sync"
)

// FeatureFlags manages feature toggles
type FeatureFlags struct {
	flags sync.Map
}

// NewFeatureFlags creates a new feature flags manager
func NewFeatureFlags() *FeatureFlags {
	return &FeatureFlags{}
}

// IsEnabled checks if feature is enabled
func (ff *FeatureFlags) IsEnabled(feature string) bool {
	enabled, ok := ff.flags.Load(feature)
	if !ok {
		return true // Default: enabled
	}
	return enabled.(bool)
}

// SetEnabled sets feature state
func (ff *FeatureFlags) SetEnabled(feature string, enabled bool) {
	ff.flags.Store(feature, enabled)
}

// Disable disables a feature
func (ff *FeatureFlags) Disable(feature string) {
	ff.flags.Store(feature, false)
}

// Enable enables a feature
func (ff *FeatureFlags) Enable(feature string) {
	ff.flags.Store(feature, true)
}
```

## Fallback Strategy

**File:** `server/fallback.go`

```go
// Issue: #1588 - Fallback Strategy for graceful degradation
package server

import (
	"context"
	"encoding/json"
	"log"

	"github.com/redis/go-redis/v9"
)

// FallbackStrategy implements 3-tier fallback (primary → cache → default)
type FallbackStrategy struct {
	primary func(ctx context.Context) (interface{}, error)
	cache   func(ctx context.Context) (interface{}, error)
	default func(ctx context.Context) (interface{}, error)
	logger  *log.Logger
}

// NewFallbackStrategy creates a new fallback strategy
func NewFallbackStrategy(
	primary func(ctx context.Context) (interface{}, error),
	cache func(ctx context.Context) (interface{}, error),
	defaultFn func(ctx context.Context) (interface{}, error),
) *FallbackStrategy {
	return &FallbackStrategy{
		primary: primary,
		cache:   cache,
		default: defaultFn,
	}
}

// Execute executes with fallback
func (fs *FallbackStrategy) Execute(ctx context.Context) (interface{}, error) {
	// Try primary (DB)
	result, err := fs.primary(ctx)
	if err == nil {
		return result, nil
	}
	
	log.Printf("Primary failed, trying cache: %v", err)
	
	// Fallback 1: Cache
	result, err = fs.cache(ctx)
	if err == nil {
		return result, nil
	}
	
	log.Printf("Cache failed, using default: %v", err)
	
	// Fallback 2: Default (degraded)
	return fs.default(ctx)
}
```

## Usage Example

**File:** `server/service.go` (example integration)

```go
// Issue: #1588 - Resilience patterns integration
package server

import (
	"context"
	"database/sql"
)

type Service struct {
	db          *sql.DB
	dbCB        *DBCircuitBreaker
	loadShedder *LoadShedder
	features    *FeatureFlags
}

func NewService(db *sql.DB) *Service {
	return &Service{
		db:          db,
		dbCB:        NewDBCircuitBreaker("database"),
		loadShedder: NewLoadShedder(1000), // Max 1000 concurrent
		features:    NewFeatureFlags(),
	}
}

func (s *Service) GetPlayer(ctx context.Context, playerID string) (*Player, error) {
	// Check feature flag
	if !s.features.IsEnabled("player_lookup") {
		return nil, ErrFeatureDisabled
	}
	
	// Use circuit breaker for DB query
	result, err := s.dbCB.Execute(func() (interface{}, error) {
		return s.db.QueryContext(ctx, "SELECT * FROM players WHERE id = $1", playerID)
	})
	
	if err != nil {
		return nil, err
	}
	
	return result.(*Player), nil
}
```

## Dependencies

**Add to `go.mod`:**

```go
require (
	github.com/sony/gobreaker v0.5.0
)
```

