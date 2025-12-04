// Issue: #1585 - Runtime Goroutine Leak Monitoring
package server

import (
	"runtime"
	"time"

	"github.com/sirupsen/logrus"
)

// GoroutineMonitor monitors goroutine count and detects leaks
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *logrus.Logger
	ctx           chan struct{}
}

// NewGoroutineMonitor creates a new goroutine monitor
func NewGoroutineMonitor(max int) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: max,
		logger:        GetLogger(),
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
				gm.logger.WithFields(logrus.Fields{
					"current_goroutines": count,
					"max_goroutines":     gm.maxGoroutines,
				}).Warn("Goroutine count exceeded threshold, potential leak detected!")

				// Dump goroutine stack traces for debugging
				buf := make([]byte, 1<<20) // 1MB buffer
				n := runtime.Stack(buf, true)
				gm.logger.WithField("stack_trace", string(buf[:n])).Error("Goroutine stack dump")
			} else {
				gm.logger.WithField("goroutine_count", count).Debug("Goroutine count OK")
			}

			// TODO: Integrate with Prometheus metric go_goroutines
		}
	}
}

// Stop stops monitoring
func (gm *GoroutineMonitor) Stop() {
	close(gm.ctx)
}

