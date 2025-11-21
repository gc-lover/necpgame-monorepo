package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	inventoryRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "inventory_requests_total",
			Help: "Total number of inventory requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	inventoryRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "inventory_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	inventoryItemsTotal = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "inventory_items_total",
			Help: "Total number of items in inventory",
		},
		[]string{"character_id"},
	)

	inventoryErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "inventory_errors_total",
			Help: "Total number of inventory errors",
		},
		[]string{"type"},
	)
)

func RecordRequest(method, endpoint, status string) {
	inventoryRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordRequestDuration(method, endpoint string, duration float64) {
	inventoryRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func SetInventoryItems(characterID string, count float64) {
	inventoryItemsTotal.WithLabelValues(characterID).Set(count)
}

func RecordError(errorType string) {
	inventoryErrorsTotal.WithLabelValues(errorType).Inc()
}
