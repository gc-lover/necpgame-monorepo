// Package server Issue: #1585 - Runtime Goroutine Leak Monitoring for P2P Trade Service
package server

import (
	"context"
	"log"
	"runtime"
	"time"

	"go.uber.org/zap"
)

// GoroutineMonitor monitors goroutine count and detects leaks
// Issue: #1585 - Uses context cancellation for proper cleanup
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *log.Logger
	ctx           context.Context
	cancel        context.CancelFunc
}

// NewGoroutineMonitor creates a new goroutine monitor
func NewGoroutineMonitor(max int, logger *zap.Logger) *GoroutineMonitor {
	ctx, cancel := context.WithCancel(context.Background())
	return &GoroutineMonitor{
		maxGoroutines: max,
		logger:        logger,
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
				gm.logger.Printf("WARN: Goroutine count exceeded threshold: %d > %d (potential leak detected!)", count, gm.maxGoroutines)

				// Dump goroutine stack traces for debugging
				buf := make([]byte, 1<<20) // 1MB buffer
				n := runtime.Stack(buf, true)
				gm.logger.Printf("ERROR: Goroutine stack dump:\n%s", string(buf[:n]))
			} else {
				gm.logger.Printf("DEBUG: Goroutine count OK: %d", count)
			}
		}
	}
}

// Stop stops monitoring
func (gm *GoroutineMonitor) Stop() {
	gm.cancel()
}
