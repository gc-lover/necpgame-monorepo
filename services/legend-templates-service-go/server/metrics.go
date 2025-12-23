// Issue: #2241
package server

import (
	"sync"
	"time"
)

// MetricsCollector collects performance and business metrics
type MetricsCollector struct {
	mu sync.RWMutex

	// Request metrics
	requestCount   map[string]int64
	requestLatency map[string][]time.Duration
	errorCount     map[string]int64

	// Business metrics
	legendsGenerated int64
	templatesCreated int64
	variablesCreated int64
	cacheHits        int64
	cacheMisses      int64

	// Performance metrics
	goroutines int64
	memoryUsage int64
}

// NewMetricsCollector creates a new metrics collector
func NewMetricsCollector() *MetricsCollector {
	return &MetricsCollector{
		requestCount:   make(map[string]int64),
		requestLatency: make(map[string][]time.Duration),
		errorCount:     make(map[string]int64),
	}
}

// RecordDuration records request duration for a specific operation
func (m *MetricsCollector) RecordDuration(operation string, duration time.Duration) {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.requestLatency[operation] == nil {
		m.requestLatency[operation] = make([]time.Duration, 0, 100)
	}

	// Keep only last 100 measurements for memory efficiency
	latencies := m.requestLatency[operation]
	if len(latencies) >= 100 {
		// Remove oldest measurement
		copy(latencies, latencies[1:])
		latencies = latencies[:len(latencies)-1]
	}

	m.requestLatency[operation] = append(latencies, duration)
	m.requestCount[operation]++
}

// RecordError records an error for a specific operation
func (m *MetricsCollector) RecordError(operation, errorType string) {
	m.mu.Lock()
	defer m.mu.Unlock()

	key := operation + ":" + errorType
	m.errorCount[key]++
}

// RecordSuccess records a successful operation
func (m *MetricsCollector) RecordSuccess(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()

	switch operation {
	case "generate_legend":
		m.legendsGenerated++
	case "create_template":
		m.templatesCreated++
	case "create_variable":
		m.variablesCreated++
	}
}

// RecordCacheHit records a cache hit
func (m *MetricsCollector) RecordCacheHit(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.cacheHits++
}

// RecordCacheMiss records a cache miss
func (m *MetricsCollector) RecordCacheMiss(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.cacheMisses++
}

// GetMetrics returns current metrics snapshot
func (m *MetricsCollector) GetMetrics() map[string]interface{} {
	m.mu.RLock()
	defer m.mu.RUnlock()

	metrics := make(map[string]interface{})

	// Request metrics
	for operation, count := range m.requestCount {
		metrics["requests:"+operation] = count

		if latencies, exists := m.requestLatency[operation]; exists && len(latencies) > 0 {
			// Calculate average latency
			var total time.Duration
			for _, latency := range latencies {
				total += latency
			}
			avg := total / time.Duration(len(latencies))
			metrics["latency_avg:"+operation] = avg.Milliseconds()

			// Calculate P95 latency
			if len(latencies) > 5 {
				sorted := make([]time.Duration, len(latencies))
				copy(sorted, latencies)
				// Simple sort for P95 approximation
				for i := 0; i < len(sorted)-1; i++ {
					for j := i + 1; j < len(sorted); j++ {
						if sorted[i] > sorted[j] {
							sorted[i], sorted[j] = sorted[j], sorted[i]
						}
					}
				}
				p95Index := int(float64(len(sorted)) * 0.95)
				if p95Index < len(sorted) {
					metrics["latency_p95:"+operation] = sorted[p95Index].Milliseconds()
				}
			}
		}
	}

	// Error metrics
	for errorKey, count := range m.errorCount {
		metrics["errors:"+errorKey] = count
	}

	// Business metrics
	metrics["legends_generated"] = m.legendsGenerated
	metrics["templates_created"] = m.templatesCreated
	metrics["variables_created"] = m.variablesCreated

	// Cache metrics
	totalCache := m.cacheHits + m.cacheMisses
	if totalCache > 0 {
		hitRate := float64(m.cacheHits) / float64(totalCache) * 100
		metrics["cache_hit_rate"] = hitRate
	}

	return metrics
}

// Reset resets all metrics (useful for testing)
func (m *MetricsCollector) Reset() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.requestCount = make(map[string]int64)
	m.requestLatency = make(map[string][]time.Duration)
	m.errorCount = make(map[string]int64)
	m.legendsGenerated = 0
	m.templatesCreated = 0
	m.variablesCreated = 0
	m.cacheHits = 0
	m.cacheMisses = 0
}
