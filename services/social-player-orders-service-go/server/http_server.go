// Issue: #81
package server

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

type HTTPServer struct {
	addr    string
	router  chi.Router
	service *OrderService
	server  *http.Server
}

func NewHTTPServer(addr string, service *OrderService) *HTTPServer {
	router := chi.NewRouter()

	// Global middleware
	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.Timeout(60 * time.Second))
	router.Use(LoggingMiddleware)
	router.Use(MetricsMiddleware)

	// Create handlers
	handlers := NewOrderHandlers(service)

	// Register API routes (will be generated)
	// api.HandlerWithOptions(handlers, api.ChiServerOptions{
	//     BaseURL:    "/api/v1",
	//     BaseRouter: router,
	// })

	// Manual routes (for now)
	router.Route("/api/v1/social/orders", func(r chi.Router) {
		r.Get("/", handlers.ListOrders)
		r.Post("/create", handlers.CreateOrder)
		r.Get("/{orderId}", handlers.GetOrder)
		r.Post("/{orderId}/accept", handlers.AcceptOrder)
		r.Post("/{orderId}/complete", handlers.CompleteOrder)
		r.Post("/{orderId}/cancel", handlers.CancelOrder)
	})

	// Health check
	router.Get("/health", healthCheck)
	router.Get("/metrics", promhttp.Handler().ServeHTTP)

	return &HTTPServer{
		addr:    addr,
		router:  router,
		service: service,
	}
}

func (s *HTTPServer) Start() error {
	s.server = &http.Server{
		Addr:    s.addr,
		Handler: s.router,
	}
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{"status":"ok","service":"player-orders"}`)
}

