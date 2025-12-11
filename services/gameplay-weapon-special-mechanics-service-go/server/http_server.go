// Issue: #1595
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-weapon-special-mechanics-service-go/pkg/api"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	router *http.ServeMux
}

func NewHTTPServer(addr string, service *Service) *HTTPServer {
	router := http.NewServeMux()

	handlers := NewHandlers(service)
	secHandler := NewSecurityHandler()

	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	var handler http.Handler = ogenServer
	handler = LoggingMiddleware(handler)
	handler = MetricsMiddleware(handler)
	handler = RecoveryMiddleware(handler)
	router.Handle("/api/v1/", handler)

	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
		},
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# HELP weapon_special_mechanics_service metrics\n"))
}

// LoggingMiddleware logs requests.
func LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		_ = time.Since(start)
	})
}

// MetricsMiddleware is a stub for metrics collection.
func MetricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		next.ServeHTTP(w, r)
	})
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

