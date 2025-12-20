// Package server Issue: #???
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"net/http"
	"time"

	_ "net/http/pprof" // Import for pprof endpoints

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"
)

// LeagueSystemServer represents the league system service server
type LeagueSystemServer struct {
	server     *http.Server
	logger     *zap.Logger
	service    *LeagueService
	middleware *AuthMiddleware
}

// NewLeagueSystemServer creates a new league system server
func NewLeagueSystemServer(logger *zap.Logger, db *sql.DB, jwtSecret string) *LeagueSystemServer {
	service := NewLeagueService(db, logger)
	authMiddleware := NewAuthMiddleware(logger, jwtSecret)

	r := chi.NewRouter()

	// Performance middleware for MMOFPS league operations
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

	// Profiling endpoints for MMOFPS optimization (imported via blank import)

	// API v1 routes
	r.Route("/api/v1", func(r chi.Router) {
		// Protected routes
		r.Group(func(r chi.Router) {
			r.Use(authMiddleware.JWTAuth)

			// League information
			r.Get("/league/current", service.GetCurrentLeagueHandler)
			r.Get("/league/{leagueId}/statistics", service.GetLeagueStatisticsHandler)
			r.Get("/league/countdown", service.GetLeagueCountdownHandler)
			r.Get("/league/phases", service.GetLeaguePhasesHandler)

			// League actions
			r.Post("/league/end-event/register", service.RegisterForEndEventHandler)
			r.Post("/league/reset/trigger", service.TriggerLeagueResetHandler) // Admin only

			// Player meta-progression
			r.Get("/player/legacy-progress", service.GetPlayerLegacyProgressHandler)

			// Hall of Fame
			r.Get("/league/hall-of-fame", service.GetHallOfFameHandler)

			// Legacy Shop
			r.Get("/league/legacy-shop/items", service.GetLegacyShopItemsHandler)
			r.Post("/league/legacy-shop/purchase", service.PurchaseLegacyItemHandler)
		})
	})

	server := &http.Server{
		Addr:         ":8093",
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &LeagueSystemServer{
		server:     server,
		logger:     logger,
		service:    service,
		middleware: authMiddleware,
	}
}

// Start starts the HTTP server
func (s *LeagueSystemServer) Start(addr string) error {
	s.server.Addr = addr
	s.logger.Info("Starting League System server", zap.String("addr", addr))
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *LeagueSystemServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down League System server")
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
		w.Header().Set("X-Content-Security-Policy", "default-src 'self'")
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
