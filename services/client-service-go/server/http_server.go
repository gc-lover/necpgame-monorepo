package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	api "github.com/necpgame/client-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr   string
	router *http.ServeMux
	logger *logrus.Logger
	server *http.Server
}

func NewHTTPServer(addr string, weaponEffectsService WeaponEffectsServiceInterface) *HTTPServer {
	server := &HTTPServer{
		addr:   addr,
		router: http.NewServeMux(),
		logger: GetLogger(),
	}

	// Middleware chain applied per handler
	handlerChain := func(h http.Handler) http.Handler {
		h = server.loggingMiddleware(h)
		h = server.metricsMiddleware(h)
		h = server.corsMiddleware(h)
		h = http.TimeoutHandler(h, 60*time.Second, "request timed out")
		return h
	}

	if weaponEffectsService != nil {
		// Handlers (реализация api.Handler из handlers.go)
		handlers := NewHandlers(weaponEffectsService)

		// Security handler
		secHandler := NewSecurityHandler()

		// Integration with ogen
		ogenServer, err := api.NewServer(handlers, secHandler)
		if err != nil {
			panic(err)
		}

		server.router.Handle("/api/v1", handlerChain(ogenServer))
	}

	server.router.Handle("/health", handlerChain(http.HandlerFunc(server.healthCheck)))

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
			defer close(errChan)
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

// Issue: #141886468
func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		s.logger.WithFields(logrus.Fields{
			"method": r.Method,
			"path":   r.URL.Path,
		}).Info("Request")
		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}
		next.ServeHTTP(w, r)
	})
}


