package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/stock-dividends-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr     string
	router   *chi.Mux
	handlers *Handlers
	logger   *logrus.Logger
	server   *http.Server
}

func NewHTTPServer(addr string, handlers *Handlers, logger *logrus.Logger) *HTTPServer {
	router := chi.NewRouter()
	
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(corsMiddleware)
	router.Use(loggingMiddleware(logger))
	router.Use(metricsMiddleware)

	apiRouter := chi.NewRouter()
	
	handlerWrapper := api.HandlerFromMux(handlers, apiRouter)
	router.Mount("/v1", handlerWrapper)

	router.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	return &HTTPServer{
		addr:     addr,
		router:   router,
		handlers: handlers,
		logger:   logger,
	}
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

func loggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			next.ServeHTTP(w, r)
			duration := time.Since(start)
			logger.WithFields(map[string]interface{}{
				"method":   r.Method,
				"path":     r.URL.Path,
				"duration": duration,
			}).Info("HTTP request")
		})
	}
}

func metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		duration := time.Since(start).Seconds()
		RecordDividendRequestDuration(r.Method, r.URL.Path, duration)
	})
}

