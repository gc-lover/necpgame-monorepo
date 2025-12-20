package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	characterRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "character_requests_total",
			Help: "Total number of character requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	characterRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "character_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	charactersTotal = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "characters_total",
			Help: "Total number of characters",
		},
		[]string{"account_id"},
	)

	_ = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "character_errors_total",
			Help: "Total number of character errors",
		},
		[]string{"type"},
	)
)

func RecordRequest(method, endpoint, status string) {
	characterRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordRequestDuration(method, endpoint string, duration float64) {
	characterRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func SetCharactersCount(accountID string, count float64) {
	charactersTotal.WithLabelValues(accountID).Set(count)
}
