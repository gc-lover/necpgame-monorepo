// Issue: #1585 - Runtime Goroutine Monitoring
package server

import (
	"context"
	"runtime"
	"time"

	"github.com/sirupsen/logrus"
)

// GoroutineMonitor monitors goroutine count and alerts if exceeds threshold
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *logrus.Logger
	stopCh        chan struct{}
}

// NewGoroutineMonitor creates new goroutine monitor
func NewGoroutineMonitor(maxGoroutines int, logger *logrus.Logger) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: maxGoroutines,
		logger:        logger,
		stopCh:        make(chan struct{}),
	}
}

// Start begins monitoring goroutines
func (gm *GoroutineMonitor) Start() {
	go func() {
		ticker := time.NewTicker(30 * time.Second) // Check every 30 seconds
		defer ticker.Stop()

		for {
			select {
			case <-gm.stopCh:
				return
			case <-ticker.C:
				gm.checkGoroutines()
			}
		}
	}()
}

// Stop stops monitoring
func (gm *GoroutineMonitor) Stop() {
	close(gm.stopCh)
}

// checkGoroutines checks current goroutine count
func (gm *GoroutineMonitor) checkGoroutines() {
	count := runtime.NumGoroutine()

	if count > gm.maxGoroutines {
		gm.logger.WithFields(logrus.Fields{
			"goroutines":      count,
			"max_goroutines":  gm.maxGoroutines,
			"threshold_exceeded": true,
		}).Warn("High goroutine count detected")
	} else {
		gm.logger.WithFields(logrus.Fields{
			"goroutines":     count,
			"max_goroutines": gm.maxGoroutines,
		}).Debug("Goroutine count normal")
	}
}