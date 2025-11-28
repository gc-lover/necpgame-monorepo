package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	chartsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_analytics_charts_requests_total",
			Help: "Total number of charts requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	chartsRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_analytics_charts_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	indicatorsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_analytics_indicators_requests_total",
			Help: "Total number of indicators requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	chartsErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_analytics_charts_errors_total",
			Help: "Total number of charts errors",
		},
		[]string{"type"},
	)
)

func RecordChartRequest(method, endpoint, status string) {
	chartsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordChartRequestDuration(method, endpoint string, duration float64) {
	chartsRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordIndicatorRequest(method, endpoint, status string) {
	indicatorsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	chartsErrorsTotal.WithLabelValues(errorType).Inc()
}

