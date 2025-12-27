// Metrics collection for Announcement Service
// Issue: #323
// PERFORMANCE: Lightweight metrics with minimal overhead

package server

import (
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/pkg/api"
	"go.uber.org/zap"
)

// Metrics handles service metrics
type Metrics struct {
	mu           sync.RWMutex
	requestCount map[string]int64
	errorCount   map[string]int64
	logger       *zap.Logger
}

// NewMetrics creates a new metrics instance
func NewMetrics(logger *zap.Logger) *Metrics {
	return &Metrics{
		requestCount: make(map[string]int64),
		errorCount:   make(map[string]int64),
		logger:       logger,
	}
}

// RecordRequest records a general request
func (m *Metrics) RecordRequest(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.requestCount[operation]++
	m.logger.Debug("Request recorded", zap.String("operation", operation), zap.Int64("count", m.requestCount[operation]))
}

// RecordError records an error for a specific operation
func (m *Metrics) RecordError(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.errorCount[operation]++
	m.logger.Error("Error recorded", zap.String("operation", operation), zap.Int64("count", m.errorCount[operation]))
}

// GetMetrics returns the current metrics
func (m *Metrics) GetMetrics() api.MetricsResponse {
	m.mu.RLock()
	defer m.mu.RUnlock()

	// This is a simplified example. In a real system, you'd integrate with Prometheus/OpenTelemetry
	return api.MetricsResponse{
		TotalRequests: api.NewOptInt64(m.getTotalRequests()),
		TotalErrors:   api.NewOptInt64(m.getTotalErrors()),
		// Add more detailed metrics as needed
	}
}

func (m *Metrics) getTotalRequests() int64 {
	var total int64
	for _, count := range m.requestCount {
		total += count
	}
	return total
}

func (m *Metrics) getTotalErrors() int64 {
	var total int64
	for _, count := range m.errorCount {
		total += count
	}
	return total
}



