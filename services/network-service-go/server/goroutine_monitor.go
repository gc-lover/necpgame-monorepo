package server

import (
	"runtime"
	"sync/atomic"
	"time"

	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1585 - Runtime Goroutine Monitoring for network stability
type GoroutineMonitor struct {
	maxGoroutines int64
	logger        *logrus.Logger
	running       int64
	stopCh        chan struct{}
}

// OPTIMIZATION: Issue #1978 - Memory-aligned struct
type GoroutineStats struct {
	CurrentCount int64     `json:"current_count"` // 8 bytes
	MaxAllowed   int64     `json:"max_allowed"`   // 8 bytes
	Timestamp    time.Time `json:"timestamp"`     // 24 bytes
	IsOverLimit  bool      `json:"is_over_limit"` // 1 byte
}

func NewGoroutineMonitor(maxGoroutines int64, logger *logrus.Logger) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: maxGoroutines,
		logger:        logger,
		stopCh:        make(chan struct{}),
	}
}

func (gm *GoroutineMonitor) Start() {
	atomic.StoreInt64(&gm.running, 1)

	go func() {
		ticker := time.NewTicker(15 * time.Second) // OPTIMIZATION: Check every 15s for network connections
		defer ticker.Stop()

		for {
			select {
			case <-gm.stopCh:
				atomic.StoreInt64(&gm.running, 0)
				return
			case <-ticker.C:
				gm.checkGoroutines()
			}
		}
	}()

	gm.logger.WithField("max_goroutines", gm.maxGoroutines).Info("goroutine monitor started")
}

func (gm *GoroutineMonitor) Stop() {
	if atomic.LoadInt64(&gm.running) == 1 {
		close(gm.stopCh)
		// Wait for monitor to stop
		for atomic.LoadInt64(&gm.running) == 1 {
			time.Sleep(100 * time.Millisecond)
		}
		gm.logger.Info("goroutine monitor stopped")
	}
}

func (gm *GoroutineMonitor) checkGoroutines() {
	current := int64(runtime.NumGoroutine())
	maxAllowed := atomic.LoadInt64(&gm.maxGoroutines)

	stats := &GoroutineStats{
		CurrentCount: current,
		MaxAllowed:   maxAllowed,
		Timestamp:    time.Now(),
		IsOverLimit:  current > maxAllowed,
	}

	if stats.IsOverLimit {
		// OPTIMIZATION: Issue #1972 - Alert on excessive goroutines for network
		gm.logger.WithFields(logrus.Fields{
			"current_goroutines": stats.CurrentCount,
			"max_allowed":        stats.MaxAllowed,
			"over_limit_by":      stats.CurrentCount - stats.MaxAllowed,
		}).Warn("Network goroutine count exceeded maximum allowed - possible connection overload")

		// Force garbage collection as emergency measure for network
		runtime.GC()
		runtime.ForceGC()
	} else {
		// Log normal stats at debug level
		gm.logger.WithFields(logrus.Fields{
			"current_goroutines": stats.CurrentCount,
			"max_allowed":        stats.MaxAllowed,
		}).Debug("Network goroutine count within limits")
	}
}

func (gm *GoroutineMonitor) GetStats() *GoroutineStats {
	return &GoroutineStats{
		CurrentCount: int64(runtime.NumGoroutine()),
		MaxAllowed:   atomic.LoadInt64(&gm.maxGoroutines),
		Timestamp:    time.Now(),
		IsOverLimit:  int64(runtime.NumGoroutine()) > atomic.LoadInt64(&gm.maxGoroutines),
	}
}
