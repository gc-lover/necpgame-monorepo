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
		
		// Initialize Friend service
		friendService := NewFriendService(db, logger)
		handlers.SetFriendService(friendService)
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
			// Add auth middleware for order routes
			r.Use(orderAuthMiddleware(logger))
			r.Post("/create", orderHandlers.CreatePlayerOrder)
			r.Get("/", orderHandlers.GetPlayerOrders)
			r.Get("/{orderId}", orderHandlers.GetPlayerOrder)
			r.Post("/{orderId}/accept", orderHandlers.AcceptPlayerOrder)
			r.Post("/{orderId}/start", orderHandlers.StartPlayerOrder)
			r.Post("/{orderId}/complete", orderHandlers.CompletePlayerOrder)
			r.Post("/{orderId}/cancel", orderHandlers.CancelPlayerOrder)
		})
	}

	// Issue: #1490 - Register chat command handlers
	chatCommandService := NewChatCommandService(logger)
	chatCommandHandlers := NewChatCommandHandlers(chatCommandService, logger)
	router.Post("/api/v1/social/chat/commands/execute", chatCommandHandlers.ExecuteChatCommand)

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

// Issue: #1509 - Auth middleware for order routes
// Extracts user_id from Authorization header and adds to context
func orderAuthMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			authHeader := r.Header.Get("Authorization")
			if authHeader == "" {
				http.Error(w, "Unauthorized", http.StatusUnauthorized)
				return
			}

			// TODO: Implement proper JWT validation
			// For now, extract user_id from token (mock implementation)
			// In production, this should validate JWT and extract user_id from claims
			// For development, we can use a simple mock
			
			// Mock: Extract user_id from token (in production, parse JWT)
			// This is a temporary solution until JWT validation is implemented
			userID := r.Header.Get("X-User-ID") // For development/testing
			if userID == "" {
				// Try to extract from Authorization header (mock)
				// In production, this should parse JWT token
				userID = "00000000-0000-0000-0000-000000000000" // Mock user ID
			}

			ctx := context.WithValue(r.Context(), "user_id", userID)
			next.ServeHTTP(w, r.WithContext(ctx))
		})
	}
}

