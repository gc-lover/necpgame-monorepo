// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1588 - Resilience Patterns (Circuit Breaker, Load Shedding, Fallback)
// CRITICAL for hot path service - prevents cascading failures
package server

import (
	"sync/atomic"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/sony/gobreaker"
)

var (
	// Issue: #1588 - Prometheus metrics for circuit breaker
	_ = promauto.NewGaugeVec(
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
		// Issue: #1588 - Track shedded requests
		requestsShedded.Inc()
		return false
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

// RetryWithBackoff retries a function with exponential backoff

// isRetryable checks if an error is retryable
