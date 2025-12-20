package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	_ = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "admin_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	_ = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "admin_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	AdminActionsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "admin_service_actions_total",
			Help: "Total number of admin actions",
		},
		[]string{"action_type"},
	)
)

func RecordAdminAction(actionType string) {
	AdminActionsTotal.WithLabelValues(actionType).Inc()
}
