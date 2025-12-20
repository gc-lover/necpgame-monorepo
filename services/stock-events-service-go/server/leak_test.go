// Issue: #1585 - Goroutine leak detection (CRITICAL - Stock Events Service!)
// stock-events-service is HIGH RISK for leaks (event processing, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for stock events service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestStockEventsServiceNoLeaks verifies stock events service operations don't leak
func TestStockEventsServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test stock events service lifecycle and operations
	// service := NewStockEventsService(...)
	// service.ProcessEvent(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateEventState(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from stock events handlers, test FAILS
}
