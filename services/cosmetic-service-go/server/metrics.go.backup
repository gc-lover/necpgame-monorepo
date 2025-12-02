package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	RequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "cosmetic_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "cosmetic_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	CosmeticsPurchasedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "cosmetic_service_purchases_total",
			Help: "Total number of cosmetics purchased",
		},
		[]string{"category", "rarity"},
	)

	CosmeticsEquippedTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "cosmetic_service_equips_total",
			Help: "Total number of cosmetics equipped",
		},
		[]string{"category"},
	)
)

func RecordRequest(method, path, status string) {
	RequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordCosmeticPurchase(category, rarity string) {
	CosmeticsPurchasedTotal.WithLabelValues(category, rarity).Inc()
}

func RecordCosmeticEquip(category string) {
	CosmeticsEquippedTotal.WithLabelValues(category).Inc()
}

