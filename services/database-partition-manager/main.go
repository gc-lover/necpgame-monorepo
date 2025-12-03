// Issue: #1582
// Database Partition Manager - Automated partition management for time-series tables
// PERFORMANCE: Auto creates partitions 7 days ahead, drops 30+ days old
// RUNS: Daily via Kubernetes CronJob
package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"os"
	"time"

	_ "github.com/lib/pq"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		log.Fatal("DATABASE_URL environment variable not set")
	}

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Test connection
	if err := db.Ping(); err != nil {
		log.Fatalf("Failed to ping database: %v", err)
	}

	log.Println("Database Partition Manager starting...")

	pm := NewPartitionManager(db)

	// Run partition management
	if err := pm.ManagePartitions(context.Background()); err != nil {
		log.Fatalf("Partition management failed: %v", err)
	}

	log.Println("OK Partition management completed successfully")
}

// PartitionManager manages time-series table partitions
type PartitionManager struct {
	db *sql.DB
}

// NewPartitionManager creates partition manager
func NewPartitionManager(db *sql.DB) *PartitionManager {
	return &PartitionManager{db: db}
}

// PartitionConfig defines partition configuration
type PartitionConfig struct {
	Schema        string
	Table         string
	PartitionKey  string
	FutureDays    int // Create partitions N days ahead
	RetentionDays int // Drop partitions older than N days
}

// ManagePartitions manages all partitioned tables
func (pm *PartitionManager) ManagePartitions(ctx context.Context) error {
	// Tables requiring partitioning (Issue #1582)
	tables := []PartitionConfig{
		{
			Schema:        "mvp_core",
			Table:         "combat_logs",
			PartitionKey:  "created_at",
			FutureDays:    7,  // Create 7 days ahead
			RetentionDays: 30, // Keep 30 days
		},
		{
			Schema:        "world_events",
			Table:         "event_history",
			PartitionKey:  "timestamp",
			FutureDays:    7,
			RetentionDays: 30,
		},
		{
			Schema:        "matchmaking",
			Table:         "match_history",
			PartitionKey:  "created_at",
			FutureDays:    7,
			RetentionDays: 30,
		},
		{
			Schema:        "game_events",
			Table:         "game_events",
			PartitionKey:  "created_at",
			FutureDays:    7,
			RetentionDays: 30,
		},
	}

	for _, config := range tables {
		log.Printf("Managing partitions for %s.%s", config.Schema, config.Table)

		// Create future partitions
		if err := pm.createFuturePartitions(ctx, config); err != nil {
			log.Printf("❌ Failed to create partitions for %s.%s: %v", 
				config.Schema, config.Table, err)
			continue
		}

		// Drop old partitions
		if err := pm.dropOldPartitions(ctx, config); err != nil {
			log.Printf("❌ Failed to drop old partitions for %s.%s: %v", 
				config.Schema, config.Table, err)
			continue
		}

		log.Printf("OK %s.%s partitions managed successfully", config.Schema, config.Table)
	}

	return nil
}

// createFuturePartitions creates partitions for future dates
func (pm *PartitionManager) createFuturePartitions(ctx context.Context, config PartitionConfig) error {
	now := time.Now()

	for i := 0; i <= config.FutureDays; i++ {
		date := now.AddDate(0, 0, i)
		partitionName := fmt.Sprintf("%s_%s", config.Table, date.Format("2006_01_02"))
		startDate := date.Format("2006-01-02 00:00:00")
		endDate := date.AddDate(0, 0, 1).Format("2006-01-02 00:00:00")

		// Check if partition exists
		var exists bool
		query := `
			SELECT EXISTS (
				SELECT 1 FROM pg_tables 
				WHERE schemaname = $1 AND tablename = $2
			)
		`
		err := pm.db.QueryRowContext(ctx, query, config.Schema, partitionName).Scan(&exists)
		if err != nil {
			return fmt.Errorf("failed to check partition existence: %w", err)
		}

		if exists {
			continue // Partition already exists
		}

		// Create partition
		createSQL := fmt.Sprintf(`
			CREATE TABLE %s.%s PARTITION OF %s.%s
			FOR VALUES FROM ('%s') TO ('%s')
		`, config.Schema, partitionName, config.Schema, config.Table, startDate, endDate)

		_, err = pm.db.ExecContext(ctx, createSQL)
		if err != nil {
			return fmt.Errorf("failed to create partition %s: %w", partitionName, err)
		}

		log.Printf("  OK Created partition: %s (%s to %s)", partitionName, startDate, endDate)
	}

	return nil
}

// dropOldPartitions drops partitions older than retention period
func (pm *PartitionManager) dropOldPartitions(ctx context.Context, config PartitionConfig) error {
	retentionDate := time.Now().AddDate(0, 0, -config.RetentionDays)

	// Find old partitions
	query := `
		SELECT tablename 
		FROM pg_tables 
		WHERE schemaname = $1 
		AND tablename LIKE $2
	`
	pattern := config.Table + "_%"

	rows, err := pm.db.QueryContext(ctx, query, config.Schema, pattern)
	if err != nil {
		return fmt.Errorf("failed to list partitions: %w", err)
	}
	defer rows.Close()

	var droppedCount int
	for rows.Next() {
		var tableName string
		if err := rows.Scan(&tableName); err != nil {
			continue
		}

		// Parse date from partition name (format: table_YYYY_MM_DD)
		partitionDate, err := parsePartitionDate(tableName, config.Table)
		if err != nil {
			continue // Skip if can't parse
		}

		// Drop if older than retention
		if partitionDate.Before(retentionDate) {
			dropSQL := fmt.Sprintf("DROP TABLE %s.%s", config.Schema, tableName)
			_, err := pm.db.ExecContext(ctx, dropSQL)
			if err != nil {
				log.Printf("  ❌ Failed to drop partition %s: %v", tableName, err)
				continue
			}

			log.Printf("  🗑️  Dropped old partition: %s (date: %s)", 
				tableName, partitionDate.Format("2006-01-02"))
			droppedCount++
		}
	}

	if droppedCount > 0 {
		log.Printf("  OK Dropped %d old partitions", droppedCount)
	}

	return nil
}

// parsePartitionDate parses date from partition name
// Format: table_YYYY_MM_DD
func parsePartitionDate(partitionName, tablePrefix string) (time.Time, error) {
	// Remove table prefix and underscore
	dateStr := partitionName[len(tablePrefix)+1:]
	
	// Parse date (format: 2006_01_02)
	return time.Parse("2006_01_02", dateStr)
}


