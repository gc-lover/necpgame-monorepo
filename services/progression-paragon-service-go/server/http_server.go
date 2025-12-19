package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"net/http"
	"time"

	"github.com/necpgame/progression-paragon-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr           string
	router         *http.ServeMux
	paragonService ParagonServiceInterface
	logger         *logrus.Logger
	server         *http.Server
}

func NewHTTPServer(addr string, paragonService ParagonServiceInterface) *HTTPServer {
	router := http.NewServeMux()

	server := &HTTPServer{
		addr:           addr,
		router:         router,
		paragonService: paragonService,
		logger:         GetLogger(),
	}

	handlers := NewParagonHandlers(paragonService)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	var handler http.Handler = ogenServer
	handler = server.loggingMiddleware(handler)
	handler = server.metricsMiddleware(handler)
	handler = server.corsMiddleware(handler)
	handler = RecoveryMiddleware(handler)
	router.Handle("/api/v1/", handler)
	router.HandleFunc("/health", server.healthCheck)

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  30 * time.Second,  // Prevent slowloris attacks,
		WriteTimeout: 30 * time.Second,  // Prevent hanging writes,
		IdleTimeout:  120 * time.Second, // Keep connections alive for reuse,
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
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

// RecoveryMiddleware recovers from panics.
func RecoveryMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				http.Error(w, "internal server error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}




