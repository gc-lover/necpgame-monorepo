package api

import (
	"net/http"
	"strconv"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

var (
	// HTTP request metrics
	httpRequestsTotal = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "battle_pass_http_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	httpRequestDuration = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "battle_pass_http_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "endpoint"},
	)

	// Business metrics
	xpGrantedTotal = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "battle_pass_xp_granted_total",
			Help: "Total XP granted to players",
		},
		[]string{"reason"},
	)

	rewardsClaimedTotal = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "battle_pass_rewards_claimed_total",
			Help: "Total rewards claimed by players",
		},
		[]string{"tier", "type"},
	)

	activePlayers = prometheus.NewGauge(
		prometheus.GaugeOpts{
			Name: "battle_pass_active_players",
			Help: "Number of currently active players",
		},
	)

	// Database metrics
	dbConnectionsActive = prometheus.NewGauge(
		prometheus.GaugeOpts{
			Name: "battle_pass_db_connections_active",
			Help: "Number of active database connections",
		},
	)

	dbQueryDuration = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "battle_pass_db_query_duration_seconds",
			Help:    "Database query duration in seconds",
			Buckets: []float64{.001, .005, .01, .025, .05, .1, .25, .5, 1, 2.5},
		},
		[]string{"query_type"},
	)
)

func init() {
	// Register metrics with Prometheus
	prometheus.MustRegister(
		httpRequestsTotal,
		httpRequestDuration,
		xpGrantedTotal,
		rewardsClaimedTotal,
		activePlayers,
		dbConnectionsActive,
		dbQueryDuration,
	)
}

// MetricsHandler returns the Prometheus metrics handler
func MetricsHandler() http.Handler {
	return promhttp.Handler()
}

// RecordHTTPRequest records HTTP request metrics
func RecordHTTPRequest(method, endpoint string, statusCode int, duration time.Duration) {
	status := strconv.Itoa(statusCode)
	httpRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
	httpRequestDuration.WithLabelValues(method, endpoint).Observe(duration.Seconds())
}

// RecordXPGranted records XP grant metrics
func RecordXPGranted(amount int, reason string) {
	xpGrantedTotal.WithLabelValues(reason).Add(float64(amount))
}

// RecordRewardClaimed records reward claim metrics
func RecordRewardClaimed(tier, rewardType string) {
	rewardsClaimedTotal.WithLabelValues(tier, rewardType).Inc()
}

// UpdateActivePlayers updates the active players gauge
func UpdateActivePlayers(count int) {
	activePlayers.Set(float64(count))
}

// UpdateDBConnections updates the database connections gauge
func UpdateDBConnections(count int) {
	dbConnectionsActive.Set(float64(count))
}

// RecordDBQuery records database query metrics
func RecordDBQuery(queryType string, duration time.Duration) {
	dbQueryDuration.WithLabelValues(queryType).Observe(duration.Seconds())
}