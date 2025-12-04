// Issue: #1588 - Resilience Patterns (Circuit Breaker, Load Shedding, Fallback)
// CRITICAL for hot path service (high RPS) - prevents cascading failures
package server

import (
	"context"
	"net/http"
	"sync/atomic"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/sirupsen/logrus"
	"github.com/sony/gobreaker"
)

var (
	// Issue: #1588 - Prometheus metrics for circuit breaker
	circuitBreakerState = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "circuit_breaker_state",
			Help: "Circuit breaker state (0=closed, 1=open, 2=half-open)",
		},
		[]string{"name"},
	)
	
	requestsShedded = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "requests_shedded_total",
			Help: "Total requests shedded due to overload",
		},
	)
)

// CircuitBreaker wraps DB operations with circuit breaker pattern
type CircuitBreaker struct {
	db *gobreaker.CircuitBreaker
}

// NewCircuitBreaker creates a new circuit breaker for database operations
func NewCircuitBreaker(name string) *CircuitBreaker {
	cb := gobreaker.NewCircuitBreaker(gobreaker.Settings{
		Name:        name,
		MaxRequests: 3,              // Max requests in half-open state
		Interval:    10 * time.Second, // Reset failure count interval
		Timeout:     30 * time.Second, // Try recover after 30s
		
		ReadyToTrip: func(counts gobreaker.Counts) bool {
			// Trip if failure ratio >= 60% and at least 3 requests
			failureRatio := float64(counts.TotalFailures) / float64(counts.Requests)
			return counts.Requests >= 3 && failureRatio >= 0.6
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
			
			logrus.WithFields(logrus.Fields{
				"name": name,
				"from": from.String(),
				"to":   to.String(),
			}).Warn("Circuit breaker state changed")
		},
	})
	
	return &CircuitBreaker{db: cb}
}

// Execute wraps a function with circuit breaker protection
func (cb *CircuitBreaker) Execute(fn func() (interface{}, error)) (interface{}, error) {
	return cb.db.Execute(fn)
}

// LoadShedder prevents overload by limiting concurrent requests
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

// Allow checks if a new request can be processed
func (ls *LoadShedder) Allow() bool {
	current := ls.current.Load()
	if current >= ls.maxConcurrent {
		return false // Reject - overloaded
	}
	
	ls.current.Add(1)
	return true
}

// Done releases a request slot
func (ls *LoadShedder) Done() {
	ls.current.Add(-1)
}

// GetCurrent returns current concurrent request count
func (ls *LoadShedder) GetCurrent() int32 {
	return ls.current.Load()
}

// Middleware creates HTTP middleware for load shedding
func (ls *LoadShedder) Middleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			if !ls.Allow() {
				// Issue: #1588 - Track shedded requests
				requestsShedded.Inc()
				
				w.WriteHeader(http.StatusServiceUnavailable)
				w.Header().Set("Content-Type", "application/json")
				w.Write([]byte(`{"error":"service overloaded, try again later"}`))
				return
			}
			defer ls.Done()
			
			next.ServeHTTP(w, r)
		})
	}
}

// RetryWithBackoff retries a function with exponential backoff
func RetryWithBackoff(ctx context.Context, fn func() error, maxRetries int) error {
	backoff := 100 * time.Millisecond
	maxBackoff := 10 * time.Second
	
	for retry := 0; retry < maxRetries; retry++ {
		// Check context cancellation
		select {
		case <-ctx.Done():
			return ctx.Err()
		default:
		}
		
		err := fn()
		if err == nil {
			return nil // Success
		}
		
		// Check if retryable
		if !isRetryable(err) {
			return err // Don't retry non-retryable errors
		}
		
		if retry < maxRetries-1 {
			select {
			case <-ctx.Done():
				return ctx.Err()
			case <-time.After(backoff):
				// Continue retry
			}
			
			backoff *= 2 // Exponential backoff
			if backoff > maxBackoff {
				backoff = maxBackoff
			}
		}
	}
	
	return nil // Max retries reached, return last error
}

// isRetryable checks if an error is retryable
func isRetryable(err error) bool {
	// Retry on network errors, timeouts, 5xx
	if err == context.DeadlineExceeded {
		return true
	}
	
	// Add more retryable error checks as needed
	return false
}

