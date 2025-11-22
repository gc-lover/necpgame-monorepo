package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	HousingRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "housing_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	RequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "housing_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	ApartmentsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "housing_service_apartments_total",
			Help: "Total number of apartment operations",
		},
		[]string{"operation"},
	)

	FurniturePlacedTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "housing_service_furniture_placed_total",
			Help: "Total number of furniture items placed",
		},
	)
)

func RecordRequest(method, path, status string) {
	HousingRequestsTotal.WithLabelValues(method, path, status).Inc()
}

func RecordRequestDuration(method, path string, duration float64) {
	RequestDuration.WithLabelValues(method, path).Observe(duration)
}

func RecordApartmentOperation(operation string) {
	ApartmentsTotal.WithLabelValues(operation).Inc()
}

func RecordFurniturePlaced() {
	FurniturePlacedTotal.Inc()
}

