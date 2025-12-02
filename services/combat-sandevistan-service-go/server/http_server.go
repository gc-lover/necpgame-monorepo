// Issue: #39
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/necpgame/combat-sandevistan-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr   string
	router *chi.Mux
	logger *logrus.Logger
	server *http.Server
}

func NewHTTPServer(addr string) *HTTPServer {
	router := chi.NewRouter()

	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.Timeout(60 * time.Second))

	logger := GetLogger()
	router.Use(loggingMiddleware(logger))
	router.Use(metricsMiddleware())
	router.Use(corsMiddleware())

	handlers := NewSandevistanHandlers()
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	router.Get("/health", healthCheck)

	server := &HTTPServer{
		addr:   addr,
		router: router,
		logger: logger,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
	}

	return server
}

func (s *HTTPServer) ListenAndServe() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func respondJSON(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		GetLogger().WithError(err).Error("Failed to encode JSON response")
	}
}

func respondError(w http.ResponseWriter, statusCode int, err error, details string) {
	GetLogger().WithError(err).Error(details)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	code := http.StatusText(statusCode)
	errorResponse := api.Error{
		Code:    &code,
		Message: details,
		Error:   "error",
	}
	if err := json.NewEncoder(w).Encode(errorResponse); err != nil {
		GetLogger().WithError(err).Error("Failed to encode JSON error response")
	}
}

