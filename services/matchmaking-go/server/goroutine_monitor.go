// Issue: #1585 - Runtime Goroutine Leak Monitoring
package server

import (
	"log"
	"runtime"
	"time"
)

// GoroutineMonitor monitors goroutine count and detects leaks
type GoroutineMonitor struct {
	maxGoroutines int
	ctx           chan struct{}
}

// NewGoroutineMonitor creates a new goroutine monitor
func NewGoroutineMonitor(max int) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: max,
		ctx:           make(chan struct{}),
	}
}

// Start starts monitoring goroutine count
func (gm *GoroutineMonitor) Start() {
	ticker := time.NewTicker(10 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-gm.ctx:
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

			// Prometheus metric (if metrics available)
			// goroutineCount.Set(float64(count))
		}
	}
}

// Stop stops monitoring
func (gm *GoroutineMonitor) Stop() {
	close(gm.ctx)
}

