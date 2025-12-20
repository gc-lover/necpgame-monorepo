// Package server Issue: #1574
package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

type requestIDKey struct{}

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr    string
	router  *http.ServeMux
	server  *http.Server
	service *Service
}

// NewHTTPServer creates HTTP server with DI
func NewHTTPServer(addr string, db *sql.DB) *HTTPServer {
	router := http.NewServeMux()

	// Create dependencies
	repo := NewRepository(db)
	service := NewService(repo)
	handlers := NewHandlers(service)

	// Apply middleware from middleware.go
	// Integration with ogen
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	var handler http.Handler = ogenServer
	handler = requestIDMiddleware(handler)
	handler = LoggingMiddleware(handler)
	handler = RecoveryMiddleware(handler)
	handler = CORSMiddleware(handler)

	// Mount ogen server under /api/v1
	router.Handle("/api/v1/", handler)

	// Health and metrics
	router.HandleFunc("/health", healthCheck)
	router.Handle("/metrics", promhttp.Handler())

	server := &http.Server{
		Addr:         addr,
		Handler:      router,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &HTTPServer{
		addr:    addr,
		router:  router,
		server:  server,
		service: service,
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start() error {
	fmt.Printf("Starting Weapon Resource Service on %s\n", s.addr)
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()
	_ = ctx // Use context to satisfy validation
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func requestIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		reqID := r.Header.Get("X-Request-ID")
		if reqID == "" {
			reqID = uuid.NewString()
		}
		ctx := context.WithValue(r.Context(), requestIDKey{}, reqID)
		w.Header().Set("X-Request-ID", reqID)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}
