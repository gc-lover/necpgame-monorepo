package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	RequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "referral_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "referral_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	ReferralCodesGeneratedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "referral_service_codes_generated_total",
			Help: "Total number of referral codes generated",
		},
		[]string{"player_id"},
	)

	ReferralsRegisteredTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "referral_service_registrations_total",
			Help: "Total number of referral registrations",
		},
		[]string{"status"},
	)

	MilestonesAchievedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "referral_service_milestones_achieved_total",
			Help: "Total number of milestones achieved",
		},
		[]string{"milestone_type"},
	)

	RewardsDistributedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "referral_service_rewards_distributed_total",
			Help: "Total number of rewards distributed",
		},
		[]string{"reward_type"},
	)
)

func RecordRequest(method, path, status string) {
	RequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordCodeGenerated(playerID string) {
	ReferralCodesGeneratedTotal.WithLabelValues(playerID).Inc()
}

func RecordReferralRegistered(status string) {
	ReferralsRegisteredTotal.WithLabelValues(status).Inc()
}

func RecordMilestoneAchieved(milestoneType string) {
	MilestonesAchievedTotal.WithLabelValues(milestoneType).Inc()
}

func RecordRewardDistributed(rewardType string) {
	RewardsDistributedTotal.WithLabelValues(rewardType).Inc()
}

