package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	eventsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_events_requests_total",
			Help: "Total number of events requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	eventsRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_events_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	impactsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_events_impacts_requests_total",
			Help: "Total number of impacts requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	eventsErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_events_errors_total",
			Help: "Total number of events errors",
		},
		[]string{"type"},
	)
)

func RecordEventRequest(method, endpoint, status string) {
	eventsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordEventRequestDuration(method, endpoint string, duration float64) {
	eventsRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordImpactRequest(method, endpoint, status string) {
	impactsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	eventsErrorsTotal.WithLabelValues(errorType).Inc()
}

