package main

import (
	"context"
	"database/sql"
	"flag"
	"log/slog"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "github.com/lib/pq" // PostgreSQL driver

	"necpgame/services/ai-combat-calculator-service-go/server"
)

func main() {
	var (
		addr     = flag.String("addr", ":8082", "HTTP server address")
		dbURL    = flag.String("db-url", "postgres://user:password@localhost/necpgame?sslmode=disable", "Database URL")
		logLevel = flag.String("log-level", "info", "Log level (debug, info, warn, error)")
	)
	flag.Parse()

	// Setup structured logging
	setupLogging(*logLevel)

	slog.Info("Starting AI Combat Calculator Service",
		"addr", *addr,
		"version", "1.0.0",
	)

	// Initialize database connection
	db, err := initDatabase(*dbURL)
	if err != nil {
		slog.Error("Failed to initialize database", "error", err)
		os.Exit(1)
	}
	defer db.Close()

	// Initialize repository
	repo := server.NewPostgresRepository(db)

	// Initialize service
	service := server.NewAiCombatCalculatorService(repo)

	// Initialize middleware
	middleware := server.NewMiddleware()

	// Create API server
	apiServer := server.NewAiCombatCalculatorServer(service)

	// Setup HTTP server
	httpServer := server.NewHTTPServer(*addr, setupRouter(apiServer, middleware))

	// Start server in goroutine
	go func() {
		slog.Info("Starting HTTP server", "addr", *addr)
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			slog.Error("HTTP server failed", "error", err)
			os.Exit(1)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	slog.Info("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Stop(ctx); err != nil {
		slog.Error("Server forced to shutdown", "error", err)
		os.Exit(1)
	}

	slog.Info("Server exited")
}

func setupLogging(level string) {
	var slogLevel slog.Level
	switch level {
	case "debug":
		slogLevel = slog.LevelDebug
	case "info":
		slogLevel = slog.LevelInfo
	case "warn":
		slogLevel = slog.LevelWarn
	case "error":
		slogLevel = slog.LevelError
	default:
		slogLevel = slog.LevelInfo
	}

	handler := slog.NewTextHandler(os.Stdout, &slog.HandlerOptions{
		Level: slogLevel,
	})
	logger := slog.New(handler)
	slog.SetDefault(logger)
}

func initDatabase(dbURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		return nil, err
	}

	// Performance: Database connection pooling
	db.SetMaxOpenConns(25)  // Performance: DB pool config
	db.SetMaxIdleConns(5)   // Performance: Connection reuse
	db.SetConnMaxLifetime(5 * time.Minute)

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		db.Close()
		return nil, err
	}

	slog.Info("Database connection established")
	return db, nil
}

func setupRouter(apiServer *server.AiCombatCalculatorServer, middleware *server.Middleware) http.Handler {
	mux := http.NewServeMux()

	// Health check endpoint (no auth required)
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		// TODO: Implement health check
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ok"}`))
	})

	// API endpoints with middleware
	apiHandler := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Route to appropriate handler based on path
		switch r.URL.Path {
		case "/api/v1/combat/calculate":
			if r.Method == "POST" {
				// TODO: Implement damage calculation
				w.WriteHeader(http.StatusNotImplemented)
			} else {
				http.NotFound(w, r)
			}
		default:
			http.NotFound(w, r)
		}
	})

	// Apply middleware stack
	handler := middleware.CORS(
		middleware.RequestID(
			middleware.RealIP(
				middleware.Logger(
					middleware.Recoverer(
						middleware.Timeout(30 * time.Second)(
							middleware.Auth(
								middleware.Metrics(
									apiHandler,
								),
							),
						),
					),
				),
			),
		),
	)

	return handler
}