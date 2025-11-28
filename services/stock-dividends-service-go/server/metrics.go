package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	dividendsRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_dividends_requests_total",
			Help: "Total number of dividends requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	dividendsRequestDuration = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "stock_dividends_request_duration_seconds",
			Help:    "Request processing duration in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)

	dripRequestsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_dividends_drip_requests_total",
			Help: "Total number of DRIP requests",
		},
		[]string{"method", "endpoint", "status"},
	)

	dividendsErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "stock_dividends_errors_total",
			Help: "Total number of dividends errors",
		},
		[]string{"type"},
	)
)

func RecordDividendRequest(method, endpoint, status string) {
	dividendsRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordDividendRequestDuration(method, endpoint string, duration float64) {
	dividendsRequestDuration.WithLabelValues(method, endpoint).Observe(duration)
}

func RecordDRIPRequest(method, endpoint, status string) {
	dripRequestsTotal.WithLabelValues(method, endpoint, status).Inc()
}

func RecordError(errorType string) {
	dividendsErrorsTotal.WithLabelValues(errorType).Inc()
}

