// Combat Stats Metrics - Monitoring and observability
// Issue: #2245
// PERFORMANCE: Efficient metrics collection

package server

import (
	"time"
)

// Metrics provides monitoring for combat statistics service
type Metrics struct {
	requestCount    int64
	errorCount      int64
	responseTime    time.Duration
	activeSessions  int64
	statsProcessed  int64
}

// NewMetrics creates a new metrics instance
func NewMetrics() *Metrics {
	return &Metrics{}
}

// RecordRequest records a request metric
func (m *Metrics) RecordRequest(duration time.Duration, success bool) {
	m.requestCount++
	m.responseTime = duration

	if !success {
		m.errorCount++
	}
}

// GetStats returns current metrics
func (m *Metrics) GetStats() map[string]interface{} {
	return map[string]interface{}{
		"requests_total":       m.requestCount,
		"errors_total":         m.errorCount,
		"avg_response_time_ms": m.responseTime.Milliseconds(),
		"active_sessions":      m.activeSessions,
		"stats_processed_per_s": m.statsProcessed,
	}
}
