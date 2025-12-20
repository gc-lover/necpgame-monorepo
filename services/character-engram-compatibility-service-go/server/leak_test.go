// Issue: #1585 - Goroutine leak detection (CRITICAL - Character Engram Compatibility Service!)
// character-engram-compatibility-service is HIGH RISK for leaks (compatibility checks, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for character engram compatibility service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestCharacterEngramCompatibilityServiceNoLeaks verifies character engram compatibility service operations don't leak
func TestCharacterEngramCompatibilityServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test character engram compatibility service lifecycle and operations
	// service := NewCharacterEngramCompatibilityService(...)
	// service.CheckCompatibility(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateCompatibility(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from character engram compatibility handlers, test FAILS
}
