package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	_ = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "achievement_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	_ = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "achievement_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	_ = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "achievement_service_unlocks_total",
			Help: "Total number of achievements unlocked",
		},
		[]string{"category", "rarity"},
	)

	_ = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "achievement_service_progress_total",
			Help: "Total number of achievement progress updates",
		},
		[]string{"category"},
	)
)
