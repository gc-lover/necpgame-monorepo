package server

import (
	"context"
	"net/http"
	"time"

	api "github.com/gc-lover/necpgame-monorepo/services/league-service-go/pkg/api"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	logger *logrus.Logger
}

func NewHTTPServer(addr string, logger *logrus.Logger) *HTTPServer {
	handlers := NewHandlers(logger)

	router := http.NewServeMux()

	// ogen server integration
	ogenServer, err := api.NewServer(handlers, nil)
	if err != nil {
		logger.Fatalf("Failed to create ogen server: %v", err)
	}

	var handler http.Handler = ogenServer
	handler = corsMiddleware(handler)
	router.Handle("/", handler)

	router.Handle("/metrics", promhttp.Handler())

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
		logger: logger,
	}
}

func (s *HTTPServer) Start() error {
	s.logger.WithField("address", s.addr).Info("Starting HTTP server")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down HTTP server")
	return s.server.Shutdown(ctx)
}

func corsMiddleware(next http.Handler) http.Handler {
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



