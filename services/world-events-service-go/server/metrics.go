// World Events Metrics - Performance monitoring for MMOFPS service
// PERFORMANCE: Real-time metrics collection with Prometheus integration

package server

import (
	"sync/atomic"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// MetricsCollector handles performance metrics collection
type MetricsCollector struct {
	// Request metrics
	requestsTotal *prometheus.CounterVec
	requestDuration *prometheus.HistogramVec
	requestsInFlight *prometheus.GaugeVec

	// Business metrics
	activeEvents prometheus.Gauge
	participantsTotal prometheus.Counter
	eventsProcessed prometheus.Counter

	// Performance metrics
	concurrentRequests int64
	slowRequests *prometheus.CounterVec
	cacheHits *prometheus.CounterVec
	cacheMisses *prometheus.CounterVec

	// Error metrics
	errorsTotal *prometheus.CounterVec
}

// NewMetricsCollector creates a new metrics collector
func NewMetricsCollector() *MetricsCollector {
	mc := &MetricsCollector{
		requestsTotal: promauto.NewCounterVec(
			prometheus.CounterOpts{
				Name: "world_events_requests_total",
				Help: "Total number of requests processed",
			},
			[]string{"method", "endpoint", "status"},
		),

		requestDuration: promauto.NewHistogramVec(
			prometheus.HistogramOpts{
				Name:    "world_events_request_duration_seconds",
				Help:    "Request duration in seconds",
				Buckets: prometheus.DefBuckets,
			},
			[]string{"method", "endpoint"},
		),

		requestsInFlight: promauto.NewGaugeVec(
			prometheus.GaugeOpts{
				Name: "world_events_requests_in_flight",
				Help: "Number of requests currently being processed",
			},
			[]string{"method", "endpoint"},
		),

		activeEvents: promauto.NewGauge(
			prometheus.GaugeOpts{
				Name: "world_events_active_total",
				Help: "Total number of currently active world events",
			},
		),

		participantsTotal: promauto.NewCounter(
			prometheus.CounterOpts{
				Name: "world_events_participants_total",
				Help: "Total number of event participants",
			},
		),

		eventsProcessed: promauto.NewCounter(
			prometheus.CounterOpts{
				Name: "world_events_processed_total",
				Help: "Total number of events processed",
			},
		),

		slowRequests: promauto.NewCounterVec(
			prometheus.CounterOpts{
				Name: "world_events_slow_requests_total",
				Help: "Total number of slow requests (>50ms)",
			},
			[]string{"method", "endpoint"},
		),

		cacheHits: promauto.NewCounterVec(
			prometheus.CounterOpts{
				Name: "world_events_cache_hits_total",
				Help: "Total number of cache hits",
			},
			[]string{"cache_type"},
		),

		cacheMisses: promauto.NewCounterVec(
			prometheus.CounterOpts{
				Name: "world_events_cache_misses_total",
				Help: "Total number of cache misses",
			},
			[]string{"cache_type"},
		),

		errorsTotal: promauto.NewCounterVec(
			prometheus.CounterOpts{
				Name: "world_events_errors_total",
				Help: "Total number of errors",
			},
			[]string{"type", "endpoint"},
		),
	}

	return mc
}

// RecordRequest records request metrics
func (mc *MetricsCollector) RecordRequest(operation string, statusCode int, duration time.Duration) {
	mc.requestsTotal.WithLabelValues(operation, string(rune(statusCode)), string(rune(statusCode/100*100))).Inc()
	mc.requestDuration.WithLabelValues(operation, string(rune(statusCode/100*100))).Observe(duration.Seconds())

	// Track slow requests for hot paths
	if duration > 50*time.Millisecond && (operation == "GetActiveEvents" || operation == "ParticipateInEvent") {
		mc.slowRequests.WithLabelValues(operation, string(rune(statusCode/100*100))).Inc()
	}
}

// IncrementConcurrentRequests increments concurrent requests counter
func (mc *MetricsCollector) IncrementConcurrentRequests() {
	atomic.AddInt64(&mc.concurrentRequests, 1)
	mc.requestsInFlight.WithLabelValues("total", "all").Inc()
}

// DecrementConcurrentRequests decrements concurrent requests counter
func (mc *MetricsCollector) DecrementConcurrentRequests() {
	atomic.AddInt64(&mc.concurrentRequests, -1)
	mc.requestsInFlight.WithLabelValues("total", "all").Dec()
}

// GetConcurrentRequests returns current concurrent requests count
func (mc *MetricsCollector) GetConcurrentRequests() int64 {
	return atomic.LoadInt64(&mc.concurrentRequests)
}

// RecordCacheHit records cache hit
func (mc *MetricsCollector) RecordCacheHit(cacheType string) {
	mc.cacheHits.WithLabelValues(cacheType).Inc()
}

// RecordCacheMiss records cache miss
func (mc *MetricsCollector) RecordCacheMiss(cacheType string) {
	mc.cacheMisses.WithLabelValues(cacheType).Inc()
}

// UpdateActiveEvents updates active events gauge
func (mc *MetricsCollector) UpdateActiveEvents(count int) {
	mc.activeEvents.Set(float64(count))
}

// IncrementParticipants increments participants counter
func (mc *MetricsCollector) IncrementParticipants() {
	mc.participantsTotal.Inc()
}

// IncrementEventsProcessed increments processed events counter
func (mc *MetricsCollector) IncrementEventsProcessed() {
	mc.eventsProcessed.Inc()
}

// RecordError records error metrics
func (mc *MetricsCollector) RecordError(errorType, endpoint string) {
	mc.errorsTotal.WithLabelValues(errorType, endpoint).Inc()
}
