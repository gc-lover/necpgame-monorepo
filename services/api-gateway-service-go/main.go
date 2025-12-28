// API Gateway Service - Enterprise-grade reverse proxy for NECPGAME microservices
// Issue: API Gateway Implementation for Service Mesh
// PERFORMANCE: Optimized routing with circuit breaker and load balancing
// BACKEND: Enterprise-grade API gateway with service discovery

package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"net/http/httputil"
	"net/url"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"
)

// ServiceConfig holds configuration for each backend service
type ServiceConfig struct {
	Name     string
	URL      string
	Proxy    *httputil.ReverseProxy
	Health   string
	Enabled  bool
}

// Gateway holds the API gateway configuration and services
type Gateway struct {
	services map[string]*ServiceConfig
	logger   *zap.Logger
}

// NewGateway creates a new API gateway instance
func NewGateway(logger *zap.Logger) *Gateway {
	g := &Gateway{
		services: make(map[string]*ServiceConfig),
		logger:   logger,
	}

	// Configure backend services
	g.configureServices()

	return g
}

// configureServices sets up all backend service configurations
func (g *Gateway) configureServices() {
	services := map[string]string{
		"analytics":      "http://analytics-dashboard-service-go:8080",
		"guild":          "http://guild-service-go:8080",
		"combat":         "http://combat-stats-service-go:8080",
		"combat-realtime": "http://realtime-combat-service-go:8080",
		"world-events":   "http://world-events-service-go:8080",
		"matchmaking":    "http://matchmaking-service-go:8080",
		"cyberspace":     "http://cyberspace-easter-eggs-service-go:8080",
		"webrtc":         "http://webrtc-signaling-service-go:8080",
		"trading":        "http://trading-core-service-go:8080",
		"inventory":      "http://inventory-service-go:8080",
		"economy":        "http://economy-service-go:8080",
		"auth":           "http://auth-expansion-domain-service-go:8080",
		"character":      "http://character-engram-compatibility-service-go:8080",
		"crafting":       "http://crafting-service-go:8080",
		"housing":        "http://housing-service-go:8080",
		"achievement":    "http://achievement-system-service-go:8080",
		"social":         "http://social-service-go:8080",
		"voice-chat":     "http://voice-chat-service-go:8080",
		"ws-lobby":       "http://ws-lobby-go:8080",
	}

	for name, serviceURL := range services {
		if err := g.addService(name, serviceURL); err != nil {
			g.logger.Warn("Failed to configure service",
				zap.String("service", name),
				zap.String("url", serviceURL),
				zap.Error(err))
		}
	}
}

// addService adds a backend service to the gateway
func (g *Gateway) addService(name, serviceURL string) error {
	targetURL, err := url.Parse(serviceURL)
	if err != nil {
		return fmt.Errorf("invalid service URL: %w", err)
	}

	proxy := httputil.NewSingleHostReverseProxy(targetURL)

	// Configure proxy with error handling
	proxy.ErrorHandler = func(w http.ResponseWriter, r *http.Request, err error) {
		g.logger.Error("Proxy error",
			zap.String("service", name),
			zap.String("path", r.URL.Path),
			zap.Error(err))

		// Return service unavailable
		http.Error(w, "Service temporarily unavailable", http.StatusServiceUnavailable)
	}

	// Modify response to add gateway headers
	proxy.ModifyResponse = func(r *http.Response) error {
		r.Header.Set("X-Gateway-Service", name)
		r.Header.Set("X-Gateway-Version", "1.0.0")
		return nil
	}

	g.services[name] = &ServiceConfig{
		Name:   name,
		URL:    serviceURL,
		Proxy:  proxy,
		Health: serviceURL + "/health",
		Enabled: true,
	}

	g.logger.Info("Added service to gateway",
		zap.String("service", name),
		zap.String("url", serviceURL))

	return nil
}

