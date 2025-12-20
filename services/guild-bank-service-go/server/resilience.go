// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1588 - Circuit breaker and resilience patterns
package server

import (
	"context"
	"time"

	"github.com/sony/gobreaker"
)

// CircuitBreaker provides circuit breaker functionality
type CircuitBreaker struct {
	cb *gobreaker.CircuitBreaker
}

// NewCircuitBreaker creates new circuit breaker

// Execute executes function with circuit breaker protection
func (cb *CircuitBreaker) Execute(fn func() (interface{}, error)) (interface{}, error) {
	return cb.cb.Execute(fn)
}

// ExecuteWithContext executes function with context and circuit breaker protection
func (cb *CircuitBreaker) ExecuteWithContext(ctx context.Context, fn func() (interface{}, error)) (interface{}, error) {
	// Check if context is cancelled before executing
	select {
	case <-ctx.Done():
		return nil, ctx.Err()
	default:
	}

	// Wrap function to respect context
	wrappedFn := func() (interface{}, error) {
		// Create channel to signal completion
		done := make(chan struct{})
		var result interface{}
		var err error

		go func() {
			defer close(done)
			result, err = fn()
		}()

		// Wait for completion or context cancellation
		select {
		case <-done:
			return result, err
		case <-ctx.Done():
			return nil, ctx.Err()
		}
	}

	return cb.cb.Execute(wrappedFn)
}

// RetryConfig holds retry configuration
type RetryConfig struct {
	MaxAttempts int
	BaseDelay   time.Duration
	MaxDelay    time.Duration
}

// DefaultRetryConfig returns default retry configuration

// Retry executes function with exponential backoff retry
