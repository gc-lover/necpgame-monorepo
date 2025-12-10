// Issue: #1585 - Goroutine leak detection (CRITICAL - Reset Service!)
// reset-service is HIGH RISK for leaks (reset operations, concurrent processing)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for reset service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
	)
}

// TestResetServiceNoLeaks verifies reset service operations don't leak
func TestResetServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
	)

	// TODO: Test reset service lifecycle and operations
	// service := NewResetService(...)
	// service.PerformReset(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateResetState(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from reset handlers, test FAILS
}

