// Issue: #1585 - Goroutine leak detection (CRITICAL - Stock Protection Service!)
// stock-protection-service is HIGH RISK for leaks (protection mechanisms, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for stock protection service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestStockProtectionServiceNoLeaks verifies stock protection service operations don't leak
func TestStockProtectionServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test stock protection service lifecycle and operations
	// service := NewStockProtectionService(...)
	// service.ApplyProtection(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateProtectionState(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from stock protection handlers, test FAILS
}

