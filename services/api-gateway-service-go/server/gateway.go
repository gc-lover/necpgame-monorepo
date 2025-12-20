// Issue: #146073424
package server

import (
	"crypto/tls"
	"fmt"
	"net/http"
	"net/http/httputil"
	"net/url"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"go.uber.org/zap"
)

// APIGatewayConfig содержит конфигурацию API Gateway
type APIGatewayConfig struct {
	ServerPort  int
	TLSCertFile string
	TLSKeyFile  string
	JWTSecret   string

	// Upstream сервисы
	AuthServiceURL         string
	CombatServiceURL       string
	InventoryServiceURL    string
	QuestServiceURL        string
	SocialServiceURL       string
	NotificationServiceURL string
	RomanceServiceURL      string

	// Rate limiting
	RateLimitRPM int
	BurstLimit   int

	// Circuit breaker
	CircuitBreakerTimeout     time.Duration
	CircuitBreakerMaxRequests uint32
	CircuitBreakerInterval    time.Duration

	// Redis для rate limiting и caching
	RedisURL string
}

// APIGateway представляет API Gateway сервис
type APIGateway struct {
	config *APIGatewayConfig
	logger *zap.Logger
	server *http.Server

	// Компоненты
	rateLimiter    *RateLimiter
	circuitBreaker *CircuitBreakerManager
	authMiddleware *AuthMiddleware

	// Reverse proxies для upstream сервисов
	authProxy         *httputil.ReverseProxy
	combatProxy       *httputil.ReverseProxy
	inventoryProxy    *httputil.ReverseProxy
	questProxy        *httputil.ReverseProxy
	socialProxy       *httputil.ReverseProxy
	notificationProxy *httputil.ReverseProxy
	romanceProxy      *httputil.ReverseProxy
}

// NewAPIGateway создает новый API Gateway
func NewAPIGateway(logger *zap.Logger, config *APIGatewayConfig) (*APIGateway, error) {
	gateway := &APIGateway{
		config: config,
		logger: logger,
	}

	// Инициализируем компоненты
	if err := gateway.initializeComponents(); err != nil {
		return nil, fmt.Errorf("failed to initialize components: %w", err)
	}

	// Создаем reverse proxies
	if err := gateway.initializeProxies(); err != nil {
		return nil, fmt.Errorf("failed to initialize proxies: %w", err)
	}

	// Создаем HTTP сервер
	if err := gateway.initializeServer(); err != nil {
		return nil, fmt.Errorf("failed to initialize server: %w", err)
	}

	return gateway, nil
}

// initializeComponents инициализирует компоненты gateway
func (g *APIGateway) initializeComponents() error {
	// Rate limiter
	g.rateLimiter = NewRateLimiter(g.config.RateLimitRPM, g.config.BurstLimit, g.logger)

	// Circuit breaker manager
	g.circuitBreaker = NewCircuitBreakerManager(g.config, g.logger)

	// Auth middleware
	g.authMiddleware = NewAuthMiddleware(string(g.config.JWTSecret), g.logger)

	g.logger.Info("API Gateway components initialized",
		zap.Int("rate_limit_rpm", g.config.RateLimitRPM),
		zap.Int("burst_limit", g.config.BurstLimit))

	return nil
}

