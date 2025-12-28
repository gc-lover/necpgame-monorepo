// Issue: Implement integration-domain-service-go
// Circuit breaker implementation for resilient service communication
// Enterprise-grade circuit breaker with configurable timeouts and retry logic

package service

import (
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/pkg/models"
)

// CircuitBreakerState represents the state of a circuit breaker
type CircuitBreakerState int

const (
	StateClosed CircuitBreakerState = iota
	StateOpen
	StateHalfOpen
)

// CircuitBreakerResult represents the result of a circuit breaker operation
type CircuitBreakerResult struct {
	Value interface{}
	Error error
}

// CircuitBreaker implements the circuit breaker pattern
type CircuitBreaker struct {
	timeout       time.Duration
	maxRequests   uint32
	interval      time.Duration
	state         CircuitBreakerState
	failureCount  int
	lastFailure   time.Time
	nextRetry     time.Time
	successCount  int
	requests      uint32
	mu            sync.RWMutex
}

// NewCircuitBreaker creates a new circuit breaker
func NewCircuitBreaker(timeout time.Duration, maxRequests uint32, interval time.Duration) *CircuitBreaker {
	return &CircuitBreaker{
		timeout:     timeout,
		maxRequests: maxRequests,
		interval:    interval,
		state:       StateClosed,
	}
}

// Call executes a function with circuit breaker protection
func (cb *CircuitBreaker) Call(fn func() (interface{}, error)) CircuitBreakerResult {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	switch cb.state {
	case StateOpen:
		if time.Now().Before(cb.nextRetry) {
			return CircuitBreakerResult{Error: &CircuitBreakerError{Message: "circuit breaker is open"}}
		}
		cb.state = StateHalfOpen
		fallthrough

	case StateHalfOpen:
		if cb.requests >= cb.maxRequests {
			return CircuitBreakerResult{Error: &CircuitBreakerError{Message: "circuit breaker max requests reached"}}
		}
		cb.requests++

	case StateClosed:
		// Allow request
	}

	result := cb.executeWithTimeout(fn)

	cb.mu.Lock()
	defer cb.mu.Unlock()

	if result.Error != nil {
		cb.recordFailure()
	} else {
		cb.recordSuccess()
	}

	return result
}

// executeWithTimeout executes a function with timeout
func (cb *CircuitBreaker) executeWithTimeout(fn func() (interface{}, error)) CircuitBreakerResult {
	done := make(chan CircuitBreakerResult, 1)

	go func() {
		value, err := fn()
		done <- CircuitBreakerResult{Value: value, Error: err}
	}()

	select {
	case result := <-done:
		return result
	case <-time.After(cb.timeout):
		return CircuitBreakerResult{Error: &CircuitBreakerError{Message: "timeout"}}
	}
}

// recordFailure records a failure and potentially opens the circuit
func (cb *CircuitBreaker) recordFailure() {
	cb.failureCount++
	cb.lastFailure = time.Now()

	if cb.state == StateHalfOpen {
		cb.state = StateOpen
		cb.nextRetry = time.Now().Add(cb.interval)
		cb.requests = 0
	} else if cb.failureCount >= 5 { // Threshold for opening circuit
		cb.state = StateOpen
		cb.nextRetry = time.Now().Add(cb.interval)
	}
}

// recordSuccess records a success and potentially closes the circuit
func (cb *CircuitBreaker) recordSuccess() {
	cb.successCount++

	if cb.state == StateHalfOpen {
		cb.requests--
		if cb.requests == 0 {
			cb.state = StateClosed
			cb.failureCount = 0
			cb.successCount = 0
		}
	} else if cb.state == StateClosed {
		cb.failureCount = 0
	}
}

// GetState returns the current state of the circuit breaker
func (cb *CircuitBreaker) GetState(name string) models.CircuitBreakerState {
	cb.mu.RLock()
	defer cb.mu.RUnlock()

	stateStr := "closed"
	switch cb.state {
	case StateOpen:
		stateStr = "open"
	case StateHalfOpen:
		stateStr = "half-open"
	}

	return models.CircuitBreakerState{
		Name:         name,
		State:        stateStr,
		FailureCount: cb.failureCount,
		LastFailure:  cb.lastFailure,
		NextRetry:    cb.nextRetry,
		SuccessCount: cb.successCount,
		Timeout:      cb.timeout,
		MaxRequests:  cb.maxRequests,
		Interval:     cb.interval,
	}
}

// CircuitBreakerError represents a circuit breaker error
type CircuitBreakerError struct {
	Message string
}

func (e *CircuitBreakerError) Error() string {
	return e.Message
}

