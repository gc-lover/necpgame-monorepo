// Metrics collection for Maintenance Windows Service
// Issue: #316
// PERFORMANCE: Lightweight metrics with minimal overhead

package server

import (
	"sync"
	"time"
)

// MetricsCollector handles service metrics
type MetricsCollector struct {
	mu                        sync.RWMutex
	requestCount              int64
	errorCount                int64
	cacheHitCount             int64
	cacheMissCount            int64
	createWindowCount         int64
	updateWindowCount         int64
	cancelWindowCount         int64
	getWindowCount            int64
	listWindowsCount          int64
	requestDuration           time.Duration
	lastRequestTime           time.Time
}

// NewMetricsCollector creates a new metrics collector
func NewMetricsCollector() *MetricsCollector {
	return &MetricsCollector{}
}

// RecordRequest records a general request
func (m *MetricsCollector) RecordRequest(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.requestCount++
	m.lastRequestTime = time.Now()

	// Record operation-specific metrics
	switch operation {
	case "CreateMaintenanceWindow":
		m.createWindowCount++
	case "UpdateMaintenanceWindow":
		m.updateWindowCount++
	case "CancelMaintenanceWindow":
		m.cancelWindowCount++
	case "GetMaintenanceWindow":
		m.getWindowCount++
	case "GetMaintenanceWindows":
		m.listWindowsCount++
	}
}

// RecordError records an error
func (m *MetricsCollector) RecordError(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.errorCount++
}

// RecordCacheHit records a cache hit
func (m *MetricsCollector) RecordCacheHit(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.cacheHitCount++
}

// RecordCacheMiss records a cache miss
func (m *MetricsCollector) RecordCacheMiss(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.cacheMissCount++
}

// RecordSuccess records a successful operation
func (m *MetricsCollector) RecordSuccess(operation string) {
	// Could add success-specific metrics here
}

// GetMetrics returns current metrics snapshot
func (m *MetricsCollector) GetMetrics() map[string]interface{} {
	m.mu.RLock()
	defer m.mu.RUnlock()

	return map[string]interface{}{
		"total_requests":       m.requestCount,
		"total_errors":         m.errorCount,
		"cache_hit_ratio":      m.calculateCacheHitRatio(),
		"create_windows":       m.createWindowCount,
		"update_windows":       m.updateWindowCount,
		"cancel_windows":       m.cancelWindowCount,
		"get_windows":          m.getWindowCount,
		"list_windows":         m.listWindowsCount,
		"last_request_time":    m.lastRequestTime,
		"uptime_seconds":       time.Since(m.lastRequestTime).Seconds(),
	}
}

// calculateCacheHitRatio calculates cache hit ratio
func (m *MetricsCollector) calculateCacheHitRatio() float64 {
	total := m.cacheHitCount + m.cacheMissCount
	if total == 0 {
		return 0.0
	}
	return float64(m.cacheHitCount) / float64(total)
}

// Reset resets all metrics (useful for testing)
func (m *MetricsCollector) Reset() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.requestCount = 0
	m.errorCount = 0
	m.cacheHitCount = 0
	m.cacheMissCount = 0
	m.createWindowCount = 0
	m.updateWindowCount = 0
	m.cancelWindowCount = 0
	m.getWindowCount = 0
	m.listWindowsCount = 0
	m.requestDuration = 0
	m.lastRequestTime = time.Time{}
}
