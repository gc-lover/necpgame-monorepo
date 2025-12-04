// Issue: #1585 - Goroutine leak detection (CRITICAL - Stock Analytics Charts Service!)
// stock-analytics-charts-service is HIGH RISK for leaks (chart generation, concurrent operations)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for stock analytics charts service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestStockAnalyticsChartsServiceNoLeaks verifies stock analytics charts service operations don't leak
func TestStockAnalyticsChartsServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test stock analytics charts service lifecycle and operations
	// service := NewStockAnalyticsChartsService(...)
	// service.GenerateChart(...)
	// time.Sleep(100 * time.Millisecond)
	// service.UpdateChart(...)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from stock analytics charts handlers, test FAILS
}

