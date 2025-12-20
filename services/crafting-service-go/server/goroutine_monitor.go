// Issue: #1585 - Runtime Goroutine Monitoring
package server

import (
	"runtime"
	"time"

	"github.com/sirupsen/logrus"
)

// GoroutineMonitor monitors and limits goroutine count
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *logrus.Logger
	stopCh        chan struct{}
}

// NewGoroutineMonitor creates new goroutine monitor
func NewGoroutineMonitor(maxGoroutines int) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: maxGoroutines,
		logger:        GetLogger(),
		stopCh:        make(chan struct{}),
	}
}

// Start begins monitoring goroutines
func (m *GoroutineMonitor) Start() {
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-m.stopCh:
			return
		case <-ticker.C:
			count := runtime.NumGoroutine()
			if count > m.maxGoroutines {
				m.logger.WithFields(logrus.Fields{
					"current": count,
					"max":     m.maxGoroutines,
				}).Warn("WARNING High goroutine count detected")
			} else {
				m.logger.WithFields(logrus.Fields{
					"current": count,
					"max":     m.maxGoroutines,
				}).Debug("Goroutine count normal")
			}
		}
	}
}

// Stop stops the monitor
func (m *GoroutineMonitor) Stop() {
	close(m.stopCh)
}
