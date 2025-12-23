package monitoring

import (
	"io"
	"net/http"
	"os"
	"time"

	"github.com/go-chi/chi/v5/middleware"
	"github.com/rs/zerolog"
)

// NewLogger creates a new structured logger with appropriate configuration
func NewLogger(level string) zerolog.Logger {
	// Set global log level
	logLevel, err := zerolog.ParseLevel(level)
	if err != nil {
		logLevel = zerolog.InfoLevel
	}
	zerolog.SetGlobalLevel(logLevel)

	// Configure console writer for development
	var output io.Writer = zerolog.ConsoleWriter{
		Out:        os.Stdout,
		TimeFormat: time.RFC3339,
	}

	// In production, use JSON format
	if os.Getenv("ENVIRONMENT") == "production" {
		output = os.Stdout
	}

	logger := zerolog.New(output).
		With().
		Timestamp().
		Caller().
		Logger()

	return logger
}

// NewStructuredLogger returns a Chi middleware for structured logging
func NewStructuredLogger(logger zerolog.Logger) func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		fn := func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			defer func() {
				logger.Info().
					Str("method", r.Method).
					Str("url", r.URL.String()).
					Int("status", ww.Status()).
					Int("bytes", ww.BytesWritten()).
					Dur("duration", time.Since(start)).
					Str("remote_ip", r.RemoteAddr).
					Str("user_agent", r.UserAgent()).
					Str("request_id", middleware.GetReqID(r.Context())).
					Msg("HTTP request")
			}()

			next.ServeHTTP(ww, r)
		}
		return http.HandlerFunc(fn)
	}
}
