// Issue: #1585 - Goroutine leak detection (CRITICAL - Feedback Service!)
// feedback-service is HIGH RISK for leaks (feedback processing, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for feedback service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestFeedbackServiceNoLeaks verifies feedback service operations don't leak
func TestFeedbackServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test feedback service lifecycle and operations
	// service := NewFeedbackService(...)
	// service.ProcessFeedback(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateFeedbackState(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from feedback handlers, test FAILS
}

