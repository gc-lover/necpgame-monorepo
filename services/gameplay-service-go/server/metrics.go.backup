package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	ProgressionRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "gameplay_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "gameplay_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	ExperienceAddedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "gameplay_service_experience_added_total",
			Help: "Total experience added to characters",
		},
		[]string{"source"},
	)

	LevelUpsTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "gameplay_service_level_ups_total",
			Help: "Total number of level ups",
		},
	)
)

func RecordRequest(method, path, status string) {
	ProgressionRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordExperienceAdded(source string, amount float64) {
	ExperienceAddedTotal.WithLabelValues(source).Add(amount)
}

func RecordLevelUp() {
	LevelUpsTotal.Inc()
}

