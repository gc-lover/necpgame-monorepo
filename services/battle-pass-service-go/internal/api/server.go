package api

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

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
	// Initialize services
	seasonService := services.NewSeasonService(db, rdb, logger)
	progressService := services.NewProgressService(db, rdb, logger)
	rewardService := services.NewRewardService(db, rdb, logger)
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

	// API routes
	apiRouter := http.NewServeMux()

	// Season routes
	apiRouter.HandleFunc("/seasons", seasonHandler.ListSeasons)
	apiRouter.HandleFunc("/seasons/", seasonHandler.GetSeason)

	// Progress routes
	apiRouter.HandleFunc("/progress/", progressHandler.GetProgress)
	apiRouter.HandleFunc("/progress/xp", progressHandler.GrantXP)

	// Reward routes
	apiRouter.HandleFunc("/rewards/claim", rewardHandler.ClaimReward)
	apiRouter.HandleFunc("/rewards/available", rewardHandler.GetAvailableRewards)

	// Statistics routes
	apiRouter.HandleFunc("/statistics/player/", analyticsHandler.GetPlayerStatistics)

	// Apply middleware
	middlewareStack := middleware.Chain(
		middleware.CORS(),
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