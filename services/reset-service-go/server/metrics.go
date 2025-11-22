package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	ResetRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "reset_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "reset_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	ResetsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "reset_service_resets_total",
			Help: "Total number of resets",
		},
		[]string{"type", "status"},
	)

	ResetDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "reset_service_reset_duration_seconds",
			Help:    "Reset execution duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"type"},
	)
)

func RecordRequest(method, path, status string) {
	ResetRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordReset(resetType, status string) {
	ResetsTotal.WithLabelValues(resetType, status).Inc()
}

func RecordResetDuration(resetType string, duration float64) {
	ResetDuration.WithLabelValues(resetType).Observe(duration)
}

