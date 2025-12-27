// Issue: #implement-analysis-domain-service
// HTTP Server implementation for Analysis Domain Service

package server

import (
	"context"
	"crypto/tls"
	"fmt"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"go.uber.org/zap"

	"analysis-domain-service-go/pkg/service"
)

// HTTPServer wraps Chi router with enterprise-grade middleware
type HTTPServer struct {
	router  *chi.Mux
	service service.ServiceInterface
	logger  *zap.Logger
	server  *http.Server
}

// NewHTTPServer creates a new HTTP server instance
func NewHTTPServer(svc service.ServiceInterface, logger *zap.Logger) *HTTPServer {
	s := &HTTPServer{
		router:  chi.NewRouter(),
		service: svc,
		logger:  logger,
	}

	s.setupMiddleware()
	s.setupRoutes()

	return s
}

// setupMiddleware configures enterprise-grade middleware stack
func (s *HTTPServer) setupMiddleware() {
	s.router.Use(middleware.RequestID)
	s.router.Use(middleware.RealIP)

	// Structured logging middleware
	s.router.Use(s.loggingMiddleware())

	s.router.Use(middleware.Recoverer)
	s.router.Use(middleware.Timeout(30 * time.Second))

	// CORS configuration
	s.router.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// Security headers
	s.router.Use(s.securityHeadersMiddleware())

	// Rate limiting (simplified)
	s.router.Use(s.rateLimitMiddleware())
}

// loggingMiddleware provides structured request logging
func (s *HTTPServer) loggingMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			path := r.URL.Path
			method := r.Method

			// Create response writer wrapper for status capture
			wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

			defer func() {
				duration := time.Since(start)
				s.logger.Info("HTTP Request",
					zap.String("method", method),
					zap.String("path", path),
					zap.String("remote_addr", r.RemoteAddr),
					zap.Int("status", wrapped.statusCode),
					zap.Duration("duration", duration),
					zap.String("request_id", middleware.GetReqID(r.Context())),
				)
			}()

			next.ServeHTTP(wrapped, r)
		})
	}
}

// securityHeadersMiddleware adds security headers
func (s *HTTPServer) securityHeadersMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			w.Header().Set("X-Content-Type-Options", "nosniff")
			w.Header().Set("X-Frame-Options", "DENY")
			w.Header().Set("X-XSS-Protection", "1; mode=block")
			w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
			w.Header().Set("Content-Security-Policy", "default-src 'self'")
			next.ServeHTTP(w, r)
		})
	}
}

// rateLimitMiddleware provides basic rate limiting
func (s *HTTPServer) rateLimitMiddleware() func(http.Handler) http.Handler {
	// Simplified rate limiting - in production use redis-based rate limiter
	requests := make(map[string][]time.Time)
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			clientIP := r.RemoteAddr
			now := time.Now()

			// Clean old requests (keep last minute)
			var recent []time.Time
			for _, t := range requests[clientIP] {
				if now.Sub(t) < time.Minute {
					recent = append(recent, t)
				}
			}
			requests[clientIP] = recent

			// Check rate limit (100 requests per minute)
			if len(recent) >= 100 {
				s.logger.Warn("Rate limit exceeded",
					zap.String("client_ip", clientIP))
				http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
				return
			}

			// Add current request
			requests[clientIP] = append(requests[clientIP], now)

			next.ServeHTTP(w, r)
		})
	}
}

// setupRoutes configures API routes
func (s *HTTPServer) setupRoutes() {
	// Health check endpoints
	s.router.Get("/health", s.healthCheckHandler())
	s.router.Get("/ready", s.readinessCheckHandler())

	// Metrics endpoint (for Prometheus)
	s.router.Handle("/metrics", s.metricsHandler())

	// API routes will be added here when integrating with OpenAPI handlers
}

// healthCheckHandler provides basic health check
func (s *HTTPServer) healthCheckHandler() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
		defer cancel()

		if err := s.service.HealthCheck(ctx); err != nil {
			s.logger.Error("Health check failed", zap.Error(err))
			w.WriteHeader(http.StatusServiceUnavailable)
			fmt.Fprintf(w, `{"status":"unhealthy","error":"%s","timestamp":"%s"}`,
				err.Error(), time.Now().Format(time.RFC3339))
			return
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		fmt.Fprintf(w, `{"status":"healthy","service":"analysis-domain","timestamp":"%s"}`,
			time.Now().Format(time.RFC3339))
	}
}

// readinessCheckHandler provides readiness check
func (s *HTTPServer) readinessCheckHandler() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Check if service is ready to accept traffic
		ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
		defer cancel()

		if err := s.service.HealthCheck(ctx); err != nil {
			w.WriteHeader(http.StatusServiceUnavailable)
			fmt.Fprintf(w, `{"status":"not ready","error":"%s"}`, err.Error())
			return
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		fmt.Fprintf(w, `{"status":"ready","service":"analysis-domain"}`)
	}
}

// metricsHandler provides service metrics
func (s *HTTPServer) metricsHandler() http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "text/plain; charset=utf-8")
		w.WriteHeader(http.StatusOK)

		// Basic metrics output (in production integrate with Prometheus)
		fmt.Fprintf(w, "# Analysis Domain Service Metrics\n")
		fmt.Fprintf(w, "# Timestamp: %s\n", time.Now().Format(time.RFC3339))
		fmt.Fprintf(w, "analysis_service_up 1\n")
		fmt.Fprintf(w, "analysis_service_start_time %d\n", time.Now().Unix())
	})
}

// Start begins serving HTTP requests
func (s *HTTPServer) Start(addr string) error {
	s.server = &http.Server{
		Addr:         addr,
		Handler:      s.router,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
		TLSConfig:    s.getTLSConfig(),
	}

	s.logger.Info("Starting HTTP server",
		zap.String("addr", addr),
		zap.Bool("tls_enabled", s.server.TLSConfig != nil))

	if s.server.TLSConfig != nil {
		return s.server.ListenAndServeTLS("", "")
	}

	return s.server.ListenAndServe()
}

// Stop gracefully shuts down the HTTP server
func (s *HTTPServer) Stop(ctx context.Context) error {
	s.logger.Info("Stopping HTTP server")

	shutdownCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(shutdownCtx); err != nil {
		s.logger.Error("HTTP server shutdown failed", zap.Error(err))
		return err
	}

	s.logger.Info("HTTP server stopped gracefully")
	return nil
}

// getTLSConfig returns TLS configuration for HTTPS
func (s *HTTPServer) getTLSConfig() *tls.Config {
	// In production, load certificates from secure storage
	// For now, return nil to use HTTP
	return nil
}

// GetRouter returns the Chi router for adding additional routes
func (s *HTTPServer) GetRouter() *chi.Mux {
	return s.router
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
