// Package server Issue: #150 - Middleware
package server

import (
	"log"
	"net/http"
	"time"
)

// LoggingMiddleware logs HTTP requests (structured JSON format)
func LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Wrap response writer to capture status code
		ww := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(ww, r)

		// Log in JSON format
		duration := time.Since(start)
		log.Printf(`{"method":"%s","path":"%s","status":%d,"duration_ms":%d}`,
			r.Method, r.URL.Path, ww.statusCode, duration.Milliseconds())
	})
}

// MetricsMiddleware collects metrics
func MetricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		next.ServeHTTP(w, r)

		// TODO: Collect real metrics (Prometheus)
		_ = time.Since(start)
	})
}

// responseWriter wraps http.ResponseWriter to capture status code
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}
