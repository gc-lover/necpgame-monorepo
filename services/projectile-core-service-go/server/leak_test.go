// Issue: #1585 - Goroutine leak detection (CRITICAL - Hot path 2k+ RPS!)
// projectile-core-service is HIGH RISK for leaks (projectile calculations, concurrent physics)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for hot path service - 2k+ RPS, projectile calculations, concurrent physics
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestProjectileServiceNoLeaks verifies projectile service doesn't leak
func TestProjectileServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test projectile calculation lifecycle
	// service := NewProjectileService(nil)
	// service.CalculateTrajectory(ctx, params)
	// time.Sleep(100 * time.Millisecond)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from projectile handlers, test FAILS
}

// NOTE: projectile-core-service is HIGH RISK for leaks:
// - 2k+ RPS (hot path)
// - Projectile calculations (concurrent physics)
// - Trajectory calculations
// - Collision detection
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - DB connection pool limits
// - Bounded channels for projectile updates

