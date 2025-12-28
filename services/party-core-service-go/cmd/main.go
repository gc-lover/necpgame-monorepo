package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/joho/godotenv"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/party-core-service-go/api"
	"github.com/gc-lover/necpgame/services/party-core-service-go/internal/handlers"
	"github.com/gc-lover/necpgame/services/party-core-service-go/internal/repository"
	"github.com/gc-lover/necpgame/services/party-core-service-go/internal/service"
)

func main() {
	// Загружаем переменные окружения
	if err := godotenv.Load(); err != nil {
		log.Printf("Warning: .env file not found: %v", err)
	}

	// Инициализируем логгер
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to create logger: %v", err)
	}
	defer logger.Sync()

	// Подключаемся к базе данных
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://user:password@localhost:5432/necpgame?sslmode=disable"
	}

	dbConfig, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		logger.Fatal("Failed to parse database config", zap.Error(err))
	}

	// Настраиваем пул соединений
	dbConfig.MaxConns = 10
	dbConfig.MinConns = 2
	dbConfig.MaxConnLifetime = time.Hour

	db, err := pgxpool.NewWithConfig(context.Background(), dbConfig)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Проверяем соединение с БД
	if err := db.Ping(context.Background()); err != nil {
		logger.Fatal("Failed to ping database", zap.Error(err))
	}
	logger.Info("Connected to database successfully")

	// Инициализируем репозиторий
	partyRepo := repository.NewPostgresPartyRepository(db)

	// Инициализируем сервис
	partyService := service.NewPartyService(partyRepo, logger)

	// Инициализируем хендлер
	partyHandler := handlers.NewPartyHandler(partyService, logger)

	// Настраиваем роутер
	e := echo.New()

	// Middleware
	e.Use(middleware.RequestID())
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.TimeoutWithConfig(middleware.TimeoutConfig{
		Timeout: 30 * time.Second,
	}))

	// CORS
	e.Use(middleware.CORSWithConfig(middleware.CORSConfig{
		AllowOrigins:     []string{"*"}, // В продакшене указать конкретные домены
		AllowMethods:     []string{echo.GET, echo.POST, echo.PUT, echo.DELETE, echo.OPTIONS},
		AllowHeaders:     []string{echo.HeaderAccept, echo.HeaderAuthorization, echo.HeaderContentType},
		ExposeHeaders:    []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health check
	e.GET("/health", func(c echo.Context) error {
		return c.String(http.StatusOK, "OK")
	})

	// Metrics endpoint (можно добавить Prometheus)
	e.GET("/metrics", func(c echo.Context) error {
		return c.String(http.StatusOK, "Metrics endpoint")
	})

	// API routes
	api.RegisterHandlers(e, partyHandler)

	// Получаем порт из переменных окружения
	port := os.Getenv("PORT")
	if port == "" {
		port = "8084"
	}

	// Запускаем сервер в горутине
	go func() {
		logger.Info("Starting server", zap.String("port", port))
		if err := e.Start(":" + port); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Ожидаем сигнал завершения
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := e.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}
