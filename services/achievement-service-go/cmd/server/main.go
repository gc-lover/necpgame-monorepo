package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/kelseyhightower/envconfig"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/server"
)

// Config ...
type Config struct {
	Port           string `envconfig:"PORT" default:"8086"`
	DatabaseURL    string `envconfig:"DATABASE_URL" required:"true"`
	IsDevelopment bool   `envconfig:"IS_DEVELOPMENT" default:"false"`
}

func main() {
	var cfg Config
	err := envconfig.Process("ACHIEVEMENT", &cfg)
	if err != nil {
		log.Fatal().Err(err).Msg("Failed to load config")
	}

	// Настройка логирования
	if cfg.IsDevelopment {
		log.Logger = log.Output(zerolog.ConsoleWriter{Out: os.Stderr})
	}

	log.Info().Msg("Starting Achievement Service")

	// Подключение к БД
	pool, err := pgxpool.New(context.Background(), cfg.DatabaseURL)
	if err != nil {
		log.Fatal().Err(err).Msg("Failed to connect to database")
	}
	defer pool.Close()

	// Инициализация сервисов
	svc := server.NewAchievementService(pool)
	handlers := server.NewHandlers(svc)

	httpServer := server.NewHTTPServer(":"+cfg.Port, handlers, server.RequestLogger, server.Recoverer)

	go func() {
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			log.Fatal().Err(err).Msg("HTTP server failed")
		}
	}()

	// Ожидание сигнала завершения
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)
	<-sigChan

	log.Info().Msg("Shutting down Achievement Service...")

	// Корректное завершение HTTP сервера
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := httpServer.Shutdown(ctx); err != nil {
		log.Error().Err(err).Msg("HTTP server shutdown failed")
	}
	log.Info().Msg("Achievement Service shut down gracefully")
}


