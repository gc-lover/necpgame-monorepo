package api

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
	"strings"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"battle-pass-service-go/internal/clients"
	"battle-pass-service-go/internal/config"
	"battle-pass-service-go/internal/handlers"
	"battle-pass-service-go/internal/middleware"
	"battle-pass-service-go/internal/services"
)

// Server represents the HTTP server
type Server struct {
	*http.Server
	logger   *zap.Logger
	config   *config.Config
	database *sql.DB
	redis    *redis.Client
}

// NewServer creates a new HTTP server instance
func NewServer(cfg *config.Config, db *sql.DB, rdb *redis.Client, logger *zap.Logger) *Server {
	// Initialize external service clients
	playerClient := clients.NewPlayerClient(cfg.Services.PlayerServiceURL, logger)
	inventoryClient := clients.NewInventoryClient(cfg.Services.InventoryServiceURL, logger)
	economyClient := clients.NewEconomyClient(cfg.Services.EconomyServiceURL, logger)

	// Initialize services
	seasonService := services.NewSeasonService(db, rdb, logger)
	progressService := services.NewProgressService(db, rdb, logger, economyClient, playerClient)
	rewardService := services.NewRewardService(db, rdb, logger, inventoryClient, economyClient, playerClient)
	analyticsService := services.NewAnalyticsService(db, rdb, logger)

	// Initialize handlers
	seasonHandler := handlers.NewSeasonHandler(seasonService, logger)
	progressHandler := handlers.NewProgressHandler(progressService, logger)
	rewardHandler := handlers.NewRewardHandler(rewardService, logger)
	analyticsHandler := handlers.NewAnalyticsHandler(analyticsService, logger)

	// Create router
	router := http.NewServeMux()

	// Health check
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	// Metrics endpoint for monitoring
	router.Handle("/metrics", MetricsHandler())

	// API routes
	apiRouter := http.NewServeMux()

	// Season routes
	apiRouter.HandleFunc("/seasons", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodGet {
			seasonHandler.ListSeasons(w, r)
		} else if r.Method == http.MethodPost {
			seasonHandler.CreateSeason(w, r)
		} else {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})
	apiRouter.HandleFunc("/seasons/", func(w http.ResponseWriter, r *http.Request) {
		pathParts := strings.Split(strings.Trim(r.URL.Path, "/"), "/")
		if len(pathParts) >= 3 && pathParts[2] == "activate" {
			if r.Method == http.MethodPost {
				seasonHandler.ActivateSeason(w, r)
			} else {
				http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			}
		} else {
			if r.Method == http.MethodGet {
				seasonHandler.GetSeason(w, r)
			} else if r.Method == http.MethodPut {
				seasonHandler.UpdateSeason(w, r)
			} else {
				http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			}
		}
	})

	// Progress routes
	apiRouter.HandleFunc("/progress/", progressHandler.GetProgress)
	apiRouter.HandleFunc("/progress/xp", progressHandler.GrantXP)
	apiRouter.HandleFunc("/progress/premium", progressHandler.PurchasePremiumPass)
	apiRouter.HandleFunc("/progress/leaderboard", progressHandler.GetLeaderboard)

	// Reward routes
	apiRouter.HandleFunc("/rewards", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodGet {
			rewardHandler.ListRewards(w, r)
		} else if r.Method == http.MethodPost {
			rewardHandler.CreateReward(w, r)
		} else {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})
	apiRouter.HandleFunc("/rewards/claim", rewardHandler.ClaimReward)
	apiRouter.HandleFunc("/rewards/available", rewardHandler.GetAvailableRewards)
	apiRouter.HandleFunc("/rewards/history", rewardHandler.GetClaimHistory)

	// Statistics routes
	apiRouter.HandleFunc("/statistics/player/", analyticsHandler.GetPlayerStatistics)
	apiRouter.HandleFunc("/statistics/global", analyticsHandler.GetGlobalStatistics)
	apiRouter.HandleFunc("/statistics/season/", analyticsHandler.GetSeasonAnalytics)

	// Apply middleware (optimized for production)
	middlewareStack := middleware.Chain(
		middleware.CORS(),
		middleware.Compression(),     // Enable gzip compression
		middleware.RateLimit(1000),  // 1000 requests per minute per IP
		middleware.Logging(logger),
		middleware.Auth(cfg.JWT),
		middleware.Recovery(logger),
	)

	// Mount API routes
	router.Handle("/api/v1/", middlewareStack(http.StripPrefix("/api/v1", apiRouter)))

	return &Server{
		Server: &http.Server{
			Addr:         cfg.Server.Host + ":" + fmt.Sprintf("%d", cfg.Server.Port),
			Handler:      router,
			ReadTimeout:  cfg.Server.ReadTimeout,
			WriteTimeout: cfg.Server.WriteTimeout,
		},
		logger:   logger,
		config:   cfg,
		database: db,
		redis:    rdb,
	}
}