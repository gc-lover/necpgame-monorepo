package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	CompanionRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "companion_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "companion_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	CompanionsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "companion_service_companions_total",
			Help: "Total number of companion operations",
		},
		[]string{"operation"},
	)

	CompanionExperienceAdded = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "companion_service_experience_added_total",
			Help: "Total experience added to companions",
		},
		[]string{"source"},
	)
)

func RecordRequest(method, path, status string) {
	CompanionRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordCompanionOperation(operation string) {
	CompanionsTotal.WithLabelValues(operation).Inc()
}

func RecordExperienceAdded(source string, amount float64) {
	CompanionExperienceAdded.WithLabelValues(source).Add(amount)
}

