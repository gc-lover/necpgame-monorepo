package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	optionsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_options_requests_total",
			Help: "Total number of options requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	optionsRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_options_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	positionsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_options_positions_requests_total",
			Help: "Total number of positions requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	optionsErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_options_errors_total",
			Help: "Total number of options errors",
		},
		[]string{"type"},
	)
)

func RecordOptionsRequest(method, endpoint, status string) {
	optionsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordOptionsRequestDuration(method, endpoint string, duration float64) {
	optionsRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordPositionRequest(method, endpoint, status string) {
	positionsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	optionsErrorsTotal.WithLabelValues(errorType).Inc()
}
