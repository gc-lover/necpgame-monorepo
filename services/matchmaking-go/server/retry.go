// Issue: #1588 - Retry with Exponential Backoff
package server

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	retriesTotal = promauto.NewHistogram(
		prometheus.HistogramOpts{
			Name:    "operation_retries",
			Help:    "Number of retries per operation",
			Buckets: []float64{0, 1, 2, 3, 5, 10},
		},
	)
)

// RetryWithBackoff retries operation with exponential backoff
func RetryWithBackoff(ctx context.Context, fn func() error, maxRetries int) error {
	backoff := 100 * time.Millisecond
	maxBackoff := 10 * time.Second
	
	var retries int
	for retry := 0; retry < maxRetries; retry++ {
		err := fn()
		if err == nil {
			retriesTotal.Observe(float64(retry))
			return nil // Success
		}
		
		// Check if retryable
		if !isRetryable(err) {
			return err // Don't retry
		}
		
		if retry < maxRetries-1 {
			// Check context cancellation
			select {
			case <-ctx.Done():
				return ctx.Err()
			case <-time.After(backoff):
				// Continue retry
			}
			
			backoff *= 2
			if backoff > maxBackoff {
				backoff = maxBackoff
			}
			retries++
		}
	}
	
	retriesTotal.Observe(float64(retries))
	return fmt.Errorf("failed after %d retries", maxRetries)
}

// isRetryable checks if error is retryable
func isRetryable(err error) bool {
	// Retry on network errors, timeouts, 5xx
	if errors.Is(err, context.DeadlineExceeded) {
		return true
	}
	
	// Add more retryable error checks as needed
	return false
}

