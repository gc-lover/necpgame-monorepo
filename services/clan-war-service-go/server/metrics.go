package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	ClanWarRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "clan_war_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "clan_war_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	WarsDeclaredTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "clan_war_service_wars_declared_total",
			Help: "Total number of wars declared",
		},
	)

	BattlesCreatedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "clan_war_service_battles_created_total",
			Help: "Total number of battles created",
		},
		[]string{"type"},
	)

	WarsCompletedTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "clan_war_service_wars_completed_total",
			Help: "Total number of wars completed",
		},
	)
)

func RecordRequest(method, path, status string) {
	ClanWarRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordWarDeclared() {
	WarsDeclaredTotal.Inc()
}

func RecordBattleCreated(battleType string) {
	BattlesCreatedTotal.WithLabelValues(battleType).Inc()
}

func RecordWarCompleted() {
	WarsCompletedTotal.Inc()
}
