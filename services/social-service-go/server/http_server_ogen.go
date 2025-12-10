// Issue: Social Service ogen Migration
// HTTP Server setup with ogen integration
package server

import (
	"context"
	"net/http"
	"strings"
	"time"

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
	
	router := http.NewServeMux()
	var handler http.Handler

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
	router.Handle("/api/v1/", srv)

	// Issue: #1509 - Register order handlers
	if db != nil {
		orderService := NewOrderService(db, logger)
		orderHandlers := NewOrderHandlers(orderService, logger)
		router.Handle("/api/v1/social/orders/create", orderAuthMiddleware(logger)(http.HandlerFunc(orderHandlers.CreatePlayerOrder)))
		router.Handle("/api/v1/social/orders", orderAuthMiddleware(logger)(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			if r.Method == http.MethodGet && r.URL.Path == "/api/v1/social/orders" {
				orderHandlers.GetPlayerOrders(w, r)
				return
			}
			path := strings.TrimPrefix(r.URL.Path, "/api/v1/social/orders/")
			if path == r.URL.Path {
				http.NotFound(w, r)
				return
			}
			orderID := path
			switch r.Method {
			case http.MethodGet:
				r = r.WithContext(context.WithValue(r.Context(), "orderId", orderID))
				orderHandlers.GetPlayerOrder(w, r)
			case http.MethodPost:
				switch {
				case strings.HasSuffix(path, "/accept"):
					orderHandlers.AcceptPlayerOrder(w, r.WithContext(context.WithValue(r.Context(), "orderId", strings.TrimSuffix(orderID, "/accept"))))
				case strings.HasSuffix(path, "/start"):
					orderHandlers.StartPlayerOrder(w, r.WithContext(context.WithValue(r.Context(), "orderId", strings.TrimSuffix(orderID, "/start"))))
				case strings.HasSuffix(path, "/complete"):
					orderHandlers.CompletePlayerOrder(w, r.WithContext(context.WithValue(r.Context(), "orderId", strings.TrimSuffix(orderID, "/complete"))))
				case strings.HasSuffix(path, "/cancel"):
					orderHandlers.CancelPlayerOrder(w, r.WithContext(context.WithValue(r.Context(), "orderId", strings.TrimSuffix(orderID, "/cancel"))))
				default:
					http.NotFound(w, r)
				}
			default:
				http.NotFound(w, r)
			}
		})))
	}

	// Issue: #1490 - Register chat command handlers
	chatCommandService := NewChatCommandService(logger)
	chatCommandHandlers := NewChatCommandHandlers(chatCommandService, logger)
	router.Handle("/api/v1/social/chat/commands/execute", http.HandlerFunc(chatCommandHandlers.ExecuteChatCommand))

	// Health check (outside ogen routes)
	router.Handle("/health", http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ok","service":"social-service"}`))
	}))

	// Ready check
	router.Handle("/ready", http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ready"}`))
	}))

	handler = srv
	handler = loggingMiddleware(handler)
	handler = recoverMiddleware(handler)
	handler = timeoutMiddleware(handler, 60*time.Second)

	return &HTTPServerOgen{
		addr:   addr,
		logger: logger,
		server: &http.Server{
			Addr:              addr,
			Handler:           handler,
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

// chi-free middleware replacements
func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		logrus.WithFields(logrus.Fields{
			"method": r.Method,
			"path":   r.URL.Path,
		}).Info("request")
		next.ServeHTTP(w, r)
	})
}

func recoverMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				logrus.WithField("panic", rec).Error("recovered panic")
				http.Error(w, "internal error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}

func timeoutMiddleware(next http.Handler, d time.Duration) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), d)
		defer cancel()
		next.ServeHTTP(w, r.WithContext(ctx))
	})
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

