package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	indicesRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_indices_requests_total",
			Help: "Total number of indices requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	indicesRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_indices_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	constituentsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_indices_constituents_requests_total",
			Help: "Total number of constituents requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	indicesErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_indices_errors_total",
			Help: "Total number of indices errors",
		},
		[]string{"type"},
	)
)

func RecordIndexRequest(method, endpoint, status string) {
	indicesRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordIndexRequestDuration(method, endpoint string, duration float64) {
	indicesRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordConstituentRequest(method, endpoint, status string) {
	constituentsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	indicesErrorsTotal.WithLabelValues(errorType).Inc()
}

