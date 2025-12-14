// Issue: #136 - Runtime Goroutine Leak Monitoring for Auth Service
package server

import (
	"context"
	"runtime"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"go.uber.org/zap"
)

var (
	// goroutineCount is a Prometheus gauge for current goroutine count
	// Note: Using custom name to avoid conflict with standard go_goroutines metric
	goroutineCount = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "necpgame_auth_goroutines",
		Help: "Current number of goroutines in auth service (custom metric)",
	})
)

func init() {
	prometheus.MustRegister(goroutineCount)
}

// GoroutineMonitor monitors goroutine count and detects leaks
// Issue: #136 - Uses context cancellation for proper cleanup
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *zap.Logger
	stopChan      chan struct{}
}

// NewGoroutineMonitor creates a new goroutine monitor
func NewGoroutineMonitor(maxGoroutines int, logger *zap.Logger) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: maxGoroutines,
		logger:        logger,
		stopChan:      make(chan struct{}),
	}
}

// Start begins monitoring goroutines
func (gm *GoroutineMonitor) Start(ctx context.Context) {
	go func() {
		ticker := time.NewTicker(30 * time.Second) // Check every 30 seconds for MMOFPS performance
		defer ticker.Stop()

		for {
			select {
			case <-ctx.Done():
				gm.logger.Info("Goroutine monitor stopped", zap.Error(ctx.Err()))
				return
			case <-gm.stopChan:
				gm.logger.Info("Goroutine monitor stopped")
				return
			case <-ticker.C:
				gm.checkGoroutines()
			}
		}
	}()
}

// Stop stops the goroutine monitor
func (gm *GoroutineMonitor) Stop() {
	close(gm.stopChan)
}

// checkGoroutines checks current goroutine count and logs warnings if too high
func (gm *GoroutineMonitor) checkGoroutines() {
	count := runtime.NumGoroutine()
	goroutineCount.Set(float64(count))

	if count > gm.maxGoroutines {
		gm.logger.Warn("High goroutine count detected",
			zap.Int("current", count),
			zap.Int("max_allowed", gm.maxGoroutines),
			zap.String("service", "auth-service"),
		)
	}

	// Log goroutine count for monitoring (every 5 minutes)
	if count > 100 { // Only log if significant number
		gm.logger.Debug("Goroutine count",
			zap.Int("count", count),
			zap.String("service", "auth-service"),
		)
	}
}
