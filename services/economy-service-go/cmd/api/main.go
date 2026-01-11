//go:generate go run github.com/ogen-go/ogen/cmd/ogen@latest --target ../../pkg/services/economy-service-go/pkg/api --package api ../../bundled.yaml

package main

import (
	"context"
	"fmt"
	"log"
	"math/rand"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"necpgame/services/economy-service-go/config"
	"necpgame/services/economy-service-go/internal/repository"
	"necpgame/services/economy-service-go/internal/service"
	"necpgame/services/economy-service-go/internal/simulation/bazaar"
)

// Global connection pools for enterprise-grade performance
var (
	dbPool   *pgxpool.Pool
	redisClient *redis.Client
)

func initDatabasePool(ctx context.Context, cfg *config.Config, logger *zap.Logger) error {
	// PERFORMANCE: Configure database connection pool for MMOFPS scale
	poolConfig, err := pgxpool.ParseConfig(cfg.Database.GetDSN())
	if err != nil {
		return fmt.Errorf("failed to parse database config: %w", err)
	}

	// Apply enterprise-grade pool optimizations for MMOFPS
	poolConfig.MaxConns = int32(cfg.Database.MaxConns)
	poolConfig.MinConns = int32(cfg.Database.MinConns)
	poolConfig.MaxConnLifetime = cfg.Database.MaxConnLifetime
	poolConfig.MaxConnIdleTime = cfg.Database.MaxConnIdleTime
	poolConfig.HealthCheckPeriod = cfg.Database.HealthCheckPeriod

	pool, err := pgxpool.NewWithConfig(ctx, poolConfig)
	if err != nil {
		return fmt.Errorf("failed to create connection pool: %w", err)
	}

	if err := pool.Ping(ctx); err != nil {
		return fmt.Errorf("failed to ping database: %w", err)
	}

	dbPool = pool
	logger.Info("Database connection pool initialized",
		zap.Int32("max_conns", poolConfig.MaxConns),
		zap.Int32("min_conns", poolConfig.MinConns))
	return nil
}

