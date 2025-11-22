package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	RequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "economy_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "economy_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	TradesTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "economy_service_trades_total",
			Help: "Total number of trades",
		},
		[]string{"status"},
	)

	TradesCompletedTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "economy_service_trades_completed_total",
			Help: "Total number of completed trades",
		},
	)
)

func RecordRequest(method, path, status string) {
	RequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordTrade(status string) {
	TradesTotal.WithLabelValues(status).Inc()
}

func RecordTradeCompleted() {
	TradesCompletedTotal.Inc()
}

