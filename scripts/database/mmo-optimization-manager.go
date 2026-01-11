// MMO Database Optimization Manager
// Issue: #1949 - Database optimization utility for MMO workloads
// Manages composite indexes, partitioning, and performance tuning

package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"necpgame/services/shared-go/database"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to create logger:", err)
	}
	defer logger.Sync()

	logger.Info("Starting MMO Database Optimization Manager")

	// Database connection
	ctx := context.Background()
	pool, err := createPool(ctx, logger)
	if err != nil {
		logger.Fatal("Failed to create database pool", zap.Error(err))
	}
	defer pool.Close()

	// MMO table optimization
	mmoTables := []string{
		"combat_sessions",
		"player_inventory",
		"auctions",
		"quest_progress",
		"transaction_history",
		"market_price_history",
	}

	logger.Info("Optimizing MMO tables", zap.Int("count", len(mmoTables)))

	for _, table := range mmoTables {
		logger.Info("Optimizing table", zap.String("table", table))

		// Apply MMO-specific optimizations
		if err := database.OptimizeTableForMMO(ctx, pool, table, logger); err != nil {
			logger.Error("Failed to optimize table",
				zap.String("table", table), zap.Error(err))
			continue
		}

		logger.Info("Table optimized successfully", zap.String("table", table))
	}

	// Create time-based partitions for high-volume tables
	if err := createTimePartitions(ctx, pool, logger); err != nil {
		logger.Error("Failed to create time partitions", zap.Error(err))
	}

	// Analyze index usage
	if err := analyzeIndexUsage(ctx, pool, logger); err != nil {
		logger.Error("Failed to analyze index usage", zap.Error(err))
	}

	logger.Info("MMO Database Optimization completed successfully")
}

func createPool(ctx context.Context, logger *zap.Logger) (*pgxpool.Pool, error) {
	config := database.PoolConfig{
		Host:                   getEnvOrDefault("DB_HOST", "localhost"),
		Port:                   5432,
		User:                   getEnvOrDefault("DB_USER", "postgres"),
		Password:               getEnvOrDefault("DB_PASSWORD", ""),
		Database:               getEnvOrDefault("DB_NAME", "necpgame"),
		SSLMode:                "disable",
		MaxConns:               10,
		MinConns:               2,
		MaxConnLifetime:        30 * time.Minute,
		MaxConnIdleTime:        10 * time.Minute,
		HealthCheckPeriod:      30 * time.Second,
		StatementCacheCapacity: 100,
		PreparedStatementCacheEnabled: true,
	}

	return database.NewPool(ctx, config, logger)
}

func createTimePartitions(ctx context.Context, pool *pgxpool.Pool, logger *zap.Logger) error {
	logger.Info("Creating time-based partitions")

	// Combat sessions partitions (monthly)
	combatPartitions := []database.PartitionDefinition{
		{
			TableName:     "gameplay.combat_sessions_partitioned",
			PartitionName: "gameplay.combat_sessions_2024_07",
			PartitionType: "RANGE",
			PartitionKey:  "created_at",
			FromValue:     "2024-07-01",
			ToValue:       "2024-08-01",
		},
		{
			TableName:     "gameplay.combat_sessions_partitioned",
			PartitionName: "gameplay.combat_sessions_2024_08",
			PartitionType: "RANGE",
			PartitionKey:  "created_at",
			FromValue:     "2024-08-01",
			ToValue:       "2024-09-01",
		},
	}

	for _, partition := range combatPartitions {
		if err := database.CreateTimeBasedPartition(ctx, pool, partition, logger); err != nil {
			logger.Warn("Failed to create partition",
				zap.String("partition", partition.PartitionName), zap.Error(err))
		}
	}

	// Transaction history partitions (quarterly)
	transactionPartitions := []database.PartitionDefinition{
		{
			TableName:     "gameplay.transaction_history_partitioned",
			PartitionName: "gameplay.transaction_history_2025_q1",
			PartitionType: "RANGE",
			PartitionKey:  "created_at",
			FromValue:     "2025-01-01",
			ToValue:       "2025-04-01",
		},
	}

	for _, partition := range transactionPartitions {
		if err := database.CreateTimeBasedPartition(ctx, pool, partition, logger); err != nil {
			logger.Warn("Failed to create partition",
				zap.String("partition", partition.PartitionName), zap.Error(err))
		}
	}

	return nil
}

func analyzeIndexUsage(ctx context.Context, pool *pgxpool.Pool, logger *zap.Logger) error {
	logger.Info("Analyzing index usage")

	schemas := []string{"gameplay", "combat", "social"}

	for _, schema := range schemas {
		stats, err := database.AnalyzeIndexUsage(ctx, pool, schema, logger)
		if err != nil {
			logger.Warn("Failed to analyze schema",
				zap.String("schema", schema), zap.Error(err))
			continue
		}

		logger.Info("Index usage analysis completed",
			zap.String("schema", schema),
			zap.Int("indexes", len(stats)))

		// Log top 5 most used indexes
		for i, stat := range stats {
			if i >= 5 {
				break
			}
			logger.Info("Top index usage",
				zap.String("index", stat.IndexName),
				zap.Int64("scans", stat.IndexScans),
				zap.Int64("tuples_read", stat.TuplesRead))
		}

		// Find unused indexes
		unused, err := database.FindUnusedIndexes(ctx, pool, schema, logger)
		if err != nil {
			logger.Warn("Failed to find unused indexes",
				zap.String("schema", schema), zap.Error(err))
			continue
		}

		if len(unused) > 0 {
			logger.Warn("Unused indexes found",
				zap.String("schema", schema),
				zap.Strings("indexes", unused))
		}
	}

	return nil
}

func getEnvOrDefault(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}