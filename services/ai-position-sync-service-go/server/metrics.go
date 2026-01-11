package server

import (
	"sync"
	"time"
)

// ServiceMetrics tracks performance metrics for the AI Position Sync Service
type ServiceMetrics struct {
	mu           sync.RWMutex
	requestCount map[string]int64
	errorCount   map[string]int64
	latencySum   map[string]time.Duration
	latencyCount map[string]int64
	cacheHits    map[string]int64
	cacheMisses  map[string]int64
}

func NewServiceMetrics() *ServiceMetrics {
	return &ServiceMetrics{
		requestCount: make(map[string]int64),
		errorCount:   make(map[string]int64),
		latencySum:   make(map[string]time.Duration),
		latencyCount: make(map[string]int64),
		cacheHits:    make(map[string]int64),
		cacheMisses:  make(map[string]int64),
	}
}

func (m *ServiceMetrics) IncrementRequests(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.requestCount[operation]++
}

func (m *ServiceMetrics) IncrementErrors(operation string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.errorCount[operation]++
}

func (m *ServiceMetrics) RecordLatency(operation string, duration time.Duration) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.latencySum[operation] += duration
	m.latencyCount[operation]++
}

func (m *ServiceMetrics) IncrementCacheHits(cacheType string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.cacheHits[cacheType]++
}

func (m *ServiceMetrics) IncrementCacheMisses(cacheType string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.cacheMisses[cacheType]++
}

// GetRequestCount returns the total number of requests for an operation
func (m *ServiceMetrics) GetRequestCount(operation string) int64 {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.requestCount[operation]
}

// GetErrorCount returns the total number of errors for an operation
func (m *ServiceMetrics) GetErrorCount(operation string) int64 {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.errorCount[operation]
}

// GetAverageLatency returns the average latency for an operation in milliseconds
func (m *ServiceMetrics) GetAverageLatency(operation string) float64 {
	m.mu.RLock()
	defer m.mu.RUnlock()

	count := m.latencyCount[operation]
	if count == 0 {
		return 0
	}

	total := m.latencySum[operation]
	return float64(total.Milliseconds()) / float64(count)
}

// GetCacheHitRatio returns the cache hit ratio for a cache type
func (m *ServiceMetrics) GetCacheHitRatio(cacheType string) float64 {
	m.mu.RLock()
	defer m.mu.RUnlock()

	hits := m.cacheHits[cacheType]
	misses := m.cacheMisses[cacheType]
	total := hits + misses

	if total == 0 {
		return 0
	}

	return float64(hits) / float64(total)
}

// GetAllMetrics returns a snapshot of all metrics
func (m *ServiceMetrics) GetAllMetrics() map[string]interface{} {
	m.mu.RLock()
	defer m.mu.RUnlock()

	metrics := make(map[string]interface{})

	// Request counts
	for op, count := range m.requestCount {
		metrics["requests_"+op] = count
	}

	// Error counts
	for op, count := range m.errorCount {
		metrics["errors_"+op] = count
	}

	// Average latencies
	for op := range m.latencySum {
		if count := m.latencyCount[op]; count > 0 {
			avgLatency := float64(m.latencySum[op].Milliseconds()) / float64(count)
			metrics["latency_avg_"+op+"_ms"] = avgLatency
		}
	}

	// Cache hit ratios
	for cacheType := range m.cacheHits {
		hits := m.cacheHits[cacheType]
		misses := m.cacheMisses[cacheType]
		total := hits + misses
		if total > 0 {
			ratio := float64(hits) / float64(total)
			metrics["cache_hit_ratio_"+cacheType] = ratio
		}
	}

	return metrics
}

// Middleware returns an HTTP middleware that records request metrics
func (m *ServiceMetrics) Middleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Record response using a wrapper to capture status code
		wrapper := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(wrapper, r)

		duration := time.Since(start)

		// Record metrics
		operation := r.Method + "_" + r.URL.Path
		m.RecordLatency(operation, duration)

		if wrapper.statusCode >= 400 {
			m.IncrementErrors(operation)
		}

		m.IncrementRequests(operation)
	})
}

// responseWriter wraps http.ResponseWriter to capture status code
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}