// Package server Issue: #1585 - Runtime Goroutine Leak Monitoring
package server

import (
	"context"
	"runtime"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/sirupsen/logrus"
)

var (
	// goroutineCount is a Prometheus gauge for current goroutine count
	goroutineCount = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "ws_lobby_goroutines",
		Help: "Current number of goroutines in ws-lobby-go",
	})
)

func init() {
	prometheus.MustRegister(goroutineCount)
}

// GoroutineMonitor monitors goroutine count and detects leaks
// Issue: #1585 - Uses context cancellation for proper cleanup
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *logrus.Logger
	ctx           context.Context
	cancel        context.CancelFunc
}

// NewGoroutineMonitor creates a new goroutine monitor
func NewGoroutineMonitor(max int) *GoroutineMonitor {
	ctx, cancel := context.WithCancel(context.Background())
	return &GoroutineMonitor{
		maxGoroutines: max,
		logger:        GetLogger(),
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

			// Prometheus metric (Issue: #1585)
			goroutineCount.Set(float64(count))
		}
	}
}

// Stop stops monitoring
func (gm *GoroutineMonitor) Stop() {
	gm.cancel()
}
