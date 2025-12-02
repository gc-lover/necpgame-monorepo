package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	RequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "battle_pass_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "battle_pass_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	BattlePassLevelUpsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "battle_pass_service_level_ups_total",
			Help: "Total number of battle pass level ups",
		},
		[]string{"season_id"},
	)

	BattlePassRewardsClaimedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "battle_pass_service_rewards_claimed_total",
			Help: "Total number of battle pass rewards claimed",
		},
		[]string{"season_id", "track"},
	)

	BattlePassPremiumPurchasedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "battle_pass_service_premium_purchased_total",
			Help: "Total number of premium battle pass purchases",
		},
		[]string{"season_id"},
	)
)

func RecordRequest(method, path, status string) {
	RequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordLevelUp(seasonID string) {
	BattlePassLevelUpsTotal.WithLabelValues(seasonID).Inc()
}

func RecordRewardClaimed(seasonID, track string) {
	BattlePassRewardsClaimedTotal.WithLabelValues(seasonID, track).Inc()
}

func RecordPremiumPurchased(seasonID string) {
	BattlePassPremiumPurchasedTotal.WithLabelValues(seasonID).Inc()
}

