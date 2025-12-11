// Issue: #1604
package server

import (
	"context"
	"net/http"
	"time"

	api "github.com/gc-lover/necpgame-monorepo/services/loot-service-go/pkg/api"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	logger *logrus.Logger
}

func NewHTTPServer(addr string, logger *logrus.Logger, service LootServiceInterface) *HTTPServer {
	handlers := NewHandlers(logger, service)
	secHandler := &SecurityHandler{}

	router := http.NewServeMux()

	// ogen server
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		logger.WithError(err).Fatal("Failed to create ogen server")
	}

	var handler http.Handler = ogenServer
	handler = loggingMiddleware(logger)(handler)
	handler = recoverMiddleware(logger)(handler)
	handler = corsMiddleware(handler)
	router.Handle("/api/v1/", handler)

	router.Handle("/metrics", promhttp.Handler())
	router.HandleFunc("/health", healthCheckHandler)

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

func healthCheckHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
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

func recoverMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if rec := recover(); rec != nil {
					logger.WithField("panic", rec).Error("recovered from panic")
					http.Error(w, "internal server error", http.StatusInternalServerError)
				}
			}()
			next.ServeHTTP(w, r)
		})
	}
}

func loggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			rr := &responseRecorder{ResponseWriter: w, statusCode: http.StatusOK}
			next.ServeHTTP(rr, r)
			logger.WithFields(logrus.Fields{
				"method":   r.Method,
				"path":     r.URL.Path,
				"status":   rr.statusCode,
				"duration": time.Since(start).String(),
			}).Info("request completed")
		})
	}
}

type responseRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (rr *responseRecorder) WriteHeader(code int) {
	rr.statusCode = code
	rr.ResponseWriter.WriteHeader(code)
}
