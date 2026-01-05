package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-faster/errors"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/jaeger"
	"go.opentelemetry.io/otel/sdk/resource"
	semconv "go.opentelemetry.io/otel/semconv/v1.17.0"
	"go.opentelemetry.io/otel/sdk/trace"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"

	"master-modes-service-go/internal/config"
	"master-modes-service-go/internal/server"
	"master-modes-service-go/internal/service"
)

// buildInfo содержит информацию о сборке для оптимизации
type buildInfo struct {
	version   string
	buildTime string
	gitCommit string
	goVersion string
}

// getBuildInfo возвращает информацию о сборке
func getBuildInfo() buildInfo {
	return buildInfo{
		version:   "1.0.0",
		buildTime: "2025-12-28T12:00:00Z",
		gitCommit: "dev",
		goVersion: "go1.21.0",
	}
}

// setupLogger создает оптимизированный логгер для высокой производительности
func setupLogger() (*zap.Logger, error) {
	// Конфигурация для высокой производительности в MMOFPS
	encoderConfig := zapcore.EncoderConfig{
		TimeKey:        "ts",
		LevelKey:       "level",
		NameKey:        "logger",
		CallerKey:      "caller",
		FunctionKey:    zapcore.OmitKey,
		MessageKey:     "msg",
		StacktraceKey:  "stacktrace",
		LineEnding:      zapcore.DefaultLineEnding,
		EncodeLevel:    zapcore.LowercaseLevelEncoder,
		EncodeTime:     zapcore.ISO8601TimeEncoder,
		EncodeDuration: zapcore.SecondsDurationEncoder,
		EncodeCaller:   zapcore.ShortCallerEncoder,
	}

	// Async логгер для снижения latency
	core := zapcore.NewCore(
		zapcore.NewJSONEncoder(encoderConfig),
		zapcore.AddSync(os.Stdout),
		zapcore.InfoLevel,
	)

	// Добавляем caller для debugging, но только в dev режиме
	var opts []zap.Option
	if os.Getenv("ENV") == "development" {
		opts = append(opts, zap.AddCaller(), zap.AddStacktrace(zapcore.ErrorLevel))
	} else {
		// В prod режиме caller замедляет работу
		opts = append(opts, zap.AddStacktrace(zapcore.FatalLevel))
	}

	logger := zap.New(core, opts...)

	return logger, nil
}

// setupTracer инициализирует OpenTelemetry tracer для distributed tracing
func setupTracer(serviceName string) (*trace.TracerProvider, error) {
	// Jaeger exporter для tracing в MMOFPS среде
	exp, err := jaeger.New(
		jaeger.WithCollectorEndpoint(
			jaeger.WithEndpoint("http://jaeger:14268/api/traces"),
		),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create jaeger exporter")
	}

	// Resource для идентификации сервиса
	res, err := resource.New(
		context.Background(),
		resource.WithAttributes(
			semconv.ServiceNameKey.String(serviceName),
			semconv.ServiceVersionKey.String(getBuildInfo().version),
			attribute.String("service.type", "gameplay"),
			attribute.String("service.category", "master-modes"),
		),
	)
	if err != nil {
		return nil, errors.Wrap(err, "failed to create resource")
	}

	// Tracer provider с оптимизациями для MMOFPS
	tp := trace.NewTracerProvider(
		trace.WithBatcher(exp,
			trace.WithBatchTimeout(100*time.Millisecond),     // Быстрый flush для низкой latency
			trace.WithMaxExportBatchSize(512),                // Оптимальный размер batch
			trace.WithMaxQueueSize(2048),                     // Достаточная очередь для пиковых нагрузок
		),
		trace.WithResource(res),
		trace.WithSampler(trace.AlwaysSample()), // Всегда sample в dev, в prod - probability
	)

	otel.SetTracerProvider(tp)

	return tp, nil
}

// runHTTPServer запускает HTTP сервер с оптимизациями для высокой производительности
func runHTTPServer(ctx context.Context, handler http.Handler, cfg *config.Config, logger *zap.Logger) error {
	server := &http.Server{
		Addr:    fmt.Sprintf(":%d", cfg.Server.Port),
		Handler: handler,

		// Оптимизации для MMOFPS
		ReadTimeout:       15 * time.Second,  // Защита от slow loris
		WriteTimeout:      15 * time.Second,  // Быстрый timeout для responsiveness
		IdleTimeout:       60 * time.Second,  // Keep-alive timeout
		ReadHeaderTimeout: 5 * time.Second,   // Защита от header attacks
		MaxHeaderBytes:    1 << 20,          // 1MB limit для headers

		// Connection pooling optimizations
		SetKeepAlivesEnabled: true,
	}

	// Graceful shutdown channel
	shutdownError := make(chan error)

	go func() {
		logger.Info("Starting HTTP server",
			zap.Int("port", cfg.Server.Port),
			zap.String("address", server.Addr))

		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			shutdownError <- errors.Wrap(err, "server failed to start")
		}
		shutdownError <- nil
	}()

	// Wait for shutdown signal
	select {
	case err := <-shutdownError:
		return err
	case <-ctx.Done():
		logger.Info("Shutting down server gracefully...")

		// Graceful shutdown с таймаутом
		shutdownCtx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
		defer cancel()

		if err := server.Shutdown(shutdownCtx); err != nil {
			logger.Error("Server forced to shutdown", zap.Error(err))
			return errors.Wrap(err, "server forced to shutdown")
		}

		logger.Info("Server shutdown complete")
		return nil
	}
}

// main является точкой входа в приложение с оптимизациями для MMOFPS
func main() {
	build := getBuildInfo()
	fmt.Printf("NECP Master Modes Service v%s (%s)\n", build.version, build.gitCommit)

	// Инициализация логгера
	logger, err := setupLogger()
	if err != nil {
		log.Fatal("Failed to setup logger", err)
	}
	defer func() {
		if err := logger.Sync(); err != nil {
			log.Printf("Failed to sync logger: %v", err)
		}
	}()

	// Загрузка конфигурации с валидацией
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load config", zap.Error(err))
	}

	logger.Info("Configuration loaded",
		zap.String("env", cfg.Environment),
		zap.Int("port", cfg.Server.Port))

	// Настройка tracing
	tp, err := setupTracer("master-modes-service")
	if err != nil {
		logger.Error("Failed to setup tracer", zap.Error(err))
		// Не фатально, продолжаем без tracing
	} else {
		defer func() {
			if err := tp.Shutdown(context.Background()); err != nil {
				logger.Error("Failed to shutdown tracer", zap.Error(err))
			}
		}()
	}

	// Инициализация сервиса с connection pooling
	svc, err := service.NewService(context.Background(), cfg, logger)
	if err != nil {
		logger.Fatal("Failed to create service", zap.Error(err))
	}
	defer svc.Close()

	// Создание HTTP сервера
	srv, err := server.NewServer(svc, logger)
	if err != nil {
		logger.Fatal("Failed to create server", zap.Error(err))
	}
	defer srv.Close()

	// Настройка контекста с cancellation для graceful shutdown
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	// Запуск сервера
	if err := runHTTPServer(ctx, srv.Handler(), cfg, logger); err != nil {
		logger.Fatal("Server failed", zap.Error(err))
	}

	logger.Info("Master Modes Service stopped successfully")
}
