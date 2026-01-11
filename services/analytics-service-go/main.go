package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/handlers"
	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/repository"
	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/service"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/prometheus"
	"go.opentelemetry.io/otel/sdk/metric"
	"go.uber.org/zap"
)

func main() {
	// Инициализация логгера
	logger, err := zap.NewProduction()
	if err != nil {
		fmt.Printf("Failed to initialize logger: %v\n", err)
		os.Exit(1)
	}
	defer logger.Sync()

	logger.Info("Starting Analytics Service",
		zap.String("version", "1.0.0"),
		zap.String("service", "analytics-service"))

	// Инициализация метрик
	prometheusExporter, err := prometheus.New()
	if err != nil {
		logger.Fatal("Failed to initialize Prometheus exporter", zap.Error(err))
	}

	meterProvider := metric.NewMeterProvider(
		metric.WithReader(prometheusExporter),
	)
	otel.SetMeterProvider(meterProvider)

	meter := meterProvider.Meter("analytics-service")

	// Подключение к базе данных
	dbPool, err := initDatabase(logger)
	if err != nil {
		logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer dbPool.Close()

	// Инициализация репозитория
	repo := repository.NewPostgresRepository(dbPool, logger)

	// Инициализация сервиса
	svc, err := service.NewService(&service.AnalyticsConfig{
		Repository:               repo,
		Logger:                   logger,
		Meter:                    meter,
		BehaviorAnalysisInterval: 1 * time.Hour,      // анализ поведения каждый час
		RetentionUpdateInterval:  6 * time.Hour,      // обновление retention каждые 6 часов
		ABTestUpdateInterval:     30 * time.Minute,   // обновление A/B тестов каждые 30 минут
	})
	if err != nil {
		logger.Fatal("Failed to initialize service", zap.Error(err))
	}
	defer svc.Stop()

	// Инициализация обработчиков
	h := handlers.NewHandlers(svc, logger)

	// Настройка HTTP сервера
	httpAddr := getEnv("HTTP_ADDR", ":8081")
	server := &http.Server{
		Addr:         httpAddr,
		Handler:      h.Router(),
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Канал для graceful shutdown
	done := make(chan os.Signal, 1)
	signal.Notify(done, os.Interrupt, syscall.SIGINT, syscall.SIGTERM)

	// Запуск сервера в горутине
	go func() {
		logger.Info("Starting HTTP server", zap.String("addr", httpAddr))
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start HTTP server", zap.Error(err))
		}
	}()

	// Ожидание сигнала завершения
	<-done
	logger.Info("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := server.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

// initDatabase инициализирует подключение к PostgreSQL
func initDatabase(logger *zap.Logger) (*pgxpool.Pool, error) {
	dbURL := getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/analytics_db?sslmode=disable")

	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database config: %w", err)
	}

	// Настройка пула соединений для высокой производительности аналитики
	config.MaxConns = 20
	config.MinConns = 5
	config.MaxConnLifetime = 10 * time.Minute
	config.MaxConnIdleTime = 5 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	// Проверка подключения
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := pool.Ping(ctx); err != nil {
		pool.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established",
		zap.Int("max_conns", int(config.MaxConns)),
		zap.Int("min_conns", int(config.MinConns)))

	return pool, nil
}

// getEnv получает переменную окружения с дефолтным значением
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}