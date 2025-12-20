// Package server Issue: #1585 - Goroutine leak detection for tournament service
package server

import (
	"runtime"
	"time"

	"github.com/sirupsen/logrus"
)

// GoroutineMonitor detects goroutine leaks in tournament service
type GoroutineMonitor struct {
	maxGoroutines int64
	logger        *logrus.Logger
	stopChan      chan struct{}
}

// NewGoroutineMonitor creates goroutine monitor
func NewGoroutineMonitor(maxGoroutines int64, logger *logrus.Logger) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: maxGoroutines,
		logger:        logger,
		stopChan:      make(chan struct{}),
	}
}

// Start begins monitoring goroutines
func (m *GoroutineMonitor) Start() {
	go func() {
		ticker := time.NewTicker(30 * time.Second)
		defer ticker.Stop()

		for {
			select {
			case <-m.stopChan:
				return
			case <-ticker.C:
				m.checkGoroutines()
			}
		}
	}()
}

// Stop stops the monitor
func (m *GoroutineMonitor) Stop() {
	close(m.stopChan)
}

// checkGoroutines monitors current goroutine count
func (m *GoroutineMonitor) checkGoroutines() {
	numGoroutines := runtime.NumGoroutine()

	if int64(numGoroutines) > m.maxGoroutines {
		m.logger.WithFields(logrus.Fields{
			"current_goroutines": numGoroutines,
			"max_allowed":        m.maxGoroutines,
			"service":            "tournament-service",
		}).Warn("High goroutine count detected - potential leak")

		// Log stack traces for debugging
		buf := make([]byte, 4096)
		n := runtime.Stack(buf, true)
		m.logger.WithField("stack_trace", string(buf[:n])).Debug("Goroutine stack traces")
	} else {
		m.logger.WithFields(logrus.Fields{
			"current_goroutines": numGoroutines,
			"max_allowed":        m.maxGoroutines,
			"service":            "tournament-service",
		}).Debug("Goroutine count normal")
	}
}
