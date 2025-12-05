// Issue: Social Service ogen Migration
// HTTP Server setup with ogen integration
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// HTTPServerOgen wraps ogen-based HTTP server
type HTTPServerOgen struct {
	addr   string
	logger *logrus.Logger
	server *http.Server
}

// NewHTTPServerOgen creates new HTTP server with ogen
func NewHTTPServerOgen(addr string, logger *logrus.Logger, db *pgxpool.Pool) *HTTPServerOgen {
	// Issue: #1380 - Initialize logger for utils package
	SetLogger(logger)
	
	router := chi.NewRouter()

	// Middleware
	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.Timeout(60 * time.Second))

	// Create ogen handlers
	handlers := NewSocialHandlersOgen(logger, db)
	
	// Issue: #1488 - Initialize Party service if DB is available
	if db != nil {
		partyRepo := NewPartyRepository(db)
		partyService := NewPartyService(partyRepo)
		handlers.SetPartyService(partyService)
	}
	
	// Create security handler
	security := NewSecurityHandler()

	// Create ogen server
	srv, err := api.NewServer(handlers, security)
	if err != nil {
		panic("Failed to create ogen server: " + err.Error())
	}

	// Mount ogen routes
	router.Mount("/api/v1", srv)

	// Issue: #1509 - Register order handlers
	if db != nil {
		orderService := NewOrderService(db, logger)
		orderHandlers := NewOrderHandlers(orderService, logger)
		router.Route("/api/v1/social/orders", func(r chi.Router) {
			r.Post("/create", orderHandlers.CreatePlayerOrder)
			r.Get("/", orderHandlers.GetPlayerOrders)
			r.Get("/{orderId}", orderHandlers.GetPlayerOrder)
			r.Post("/{orderId}/accept", orderHandlers.AcceptPlayerOrder)
			r.Post("/{orderId}/start", orderHandlers.StartPlayerOrder)
			r.Post("/{orderId}/complete", orderHandlers.CompletePlayerOrder)
			r.Post("/{orderId}/cancel", orderHandlers.CancelPlayerOrder)
		})
	}

	// Health check (outside ogen routes)
	router.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ok","service":"social-service"}`))
	})

	// Ready check
	router.Get("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ready"}`))
	})

	return &HTTPServerOgen{
		addr:   addr,
		logger: logger,
		server: &http.Server{
			Addr:              addr,
			Handler:           router,
			ReadHeaderTimeout: 5 * time.Second,
			ReadTimeout:       10 * time.Second,
			WriteTimeout:      10 * time.Second,
			IdleTimeout:       60 * time.Second,
		},
	}
}

// Start starts the HTTP server
func (s *HTTPServerOgen) Start() error {
	s.logger.WithField("addr", s.addr).Info("Starting ogen HTTP server")
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServerOgen) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down ogen HTTP server")
	return s.server.Shutdown(ctx)
}

