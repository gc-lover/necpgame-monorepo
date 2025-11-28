package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	marginRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_margin_requests_total",
			Help: "Total number of margin requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	marginRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_margin_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	shortRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_margin_short_requests_total",
			Help: "Total number of short position requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	marginErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_margin_errors_total",
			Help: "Total number of margin errors",
		},
		[]string{"type"},
	)
)

func RecordMarginRequest(method, endpoint, status string) {
	marginRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordMarginRequestDuration(method, endpoint string, duration float64) {
	marginRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordShortRequest(method, endpoint, status string) {
	shortRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	marginErrorsTotal.WithLabelValues(errorType).Inc()
}

