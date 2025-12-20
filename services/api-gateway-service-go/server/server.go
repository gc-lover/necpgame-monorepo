// Issue: #146073424
package server

import (
	"context"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/http/httputil"
	"net/url"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"go.uber.org/zap"

	"necpgame/services/api-gateway-service-go/config"
)

// APIGatewayServer представляет API Gateway сервер
type APIGatewayServer struct {
	server *http.Server
	logger *zap.Logger
	config *config.Config
	proxy  *httputil.ReverseProxy
}

// NewAPIGatewayServer создает новый API Gateway сервер
func NewAPIGatewayServer(logger *zap.Logger, config *config.Config) *APIGatewayServer {
	// Создаем reverse proxy
	proxy := &httputil.ReverseProxy{
		Director: func(req *http.Request) {},
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: false},
		},
		ErrorHandler: func(w http.ResponseWriter, r *http.Request, err error) {
			logger.Error("Proxy error", zap.Error(err), zap.String("url", r.URL.String()))
			http.Error(w, "Service unavailable", http.StatusBadGateway)
		},
	}

	// Создаем Chi роутер с оптимизациями для MMOFPS
	r := chi.NewRouter()

	// Performance middleware
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)

	// Request size limit для защиты от DoS
	r.Use(middleware.RequestSize(1024 * 1024)) // 1MB limit

	// Rate limiting middleware
	rateLimiter := NewRateLimiter(config.RateLimitRPM)
	r.Use(rateLimiter.Middleware)

	// Logging middleware с structured logging
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Создаем response writer с захватом статуса
			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			next.ServeHTTP(ww, r)

			logger.Info("API Gateway Request",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Int("status", ww.Status()),
				zap.Duration("duration", time.Since(start)),
				zap.String("remote_addr", r.RemoteAddr),
			)
		})
	})

	// Health check endpoints
	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status": "healthy", "service": "api-gateway"}`))
	})

	r.Get("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status": "ready", "service": "api-gateway"}`))
	})

	// Service routing
	gateway := &APIGatewayServer{
		logger: logger,
		config: config,
		proxy:  proxy,
	}

	// Route requests to appropriate services
	r.HandleFunc("/api/v1/*", gateway.handleRequest)

	server := &http.Server{
		Addr:         fmt.Sprintf(":%d", config.ServerPort),
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &APIGatewayServer{
		server: server,
		logger: logger,
		config: config,
		proxy:  proxy,
	}
}

// handleRequest обрабатывает входящие запросы и перенаправляет их к соответствующим сервисам
func (g *APIGatewayServer) handleRequest(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1")

	// Определяем целевой сервис на основе пути
	targetService := g.determineTargetService(path)
	if targetService == "" {
		g.logger.Warn("Unknown service for path", zap.String("path", path))
		http.Error(w, "Service not found", http.StatusNotFound)
		return
	}

	// Получаем конфигурацию сервиса
	serviceConfig, exists := g.config.Services[targetService]
	if !exists {
		g.logger.Error("Service configuration not found", zap.String("service", targetService))
		http.Error(w, "Service configuration error", http.StatusInternalServerError)
		return
	}

	// Создаем новый URL для целевого сервиса
	targetURL, err := url.Parse(serviceConfig.URL + path)
	if err != nil {
		g.logger.Error("Failed to parse target URL",
			zap.String("service", targetService),
			zap.String("url", serviceConfig.URL),
			zap.Error(err))
		http.Error(w, "Invalid service URL", http.StatusInternalServerError)
		return
	}

	// Копируем query параметры
	targetURL.RawQuery = r.URL.RawQuery

	// Circuit breaker call
	err = serviceConfig.CircuitBreaker.Call(func() error {
		return g.proxyToService(w, r, targetURL, serviceConfig, targetService)
	})

	if err != nil {
		g.logger.Error("Service call failed",
			zap.String("service", targetService),
			zap.String("path", path),
			zap.Error(err))
		http.Error(w, "Service temporarily unavailable", http.StatusServiceUnavailable)
	}
}

// determineTargetService определяет целевой сервис на основе пути запроса
func (g *APIGatewayServer) determineTargetService(path string) string {
	switch {
	case strings.HasPrefix(path, "/auth"):
		return "auth"
	case strings.HasPrefix(path, "/notifications"):
		return "notification"
	case strings.HasPrefix(path, "/combat"):
		return "combat"
	case strings.HasPrefix(path, "/romance"):
		return "romance"
	case strings.HasPrefix(path, "/social"):
		return "social"
	default:
		return ""
	}
}

// proxyToService выполняет проксирование запроса к целевому сервису
func (g *APIGatewayServer) proxyToService(w http.ResponseWriter, r *http.Request, targetURL *url.URL, serviceConfig *config.ServiceConfig, serviceName string) error {
	// Создаем контекст с таймаутом
	ctx, cancel := context.WithTimeout(r.Context(), serviceConfig.Timeout)
	defer cancel()

	// Клонируем запрос
	req := r.Clone(ctx)
	req.URL = targetURL
	req.Host = targetURL.Host
	req.RequestURI = ""

	// Добавляем заголовки для трейсинга
	req.Header.Set("X-Forwarded-Host", r.Host)
	req.Header.Set("X-Forwarded-For", r.RemoteAddr)
	req.Header.Set("X-API-Gateway", "true")
	req.Header.Set("X-Service-Name", serviceName)

	// Логируем запрос
	g.logger.Info("Proxying request",
		zap.String("service", serviceName),
		zap.String("method", req.Method),
		zap.String("path", req.URL.Path),
		zap.String("target_url", targetURL.String()))

	// Выполняем запрос с retry логикой
	var lastErr error
	for attempt := 0; attempt <= serviceConfig.MaxRetries; attempt++ {
		if attempt > 0 {
			g.logger.Info("Retrying request",
				zap.String("service", serviceName),
				zap.Int("attempt", attempt))
			time.Sleep(time.Duration(attempt) * 100 * time.Millisecond)
		}

		// Создаем HTTP клиент с таймаутом
		client := &http.Client{
			Timeout: serviceConfig.Timeout,
			Transport: &http.Transport{
				TLSClientConfig: &tls.Config{InsecureSkipVerify: false},
			},
		}

		resp, err := client.Do(req)
		if err != nil {
			lastErr = err
			continue
		}
		defer resp.Body.Close()

		// Копируем заголовки ответа
		for key, values := range resp.Header {
			for _, value := range values {
				w.Header().Add(key, value)
			}
		}

		// Устанавливаем статус код
		w.WriteHeader(resp.StatusCode)

		// Копируем тело ответа
		_, err = io.Copy(w, resp.Body)
		if err != nil {
			g.logger.Error("Failed to copy response body",
				zap.String("service", serviceName),
				zap.Error(err))
			return err
		}

		return nil
	}

	return fmt.Errorf("all retry attempts failed: %w", lastErr)
}

// Start запускает API Gateway сервер
func (g *APIGatewayServer) Start() error {
	g.logger.Info("Starting API Gateway server", zap.String("addr", g.server.Addr))

	// Запускаем сервер
	if err := g.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		g.logger.Fatal("Failed to start server", zap.Error(err))
	}

	return nil
}

// HealthCheckHandler проверяет здоровье API Gateway
func (g *APIGatewayServer) HealthCheckHandler(w http.ResponseWriter, r *http.Request) {
	healthStatus := map[string]interface{}{
		"status":    "healthy",
		"service":   "api-gateway",
		"timestamp": time.Now().UTC(),
		"services":  make(map[string]interface{}),
	}

	// Проверяем здоровье всех сервисов
	for serviceName, serviceConfig := range g.config.Services {
		serviceHealth := g.checkServiceHealth(serviceName, serviceConfig)
		healthStatus["services"].(map[string]interface{})[serviceName] = serviceHealth
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(healthStatus)
}

// checkServiceHealth проверяет здоровье конкретного сервиса
func (g *APIGatewayServer) checkServiceHealth(serviceName string, serviceConfig *config.ServiceConfig) map[string]interface{} {
	healthURL := serviceConfig.URL + serviceConfig.HealthCheck

	client := &http.Client{Timeout: 5 * time.Second}
	resp, err := client.Get(healthURL)

	health := map[string]interface{}{
		"url":           healthURL,
		"healthy":       false,
		"response_time": "N/A",
	}

	if err != nil {
		health["error"] = err.Error()
		return health
	}
	defer resp.Body.Close()

	start := time.Now()
	health["healthy"] = resp.StatusCode == http.StatusOK
	health["status_code"] = resp.StatusCode
	health["response_time"] = time.Since(start).String()

	return health
}
