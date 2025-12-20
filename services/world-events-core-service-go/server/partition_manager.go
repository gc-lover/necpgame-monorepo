// Package server Issue: #1582 - Automatic Partition Management
// OPTIMIZATION: Auto-create partitions 7 days ahead
// OPTIMIZATION: Auto-drop partitions older than 30 days
// GAINS: No manual partition management, automatic retention
package server

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"time"
)

// PartitionManager manages time-series table partitions
type PartitionManager struct {
	db               *sql.DB
	retentionDays    int           // Days to keep partitions (default: 30)
	futurePartitions int           // Days to create ahead (default: 7)
	checkInterval    time.Duration // How often to check (default: 24h)
}

// NewPartitionManager creates partition manager

// Start начинает автоматическое управление партициями
func (pm *PartitionManager) Start(ctx context.Context) {
	// Run immediately on start
	if err := pm.EnsurePartitions(ctx); err != nil {
		log.Printf("Failed to ensure partitions: %v", err)
	}

	// Then run periodically
	ticker := time.NewTicker(pm.checkInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			if err := pm.EnsurePartitions(ctx); err != nil {
				log.Printf("Failed to ensure partitions: %v", err)
			}
		case <-ctx.Done():
			return
		}
	}
}

// EnsurePartitions создает будущие партиции и удаляет старые
func (pm *PartitionManager) EnsurePartitions(ctx context.Context) error {
	tables := []struct {
		schema string
		table  string
	}{
		{"mvp_core", "combat_logs"},
		{"world_events", "event_history"},
		{"matchmaking", "match_history"},
		{"game_events", "game_events"},
	}

	for _, tbl := range tables {
		if err := pm.ensureTablePartitions(ctx, tbl.schema, tbl.table); err != nil {
			log.Printf("Failed to manage partitions for %s.%s: %v", tbl.schema, tbl.table, err)
			continue
		}
	}

	return nil
}

// ensureTablePartitions управляет партициями для одной таблицы
func (pm *PartitionManager) ensureTablePartitions(ctx context.Context, schema, table string) error {
	// Create future partitions (7 days ahead)
	for i := 0; i < pm.futurePartitions; i++ {
		date := time.Now().AddDate(0, 0, i)
		if err := pm.createPartition(ctx, schema, table, date); err != nil {
			return err
		}
	}

	// Drop old partitions (older than retention)
	oldDate := time.Now().AddDate(0, 0, -pm.retentionDays)
	if err := pm.dropOldPartitions(ctx, schema, table, oldDate); err != nil {
		return err
	}

	return nil
}

// createPartition создает партицию для даты (если не существует)
func (pm *PartitionManager) createPartition(ctx context.Context, schema, table string, date time.Time) error {
	partitionName := fmt.Sprintf("%s_%s", table, date.Format("2006_01_02"))
	fullPartitionName := fmt.Sprintf("%s.%s", schema, partitionName)

	// Check if partition exists
	var exists bool
	query := `
		SELECT EXISTS (
			SELECT 1 FROM pg_class c
			JOIN pg_namespace n ON n.oid = c.relnamespace
			WHERE n.nspname = $1 AND c.relname = $2
		)
	`
	err := pm.db.QueryRowContext(ctx, query, schema, partitionName).Scan(&exists)
	if err != nil {
		return fmt.Errorf("failed to check partition: %w", err)
	}

	if exists {
		return nil // Already exists
	}

	// Create partition
	startDate := date.Format("2006-01-02 15:04:05")
	endDate := date.AddDate(0, 0, 1).Format("2006-01-02 15:04:05")

	createSQL := fmt.Sprintf(`
		CREATE TABLE IF NOT EXISTS %s PARTITION OF %s.%s
		FOR VALUES FROM ('%s') TO ('%s')
	`, fullPartitionName, schema, table, startDate, endDate)

	_, err = pm.db.ExecContext(ctx, createSQL)
	if err != nil {
		return fmt.Errorf("failed to create partition %s: %w", partitionName, err)
	}

	log.Printf("Created partition: %s (range: %s to %s)", fullPartitionName, startDate, endDate)
	return nil
}

// dropOldPartitions удаляет партиции старше retention period
func (pm *PartitionManager) dropOldPartitions(ctx context.Context, schema, table string, cutoffDate time.Time) error {
	// Find partitions older than cutoff
	query := `
		SELECT c.relname
		FROM pg_class c
		JOIN pg_namespace n ON n.oid = c.relnamespace
		JOIN pg_inherits i ON i.inhrelid = c.oid
		JOIN pg_class parent ON parent.oid = i.inhparent
		WHERE n.nspname = $1 
		AND parent.relname = $2
		AND c.relname LIKE $3
	`

	pattern := table + "_%"
	rows, err := pm.db.QueryContext(ctx, query, schema, table, pattern)
	if err != nil {
		return fmt.Errorf("failed to list partitions: %w", err)
	}
	defer rows.Close()

	for rows.Next() {
		var partitionName string
		if err := rows.Scan(&partitionName); err != nil {
			continue
		}

		// Parse date from partition name (e.g., "combat_logs_2025_11_01")
		partDate, err := parsePartitionDate(partitionName, table)
		if err != nil {
			log.Printf("Failed to parse partition date: %s", partitionName)
			continue
		}

		// Drop if older than cutoff
		if partDate.Before(cutoffDate) {
			dropSQL := fmt.Sprintf("DROP TABLE IF EXISTS %s.%s", schema, partitionName)
			_, err := pm.db.ExecContext(ctx, dropSQL)
			if err != nil {
				log.Printf("Failed to drop partition %s: %v", partitionName, err)
				continue
			}

			log.Printf("Dropped old partition: %s.%s (date: %s, retention: %d days)",
				schema, partitionName, partDate.Format("2006-01-02"), pm.retentionDays)
		}
	}

	return nil
}

// parsePartitionDate извлекает дату из имени партиции
// Format: "table_name_2025_12_01" → 2025-12-01
func parsePartitionDate(partitionName, tablePrefix string) (time.Time, error) {
	// Remove table prefix and underscore
	dateStr := partitionName[len(tablePrefix)+1:] // Skip "table_name_"

	// Parse YYYY_MM_DD format
	date, err := time.Parse("2006_01_02", dateStr)
	if err != nil {
		return time.Time{}, fmt.Errorf("invalid partition date format: %s", dateStr)
	}

	return date, nil
}

// GetPartitionInfo возвращает информацию о партициях таблицы
func (pm *PartitionManager) GetPartitionInfo(ctx context.Context, schema, table string) ([]PartitionInfo, error) {
	query := `
		SELECT 
			c.relname as partition_name,
			pg_get_expr(c.relpartbound, c.oid) as partition_range,
			pg_size_pretty(pg_total_relation_size(c.oid)) as size
		FROM pg_class c
		JOIN pg_namespace n ON n.oid = c.relnamespace
		JOIN pg_inherits i ON i.inhrelid = c.oid
		JOIN pg_class parent ON parent.oid = i.inhparent
		WHERE n.nspname = $1 
		AND parent.relname = $2
		ORDER BY c.relname
	`

	rows, err := pm.db.QueryContext(ctx, query, schema, table)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var partitions []PartitionInfo
	for rows.Next() {
		var p PartitionInfo
		if err := rows.Scan(&p.Name, &p.Range, &p.Size); err != nil {
			continue
		}
		partitions = append(partitions, p)
	}

	return partitions, nil
}

// PartitionInfo содержит информацию о партиции
type PartitionInfo struct {
	Name  string
	Range string
	Size  string
}
