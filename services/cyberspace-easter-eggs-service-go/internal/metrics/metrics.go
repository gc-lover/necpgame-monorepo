// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// Metrics collection for Easter Eggs Service - Enterprise-grade monitoring

package metrics

import (
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// Collector handles metrics collection
type Collector struct {
	requestsTotal    *prometheus.CounterVec
	requestDuration  *prometheus.HistogramVec
	errorsTotal      prometheus.Counter
	activeConnections prometheus.Gauge
	easterEggsFound  *prometheus.CounterVec
	hintsRequested   *prometheus.CounterVec
}

// NewCollector creates a new metrics collector
func NewCollector() *Collector {
	requestsTotal := promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "cyberspace_easter_eggs_requests_total",
			Help: "Total number of requests by endpoint",
		},
		[]string{"endpoint", "method", "status"},
	)

	requestDuration := promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "cyberspace_easter_eggs_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"endpoint"},
	)

	errorsTotal := promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "cyberspace_easter_eggs_errors_total",
			Help: "Total number of errors",
		},
	)

	activeConnections := promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "cyberspace_easter_eggs_active_connections",
			Help: "Number of active connections",
		},
	)

	easterEggsFound := promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "cyberspace_easter_eggs_found_total",
			Help: "Total number of easter eggs found",
		},
		[]string{"category", "difficulty"},
	)

	hintsRequested := promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "cyberspace_easter_eggs_hints_requested_total",
			Help: "Total number of hints requested",
		},
		[]string{"easter_egg_id", "hint_level"},
	)

	return &Collector{
		requestsTotal:     requestsTotal,
		requestDuration:   requestDuration,
		errorsTotal:       errorsTotal,
		activeConnections: activeConnections,
		easterEggsFound:   easterEggsFound,
		hintsRequested:    hintsRequested,
	}
}

// IncrementRequests increments the request counter
func (c *Collector) IncrementRequests(endpoint string) {
	c.requestsTotal.WithLabelValues(endpoint, "GET", "200").Inc()
}

// ObserveRequestDuration observes request duration
func (c *Collector) ObserveRequestDuration(endpoint string, start time.Time) {
	duration := time.Since(start).Seconds()
	c.requestDuration.WithLabelValues(endpoint).Observe(duration)
}

// IncrementErrors increments the error counter
func (c *Collector) IncrementErrors() {
	c.errorsTotal.Inc()
}

// IncrementActiveConnections increments active connections
func (c *Collector) IncrementActiveConnections() {
	c.activeConnections.Inc()
}

// DecrementActiveConnections decrements active connections
func (c *Collector) DecrementActiveConnections() {
	c.activeConnections.Dec()
}

// IncrementEasterEggsFound increments easter eggs found counter
func (c *Collector) IncrementEasterEggsFound(category, difficulty string) {
	c.easterEggsFound.WithLabelValues(category, difficulty).Inc()
}

// IncrementHintsRequested increments hints requested counter
func (c *Collector) IncrementHintsRequested(easterEggID string, hintLevel int) {
	c.hintsRequested.WithLabelValues(easterEggID, string(rune('0'+hintLevel))).Inc()
}

// RecordDiscovery records easter egg discovery metrics
func (c *Collector) RecordDiscovery(category, difficulty string) {
	c.IncrementEasterEggsFound(category, difficulty)
}
