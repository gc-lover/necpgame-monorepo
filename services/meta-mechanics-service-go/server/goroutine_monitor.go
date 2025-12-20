// Issue: #1585 - Goroutine leak detection for Meta Mechanics Service
// CRITICAL: League calculations, ranking updates, event processing
package server

import (
	"runtime"
	"time"

	"github.com/sirupsen/logrus"
)

// GoroutineMonitor prevents goroutine leaks in meta-mechanics operations
// CRITICAL for: league ranking updates, prestige calculations, meta-events
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *logrus.Logger
	stopCh        chan struct{}
}

// NewGoroutineMonitor creates monitor with max goroutine limit
func NewGoroutineMonitor(max int, logger *logrus.Logger) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: max,
		logger:        logger,
		stopCh:        make(chan struct{}),
	}
}

// Start begins monitoring goroutine count
func (m *GoroutineMonitor) Start() {
	ticker := time.NewTicker(5 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-m.stopCh:
			return
		case <-ticker.C:
			count := runtime.NumGoroutine()
			if count > m.maxGoroutines {
				m.logger.WithField("goroutines", count).Warn("High goroutine count detected")
			}
		}
	}
}

// Stop halts the monitoring
func (m *GoroutineMonitor) Stop() {
	close(m.stopCh)
}
