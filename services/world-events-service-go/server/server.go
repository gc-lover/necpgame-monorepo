// Package server Issue: #2224
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"
)

// WorldEventsServer represents the world events service server
type WorldEventsServer struct {
	server     *http.Server
	logger     *zap.Logger
	service    *WorldEventsService
	middleware *AuthMiddleware
}

// NewWorldEventsServer creates a new world events server
func NewWorldEventsServer(logger *zap.Logger, db *sql.DB, jwtSecret string) *WorldEventsServer {
	service := NewWorldEventsService(db, logger)
	authMiddleware := NewAuthMiddleware(logger, jwtSecret)

	r := chi.NewRouter()

	// Performance middleware for MMOFPS world events
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)

	// Security middleware
	r.Use(authMiddleware.SecurityHeadersMiddleware)
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// Logging middleware
	r.Use(authMiddleware.LoggingMiddleware)

	// Recovery middleware
	r.Use(authMiddleware.RecoveryMiddleware)

	// Health check endpoints
	r.Get("/health", service.HealthCheckHandler)
	r.Get("/ready", service.ReadinessCheckHandler)
	r.Get("/metrics", service.MetricsHandler)

	// API v1 routes
	r.Route("/api/v1", func(r chi.Router) {
		// Protected routes
		r.Group(func(r chi.Router) {
			r.Use(authMiddleware.JWTAuth)

			// World events CRUD
			r.Get("/world-events", service.ListWorldEventsHandler)
			r.Post("/world-events", service.CreateWorldEventHandler)
			r.Get("/world-events/{eventID}", service.GetWorldEventHandler)
			r.Put("/world-events/{eventID}", service.UpdateWorldEventHandler)
			r.Delete("/world-events/{eventID}", service.DeleteWorldEventHandler)

			// Event lifecycle management
			r.Post("/world-events/{eventID}/announce", service.AnnounceWorldEventHandler)
			r.Post("/world-events/{eventID}/activate", service.ActivateWorldEventHandler)
			r.Post("/world-events/{eventID}/deactivate", service.DeactivateWorldEventHandler)

			// Filtered queries
			r.Get("/world-events/active", service.GetActiveWorldEventsHandler)
			r.Get("/world-events/planned", service.GetPlannedWorldEventsHandler)
			r.Get("/world-events/by-scale/{scale}", service.GetWorldEventsByScaleHandler)
			r.Get("/world-events/by-type/{type}", service.GetWorldEventsByTypeHandler)
			r.Get("/world-events/by-frequency/{frequency}", service.GetWorldEventsByFrequencyHandler)

			// Event effects management
			r.Get("/world-events/{eventID}/effects", service.GetWorldEventEffectsHandler)
			r.Post("/world-events/{eventID}/effects", service.AddWorldEventEffectHandler)
			r.Put("/world-events/{eventID}/effects/{effectID}", service.UpdateWorldEventEffectHandler)
			r.Delete("/world-events/{eventID}/effects/{effectID}", service.DeleteWorldEventEffectHandler)

			// Event announcements
			r.Post("/world-events/{eventID}/announcements", service.CreateEventAnnouncementHandler)
		})
	})

	server := &http.Server{
		Addr:         ":8086",
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &WorldEventsServer{
		server:     server,
		logger:     logger,
		service:    service,
		middleware: authMiddleware,
	}
}

// Start starts the HTTP server
func (s *WorldEventsServer) Start(addr string) error {
	s.server.Addr = addr
	s.logger.Info("Starting World Events server", zap.String("addr", addr))
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *WorldEventsServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down World Events server")
	return s.server.Shutdown(ctx)
}

// AuthMiddleware handles authentication and authorization
type AuthMiddleware struct {
	logger    *zap.Logger
	jwtSecret string
}

// NewAuthMiddleware creates a new auth middleware
func NewAuthMiddleware(logger *zap.Logger, jwtSecret string) *AuthMiddleware {
	return &AuthMiddleware{
		logger:    logger,
		jwtSecret: jwtSecret,
	}
}

// SecurityHeadersMiddleware adds security headers
func (m *AuthMiddleware) SecurityHeadersMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("X-Content-Type-Options", "nosniff")
		w.Header().Set("X-Frame-Options", "DENY")
		w.Header().Set("X-XSS-Protection", "1; mode=block")
		w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
		next.ServeHTTP(w, r)
	})
}

// LoggingMiddleware logs HTTP requests
func (m *AuthMiddleware) LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Log request
		m.logger.Info("HTTP request started",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("remote_addr", r.RemoteAddr),
			zap.String("request_id", middleware.GetReqID(r.Context())),
		)

		// Create response writer wrapper for status code
		rw := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(rw, r)

		// Log response
		m.logger.Info("HTTP request completed",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.Int("status", rw.statusCode),
			zap.Duration("duration", time.Since(start)),
			zap.String("request_id", middleware.GetReqID(r.Context())),
		)
	})
}

// RecoveryMiddleware recovers from panics
func (m *AuthMiddleware) RecoveryMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if err := recover(); err != nil {
				m.logger.Error("Panic recovered",
					zap.Any("panic", err),
					zap.String("method", r.Method),
					zap.String("path", r.URL.Path),
					zap.String("request_id", middleware.GetReqID(r.Context())),
				)
				http.Error(w, "Internal server error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}

// JWTAuth validates JWT tokens
func (m *AuthMiddleware) JWTAuth(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			m.respondError(w, http.StatusUnauthorized, "Missing authorization header")
			return
		}

		// Extract token from "Bearer <token>"
		const bearerPrefix = "Bearer "
		if len(authHeader) < len(bearerPrefix) || authHeader[:len(bearerPrefix)] != bearerPrefix {
			m.respondError(w, http.StatusUnauthorized, "Invalid authorization header format")
			return
		}

		tokenString := authHeader[len(bearerPrefix):]

		// Parse and validate token
		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, jwt.ErrSignatureInvalid
			}
			return []byte(m.jwtSecret), nil
		})

		if err != nil || !token.Valid {
			m.respondError(w, http.StatusUnauthorized, "Invalid token")
			return
		}

		// Extract claims
		if claims, ok := token.Claims.(jwt.MapClaims); ok {
			// Add user info to context
			ctx := context.WithValue(r.Context(), "user_id", claims["user_id"])
			ctx = context.WithValue(ctx, "username", claims["username"])
			r = r.WithContext(ctx)
		}

		next.ServeHTTP(w, r)
	})
}

// responseWriter wraps http.ResponseWriter to capture status code
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// respondJSON sends a JSON response
func (m *AuthMiddleware) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

// respondError sends an error response
func (m *AuthMiddleware) respondError(w http.ResponseWriter, status int, message string) {
	m.respondJSON(w, status, map[string]interface{}{
		"error": message,
		"code":  status,
	})
}
