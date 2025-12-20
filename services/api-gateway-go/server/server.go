// Package server Issue: #146073424
package server

import (
	"context"
	"fmt"
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
	"go.uber.org/zap"
)

// APIGateway представляет API Gateway сервер
type APIGateway struct {
	server *http.Server
	logger *zap.Logger
	config *Config

	// Rate limiter
	rateLimiter *RateLimiter

	// Circuit breakers для каждого сервиса
	circuitBreakers map[string]*CircuitBreaker

	// Service URLs
	serviceURLs map[string]*url.URL
}

// Config содержит конфигурацию API Gateway
type Config struct {
	ServerPort             int
	AuthServiceURL         string
	UserServiceURL         string
	CombatServiceURL       string
	NotificationServiceURL string
	RomanceServiceURL      string
	QuestServiceURL        string
	InventoryServiceURL    string
	EconomyServiceURL      string

	RateLimitRPM int
	BurstLimit   int

	CircuitBreakerTimeout     time.Duration
	CircuitBreakerMaxRequests int
	CircuitBreakerInterval    time.Duration

	ReadTimeout  time.Duration
	WriteTimeout time.Duration
	IdleTimeout  time.Duration

	JWTSecret string
}

// NewAPIGateway создает новый API Gateway
func NewAPIGateway(logger *zap.Logger, config *Config) *APIGateway {
	gateway := &APIGateway{
		logger:          logger,
		config:          config,
		rateLimiter:     NewRateLimiter(config.RateLimitRPM, config.BurstLimit),
		circuitBreakers: make(map[string]*CircuitBreaker),
		serviceURLs:     make(map[string]*url.URL),
	}

	// Инициализируем URLs сервисов
	gateway.initServiceURLs()

	// Инициализируем circuit breakers для каждого сервиса
	gateway.initCircuitBreakers()

	// Создаем HTTP сервер
	gateway.server = gateway.createHTTPServer()

	return gateway
}

// initServiceURLs инициализирует URLs для всех сервисов
func (g *APIGateway) initServiceURLs() {
	services := map[string]string{
		"auth":         g.config.AuthServiceURL,
		"user":         g.config.UserServiceURL,
		"combat":       g.config.CombatServiceURL,
		"notification": g.config.NotificationServiceURL,
		"romance":      g.config.RomanceServiceURL,
		"quest":        g.config.QuestServiceURL,
		"inventory":    g.config.InventoryServiceURL,
		"economy":      g.config.EconomyServiceURL,
	}

	for name, urlStr := range services {
		if parsedURL, err := url.Parse(urlStr); err == nil {
			g.serviceURLs[name] = parsedURL
		} else {
			g.logger.Error("Failed to parse service URL", zap.String("service", name), zap.String("url", urlStr), zap.Error(err))
		}
	}
}

// initCircuitBreakers инициализирует circuit breakers для всех сервисов
func (g *APIGateway) initCircuitBreakers() {
	services := []string{"auth", "user", "combat", "notification", "romance", "quest", "inventory", "economy"}

	for _, service := range services {
		g.circuitBreakers[service] = NewCircuitBreaker(
			g.config.CircuitBreakerTimeout,
			g.config.CircuitBreakerMaxRequests,
			g.config.CircuitBreakerInterval,
		)
	}
}

// createHTTPServer создает HTTP сервер с middleware
func (g *APIGateway) createHTTPServer() *http.Server {
	r := chi.NewRouter()

	// Performance middleware для MMOFPS оптимизаций
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)

	// Request size limit для защиты от DoS
	r.Use(middleware.RequestSize(1024 * 1024)) // 1MB limit

	// Structured logging middleware
	r.Use(g.loggingMiddleware)

	// Security headers
	r.Use(g.securityHeadersMiddleware)

	// CORS middleware
	r.Use(g.corsMiddleware)

	// Health check endpoints (не требуют аутентификации)
	r.Get("/health", g.healthCheckHandler)
	r.Get("/ready", g.readinessCheckHandler)
	r.Get("/metrics", g.metricsHandler)

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		// Rate limiting для всех API endpoints
		r.Use(g.rateLimitMiddleware)

		// Authentication middleware для защищенных endpoints
		r.Use(g.authMiddleware)

		// Service routes
		g.setupServiceRoutes(r)
	})

	server := &http.Server{
		Addr:         fmt.Sprintf(":%d", g.config.ServerPort),
		Handler:      r,
		ReadTimeout:  g.config.ReadTimeout,
		WriteTimeout: g.config.WriteTimeout,
		IdleTimeout:  g.config.IdleTimeout,
	}

	return server
}

