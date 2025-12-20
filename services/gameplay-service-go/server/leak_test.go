// Issue: #1585 - Goroutine leak detection (CRITICAL - Gameplay service!)
// gameplay-service is HIGH RISK for leaks (gameplay mechanics, concurrent operations)
package server

import (
	"testing"
	"time"

	"github.com/sirupsen/logrus"
	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for gameplay service - concurrent gameplay operations
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("net/http.(*persistConn).readLoop"),
		goleak.IgnoreTopFunction("net/http.(*persistConn).writeLoop"),
	)
}

// TestGoroutineMonitorNoLeaks verifies GoroutineMonitor doesn't leak
func TestGoroutineMonitorNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel) // Reduce noise in tests

	monitor := NewGoroutineMonitor(300, logger)
	go monitor.Start()

	// Let it run for a bit
	time.Sleep(50 * time.Millisecond)

	// Stop should clean up all goroutines
	monitor.Stop()

	// Give it time to clean up
	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked, test FAILS
}

// TestAffixSchedulerNoLeaks verifies AffixScheduler doesn't leak
func TestAffixSchedulerNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	// Create scheduler without DB (nil is OK for test)
	scheduler := NewAffixScheduler(nil, logger)
	if scheduler == nil {
		t.Skip("Scheduler requires DB connection")
		return
	}

	scheduler.Start()

	// Let it run for a bit
	time.Sleep(50 * time.Millisecond)

	// Stop should clean up all goroutines
	scheduler.Stop()

	// Give it time to clean up
	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked, test FAILS
}

// TestHandlersNoLeaks verifies handlers don't leak goroutines
func TestHandlersNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	handlers := NewHandlers(logger, nil)

	// Handlers should not create any goroutines
	// If they do, test FAILS
	_ = handlers

	time.Sleep(50 * time.Millisecond)
}

// NOTE: gameplay-service is HIGH RISK for leaks:
// - Gameplay event processing (background goroutines)
// - Weapon mechanics calculations (concurrent operations)
// - Progression updates (goroutines)
// - Quest state management (event handlers)
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - Ticker.Stop() for all time.Ticker
// - Channel close for all event channels
// - Timeout for all DB operations
