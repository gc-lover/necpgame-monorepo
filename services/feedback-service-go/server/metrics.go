package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	feedbackRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "feedback_requests_total",
			Help: "Total number of feedback service requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	feedbackRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "feedback_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	feedbackTotal = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "feedback_total",
			Help: "Total number of feedback items",
		},
		[]string{"type", "status"},
	)

	feedbackErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "feedback_errors_total",
			Help: "Total number of feedback service errors",
		},
		[]string{"type"},
	)
)

func RecordRequest(method, endpoint, status string) {
	feedbackRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordRequestDuration(method, endpoint string, duration float64) {
	feedbackRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func SetFeedbackCount(feedbackType, status string, count float64) {
	feedbackTotal.WithLabelValues(feedbackType, status).Set(count)
}

func RecordError(errorType string) {
	feedbackErrorsTotal.WithLabelValues(errorType).Inc()
}

















































