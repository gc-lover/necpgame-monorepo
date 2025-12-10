// Issue: #1574
package server

import (
	"context"
	"database/sql"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/weapon-progression-service-go/pkg/api"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

type requestIDKey struct{}

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr    string
	router  *http.ServeMux
	service *Service
	server  *http.Server
}

// NewHTTPServer creates HTTP server with DI
func NewHTTPServer(addr string, db *sql.DB) *HTTPServer {
	router := http.NewServeMux()

	// Create dependencies
	repo := NewRepository(db)
	service := NewService(repo)
	handlers := NewHandlers(service)

	// Apply middleware
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

	return &HTTPServer{
		addr:    addr,
		router:  router,
		service: service,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
		},
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
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
