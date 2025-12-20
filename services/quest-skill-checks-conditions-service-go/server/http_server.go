package server

import (
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/quest-skill-checks-conditions-service-go/pkg/api"
)

func NewHTTPServer(addr string) *http.Server {
	mux := http.NewServeMux()

	// ogen handlers
	handlers := &Handlers{}
	secHandler := &SecurityHandler{}

	// Integration with ogen
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		GetLogger().WithError(err).Fatal("Failed to create ogen server")
	}

	// Mount ogen server under /api/v1
	var handler http.Handler = ogenServer
	handler = LoggerMiddleware(handler)
	handler = corsMiddleware(handler)
	handler = http.TimeoutHandler(handler, 60*time.Second, "request timed out")

	mux.Handle("/api/v1", handler)

	return &http.Server{
		Addr:         addr,
		Handler:      mux,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}
}

func LoggerMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		// Simple wrapper without chi dependency
		rec := &responseRecorder{ResponseWriter: w, status: http.StatusOK}

		next.ServeHTTP(rec, r)

		duration := time.Since(start)
		RecordRequest(r.Method, r.URL.Path, http.StatusText(rec.status))
		RecordRequestDuration(r.Method, r.URL.Path, duration.Seconds())

		logger := GetLogger()
		logger.WithFields(map[string]interface{}{
			"method":   r.Method,
			"path":     r.URL.Path,
			"status":   rec.status,
			"duration": duration.Milliseconds(),
		}).Info("HTTP request")
	})
}

type responseRecorder struct {
	http.ResponseWriter
	status int
}

func (r *responseRecorder) WriteHeader(statusCode int) {
	r.status = statusCode
	r.ResponseWriter.WriteHeader(statusCode)
}

func corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusNoContent)
			return
		}
		next.ServeHTTP(w, r)
	})
}
