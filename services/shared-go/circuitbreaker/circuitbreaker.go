// Circuit Breaker Pattern Implementation
// Issue: #2156
// PERFORMANCE: Fault tolerance, graceful degradation, service resilience
// Enterprise-grade circuit breaker for all Go services

package circuitbreaker

import (
	"context"
	"errors"
	"sync"
	"sync/atomic"
	"time"
)

// State represents the circuit breaker state
type State int32

const (
	StateClosed State = iota
	StateOpen
	StateHalfOpen
)

// String returns the string representation of the state
func (s State) String() string {
	switch s {
	case StateClosed:
		return "closed"
	case StateOpen:
		return "open"
	case StateHalfOpen:
		return "half-open"
	default:
		return "unknown"
	}
}

// Config holds circuit breaker configuration
type Config struct {
	// FailureThreshold is the number of failures before opening the circuit
	FailureThreshold int

	// SuccessThreshold is the number of successes in half-open state to close the circuit
	SuccessThreshold int

	// Timeout is the duration to wait before attempting to close the circuit
	Timeout time.Duration

	// HalfOpenMaxCalls is the maximum number of calls allowed in half-open state
	HalfOpenMaxCalls int

	// OnStateChange is called when the circuit breaker state changes
	OnStateChange func(from, to State)
}

// DefaultConfig returns a default circuit breaker configuration
func DefaultConfig() Config {
	return Config{
		FailureThreshold: 5,
		SuccessThreshold: 2,
		Timeout:          30 * time.Second,
		HalfOpenMaxCalls: 3,
	}
}

// CircuitBreaker provides fault tolerance and graceful degradation
// PERFORMANCE: Memory-aligned struct for cache efficiency
//go:align 64
type CircuitBreaker struct {
	config Config

	// State management (atomic for lock-free reads)
	state int32 // State (atomic)

	// Failure tracking
	failures     int64 // Atomic counter
	lastFailure  int64 // Atomic timestamp (nanoseconds)
	successes    int64 // Atomic counter (for half-open state)
	halfOpenCalls int64 // Atomic counter

	// Mutex for state transitions
	mu sync.RWMutex

	// Metrics
	totalRequests int64 // Atomic
	totalFailures int64 // Atomic
	totalSuccesses int64 // Atomic
	stateChanges   int64 // Atomic
}

// New creates a new circuit breaker with the given configuration
func New(config Config) *CircuitBreaker {
	if config.FailureThreshold <= 0 {
		config.FailureThreshold = 5
	}
	if config.SuccessThreshold <= 0 {
		config.SuccessThreshold = 2
	}
	if config.Timeout <= 0 {
		config.Timeout = 30 * time.Second
	}
	if config.HalfOpenMaxCalls <= 0 {
		config.HalfOpenMaxCalls = 3
	}

	cb := &CircuitBreaker{
		config: config,
		state:  int32(StateClosed),
	}

	return cb
}

// Execute executes a function with circuit breaker protection
func (cb *CircuitBreaker) Execute(ctx context.Context, fn func() error) error {
	atomic.AddInt64(&cb.totalRequests, 1)

	// Check if circuit is open
	if !cb.Allow() {
		atomic.AddInt64(&cb.totalFailures, 1)
		return errors.New("circuit breaker is open: service temporarily unavailable")
	}

	// Execute the function
	err := fn()

	if err != nil {
		cb.RecordFailure()
		atomic.AddInt64(&cb.totalFailures, 1)
		return err
	}

	cb.RecordSuccess()
	atomic.AddInt64(&cb.totalSuccesses, 1)
	return nil
}

// Allow checks if the circuit breaker allows the request
// PERFORMANCE: Lock-free read path for hot path
func (cb *CircuitBreaker) Allow() bool {
	state := State(atomic.LoadInt32(&cb.state))

	switch state {
	case StateClosed:
		return true

	case StateOpen:
		// Check if timeout has passed
		lastFailure := atomic.LoadInt64(&cb.lastFailure)
		if lastFailure == 0 {
			return false
		}

		timeoutNanos := cb.config.Timeout.Nanoseconds()
		if time.Now().UnixNano()-lastFailure < timeoutNanos {
			return false
		}

		// Try to transition to half-open
		cb.mu.Lock()
		defer cb.mu.Unlock()

		// Double-check after acquiring lock
		if atomic.LoadInt32(&cb.state) == int32(StateOpen) {
			cb.setState(StateHalfOpen)
			atomic.StoreInt64(&cb.halfOpenCalls, 0)
			atomic.StoreInt64(&cb.successes, 0)
			return true
		}

		return State(atomic.LoadInt32(&cb.state)) == StateHalfOpen

	case StateHalfOpen:
		// Limit calls in half-open state
		calls := atomic.LoadInt64(&cb.halfOpenCalls)
		if calls >= int64(cb.config.HalfOpenMaxCalls) {
			return false
		}

		atomic.AddInt64(&cb.halfOpenCalls, 1)
		return true

	default:
		return false
	}
}

