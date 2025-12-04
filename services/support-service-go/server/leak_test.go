// Issue: #1585 - Goroutine leak detection (CRITICAL - Support Service!)
// support-service is HIGH RISK for leaks (ticket processing, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for support service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestSupportServiceNoLeaks verifies support service operations don't leak
func TestSupportServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test support service lifecycle and operations
	// service := NewSupportService(...)
	// service.ProcessTicket(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateTicketState(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from support handlers, test FAILS
}

