package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	RequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "leaderboard_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "leaderboard_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	LeaderboardQueriesTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "leaderboard_service_queries_total",
			Help: "Total number of leaderboard queries",
		},
		[]string{"scope", "metric"},
	)

	ScoreUpdatesTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "leaderboard_service_score_updates_total",
			Help: "Total number of score updates",
		},
		[]string{"metric"},
	)
)

func RecordRequest(method, path, status string) {
	RequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordLeaderboardQuery(scope, metric string) {
	LeaderboardQueriesTotal.WithLabelValues(scope, metric).Inc()
}

func RecordScoreUpdate(metric string) {
	ScoreUpdatesTotal.WithLabelValues(metric).Inc()
}

