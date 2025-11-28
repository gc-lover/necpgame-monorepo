package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	httpRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "combat_sessions_http_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	httpRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "combat_ai_http_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	aiProfilesTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "combat_ai_profiles_total",
			Help: "Total number of AI profiles",
		},
	)

	encountersActive = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "combat_ai_encounters_active",
			Help: "Number of active encounters",
		},
	)

	raidsActive = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "combat_ai_raids_active",
			Help: "Number of active raids",
		},
	)

	aiTickDuration = promauto.NewHistogram(
		prometheus.HistogramOpts{
			Name:    "combat_ai_tick_duration_seconds",
			Help:    "AI tick processing duration in seconds",
			Buckets: []float64{0.001, 0.01, 0.05, 0.1, 0.5, 1.0},
		},
	)
)

func RecordRequest(method, path, status string) {
	httpRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	httpRequestDuration.WithLabelValues(method, path).Observe(duration)
}

func IncrementAIProfiles() {
	aiProfilesTotal.Inc()
}

func SetActiveEncounters(count float64) {
	encountersActive.Set(count)
}

func IncrementActiveEncounters() {
	encountersActive.Inc()
}

func DecrementActiveEncounters() {
	encountersActive.Dec()
}

func SetActiveRaids(count float64) {
	raidsActive.Set(count)
}

func IncrementActiveRaids() {
	raidsActive.Inc()
}

func DecrementActiveRaids() {
	raidsActive.Dec()
}

func RecordAITickDuration(duration float64) {
	aiTickDuration.Observe(duration)
}

