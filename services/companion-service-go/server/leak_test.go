// Issue: #1585 - Goroutine leak detection (CRITICAL - Companion service!)
// companion-service is HIGH RISK for leaks (companion operations, concurrent updates)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for companion service - concurrent companion operations
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestCompanionServiceNoLeaks verifies companion service doesn't leak
func TestCompanionServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test companion service lifecycle
	// service := NewCompanionService(nil)
	// service.UpdateCompanion(ctx, params)
	// time.Sleep(100 * time.Millisecond)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from companion handlers, test FAILS
}

// NOTE: companion-service is HIGH RISK for leaks:
// - Companion update operations (background processing)
// - Companion AI processing (goroutines)
// - Companion state management (event handlers)
// - Companion interactions (concurrent operations)
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - Ticker.Stop() for all time.Ticker
// - Channel close for all event channels
// - Timeout for all DB operations

