// Issue: #131
package server

import (
	"net/http"
	"time"

	"github.com/sirupsen/logrus"
)

var logger = logrus.New()

func GetLogger() *logrus.Logger {
	return logger
}

func LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		logger.WithFields(logrus.Fields{
			"method":     r.Method,
			"path":       r.URL.Path,
			"duration":   time.Since(start),
			"status":     w.Header().Get("Status"),
		}).Info("HTTP request")
	})
}

func MetricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		next.ServeHTTP(w, r)
	})
}

