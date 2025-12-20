// Package server Issue: #1595
package server

import (
	"log"
	"net/http"
	"time"
)

// LoggingMiddleware logs HTTP requests
func LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Call next handler
		next.ServeHTTP(w, r)

		// Log request
		log.Printf(
			"method=%s path=%s duration=%v",
			r.Method,
			r.URL.Path,
			time.Since(start),
		)
	})
}

// MetricsMiddleware collects metrics
func MetricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Call next handler
		next.ServeHTTP(w, r)

		// Collect metrics
		duration := time.Since(start)
		_ = duration // TODO: Send to Prometheus
	})
}
