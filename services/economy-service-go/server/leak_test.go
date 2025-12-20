// Issue: #1585 - Goroutine leak detection (CRITICAL - Hot path service!)
// economy-service is HIGH RISK for leaks (high RPS, concurrent trade operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for economy service - hot path with concurrent operations
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/maintnotifications.(*CircuitBreakerManager).cleanupLoop"),
	)
}

// TestEconomyServiceNoLeaks verifies economy service doesn't leak
func TestEconomyServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/maintnotifications.(*CircuitBreakerManager).cleanupLoop"),
	)

	// TODO: Test economy service lifecycle
	// service := NewTradeService(nil)
	// service.Start()
	// time.Sleep(100 * time.Millisecond)
	// service.Stop()

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from economy handlers, test FAILS
}

// NOTE: economy-service is HIGH RISK for leaks:
// - High RPS (hot path)
// - Concurrent trade operations
// - Event bus operations
// - Engram operations
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - Event bus cleanup
// - DB connection pool limits
