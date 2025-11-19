package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	matchmakingTicketsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "matchmaking_tickets_total",
			Help: "Total number of matchmaking tickets processed",
		},
		[]string{"status"},
	)

	matchmakingMatchesTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "matchmaking_matches_total",
			Help: "Total number of matches created",
		},
	)

	matchmakingQueueSize = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "matchmaking_queue_size",
			Help: "Current size of matchmaking queue",
		},
		[]string{"mode"},
	)

	matchmakingErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "matchmaking_errors_total",
			Help: "Total number of matchmaking errors",
		},
		[]string{"type"},
	)

	matchmakingLoopDuration = promauto.NewHistogram(
		prometheus.HistogramOpts{
			Name:    "matchmaking_loop_duration_seconds",
			Help:    "Matchmaking loop duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
	)
)

func RecordTicket(status string) {
	matchmakingTicketsTotal.WithLabelValues(status).Inc()
}

func RecordMatch() {
	matchmakingMatchesTotal.Inc()
}

func RecordQueueSize(mode string, size int) {
	matchmakingQueueSize.WithLabelValues(mode).Set(float64(size))
}

func RecordError(errorType string) {
	matchmakingErrorsTotal.WithLabelValues(errorType).Inc()
}

func RecordLoopDuration(duration float64) {
	matchmakingLoopDuration.Observe(duration)
}

