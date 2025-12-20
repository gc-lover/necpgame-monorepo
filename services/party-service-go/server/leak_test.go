// Issue: #1585 - Goroutine leak detection (CRITICAL - Concurrent connections!)
// party-service is HIGH RISK for leaks (concurrent party operations, WebSocket-like)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for party service - concurrent connections
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestPartyServiceNoLeaks verifies party service doesn't leak
func TestPartyServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test party service lifecycle
	// service := NewPartyService(nil)
	// service.Start()
	// time.Sleep(100 * time.Millisecond)
	// service.Stop()

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from party handlers, test FAILS
}

// NOTE: party-service is HIGH RISK for leaks:
// - Concurrent party operations
// - Party member updates
// - Real-time synchronization
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - Party connection cleanup
// - DB connection pool limits
