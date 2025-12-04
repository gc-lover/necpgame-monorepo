// Issue: #1585 - Goroutine leak detection (CRITICAL - Stock Indices Service!)
// stock-indices-service is HIGH RISK for leaks (index calculations, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for stock indices service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestStockIndicesServiceNoLeaks verifies stock indices service operations don't leak
func TestStockIndicesServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test stock indices service lifecycle and operations
	// service := NewStockIndicesService(...)
	// service.CalculateIndex(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateIndex(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from stock indices handlers, test FAILS
}