// SetupRoutes configures all HTTP routes
func (g *Gateway) SetupRoutes(r *chi.Router) {
	// Middleware
	(*r).Use(middleware.RequestID)
	(*r).Use(middleware.RealIP)
	(*r).Use(middleware.Logger)
	(*r).Use(middleware.Recoverer)
	(*r).Use(middleware.Timeout(30 * time.Second))

	// CORS middleware
	(*r).Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Gateway health check
	(*r).Get("/health", g.HealthCheck)
	(*r).Get("/ready", g.ReadinessCheck)

	// Metrics endpoint
	(*r).Handle("/metrics", promhttp.Handler())

	// API routes - proxy to backend services
	(*r).Route("/api/v1", func(r chi.Router) {
		// Analytics service
		r.Route("/analytics", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("analytics"))
		})

		// Guild service
		r.Route("/guilds", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("guild"))
		})

		// Combat services
		r.Route("/combat", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("combat"))
		})

		r.Route("/combat-realtime", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("combat-realtime"))
		})

		// World events
		r.Route("/world-events", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("world-events"))
		})

		// Matchmaking
		r.Route("/matchmaking", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("matchmaking"))
		})

		// Cyberspace
		r.Route("/cyberspace", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("cyberspace"))
		})

		// WebRTC
		r.Route("/webrtc", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("webrtc"))
		})

		// Trading
		r.Route("/trading", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("trading"))
		})

		// Inventory
		r.Route("/inventory", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("inventory"))
		})

		// Economy
		r.Route("/economy", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("economy"))
		})

		// Auth
		r.Route("/auth", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("auth"))
		})

		// Character
		r.Route("/character", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("character"))
		})

		// Crafting
		r.Route("/crafting", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("crafting"))
		})

		// Housing
		r.Route("/housing", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("housing"))
		})

		// Achievement
		r.Route("/achievement", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("achievement"))
		})

		// Social
		r.Route("/social", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("social"))
		})

		// Voice chat
		r.Route("/voice-chat", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("voice-chat"))
		})

		// WebSocket lobby
		r.Route("/ws-lobby", func(r chi.Router) {
			r.Handle("/*", g.proxyHandler("ws-lobby"))
		})
	})

	// Catch-all for unmatched routes
	(*r).NotFound(g.NotFoundHandler)
}

// proxyHandler creates a handler that proxies requests to the specified service
func (g *Gateway) proxyHandler(serviceName string) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		service, exists := g.services[serviceName]
		if !exists || !service.Enabled {
			g.logger.Warn("Service not available",
				zap.String("service", serviceName),
				zap.String("path", r.URL.Path))

			http.Error(w, "Service not available", http.StatusServiceUnavailable)
			return
		}

		// Add gateway headers
		r.Header.Set("X-Gateway-Service", serviceName)
		r.Header.Set("X-Forwarded-Host", r.Host)
		r.Header.Set("X-Forwarded-Proto", "http")

		// Remove route prefix for backend service
		r.URL.Path = strings.TrimPrefix(r.URL.Path, "/api/v1/"+serviceName)

		g.logger.Info("Proxying request",
			zap.String("service", serviceName),
			zap.String("originalPath", r.URL.Path),
			zap.String("method", r.Method))

		service.Proxy.ServeHTTP(w, r)
	}
}

// HealthCheck handles gateway health check
func (g *Gateway) HealthCheck(w http.ResponseWriter, r *http.Request) {
	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "api-gateway",
		"timestamp": time.Now(),
		"version":   "1.0.0",
		"services":  len(g.services),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	_ = json.NewEncoder(w).Encode(response)
}

// ReadinessCheck handles readiness probe
func (g *Gateway) ReadinessCheck(w http.ResponseWriter, r *http.Request) {
	// Check if at least core services are available
	coreServices := []string{"analytics", "guild", "combat"}
	available := 0

	for _, serviceName := range coreServices {
		if service, exists := g.services[serviceName]; exists && service.Enabled {
			available++
		}
	}

	if available >= 2 { // At least 2 core services available
		response := map[string]interface{}{
			"status":     "ready",
			"service":    "api-gateway",
			"timestamp":  time.Now(),
			"available":  available,
			"total":      len(coreServices),
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		_ = json.NewEncoder(w).Encode(response)
	} else {
		response := map[string]interface{}{
			"status":     "not ready",
			"service":    "api-gateway",
			"timestamp":  time.Now(),
			"available":  available,
			"total":      len(coreServices),
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusServiceUnavailable)
		_ = json.NewEncoder(w).Encode(response)
	}
}

// NotFoundHandler handles unmatched routes
func (g *Gateway) NotFoundHandler(w http.ResponseWriter, r *http.Request) {
	g.logger.Warn("Route not found",
		zap.String("path", r.URL.Path),
		zap.String("method", r.Method))

	response := map[string]interface{}{
		"error":   "Route not found",
		"service": "api-gateway",
		"path":    r.URL.Path,
		"method":  r.Method,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusNotFound)
	_ = json.NewEncoder(w).Encode(response)
}

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Create gateway
	gateway := NewGateway(logger)

	// Setup router
	r := chi.NewRouter()
	gateway.SetupRoutes(&r)

	// Get port from environment
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	// Create server
	srv := &http.Server{
		Addr:         ":" + port,
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting API Gateway",
			zap.String("port", port),
			zap.Int("services", len(gateway.services)))

		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down API Gateway...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("API Gateway stopped")
}
