// Issue: #1588 - Circuit breaker and resilience patterns
package server

import (
	"context"
	"errors"
	"time"

	"github.com/sony/gobreaker"
)

// CircuitBreaker provides circuit breaker functionality
type CircuitBreaker struct {
	cb *gobreaker.CircuitBreaker
}

// NewCircuitBreaker creates new circuit breaker
func NewCircuitBreaker(name string) *CircuitBreaker {
	settings := gobreaker.Settings{
		Name:        name,
		MaxRequests: 3,
		Interval:    10 * time.Second,
		Timeout:     30 * time.Second,
		ReadyToTrip: func(counts gobreaker.Counts) bool {
			failureRatio := float64(counts.TotalFailures) / float64(counts.Requests)
			return counts.Requests >= 3 && failureRatio >= 0.6
		},
		OnStateChange: func(name string, from gobreaker.State, to gobreaker.State) {
			logger := GetLogger()
			logger.WithFields(map[string]interface{}{
				"circuit_breaker": name,
				"from_state":      from.String(),
				"to_state":        to.String(),
			}).Info("Circuit breaker state changed")
		},
	}

	return &CircuitBreaker{
		cb: gobreaker.NewCircuitBreaker(settings),
	}
}

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
func DefaultRetryConfig() RetryConfig {
	return RetryConfig{
		MaxAttempts: 3,
		BaseDelay:   100 * time.Millisecond,
		MaxDelay:    2 * time.Second,
	}
}

// Retry executes function with exponential backoff retry
func Retry(ctx context.Context, config RetryConfig, fn func() error) error {
	var lastErr error

	for attempt := 1; attempt <= config.MaxAttempts; attempt++ {
		// Check context cancellation
		select {
		case <-ctx.Done():
			return ctx.Err()
		default:
		}

		// Execute function
		err := fn()
		if err == nil {
			return nil
		}

		lastErr = err

		// Don't retry on last attempt
		if attempt == config.MaxAttempts {
			break
		}

		// Calculate delay with exponential backoff
		delay := config.BaseDelay * time.Duration(1<<(attempt-1))
		if delay > config.MaxDelay {
			delay = config.MaxDelay
		}

		// Wait or context cancellation
		select {
		case <-time.After(delay):
			continue
		case <-ctx.Done():
			return ctx.Err()
		}
	}

	return errors.New("max retry attempts exceeded: " + lastErr.Error())
}