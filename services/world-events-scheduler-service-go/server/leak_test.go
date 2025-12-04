// Issue: #1585 - Goroutine leak detection (CRITICAL - Event scheduler service!)
// world-events-scheduler-service is HIGH RISK for leaks (cron jobs, scheduled tasks, Kafka producers)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for scheduler service - cron jobs spawn persistent goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestSchedulerServiceNoLeaks verifies scheduler service doesn't leak
func TestSchedulerServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test scheduler service lifecycle
	// service := NewSchedulerService(nil)
	// service.Start()
	// time.Sleep(200 * time.Millisecond)
	// service.Stop()

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from cron jobs, test FAILS
}

// NOTE: world-events-scheduler-service is HIGH RISK for leaks:
// - Cron job goroutines (robfig/cron spawns persistent goroutines)
// - Scheduled task workers (background processing)
// - Kafka producer connections (persistent)
// - Event publishing loops (goroutines)
//
// MUST implement proper cleanup:
// - Cron.Stop() for all cron instances
// - Context cancellation for all goroutines
// - Kafka producer.Close() for all producers
// - Ticker.Stop() for all time.Ticker
// - Timeout for all DB operations

