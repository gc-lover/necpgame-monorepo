// Package server implements the HTTP server for Cyberware Service
package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
	"os"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"
)

// CyberwareServer represents the HTTP server for cyberware service
type CyberwareServer struct {
	server  *http.Server
	logger  *zap.Logger
	service *CyberwareService
}

// NewCyberwareServer creates a new cyberware HTTP server
func NewCyberwareServer(db *sql.DB, logger *zap.Logger) (*CyberwareServer, error) {
	service := NewCyberwareService(db, logger)

	r := chi.NewRouter()

	// Middleware stack
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(NewStructuredLogger(logger))
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// Security headers
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			w.Header().Set("X-Content-Type-Options", "nosniff")
			w.Header().Set("X-Frame-Options", "DENY")
			w.Header().Set("X-XSS-Protection", "1; mode=block")
			w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
			next.ServeHTTP(w, r)
		})
	})

	// CORS configuration for web client
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"https://*.necp.game", "http://localhost:*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// JWT Authentication middleware
	r.Use(JWTAuthMiddleware)

	// Health check endpoints
	r.Get("/health", service.HealthCheckHandler)
	r.Get("/ready", service.ReadinessCheckHandler)
	r.Get("/metrics", service.MetricsHandler)

	// API routes
	r.Route("/api/v1/cyberware", func(r chi.Router) {
		// Catalog endpoints
		r.Get("/catalog", service.GetImplantCatalogHandler)
		r.Get("/catalog/{implantId}", service.GetImplantDetailHandler)

		// Character-specific endpoints
		r.Route("/characters/{characterId}", func(r chi.Router) {
			r.Get("/implants", service.GetCharacterImplantsHandler)
			r.Post("/implants/acquire", service.AcquireImplantHandler)
			r.Post("/implants/install", service.InstallImplantHandler)
			r.Post("/implants/{implantId}/uninstall", service.UninstallImplantHandler)
			r.Post("/implants/{implantId}/upgrade", service.UpgradeImplantHandler)
			r.Get("/limits", service.GetImplantLimitsHandler)
			r.Post("/compatibility", service.CheckCompatibilityHandler)
			r.Get("/cyberpsychosis", service.GetCyberpsychosisStateHandler)
			r.Get("/synergies", service.GetActiveSynergiesHandler)
			r.Get("/visuals", service.GetImplantVisualsHandler)
			r.Put("/visuals", service.UpdateImplantVisualsHandler)
		})
	})

	server := &http.Server{
		Addr:         ":8086",
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	return &CyberwareServer{
		server:  server,
		logger:  logger,
		service: service,
	}, nil
}

// Start starts the HTTP server
func (s *CyberwareServer) Start() error {
	s.logger.Info("Cyberware Service HTTP server starting", zap.String("addr", s.server.Addr))
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *CyberwareServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// JWTAuthMiddleware validates JWT tokens
func JWTAuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Skip auth for health checks
		if r.URL.Path == "/health" || r.URL.Path == "/ready" || r.URL.Path == "/metrics" {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			http.Error(w, "Authorization header required", http.StatusUnauthorized)
			return
		}

		// Extract token from "Bearer <token>"
		if len(authHeader) < 7 || authHeader[:7] != "Bearer " {
			http.Error(w, "Invalid authorization header format", http.StatusUnauthorized)
			return
		}

		tokenString := authHeader[7:]
		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
			}
			return []byte(os.Getenv("JWT_SECRET")), nil
		})

		if err != nil || !token.Valid {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		// Extract claims and add to context
		if claims, ok := token.Claims.(jwt.MapClaims); ok {
			ctx := context.WithValue(r.Context(), "user_claims", claims)
			r = r.WithContext(ctx)
		}

		next.ServeHTTP(w, r)
	})
}

// NewStructuredLogger creates a structured logger middleware
func NewStructuredLogger(logger *zap.Logger) func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			defer func() {
				logger.Info("HTTP Request",
					zap.String("method", r.Method),
					zap.String("path", r.URL.Path),
					zap.String("query", r.URL.RawQuery),
					zap.String("ip", r.RemoteAddr),
					zap.String("user-agent", r.UserAgent()),
					zap.Int("status", ww.Status()),
					zap.Int("bytes", ww.BytesWritten()),
					zap.Duration("duration", time.Since(start)),
					zap.String("request-id", middleware.GetReqID(r.Context())),
				)
			}()

			next.ServeHTTP(ww, r)
		})
	}
}
