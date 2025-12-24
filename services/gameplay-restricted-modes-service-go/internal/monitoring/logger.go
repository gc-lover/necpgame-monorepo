// Issue: #1499
package monitoring

import (
	"net/http"
	"os"
	"strings"
	"time"

	"github.com/rs/zerolog"
)

// NewLogger creates a new structured logger
func NewLogger(level string) zerolog.Logger {
	// Parse log level
	logLevel := zerolog.InfoLevel
	switch strings.ToLower(level) {
	case "debug":
		logLevel = zerolog.DebugLevel
	case "info":
		logLevel = zerolog.InfoLevel
	case "warn", "warning":
		logLevel = zerolog.WarnLevel
	case "error":
		logLevel = zerolog.ErrorLevel
	case "fatal":
		logLevel = zerolog.FatalLevel
	case "panic":
		logLevel = zerolog.PanicLevel
	}

	// Configure logger
	logger := zerolog.New(os.Stdout).With().Timestamp().Logger().Level(logLevel)

	// Add service context
	logger = logger.With().Str("service", "gameplay-restricted-modes").Logger()

	return logger
}

// NewStructuredLogger creates a Chi middleware for structured logging
func NewStructuredLogger(logger zerolog.Logger) func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Wrap response writer to capture status code
			wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

			// Call next handler
			next.ServeHTTP(wrapped, r)

			// Log request
			logger.Info().
				Str("method", r.Method).
				Str("path", r.URL.Path).
				Str("remote_addr", r.RemoteAddr).
				Int("status", wrapped.statusCode).
				Dur("duration", time.Since(start)).
				Str("user_agent", r.UserAgent()).
				Msg("HTTP request")
		})
	}
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
