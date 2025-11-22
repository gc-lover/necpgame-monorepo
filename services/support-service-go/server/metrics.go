package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	SupportRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "support_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "support_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	TicketsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "support_service_tickets_total",
			Help: "Total number of tickets",
		},
		[]string{"status", "priority", "category"},
	)

	TicketsResolvedTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "support_service_tickets_resolved_total",
			Help: "Total number of resolved tickets",
		},
	)
)

func RecordRequest(method, path, status string) {
	SupportRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordTicket(status, priority, category string) {
	TicketsTotal.WithLabelValues(status, priority, category).Inc()
}

func RecordTicketResolved() {
	TicketsResolvedTotal.Inc()
}

