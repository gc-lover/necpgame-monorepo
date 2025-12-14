// Issue: #75
package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
	"time"

	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"

	"github.com/NECPGAME/character-management-service-go/pkg/api"
)

// HTTPServer содержит HTTP сервер и зависимости
type HTTPServer struct {
	logger      *zap.Logger
	db          *sql.DB
	redisClient *redis.Client
	kafkaWriter *kafka.Writer
	config      *Config
	server      *http.Server
}

// Config содержит конфигурацию сервера
type Config struct {
	JWTSecret   string
	ServerPort  int
	OAuthConfig OAuthConfig
}

// OAuthConfig содержит конфигурацию OAuth провайдеров
type OAuthConfig struct {
	GoogleClientID      string
	GoogleClientSecret  string
	GitHubClientID      string
	GitHubClientSecret  string
	DiscordClientID     string
	DiscordClientSecret string
}

// NewHTTPServer создает новый HTTP сервер
func NewHTTPServer(logger *zap.Logger, db *sql.DB, redisClient *redis.Client, kafkaWriter *kafka.Writer, config *Config) *HTTPServer {
	return &HTTPServer{
		logger:      logger,
		db:          db,
		redisClient: redisClient,
		kafkaWriter: kafkaWriter,
		config:      config,
	}
}

// Start запускает HTTP сервер
func (s *HTTPServer) Start() error {
	s.logger.Info("Initializing HTTP server")

	// Создаем репозиторий для работы с данными
	repo := NewRepository(s.db, s.redisClient, s.kafkaWriter, s.logger)

	// Создаем сервис с бизнес-логикой
	service := NewService(repo, s.config, s.logger)

	// Создаем обработчики
	handlers := NewHandlers(service, s.logger)

	// Создаем security handler
	secHandler := NewSecurityHandler(s.config.JWTSecret, s.logger)

	// Создаем ogen сервер
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		return fmt.Errorf("failed to create ogen server: %w", err)
	}

	// Создаем роутер с middleware
	mux := http.NewServeMux()

	// Добавляем метрики Prometheus
	mux.Handle("/metrics", promhttp.Handler())
	mux.Handle("/health", s.healthCheckHandler())

	// Добавляем API routes через ogen
	mux.Handle("/api/v1/", ogenServer)

	// Создаем HTTP сервер с оптимизациями для MMOFPS
	s.server = &http.Server{
		Addr:         fmt.Sprintf(":%d", s.config.ServerPort),
		Handler:      s.middleware(mux),
		ReadTimeout:  10 * time.Second, // Защита от slow loris
		WriteTimeout: 30 * time.Second, // Достаточно для операций с персонажами
		IdleTimeout:  60 * time.Second, // Keep-alive для высокой нагрузки
	}

	s.logger.Info("HTTP server initialized", zap.Int("port", s.config.ServerPort))

	// Запускаем сервер
	if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		return fmt.Errorf("server failed: %w", err)
	}

	return nil
}

// Stop останавливает HTTP сервер
func (s *HTTPServer) Stop(ctx context.Context) error {
	s.logger.Info("Stopping HTTP server")
	return s.server.Shutdown(ctx)
}

// middleware добавляет общее middleware
func (s *HTTPServer) middleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Добавляем CORS headers для web клиентов
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		// Handle preflight requests
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		// Добавляем request ID для трейсинга
		requestID := fmt.Sprintf("%d", time.Now().UnixNano())
		ctx := context.WithValue(r.Context(), "request_id", requestID)

		// Логируем входящий запрос
		s.logger.Info("Incoming request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("request_id", requestID),
		)

		// Передаем управление следующему обработчику
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// healthCheckHandler возвращает health check endpoint
func (s *HTTPServer) healthCheckHandler() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Проверяем подключение к БД
		if err := s.db.PingContext(r.Context()); err != nil {
			s.logger.Error("Database health check failed", zap.Error(err))
			http.Error(w, "Database unavailable", http.StatusServiceUnavailable)
			return
		}

		// Проверяем подключение к Redis
		if err := s.redisClient.Ping(r.Context()).Err(); err != nil {
			s.logger.Error("Redis health check failed", zap.Error(err))
			http.Error(w, "Redis unavailable", http.StatusServiceUnavailable)
			return
		}

		// Проверяем подключение к Kafka
		if s.kafkaWriter != nil {
			// Простая проверка - попытка записать тестовое сообщение
			testMsg := kafka.Message{
				Topic: "health-check",
				Key:   []byte("test"),
				Value: []byte("health check"),
			}
			if err := s.kafkaWriter.WriteMessages(r.Context(), testMsg); err != nil {
				s.logger.Warn("Kafka health check warning", zap.Error(err))
				// Не считаем критической ошибкой
			}
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status": "healthy", "service": "character-management-service"}`))
	}
}
