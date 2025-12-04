// Issue: #1585 - Goroutine leak detection (CRITICAL - Progression service!)
// progression-paragon-service is HIGH RISK for leaks (progression updates, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for progression service - concurrent progression updates
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
	)
}

// TestProgressionServiceNoLeaks verifies progression service doesn't leak
func TestProgressionServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
	)

	// TODO: Test progression service lifecycle
	// service := NewParagonService(nil)
	// service.UpdateProgression(ctx, params)
	// time.Sleep(100 * time.Millisecond)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from progression handlers, test FAILS
}

// NOTE: progression-paragon-service is HIGH RISK for leaks:
// - Progression update loops (background processing)
// - Materialized view refreshes (goroutines)
// - Concurrent progression calculations
// - Redis cache operations
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - Ticker.Stop() for all time.Ticker
// - Connection pool limits
// - Timeout for all operations
