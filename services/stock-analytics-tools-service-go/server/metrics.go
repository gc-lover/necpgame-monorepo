package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	toolsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_analytics_tools_requests_total",
			Help: "Total number of tools requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	toolsRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_analytics_tools_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	alertsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_analytics_alerts_requests_total",
			Help: "Total number of alerts requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	toolsErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_analytics_tools_errors_total",
			Help: "Total number of tools errors",
		},
		[]string{"type"},
	)
)

func RecordToolRequest(method, endpoint, status string) {
	toolsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordToolRequestDuration(method, endpoint string, duration float64) {
	toolsRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordAlertRequest(method, endpoint, status string) {
	alertsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	toolsErrorsTotal.WithLabelValues(errorType).Inc()
}

