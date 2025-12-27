// Issue: #2229
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

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"crafting-service-go/internal/config"
	"crafting-service-go/internal/handlers"
	"crafting-service-go/internal/service"
	"crafting-service-go/internal/repository"
	"crafting-service-go/internal/metrics"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency game service
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "50") // Lower GC threshold for MMOFPS
	}

	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		sugar.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database connection
	db, err := repository.NewConnection(cfg.DatabaseURL)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize Redis connection
	redisClient, err := repository.NewRedisClient(cfg.RedisURL)
	if err != nil {
		sugar.Fatalf("Failed to connect to Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize metrics
	metricsCollector := metrics.NewCollector()

	// Initialize repository layer
	repo := repository.NewCraftingRepository(db, redisClient, sugar)

	// Initialize service layer
	craftingService := service.NewCraftingService(repo, metricsCollector, sugar)

	// Initialize handlers
	craftingHandlers := handlers.NewCraftingHandlers(craftingService, sugar)

	// Setup HTTP server
	r := setupRouter(craftingHandlers, metricsCollector, sugar)

	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		sugar.Infof("Starting Crafting Service on port %d", cfg.Port)
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Setup graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	sugar.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		sugar.Errorf("Server forced to shutdown: %v", err)
	}

	sugar.Info("Server exited")
}

func setupRouter(handlers *handlers.CraftingHandlers, metrics *metrics.Collector, logger *zap.SugaredLogger) *chi.Mux {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health and metrics
	r.Get("/health", handlers.Health)
	r.Get("/ready", handlers.Ready)
	r.Handle("/metrics", promhttp.Handler())

	// API routes
	r.Route("/api/v1/economy/crafting", func(r chi.Router) {
		r.Use(handlers.AuthMiddleware)

		// Recipes
		r.Get("/recipes", handlers.GetRecipesByCategory)
		r.Get("/recipes/{recipeId}", handlers.GetRecipe)
		r.Post("/recipes", handlers.CreateRecipe)
		r.Put("/recipes/{recipeId}", handlers.UpdateRecipe)
		r.Delete("/recipes/{recipeId}", handlers.DeleteRecipe)

		// Orders
		r.Get("/orders", handlers.GetOrders)
		r.Get("/orders/{orderId}", handlers.GetOrder)
		r.Post("/orders", handlers.CreateOrder)
		r.Put("/orders/{orderId}", handlers.UpdateOrder)
		r.Delete("/orders/{orderId}", handlers.CancelOrder)

		// Stations
		r.Get("/stations", handlers.GetStations)
		r.Get("/stations/{stationId}", handlers.GetStation)
		r.Post("/stations/{stationId}/book", handlers.BookStation)
		r.Post("/stations/{stationId}/release", handlers.ReleaseStation)

		// Contracts
		r.Get("/contracts", handlers.GetContracts)
		r.Get("/contracts/{contractId}", handlers.GetContract)
		r.Post("/contracts", handlers.CreateContract)
		r.Put("/contracts/{contractId}", handlers.UpdateContract)

		// Production chains
		r.Get("/chains", handlers.GetProductionChains)
		r.Get("/chains/{chainId}", handlers.GetProductionChain)
		r.Post("/chains", handlers.CreateProductionChain)
		r.Put("/chains/{chainId}", handlers.UpdateProductionChain)
	})

	return r
}