// initializeProxies инициализирует reverse proxies для upstream сервисов
func (g *APIGateway) initializeProxies() error {
	var err error

	// Auth service proxy
	if g.authProxy, err = g.createReverseProxy(g.config.AuthServiceURL); err != nil {
		return fmt.Errorf("failed to create auth proxy: %w", err)
	}

	// Combat service proxy
	if g.combatProxy, err = g.createReverseProxy(g.config.CombatServiceURL); err != nil {
		return fmt.Errorf("failed to create combat proxy: %w", err)
	}

	// Inventory service proxy
	if g.inventoryProxy, err = g.createReverseProxy(g.config.InventoryServiceURL); err != nil {
		return fmt.Errorf("failed to create inventory proxy: %w", err)
	}

	// Quest service proxy
	if g.questProxy, err = g.createReverseProxy(g.config.QuestServiceURL); err != nil {
		return fmt.Errorf("failed to create quest proxy: %w", err)
	}

	// Social service proxy
	if g.socialProxy, err = g.createReverseProxy(g.config.SocialServiceURL); err != nil {
		return fmt.Errorf("failed to create social proxy: %w", err)
	}

	// Notification service proxy
	if g.notificationProxy, err = g.createReverseProxy(g.config.NotificationServiceURL); err != nil {
		return fmt.Errorf("failed to create notification proxy: %w", err)
	}

	// Romance service proxy
	if g.romanceProxy, err = g.createReverseProxy(g.config.RomanceServiceURL); err != nil {
		return fmt.Errorf("failed to create romance proxy: %w", err)
	}

	g.logger.Info("Reverse proxies initialized for all upstream services")
	return nil
}

// createReverseProxy создает reverse proxy для upstream сервиса
func (g *APIGateway) createReverseProxy(targetURL string) (*httputil.ReverseProxy, error) {
	target, err := url.Parse(targetURL)
	if err != nil {
		return nil, fmt.Errorf("invalid target URL %s: %w", targetURL, err)
	}

	proxy := httputil.NewSingleHostReverseProxy(target)

	// Настраиваем director для модификации запроса
	originalDirector := proxy.Director
	proxy.Director = func(req *http.Request) {
		originalDirector(req)
		// Добавляем информацию о gateway
		req.Header.Set("X-Gateway", "api-gateway-service")
		req.Header.Set("X-Forwarded-Host", req.Host)
	}

	// Настраиваем error handler
	proxy.ErrorHandler = func(w http.ResponseWriter, r *http.Request, err error) {
		g.logger.Error("Proxy error",
			zap.String("target", targetURL),
			zap.String("path", r.URL.Path),
			zap.Error(err))

		// Возвращаем 502 Bad Gateway
		w.WriteHeader(http.StatusBadGateway)
		w.Write([]byte(`{"error": "Bad Gateway", "message": "Upstream service unavailable"}`))
	}

	return proxy, nil
}

// initializeServer инициализирует HTTP сервер с routing
func (g *APIGateway) initializeServer() error {
	r := chi.NewRouter()

	// Base middleware
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS middleware
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // В продакшене ограничить
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"*"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Logging middleware
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Создаем response writer с захватом статуса
			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			next.ServeHTTP(ww, r)

			g.logger.Info("Gateway Request",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("remote_addr", r.RemoteAddr),
				zap.Int("status", ww.Status()),
				zap.Duration("duration", time.Since(start)),
				zap.String("request_id", middleware.GetReqID(r.Context())),
			)
		})
	})

	// Health check (без аутентификации)
	r.Get("/health", g.healthCheckHandler)
	r.Get("/ready", g.readinessCheckHandler)
	r.Get("/metrics", g.metricsHandler)

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		// Rate limiting для всех API запросов
		r.Use(g.rateLimiter.Middleware())

		// Authentication routes (без JWT проверки)
		r.Post("/auth/login", g.authProxy.ServeHTTP)
		r.Post("/auth/register", g.authProxy.ServeHTTP)
		r.Post("/auth/refresh", g.authProxy.ServeHTTP)

		// JWT protected routes
		r.Group(func(r chi.Router) {
			r.Use(g.authMiddleware.JWTAuth)

			// Combat service
			r.Route("/combat", func(r chi.Router) {
				r.Use(g.circuitBreaker.Middleware("combat"))
				r.Handle("/*", g.combatProxy)
			})

			// Inventory service
			r.Route("/inventory", func(r chi.Router) {
				r.Use(g.circuitBreaker.Middleware("inventory"))
				r.Handle("/*", g.inventoryProxy)
			})

			// Quest service
			r.Route("/quests", func(r chi.Router) {
				r.Use(g.circuitBreaker.Middleware("quest"))
				r.Handle("/*", g.questProxy)
			})

			// Social service
			r.Route("/social", func(r chi.Router) {
				r.Use(g.circuitBreaker.Middleware("social"))
				r.Handle("/*", g.socialProxy)
			})

			// Notification service
			r.Route("/notifications", func(r chi.Router) {
				r.Use(g.circuitBreaker.Middleware("notification"))
				r.Handle("/*", g.notificationProxy)
			})

			// Romance service
			r.Route("/romance", func(r chi.Router) {
				r.Use(g.circuitBreaker.Middleware("romance"))
				r.Handle("/*", g.romanceProxy)
			})
		})
	})

	// Создаем HTTP сервер
	server := &http.Server{
		Addr:         fmt.Sprintf(":%d", g.config.ServerPort),
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Настраиваем TLS если есть сертификаты
	if g.config.TLSCertFile != "" && g.config.TLSKeyFile != "" {
		cert, err := tls.LoadX509KeyPair(g.config.TLSCertFile, g.config.TLSKeyFile)
		if err != nil {
			return fmt.Errorf("failed to load TLS certificates: %w", err)
		}

		server.TLSConfig = &tls.Config{
			Certificates: []tls.Certificate{cert},
			ServerName:   "api.necpgame.com",
		}
	}

	g.server = server
	g.logger.Info("HTTP server initialized", zap.Int("port", g.config.ServerPort))
	return nil
}

