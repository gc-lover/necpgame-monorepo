package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	socialRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "social_requests_total",
			Help: "Total number of social service requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	socialRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "social_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	notificationsTotal = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "notifications_total",
			Help: "Total number of notifications",
		},
		[]string{"account_id", "type"},
	)

	socialErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "social_errors_total",
			Help: "Total number of social service errors",
		},
		[]string{"type"},
	)
)

func RecordRequest(method, endpoint, status string) {
	socialRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordRequestDuration(method, endpoint string, duration float64) {
	socialRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func SetNotificationsCount(accountID, notificationType string, count float64) {
	notificationsTotal.WithLabelValues(accountID, notificationType).Set(count)
}

func RecordError(errorType string) {
	socialErrorsTotal.WithLabelValues(errorType).Inc()
}