// RecordFailure records a failure and may open the circuit
func (cb *CircuitBreaker) RecordFailure() {
	atomic.StoreInt64(&cb.lastFailure, time.Now().UnixNano())
	failures := atomic.AddInt64(&cb.failures, 1)

	state := State(atomic.LoadInt32(&cb.state))

	switch state {
	case StateClosed:
		// Check if threshold is reached
		if failures >= int64(cb.config.FailureThreshold) {
			cb.mu.Lock()
			defer cb.mu.Unlock()

			// Double-check after acquiring lock
			if atomic.LoadInt32(&cb.state) == int32(StateClosed) {
				cb.setState(StateOpen)
			}
		}

	case StateHalfOpen:
		// Any failure in half-open state opens the circuit
		cb.mu.Lock()
		defer cb.mu.Unlock()

		if atomic.LoadInt32(&cb.state) == int32(StateHalfOpen) {
			cb.setState(StateOpen)
			atomic.StoreInt64(&cb.halfOpenCalls, 0)
			atomic.StoreInt64(&cb.successes, 0)
		}
	}
}

// RecordSuccess records a success and may close the circuit
func (cb *CircuitBreaker) RecordSuccess() {
	state := State(atomic.LoadInt32(&cb.state))

	switch state {
	case StateClosed:
		// Reset failure count on success
		atomic.StoreInt64(&cb.failures, 0)

	case StateHalfOpen:
		successes := atomic.AddInt64(&cb.successes, 1)

		// Check if threshold is reached
		if successes >= int64(cb.config.SuccessThreshold) {
			cb.mu.Lock()
			defer cb.mu.Unlock()

			// Double-check after acquiring lock
			if atomic.LoadInt32(&cb.state) == int32(StateHalfOpen) {
				cb.setState(StateClosed)
				atomic.StoreInt64(&cb.failures, 0)
				atomic.StoreInt64(&cb.halfOpenCalls, 0)
				atomic.StoreInt64(&cb.successes, 0)
			}
		}
	}
}

// setState sets the circuit breaker state (must be called with lock held)
func (cb *CircuitBreaker) setState(newState State) {
	oldState := State(atomic.LoadInt32(&cb.state))
	if oldState == newState {
		return
	}

	atomic.StoreInt32(&cb.state, int32(newState))
	atomic.AddInt64(&cb.stateChanges, 1)

	if cb.config.OnStateChange != nil {
		cb.config.OnStateChange(oldState, newState)
	}
}

// State returns the current circuit breaker state
func (cb *CircuitBreaker) State() State {
	return State(atomic.LoadInt32(&cb.state))
}

// Metrics returns circuit breaker metrics
type Metrics struct {
	State          State
	Failures       int64
	Successes      int64
	TotalRequests  int64
	TotalFailures  int64
	TotalSuccesses int64
	StateChanges   int64
	LastFailure    time.Time
}

// GetMetrics returns current circuit breaker metrics
func (cb *CircuitBreaker) GetMetrics() Metrics {
	lastFailureNanos := atomic.LoadInt64(&cb.lastFailure)
	var lastFailure time.Time
	if lastFailureNanos > 0 {
		lastFailure = time.Unix(0, lastFailureNanos)
	}

	return Metrics{
		State:          cb.State(),
		Failures:       atomic.LoadInt64(&cb.failures),
		Successes:      atomic.LoadInt64(&cb.successes),
		TotalRequests:  atomic.LoadInt64(&cb.totalRequests),
		TotalFailures:  atomic.LoadInt64(&cb.totalFailures),
		TotalSuccesses: atomic.LoadInt64(&cb.totalSuccesses),
		StateChanges:   atomic.LoadInt64(&cb.stateChanges),
		LastFailure:    lastFailure,
	}
}

// Reset resets the circuit breaker to closed state
func (cb *CircuitBreaker) Reset() {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	cb.setState(StateClosed)
	atomic.StoreInt64(&cb.failures, 0)
	atomic.StoreInt64(&cb.successes, 0)
	atomic.StoreInt64(&cb.halfOpenCalls, 0)
	atomic.StoreInt64(&cb.lastFailure, 0)
}
