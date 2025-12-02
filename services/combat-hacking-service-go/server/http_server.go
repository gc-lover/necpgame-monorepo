// Issue: #57
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/necpgame/combat-hacking-service-go/pkg/api"
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

	server := &HTTPServer{
		addr:   addr,
		router: router,
		logger: logger,
	}

	handlers := NewHackingHandlers()

	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	router.Get("/health", server.healthCheck)

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
	}
}


