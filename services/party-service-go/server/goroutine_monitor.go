// Issue: #1585 - Runtime Goroutine Leak Monitoring
package server

import (
	"context"
	"log"
	"runtime"
	"time"

	"github.com/prometheus/client_golang/prometheus"
)

var (
	// goroutineCount is a Prometheus gauge for current goroutine count
	// Note: Using custom name to avoid conflict with standard go_goroutines metric
	goroutineCount = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "necpgame_goroutines",
		Help: "Current number of goroutines (custom metric)",
	})
)

func init() {
	prometheus.MustRegister(goroutineCount)
}

// GoroutineMonitor monitors goroutine count and detects leaks
// Issue: #1585 - Uses context cancellation for proper cleanup
type GoroutineMonitor struct {
	maxGoroutines int
	ctx           context.Context
	cancel        context.CancelFunc
}

// NewGoroutineMonitor creates a new goroutine monitor
func NewGoroutineMonitor(max int) *GoroutineMonitor {
	ctx, cancel := context.WithCancel(context.Background())
	return &GoroutineMonitor{
		maxGoroutines: max,
		ctx:           ctx,
		cancel:        cancel,
	}
}

// Start starts monitoring goroutine count
func (gm *GoroutineMonitor) Start() {
	ticker := time.NewTicker(10 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-gm.ctx.Done():
			return
		case <-ticker.C:
			count := runtime.NumGoroutine()

			if count > gm.maxGoroutines {
				log.Printf("ERROR: Goroutine leak detected: count=%d, max=%d", count, gm.maxGoroutines)

				// Dump goroutine stack traces
				buf := make([]byte, 1<<20) // 1MB
				n := runtime.Stack(buf, true)
				log.Printf("Goroutine dump:\n%s", string(buf[:n]))
			} else {
				log.Printf("DEBUG: Goroutine count OK: %d", count)
			}

			// Prometheus metric (Issue: #1585)
			goroutineCount.Set(float64(count))
		}
	}
}

// Stop stops monitoring
func (gm *GoroutineMonitor) Stop() {
	gm.cancel()
}