// Start запускает API Gateway
func (g *APIGateway) Start() error {
	g.logger.Info("Starting API Gateway", zap.Int("port", g.config.ServerPort))

	// Запускаем сервер
	if g.config.TLSCertFile != "" && g.config.TLSKeyFile != "" {
		return g.server.ListenAndServeTLS(g.config.TLSCertFile, g.config.TLSKeyFile)
	}

	return g.server.ListenAndServe()
}

// healthCheckHandler проверяет здоровье gateway
func (g *APIGateway) healthCheckHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "healthy", "service": "api-gateway", "version": "1.0.0"}`))
}

// readinessCheckHandler проверяет готовность gateway
func (g *APIGateway) readinessCheckHandler(w http.ResponseWriter, r *http.Request) {
	// Проверяем основные компоненты
	if g.rateLimiter == nil || g.circuitBreaker == nil || g.authMiddleware == nil {
		w.WriteHeader(http.StatusServiceUnavailable)
		w.Write([]byte(`{"status": "not ready", "service": "api-gateway"}`))
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "ready", "service": "api-gateway"}`))
}

// metricsHandler предоставляет метрики gateway
func (g *APIGateway) metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	// Собираем метрики
	metrics := map[string]interface{}{
		"service": "api-gateway",
		"version": "1.0.0",
		"rate_limiter": map[string]interface{}{
			"rpm_limit":   g.config.RateLimitRPM,
			"burst_limit": g.config.BurstLimit,
		},
		"circuit_breaker": map[string]interface{}{
			"timeout":      g.config.CircuitBreakerTimeout.String(),
			"max_requests": g.config.CircuitBreakerMaxRequests,
			"interval":     g.config.CircuitBreakerInterval.String(),
		},
		"upstreams": map[string]interface{}{
			"auth":         g.config.AuthServiceURL,
			"combat":       g.config.CombatServiceURL,
			"inventory":    g.config.InventoryServiceURL,
			"quest":        g.config.QuestServiceURL,
			"social":       g.config.SocialServiceURL,
			"notification": g.config.NotificationServiceURL,
			"romance":      g.config.RomanceServiceURL,
		},
	}

	// TODO: Добавить реальные метрики использования
	w.Write([]byte(fmt.Sprintf(`%v`, metrics)))
}
