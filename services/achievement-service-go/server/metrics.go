package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	RequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "achievement_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "achievement_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	AchievementsUnlockedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "achievement_service_unlocks_total",
			Help: "Total number of achievements unlocked",
		},
		[]string{"category", "rarity"},
	)

	AchievementsProgressTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "achievement_service_progress_total",
			Help: "Total number of achievement progress updates",
		},
		[]string{"category"},
	)
)

func RecordRequest(method, path, status string) {
	RequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordAchievementUnlock(category, rarity string) {
	AchievementsUnlockedTotal.WithLabelValues(category, rarity).Inc()
}

func RecordAchievementProgress(category string) {
	AchievementsProgressTotal.WithLabelValues(category).Inc()
}

