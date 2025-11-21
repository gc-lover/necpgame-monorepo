package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	movementRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "movement_requests_total",
			Help: "Total number of movement requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	movementRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "movement_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	positionsSavedTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "positions_saved_total",
			Help: "Total number of positions saved to database",
		},
	)

	positionsReceivedFromGateway = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "positions_received_from_gateway_total",
			Help: "Total number of positions received from gateway",
		},
	)

	movementErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "movement_errors_total",
			Help: "Total number of movement errors",
		},
		[]string{"type"},
	)
)

func RecordRequest(method, endpoint, status string) {
	movementRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordRequestDuration(method, endpoint string, duration float64) {
	movementRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordPositionSaved() {
	positionsSavedTotal.Inc()
}

func RecordPositionReceivedFromGateway() {
	positionsReceivedFromGateway.Inc()
}

func RecordError(errorType string) {
	movementErrorsTotal.WithLabelValues(errorType).Inc()
}
