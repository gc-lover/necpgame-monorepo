// Package server Issue: #1
package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"go.uber.org/zap"
)

// HTTPServer представляет HTTP сервер с оптимизациями для MMOFPS
type HTTPServer struct {
	server     *http.Server
	logger     *zap.Logger
	service    *Service
	middleware *AuthMiddleware
	handler    *authHandler
}

// NewHTTPServer создает новый HTTP сервер с оптимизациями
func NewHTTPServer(logger *zap.Logger, db *sql.DB, jwtSecret string) *HTTPServer {
	service := NewService(logger, db, jwtSecret)
	authMiddleware := NewAuthMiddleware(logger, jwtSecret)
	handler := newAuthHandler(logger, db, jwtSecret)

	// Создаем Chi роутер с оптимизациями
	r := chi.NewRouter()

	// Performance middleware
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)

	// Security middleware
	r.Use(authMiddleware.SecurityHeadersMiddleware)
	r.Use(authMiddleware.CORSMiddleware)

	// Logging middleware
	r.Use(authMiddleware.LoggingMiddleware)

	// Recovery middleware
	r.Use(authMiddleware.RecoveryMiddleware)

	// Health check endpoint (не требует аутентификации)
	r.Get("/health", service.HealthCheckHandler)
	r.Get("/ready", service.ReadinessCheckHandler)
	r.Get("/metrics", service.MetricsHandler)

	// API endpoints
	r.Route("/api/v1", func(r chi.Router) {
		// Public endpoints (без аутентификации)
		r.Post("/auth/login", handler.LoginHandler)
		r.Post("/auth/refresh", handler.RefreshTokenHandler)

		// Protected endpoints (требуют Bearer token)
		r.Group(func(r chi.Router) {
			r.Use(authMiddleware.JWTAuth)
			r.Post("/auth/logout", handler.LogoutHandler)
		})
	})

	server := &http.Server{
		Addr:         ":8081",
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &HTTPServer{
		server:     server,
		logger:     logger,
		service:    service,
		middleware: authMiddleware,
		handler:    handler,
	}
}

// Start запускает HTTP сервер с graceful shutdown
func (s *HTTPServer) Start() error {
	s.logger.Info("Starting HTTP server", zap.String("addr", s.server.Addr))

	// Канал для graceful shutdown
	shutdown := make(chan os.Signal, 1)
	signal.Notify(shutdown, os.Interrupt, syscall.SIGTERM)

	// Запускаем сервер в горутине
	go func() {
		defer close(errChan)
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			s.logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Ждем сигнала завершения
	<-shutdown
	s.logger.Info("Shutting down server...")

	// Graceful shutdown с таймаутом
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(ctx); err != nil {
		s.logger.Error("Server forced to shutdown", zap.Error(err))
		return err
	}

	s.logger.Info("Server shutdown complete")
	return nil
}

// Stop останавливает сервер
func (s *HTTPServer) Stop(ctx context.Context) error {
	s.logger.Info("Stopping HTTP server")
	return s.server.Shutdown(ctx)
}

// HealthCheckHandler обрабатывает запросы на проверку здоровья
func (s *Service) HealthCheckHandler(w http.ResponseWriter, r *http.Request) {
	if err := s.HealthCheck(r.Context()); err != nil {
		s.logger.Error("Health check failed", zap.Error(err))
		http.Error(w, "Service unhealthy", http.StatusServiceUnavailable)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "healthy"}`))
}

// ReadinessCheckHandler проверяет готовность сервиса
func (s *Service) ReadinessCheckHandler(w http.ResponseWriter, _ *http.Request) {
	// Проверяем подключение к БД
	if err := s.db.Ping(); err != nil {
		s.logger.Error("Readiness check failed", zap.Error(err))
		http.Error(w, "Service not ready", http.StatusServiceUnavailable)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "ready"}`))
}

// MetricsHandler предоставляет метрики сервиса
func (s *Service) MetricsHandler(w http.ResponseWriter, _ *http.Request) {
	// Базовые метрики (в production использовать Prometheus)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	metrics := fmt.Sprintf(`{
		"service": "auth-login-service",
		"version": "1.0.0",
		"uptime": "running",
		"database": "connected"
	}`)

	w.Write([]byte(metrics))
}
