// Combat Combos Loadouts Service Logger
// Issue: #141890005

package server

import (
	"context"
	"os"

	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
)

// setupLogger configures structured logging
func setupLogger(cfg *Config) {
	// Set global log level
	level, err := zerolog.ParseLevel(cfg.LogLevel)
	if err != nil {
		level = zerolog.InfoLevel
	}
	zerolog.SetGlobalLevel(level)

	// Configure logger output
	switch cfg.LogFormat {
	case "json":
		log.Logger = zerolog.New(os.Stdout).With().
			Timestamp().
			Str("service", cfg.ServiceName).
			Str("version", cfg.ServiceVersion).
			Logger()
	default:
		log.Logger = zerolog.New(zerolog.ConsoleWriter{Out: os.Stdout}).With().
			Timestamp().
			Str("service", cfg.ServiceName).
			Str("version", cfg.ServiceVersion).
			Logger()
	}
}

// getLogger returns a logger with request context
func getLogger(ctx context.Context) zerolog.Logger {
	return log.With().Ctx(ctx).Logger()
}
