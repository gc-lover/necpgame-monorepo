package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	futuresRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_futures_requests_total",
			Help: "Total number of futures requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	futuresRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_futures_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	positionsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_futures_positions_requests_total",
			Help: "Total number of positions requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	futuresErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_futures_errors_total",
			Help: "Total number of futures errors",
		},
		[]string{"type"},
	)
)

func RecordFuturesRequest(method, endpoint, status string) {
	futuresRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordFuturesRequestDuration(method, endpoint string, duration float64) {
	futuresRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordPositionRequest(method, endpoint, status string) {
	positionsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	futuresErrorsTotal.WithLabelValues(errorType).Inc()
}

