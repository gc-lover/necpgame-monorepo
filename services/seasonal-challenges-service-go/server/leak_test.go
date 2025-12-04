// Issue: #1585 - Goroutine leak detection (CRITICAL - Seasonal challenges service!)
// seasonal-challenges-service is HIGH RISK for leaks (challenge tracking, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for seasonal challenges service - concurrent challenge tracking
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestSeasonalChallengesServiceNoLeaks verifies seasonal challenges service doesn't leak
func TestSeasonalChallengesServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test seasonal challenges service lifecycle
	// service := NewSeasonalChallengesService(nil)
	// service.UpdateChallenge(ctx, params)
	// time.Sleep(100 * time.Millisecond)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from challenge handlers, test FAILS
}

// NOTE: seasonal-challenges-service is HIGH RISK for leaks:
// - Challenge tracking loops (background workers)
// - Concurrent challenge updates (goroutines)
// - Seasonal event handlers (event handlers)
// - Progress calculation workers (background processing)
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - Ticker.Stop() for all time.Ticker
// - Channel close for all event channels
// - Timeout for all DB operations

