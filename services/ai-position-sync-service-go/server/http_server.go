package server

import (
	"context"
	"crypto/tls"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"necpgame/services/ai-position-sync-service-go/pkg/api"
)

type HTTPServer struct {
	server *http.Server
	api    *api.Server // This will be the ogen generated server
}

func NewHTTPServer(addr string, handler http.Handler) *HTTPServer {
	return &HTTPServer{
		server: &http.Server{
			Addr:         addr,
			Handler:      handler,
			ReadTimeout:  15 * time.Second,  // Performance: HTTP server timeouts for real-time
			WriteTimeout: 15 * time.Second,  // Performance: HTTP server timeouts for real-time
			IdleTimeout:  60 * time.Second,  // Performance: Connection reuse for MMOFPS traffic
			TLSConfig: &tls.Config{
				MinVersion: tls.VersionTLS12, // Security: TLS 1.2 minimum
				CipherSuites: []uint16{ // Security: Secure cipher suites for performance
					tls.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
					tls.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
					tls.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305,
					tls.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305,
				},
			},
		},
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) StartTLS(certFile, keyFile string) error {
	return s.server.ListenAndServeTLS(certFile, keyFile)
}

func (s *HTTPServer) Stop(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Performance: Optimized router setup for high-throughput position sync
func SetupRouter(service Service, middleware *Middleware, metrics *ServiceMetrics) *chi.Mux {
	r := chi.NewRouter()

	// Performance: Critical middleware stack for real-time position sync
	r.Use(middleware.Timeout(10 * time.Second)) // Overall request timeout
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.CORS)
	r.Use(middleware.Auth)
	r.Use(metrics.Middleware)

	// Health check (no auth required for monitoring)
	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		handler := NewHandler(service)
		handler.AiPositionSyncHealthCheck(w, r)
	})

	// API v1 routes - these will be replaced by ogen generated routes
	v1 := chi.NewRouter()
	v1.Use(middleware.Auth) // Auth required for API calls

	// Position sync endpoints with strict timeouts for real-time requirements
	v1.With(middleware.Timeout(25*time.Millisecond)).Post("/positions", func(w http.ResponseWriter, r *http.Request) {
		handler := NewHandler(service)
		handler.UpdatePosition(w, r)
	})

	v1.With(middleware.Timeout(10*time.Millisecond)).Get("/positions/{entityId}", func(w http.ResponseWriter, r *http.Request) {
		handler := NewHandler(service)
		handler.GetPosition(w, r)
	})

	v1.With(middleware.Timeout(200*time.Millisecond)).Post("/positions/batch", func(w http.ResponseWriter, r *http.Request) {
		handler := NewHandler(service)
		handler.BatchUpdatePositions(w, r)
	})

	v1.With(middleware.Timeout(50*time.Millisecond)).Get("/zones/{zoneId}/positions", func(w http.ResponseWriter, r *http.Request) {
		handler := NewHandler(service)
		handler.GetZonePositions(w, r)
	})

	v1.With(middleware.Timeout(20*time.Millisecond)).Post("/predictions/movement", func(w http.ResponseWriter, r *http.Request) {
		handler := NewHandler(service)
		handler.PredictMovement(w, r)
	})

	r.Mount("/v1", v1)

	return r
}