// Package server Issue: #1588 - Retry with Exponential Backoff
package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	_ = promauto.NewHistogram(
		prometheus.HistogramOpts{
			Name:    "operation_retries",
			Help:    "Number of retries per operation",
			Buckets: []float64{0, 1, 2, 3, 5, 10},
		},
	)
)

// RetryWithBackoff retries operation with exponential backoff

// isRetryable checks if error is retryable
