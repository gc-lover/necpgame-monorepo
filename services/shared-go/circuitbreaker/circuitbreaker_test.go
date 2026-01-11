// Circuit Breaker Tests
// Issue: #2156

package circuitbreaker

import (
	"context"
	"errors"
	"testing"
	"time"
)

func TestCircuitBreaker_DefaultConfig(t *testing.T) {
	cb := New(DefaultConfig())

	if cb.State() != StateClosed {
		t.Errorf("Expected state Closed, got %s", cb.State())
	}

	if !cb.Allow() {
		t.Error("Circuit breaker should allow requests in closed state")
	}
}

func TestCircuitBreaker_OpenOnFailures(t *testing.T) {
	config := DefaultConfig()
	config.FailureThreshold = 3
	cb := New(config)

	// Record failures
	for i := 0; i < 3; i++ {
		cb.RecordFailure()
	}

	// Circuit should be open
	if cb.State() != StateOpen {
		t.Errorf("Expected state Open, got %s", cb.State())
	}

	// Should not allow requests
	if cb.Allow() {
		t.Error("Circuit breaker should not allow requests in open state")
	}
}

func TestCircuitBreaker_HalfOpenAfterTimeout(t *testing.T) {
	config := DefaultConfig()
	config.FailureThreshold = 1
	config.Timeout = 100 * time.Millisecond
	cb := New(config)

	// Open the circuit
	cb.RecordFailure()
	if cb.State() != StateOpen {
		t.Fatalf("Expected state Open, got %s", cb.State())
	}

	// Wait for timeout
	time.Sleep(150 * time.Millisecond)

	// Should transition to half-open
	if !cb.Allow() {
		t.Error("Circuit breaker should allow requests after timeout")
	}

	if cb.State() != StateHalfOpen {
		t.Errorf("Expected state HalfOpen, got %s", cb.State())
	}
}

func TestCircuitBreaker_CloseOnSuccesses(t *testing.T) {
	config := DefaultConfig()
	config.FailureThreshold = 1
	config.SuccessThreshold = 2
	config.Timeout = 50 * time.Millisecond
	cb := New(config)

	// Open the circuit
	cb.RecordFailure()
	time.Sleep(100 * time.Millisecond)

	// Transition to half-open
	cb.Allow()

	// Record successes
	cb.RecordSuccess()
	cb.RecordSuccess()

	// Circuit should be closed
	if cb.State() != StateClosed {
		t.Errorf("Expected state Closed, got %s", cb.State())
	}
}

func TestCircuitBreaker_Execute(t *testing.T) {
	cb := New(DefaultConfig())

	// Successful execution
	err := cb.Execute(context.Background(), func() error {
		return nil
	})
	if err != nil {
		t.Errorf("Expected no error, got %v", err)
	}

	// Failed execution
	err = cb.Execute(context.Background(), func() error {
		return errors.New("test error")
	})
	if err == nil {
		t.Error("Expected error, got nil")
	}
}

func TestCircuitBreaker_Metrics(t *testing.T) {
	cb := New(DefaultConfig())

	// Execute some operations
	cb.Execute(context.Background(), func() error { return nil })
	cb.Execute(context.Background(), func() error { return errors.New("error") })

	metrics := cb.GetMetrics()
	if metrics.TotalRequests != 2 {
		t.Errorf("Expected 2 total requests, got %d", metrics.TotalRequests)
	}
	if metrics.TotalSuccesses != 1 {
		t.Errorf("Expected 1 success, got %d", metrics.TotalSuccesses)
	}
	if metrics.TotalFailures != 1 {
		t.Errorf("Expected 1 failure, got %d", metrics.TotalFailures)
	}
}

func TestCircuitBreaker_Reset(t *testing.T) {
	config := DefaultConfig()
	config.FailureThreshold = 1
	cb := New(config)

	// Open the circuit
	cb.RecordFailure()
	if cb.State() != StateOpen {
		t.Fatalf("Expected state Open, got %s", cb.State())
	}

	// Reset
	cb.Reset()

	if cb.State() != StateClosed {
		t.Errorf("Expected state Closed after reset, got %s", cb.State())
	}

	metrics := cb.GetMetrics()
	if metrics.Failures != 0 {
		t.Errorf("Expected 0 failures after reset, got %d", metrics.Failures)
	}
}
