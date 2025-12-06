// Issue: #1588 - Resilience Patterns (Circuit Breaker, Load Shedding, Fallback)
// CRITICAL for hot path service (1.5k+ RPS) - prevents cascading failures
package server

import (
	"context"
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
		MaxRequests: 3,
		Interval:    10 * time.Second,
		Timeout:     30 * time.Second,
		
		ReadyToTrip: func(counts gobreaker.Counts) bool {
			failureRatio := float64(counts.TotalFailures) / float64(counts.Requests)
			return counts.Requests >= 3 && failureRatio >= 0.6
		},
		
		OnStateChange: func(name string, from, to gobreaker.State) {
			logrus.WithFields(logrus.Fields{
				"name": name,
				"from": from.String(),
				"to":   to.String(),
			}).Warn("Circuit breaker state changed")
			
			// Issue: #1588 - Update Prometheus metric
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
func RetryWithBackoff(ctx context.Context, fn func() error, maxRetries int) error {
	backoff := 100 * time.Millisecond
	maxBackoff := 10 * time.Second
	
	for retry := 0; retry < maxRetries; retry++ {
		select {
		case <-ctx.Done():
			return ctx.Err()
		default:
		}
		
		err := fn()
		if err == nil {
			return nil
		}
		
		if !isRetryable(err) {
			return err
		}
		
		if retry < maxRetries-1 {
			select {
			case <-ctx.Done():
				return ctx.Err()
			case <-time.After(backoff):
			}
			
			backoff *= 2
			if backoff > maxBackoff {
				backoff = maxBackoff
			}
		}
	}
	
	return nil
}

// isRetryable checks if an error is retryable
func isRetryable(err error) bool {
	if err == context.DeadlineExceeded {
		return true
	}
	return false
}

