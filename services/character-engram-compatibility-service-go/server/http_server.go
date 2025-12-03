package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/necpgame/character-engram-compatibility-service-go/pkg/api"
)

func NewHTTPServer(addr string) *http.Server {
	r := chi.NewRouter()

	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(LoggerMiddleware)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	r.Use(func(next http.Handler) http.Handler {
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
	})

	handlers := &Handlers{}
	apiHandler := api.Handler(handlers)
	r.Mount("/", apiHandler)

	return &http.Server{
		Addr:         addr,
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}
}

func LoggerMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

		next.ServeHTTP(ww, r)

		duration := time.Since(start)
		RecordRequest(r.Method, r.URL.Path, http.StatusText(ww.Status()))
		RecordRequestDuration(r.Method, r.URL.Path, duration.Seconds())

		logger := GetLogger()
		logger.WithFields(map[string]interface{}{
			"method":     r.Method,
			"path":       r.URL.Path,
			"status":     ww.Status(),
			"duration":   duration.Milliseconds(),
			"request_id": middleware.GetReqID(r.Context()),
		}).Info("HTTP request")
	})
}

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		logger := GetLogger()
		logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func respondError(w http.ResponseWriter, status int, message string) {
	code := http.StatusText(status)
	err := api.Error{
		Code:    &code,
		Error:   http.StatusText(status),
		Message: message,
	}
	respondJSON(w, status, err)
}