func initRedis(ctx context.Context, cfg *config.Config, logger *zap.Logger) error {
	// PERFORMANCE: Initialize Redis with enterprise-grade pool optimization for MMOFPS economy caching
	redisClient = redis.NewClient(&redis.Options{
		Addr:         fmt.Sprintf("%s:%s", cfg.Redis.Host, cfg.Redis.Port),
		Password:     cfg.Redis.Password,
		DB:           cfg.Redis.DB,
		// BACKEND NOTE: Enterprise-grade Redis pool for MMOFPS economy caching
		PoolSize:     cfg.Redis.PoolSize,     // BACKEND NOTE: High pool for economy session caching
		MinIdleConns: cfg.Redis.MinIdleConns, // BACKEND NOTE: Keep connections ready for instant economy access
	})

	// Test Redis connection with timeout - BACKEND NOTE: Context timeout for Redis validation
	redisCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	if err := redisClient.Ping(redisCtx).Err(); err != nil {
		return fmt.Errorf("failed to ping redis: %w", err)
	}

	logger.Info("Redis connection initialized with enterprise-grade pool optimization",
		zap.Int("pool_size", cfg.Redis.PoolSize),
		zap.Int("min_idle_conns", cfg.Redis.MinIdleConns))
	return nil
}

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger", err)
	}
	defer logger.Sync()

	// Load configuration
	cfg := config.Load()

	logger.Info("Economy Service Starting", zap.String("port", cfg.Server.Port))

	// Initialize database and Redis connection pools
	ctx := context.Background()
	if err := initDatabasePool(ctx, cfg, logger); err != nil {
		logger.Fatal("Failed to initialize database pool", zap.Error(err))
	}
	defer dbPool.Close()

	if err := initRedis(ctx, cfg, logger); err != nil {
		logger.Fatal("Failed to initialize Redis", zap.Error(err))
	}
	defer redisClient.Close()

	// Initialize repository with Redis support
	repo := repository.NewRepository(dbPool, redisClient, logger.Named("economy-repo"))

	// Initialize service
	svc := service.NewService(logger, repo, cfg)

	// Create enterprise-grade HTTP server with MMOFPS optimizations
	// Configure HTTP server with enterprise-grade timeouts for MMOFPS economy operations
	srv := &http.Server{
		Addr:              cfg.Server.Port,
		Handler:           svc,
		ReadTimeout:       cfg.Server.ReadTimeout,       // BACKEND NOTE: Increased for complex economy operations
		WriteTimeout:      cfg.Server.WriteTimeout,      // BACKEND NOTE: For economy transaction responses
		IdleTimeout:       cfg.Server.IdleTimeout,       // BACKEND NOTE: Keep connections alive for economy sessions
		ReadHeaderTimeout: cfg.Server.ReadHeaderTimeout, // BACKEND NOTE: Fast header processing for economy requests
		MaxHeaderBytes:    cfg.Server.MaxHeaderBytes,    // BACKEND NOTE: 1MB max headers for security
	}

	// Start HTTP server in goroutine
	go func() {
		logger.Info("Starting HTTP server", zap.String("port", cfg.Server.Port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start HTTP server", zap.Error(err))
		}
	}()

	// Simulation Test
	simTest(logger)

	// Start Kafka consumer for event-driven market clearing (#2237)
	if err := svc.StartConsumer(ctx); err != nil {
		logger.Fatal("Failed to start Kafka consumer", zap.Error(err))
	}

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	shutdownCtx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	// Stop Kafka consumer gracefully
	if err := svc.StopConsumer(); err != nil {
		logger.Error("Failed to stop Kafka consumer", zap.Error(err))
	}

	if err := srv.Shutdown(shutdownCtx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}



// simTest demonstrates BazaarBot simulation with price convergence
// Issue: #2278
func simTest(logger *zap.Logger) {
	logger.Info("Starting BazaarBot Simulation Test",
		zap.String("simulation", "bazaarbot"),
		zap.String("commodity", string(bazaar.CommodityFood)))
	rand.Seed(time.Now().UnixNano())

	// Create a Market for Food
	market := bazaar.NewMarketLogic(bazaar.CommodityFood)

	// Create multiple agents with different personalities and beliefs
	agents := make([]*bazaar.AgentLogic, 0)

	// Create Buyers (Consumers)
	for i := 0; i < 5; i++ {
		buyer := bazaar.NewAgentLogic(fmt.Sprintf("buyer-%d", i+1), 100.0)
		// Initial beliefs: Food is worth 5-15
		buyer.SetPriceBelief(bazaar.CommodityFood, 5.0+float64(i), 15.0+float64(i))
		agents = append(agents, buyer)
	}

	// Create Sellers (Producers) with inventory
	for i := 0; i < 5; i++ {
		seller := bazaar.NewAgentLogic(fmt.Sprintf("seller-%d", i+1), 100.0)
		seller.State.Inventory[bazaar.CommodityFood] = 10 + i*5
		// Initial beliefs: Food is worth 8-12
		seller.SetPriceBelief(bazaar.CommodityFood, 8.0+float64(i), 12.0+float64(i))
		agents = append(agents, seller)
	}

	logger.Info("Created simulation agents",
		zap.Int("total_agents", len(agents)),
		zap.Int("buyers", 5),
		zap.Int("sellers", 5))

	// Run multiple trading rounds to observe price convergence
	numRounds := 10
	for round := 0; round < numRounds; round++ {
		logger.Info("Starting trading round",
			zap.Int("round", round+1),
			zap.Int("total_rounds", numRounds))

		// Create market state for agents to use in decisions
		marketState := market.CreateMarketState()

		// Agents decide on trades
		for _, agent := range agents {
			isProducer := agent.State.Inventory[bazaar.CommodityFood] > 0
			order := agent.DecideTrade(bazaar.CommodityFood, marketState, isProducer)

			if order != nil {
				market.AddOrder(order)
				if order.Type == bazaar.OrderTypeBid {
					logger.Debug("Agent placed bid order",
						zap.String("agent_id", agent.State.ID),
						zap.Float64("price", order.Price),
						zap.Int("quantity", order.Quantity))
				} else {
					logger.Debug("Agent placed ask order",
						zap.String("agent_id", agent.State.ID),
						zap.Float64("price", order.Price),
						zap.Int("quantity", order.Quantity))
				}
			}
		}

		// Clear market
		result := market.Clear(agents)
		logger.Info("Market cleared in round",
			zap.Int("round", round+1),
			zap.Float64("price", result.NewPrices[bazaar.CommodityFood]),
			zap.Int("volume", result.TotalVolume),
			zap.Float64("efficiency_percent", result.MarketEfficiency*100))

		// Show price convergence
		if len(result.ClearedTrades) > 0 {
			logger.Info("Trades executed in round",
				zap.Int("round", round+1),
				zap.Int("trade_count", len(result.ClearedTrades)))

			for _, trade := range result.ClearedTrades {
				logger.Debug("Trade executed",
					zap.String("seller_id", trade.SellerID),
					zap.String("buyer_id", trade.BuyerID),
					zap.Float64("price", trade.Price),
					zap.Int("quantity", trade.Quantity))
			}
		}
	}

	// Show final agent beliefs and wealth
	logger.Info("Simulation completed, showing final agent states")
	for _, agent := range agents {
		belief := agent.State.PriceBeliefs[bazaar.CommodityFood]
		if belief != nil {
			logger.Info("Final agent state",
				zap.String("agent_id", agent.State.ID),
				zap.Float64("belief_min", belief.Min),
				zap.Float64("belief_max", belief.Max),
				zap.Float64("wealth", agent.State.Wealth),
				zap.Int("inventory", agent.State.Inventory[bazaar.CommodityFood]))
		}
	}

	logger.Info("BazaarBot simulation completed successfully",
		zap.String("simulation", "bazaarbot"),
		zap.Int("total_rounds", numRounds),
		zap.Int("total_agents", len(agents)))
}

