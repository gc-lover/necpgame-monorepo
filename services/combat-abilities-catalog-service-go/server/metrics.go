package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	httpRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "combat_abilities_catalog_http_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	httpRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "combat_abilities_catalog_http_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	abilitiesTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "combat_abilities_catalog_total",
			Help: "Total number of abilities in catalog",
		},
	)

	loadoutsActive = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "combat_abilities_loadouts_active",
			Help: "Number of active loadouts",
		},
	)
)

func RecordRequest(method, path, status string) {
	httpRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	httpRequestDuration.WithLabelValues(method, path).Observe(duration)
}

func IncrementAbilitiesTotal() {
	abilitiesTotal.Inc()
}

func SetActiveLoadouts(count float64) {
	loadoutsActive.Set(count)
}

func IncrementActiveLoadouts() {
	loadoutsActive.Inc()
}

func DecrementActiveLoadouts() {
	loadoutsActive.Dec()
}