// setupServiceRoutes настраивает маршрутизацию к сервисам
func (g *APIGateway) setupServiceRoutes(r chi.Router) {
	// Auth service
	r.Route("/auth", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("auth"))
	})

	// User service
	r.Route("/users", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("user"))
	})

	// Combat service
	r.Route("/combat", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("combat"))
	})

	// Notification service
	r.Route("/notifications", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("notification"))
	})

	// Romance service
	r.Route("/romance", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("romance"))
	})

	// Quest service
	r.Route("/quests", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("quest"))
	})

	// Inventory service
	r.Route("/inventory", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("inventory"))
	})

	// Economy service
	r.Route("/economy", func(r chi.Router) {
		r.Handle("/*", g.createReverseProxy("economy"))
	})
}

// createReverseProxy создает reverse proxy для сервиса
func (g *APIGateway) createReverseProxy(serviceName string) http.Handler {
	serviceURL, exists := g.serviceURLs[serviceName]
	if !exists {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			http.Error(w, "Service not found", http.StatusServiceUnavailable)
		})
	}

	proxy := httputil.NewSingleHostReverseProxy(serviceURL)

	// Настраиваем director для модификации запроса
	originalDirector := proxy.Director
	proxy.Director = func(req *http.Request) {
		originalDirector(req)

		// Удаляем префикс /api/v1 из пути
		if strings.HasPrefix(req.URL.Path, "/api/v1/") {
			req.URL.Path = strings.TrimPrefix(req.URL.Path, "/api/v1")
		}

		// Добавляем информацию о gateway в headers
		req.Header.Set("X-Gateway", "api-gateway")
		req.Header.Set("X-Client-IP", req.RemoteAddr)

		g.logger.Debug("Proxying request",
			zap.String("service", serviceName),
			zap.String("method", req.Method),
			zap.String("path", req.URL.Path))
	}

	// Circuit breaker middleware
	return g.circuitBreakerMiddleware(serviceName, proxy)
}

// circuitBreakerMiddleware оборачивает запрос circuit breaker
func (g *APIGateway) circuitBreakerMiddleware(serviceName string, next http.Handler) http.Handler {
	cb := g.circuitBreakers[serviceName]

	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Проверяем состояние circuit breaker
		if !cb.CanExecute() {
			g.logger.Warn("Circuit breaker open", zap.String("service", serviceName))
			http.Error(w, "Service temporarily unavailable", http.StatusServiceUnavailable)
			return
		}

		// Выполняем запрос через circuit breaker
		start := time.Now()
		next.ServeHTTP(w, r)
		duration := time.Since(start)

		// Обновляем метрики circuit breaker
		cb.RecordResult(duration < g.config.CircuitBreakerTimeout)
	})
}

// Start запускает API Gateway
func (g *APIGateway) Start() error {
	g.logger.Info("Starting API Gateway", zap.Int("port", g.config.ServerPort))

	// Канал для graceful shutdown
	shutdown := make(chan os.Signal, 1)
	signal.Notify(shutdown, os.Interrupt, syscall.SIGTERM)

	// Запускаем сервер в горутине
	go func() {
		g.logger.Info("API Gateway started", zap.String("addr", g.server.Addr))
		if err := g.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			g.logger.Fatal("Failed to start gateway", zap.Error(err))
		}
	}()

	// Ждем сигнала завершения
	<-shutdown
	g.logger.Info("Shutting down API Gateway...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := g.server.Shutdown(ctx); err != nil {
		g.logger.Error("Gateway forced to shutdown", zap.Error(err))
		return err
	}

	g.logger.Info("API Gateway shutdown complete")
	return nil
}
