package server

import (
	"runtime"
	"time"

	"github.com/sirupsen/logrus"
)

// GoroutineMonitor monitors goroutine count to prevent leaks
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *logrus.Logger
	stopCh        chan struct{}
}

// NewGoroutineMonitor creates a new goroutine monitor
func NewGoroutineMonitor(maxGoroutines int, logger *logrus.Logger) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: maxGoroutines,
		logger:        logger,
		stopCh:        make(chan struct{}),
	}
}

// Start begins monitoring goroutines
func (gm *GoroutineMonitor) Start() {
	ticker := time.NewTicker(10 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			gm.checkGoroutines()
		case <-gm.stopCh:
			return
		}
	}
}

// Stop stops the goroutine monitor
func (gm *GoroutineMonitor) Stop() {
	close(gm.stopCh)
}

// checkGoroutines checks current goroutine count and logs warnings if too high
func (gm *GoroutineMonitor) checkGoroutines() {
	count := runtime.NumGoroutine()

	if count > gm.maxGoroutines {
		gm.logger.WithFields(logrus.Fields{
			"goroutines":     count,
			"max_allowed":    gm.maxGoroutines,
			"service":        "tournament-service",
		}).Warn("high goroutine count detected - potential leak")
	} else {
		gm.logger.WithFields(logrus.Fields{
			"goroutines":  count,
			"service":     "tournament-service",
		}).Debug("goroutine count check")
	}
}