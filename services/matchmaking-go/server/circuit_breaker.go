// SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1588 - Circuit Breaker for DB connections
package server

import (
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


