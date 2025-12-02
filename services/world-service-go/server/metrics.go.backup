package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	RequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "world_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	ResetExecutionsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_reset_executions_total",
			Help: "Total number of reset executions",
		},
		[]string{"reset_type", "status"},
	)

	QuestAssignmentsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_quest_assignments_total",
			Help: "Total number of quest assignments",
		},
		[]string{"pool_type"},
	)

	LoginRewardsClaimedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_login_rewards_claimed_total",
			Help: "Total number of login rewards claimed",
		},
		[]string{"reward_type"},
	)

	TravelEventsTriggeredTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_travel_events_triggered_total",
			Help: "Total number of travel events triggered",
		},
		[]string{"event_type"},
	)

	TravelEventsStartedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_travel_events_started_total",
			Help: "Total number of travel events started",
		},
		[]string{"event_id"},
	)

	TravelEventsCompletedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_travel_events_completed_total",
			Help: "Total number of travel events completed",
		},
		[]string{"event_type", "success"},
	)

	TravelEventsCancelledTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_travel_events_cancelled_total",
			Help: "Total number of travel events cancelled",
		},
		[]string{"event_id"},
	)

	TravelEventSkillChecksTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "world_service_travel_event_skill_checks_total",
			Help: "Total number of travel event skill checks",
		},
		[]string{"event_id", "skill", "success"},
	)
)

func RecordRequest(method, path, status string) {
	RequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordResetExecution(resetType, status string) {
	ResetExecutionsTotal.WithLabelValues(resetType, status).Inc()
}

func RecordQuestAssignment(poolType string) {
	QuestAssignmentsTotal.WithLabelValues(poolType).Inc()
}

func RecordLoginRewardClaimed(rewardType string) {
	LoginRewardsClaimedTotal.WithLabelValues(rewardType).Inc()
}

func RecordTravelEventTriggered(eventType string) {
	TravelEventsTriggeredTotal.WithLabelValues(eventType).Inc()
}

func RecordTravelEventStarted(eventID string) {
	TravelEventsStartedTotal.WithLabelValues(eventID).Inc()
}

func RecordTravelEventCompleted(eventType, success string) {
	TravelEventsCompletedTotal.WithLabelValues(eventType, success).Inc()
}

func RecordTravelEventCancelled(eventID string) {
	TravelEventsCancelledTotal.WithLabelValues(eventID).Inc()
}

func RecordTravelEventSkillCheck(eventID, skill, success string) {
	TravelEventSkillChecksTotal.WithLabelValues(eventID, skill, success).Inc()
}

