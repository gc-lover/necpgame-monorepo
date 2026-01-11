// Advanced Indexing Strategies for High-Throughput Queries
// Issue: #2101
// PERFORMANCE: Covering indexes, GIN indexes, spatial indexes, expression indexes

package database

import (
	"context"
	"fmt"
	"strings"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// AdvancedIndexType represents different types of advanced indexes
type AdvancedIndexType string

const (
	IndexTypeBTree    AdvancedIndexType = "btree"    // Default
	IndexTypeGIN      AdvancedIndexType = "gin"      // For JSONB, arrays
	IndexTypeGIST     AdvancedIndexType = "gist"     // For spatial, full-text
	IndexTypeBRIN     AdvancedIndexType = "brin"     // For large tables with sorted data
	IndexTypeHash     AdvancedIndexType = "hash"     // For equality checks
	IndexTypeSPGIST   AdvancedIndexType = "spgist"  // For non-balanced trees
)

// AdvancedIndexDefinition represents an advanced index with optimization strategies
type AdvancedIndexDefinition struct {
	Name        string
	Table       string
	Columns     []string
	IndexType   AdvancedIndexType
	Unique      bool
	Partial     string // WHERE clause for partial index
	Concurrent  bool   // Use CREATE INDEX CONCURRENTLY
	Covering    []string // INCLUDE columns for covering index
	Expressions []string // Expression-based indexes (e.g., "LOWER(email)")
	OperatorClass string // Custom operator class (e.g., "varchar_pattern_ops")
}

// CreateAdvancedIndex creates an advanced index with optimization strategies
func CreateAdvancedIndex(ctx context.Context, pool *pgxpool.Pool, index AdvancedIndexDefinition, logger *zap.Logger) error {
	var builder strings.Builder

	// Build CREATE INDEX statement
	if index.Concurrent {
		builder.WriteString("CREATE INDEX CONCURRENTLY ")
	} else {
		builder.WriteString("CREATE ")
		if index.Unique {
			builder.WriteString("UNIQUE ")
		}
		builder.WriteString("INDEX ")
	}

	builder.WriteString(fmt.Sprintf("IF NOT EXISTS %s ON %s USING %s (", index.Name, index.Table, index.IndexType))

	// Build column/expression list
	var parts []string

	// Add columns
	for _, col := range index.Columns {
		parts = append(parts, col)
	}

	// Add expressions
	for _, expr := range index.Expressions {
		parts = append(parts, fmt.Sprintf("(%s)", expr))
	}

	builder.WriteString(strings.Join(parts, ", "))
	builder.WriteString(")")

	// Add operator class if specified
	if index.OperatorClass != "" {
		builder.WriteString(fmt.Sprintf(" %s", index.OperatorClass))
	}

	// Add covering columns (INCLUDE clause)
	if len(index.Covering) > 0 {
		builder.WriteString(fmt.Sprintf(" INCLUDE (%s)", strings.Join(index.Covering, ", ")))
	}

	// Add partial index WHERE clause
	if index.Partial != "" {
		builder.WriteString(fmt.Sprintf(" WHERE %s", index.Partial))
	}

	query := builder.String()

	if _, err := pool.Exec(ctx, query); err != nil {
		return errors.Wrapf(err, "failed to create advanced index %s", index.Name)
	}

	logger.Info("Advanced index created",
		zap.String("name", index.Name),
		zap.String("table", index.Table),
		zap.String("type", string(index.IndexType)),
		zap.Strings("columns", index.Columns),
		zap.Strings("expressions", index.Expressions),
		zap.Strings("covering", index.Covering))

	return nil
}

// CreateCoveringIndex creates a covering index (includes additional columns to avoid table lookup)
func CreateCoveringIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns, covering []string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:     name,
		Table:    table,
		Columns:  columns,
		IndexType: IndexTypeBTree,
		Covering:  covering,
		Concurrent: true,
	}, logger)
}

// CreateGINIndex creates a GIN index for JSONB or array columns
func CreateGINIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns []string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:       name,
		Table:      table,
		Columns:    columns,
		IndexType:  IndexTypeGIN,
		Concurrent: true,
	}, logger)
}

// CreateGISTIndex creates a GIST index for spatial or full-text search
func CreateGISTIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns []string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:       name,
		Table:      table,
		Columns:    columns,
		IndexType:  IndexTypeGIST,
		Concurrent: true,
	}, logger)
}

// CreateBRINIndex creates a BRIN index for large sorted tables
func CreateBRINIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns []string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:       name,
		Table:      table,
		Columns:    columns,
		IndexType:  IndexTypeBRIN,
		Concurrent: true,
	}, logger)
}

// CreateExpressionIndex creates an index on an expression (e.g., LOWER(email))
func CreateExpressionIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, expression string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:        name,
		Table:       table,
		Expressions: []string{expression},
		IndexType:   IndexTypeBTree,
		Concurrent:  true,
	}, logger)
}

// CreatePartialCoveringIndex creates a partial covering index
func CreatePartialCoveringIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns, covering []string, whereClause string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:       name,
		Table:      table,
		Columns:    columns,
		IndexType:  IndexTypeBTree,
		Covering:   covering,
		Partial:    whereClause,
		Concurrent: true,
	}, logger)
}

// CreateSpatialIndex creates a spatial index using GIST for point/geometry columns
func CreateSpatialIndex(ctx context.Context, pool *pgxpool.Pool, name, table, column string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:       name,
		Table:      table,
		Columns:    []string{column},
		IndexType:  IndexTypeGIST,
		Concurrent: true,
	}, logger)
}

// CreateTextSearchIndex creates a full-text search index using GIN
func CreateTextSearchIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, tsvectorColumn string, logger *zap.Logger) error {
	return CreateAdvancedIndex(ctx, pool, AdvancedIndexDefinition{
		Name:       name,
		Table:      table,
		Columns:    []string{tsvectorColumn},
		IndexType:  IndexTypeGIN,
		Concurrent: true,
	}, logger)
}

// AnalyzeIndexUsage analyzes index usage statistics
func AnalyzeIndexUsage(ctx context.Context, pool *pgxpool.Pool, schema string, logger *zap.Logger) ([]IndexUsageStats, error) {
	query := `
		SELECT
			schemaname,
			tablename,
			indexname,
			idx_scan as index_scans,
			idx_tup_read as tuples_read,
			idx_tup_fetch as tuples_fetched
		FROM pg_stat_user_indexes
		WHERE schemaname = $1
		ORDER BY idx_scan DESC
	`

	rows, err := pool.Query(ctx, query, schema)
	if err != nil {
		return nil, errors.Wrap(err, "failed to query index usage")
	}
	defer rows.Close()

	var stats []IndexUsageStats
	for rows.Next() {
		var stat IndexUsageStats
		if err := rows.Scan(
			&stat.Schema,
			&stat.Table,
			&stat.IndexName,
			&stat.IndexScans,
			&stat.TuplesRead,
			&stat.TuplesFetched,
		); err != nil {
			return nil, errors.Wrap(err, "failed to scan index usage stats")
		}
		stats = append(stats, stat)
	}

	return stats, nil
}

// IndexUsageStats represents index usage statistics
type IndexUsageStats struct {
	Schema        string
	Table         string
	IndexName     string
	IndexScans    int64
	TuplesRead    int64
	TuplesFetched int64
}

// FindUnusedIndexes finds indexes that are never used
func FindUnusedIndexes(ctx context.Context, pool *pgxpool.Pool, schema string, logger *zap.Logger) ([]string, error) {
	query := `
		SELECT indexname
		FROM pg_stat_user_indexes
		WHERE schemaname = $1
		AND idx_scan = 0
		AND indexname NOT LIKE 'pg_toast%'
		ORDER BY pg_relation_size(indexrelid) DESC
	`

	rows, err := pool.Query(ctx, query, schema)
	if err != nil {
		return nil, errors.Wrap(err, "failed to query unused indexes")
	}
	defer rows.Close()

	var unused []string
	for rows.Next() {
		var indexName string
		if err := rows.Scan(&indexName); err != nil {
			return nil, errors.Wrap(err, "failed to scan unused index")
		}
		unused = append(unused, indexName)
	}

	return unused, nil
}

// GetIndexSize returns the size of an index
func GetIndexSize(ctx context.Context, pool *pgxpool.Pool, indexName string) (int64, error) {
	query := `SELECT pg_relation_size($1)`
	var size int64
	err := pool.QueryRow(ctx, query, indexName).Scan(&size)
	if err != nil {
		return 0, errors.Wrapf(err, "failed to get size for index %s", indexName)
	}
	return size, nil
}

// ReindexIndex rebuilds an index
func ReindexIndex(ctx context.Context, pool *pgxpool.Pool, indexName string, concurrent bool, logger *zap.Logger) error {
	var query string
	if concurrent {
		query = fmt.Sprintf("REINDEX INDEX CONCURRENTLY %s", indexName)
	} else {
		query = fmt.Sprintf("REINDEX INDEX %s", indexName)
	}

	if _, err := pool.Exec(ctx, query); err != nil {
		return errors.Wrapf(err, "failed to reindex %s", indexName)
	}

	logger.Info("Index reindexed", zap.String("index", indexName))
	return nil
}

// Partitioning Functions for MMO Workloads
// Issue: #1949 - Time-based partitioning and composite keys optimization

// PartitionDefinition represents a table partition
type PartitionDefinition struct {
	TableName    string
	PartitionName string
	PartitionType string // RANGE, LIST, HASH
	PartitionKey  string
	FromValue     string
	ToValue       string
}

// CreateTimeBasedPartition creates a time-based partition for a table
func CreateTimeBasedPartition(ctx context.Context, pool *pgxpool.Pool, partition PartitionDefinition, logger *zap.Logger) error {
	var query string

	switch partition.PartitionType {
	case "RANGE":
		query = fmt.Sprintf(`
			CREATE TABLE IF NOT EXISTS %s PARTITION OF %s
			FOR VALUES FROM ('%s') TO ('%s')`,
			partition.PartitionName, partition.TableName,
			partition.FromValue, partition.ToValue)
	case "LIST":
		query = fmt.Sprintf(`
			CREATE TABLE IF NOT EXISTS %s PARTITION OF %s
			FOR VALUES IN (%s)`,
			partition.PartitionName, partition.TableName, partition.PartitionKey)
	default:
		return fmt.Errorf("unsupported partition type: %s", partition.PartitionType)
	}

	if _, err := pool.Exec(ctx, query); err != nil {
		return fmt.Errorf("failed to create partition %s: %w", partition.PartitionName, err)
	}

	logger.Info("Time-based partition created",
		zap.String("table", partition.TableName),
		zap.String("partition", partition.PartitionName),
		zap.String("type", partition.PartitionType))

	return nil
}

// DetachPartition detaches a partition from its parent table
func DetachPartition(ctx context.Context, pool *pgxpool.Pool, tableName, partitionName string, logger *zap.Logger) error {
	query := fmt.Sprintf("ALTER TABLE %s DETACH PARTITION %s", tableName, partitionName)

	if _, err := pool.Exec(ctx, query); err != nil {
		return fmt.Errorf("failed to detach partition %s: %w", partitionName, err)
	}

	logger.Info("Partition detached",
		zap.String("table", tableName),
		zap.String("partition", partitionName))

	return nil
}

// AttachPartition attaches a partition to its parent table
func AttachPartition(ctx context.Context, pool *pgxpool.Pool, partition PartitionDefinition, logger *zap.Logger) error {
	var query string

	switch partition.PartitionType {
	case "RANGE":
		query = fmt.Sprintf(`
			ALTER TABLE %s ATTACH PARTITION %s
			FOR VALUES FROM ('%s') TO ('%s')`,
			partition.TableName, partition.PartitionName,
			partition.FromValue, partition.ToValue)
	case "LIST":
		query = fmt.Sprintf(`
			ALTER TABLE %s ATTACH PARTITION %s
			FOR VALUES IN (%s)`,
			partition.TableName, partition.PartitionName, partition.PartitionKey)
	default:
		return fmt.Errorf("unsupported partition type: %s", partition.PartitionType)
	}

	if _, err := pool.Exec(ctx, query); err != nil {
		return fmt.Errorf("failed to attach partition %s: %w", partition.PartitionName, err)
	}

	logger.Info("Partition attached",
		zap.String("table", partition.TableName),
		zap.String("partition", partition.PartitionName))

	return nil
}

// CreateCompositeIndexForMMO creates optimized composite indexes for MMO workloads
func CreateCompositeIndexForMMO(ctx context.Context, pool *pgxpool.Pool, tableName string, logger *zap.Logger) error {
	// Common MMO composite index patterns
	compositeIndexes := []AdvancedIndexDefinition{
		{
			Name:       fmt.Sprintf("idx_%s_player_time", tableName),
			Table:      tableName,
			Columns:    []string{"player_id", "created_at"},
			IndexType:  IndexTypeBTree,
			Concurrent: true,
			FillFactor: 90,
		},
		{
			Name:       fmt.Sprintf("idx_%s_region_time", tableName),
			Table:      tableName,
			Columns:    []string{"region_id", "created_at"},
			IndexType:  IndexTypeBTree,
			Concurrent: true,
			FillFactor: 90,
		},
		{
			Name:       fmt.Sprintf("idx_%s_status_time", tableName),
			Table:      tableName,
			Columns:    []string{"status", "created_at"},
			IndexType:  IndexTypeBTree,
			Concurrent: true,
			Where:      "status IN ('active', 'pending', 'completed')",
		},
	}

	for _, index := range compositeIndexes {
		if err := CreateAdvancedIndex(ctx, pool, index, logger); err != nil {
			logger.Warn("Failed to create composite index",
				zap.String("index", index.Name),
				zap.Error(err))
			// Continue with other indexes
		}
	}

	logger.Info("MMO composite indexes created for table", zap.String("table", tableName))
	return nil
}

// OptimizeTableForMMO applies MMO-specific optimizations to a table
func OptimizeTableForMMO(ctx context.Context, pool *pgxpool.Pool, tableName string, logger *zap.Logger) error {
	// Set table optimization parameters
	optimizations := []string{
		fmt.Sprintf("ALTER TABLE %s SET (autovacuum_vacuum_scale_factor = 0.02)", tableName),
		fmt.Sprintf("ALTER TABLE %s SET (autovacuum_analyze_scale_factor = 0.01)", tableName),
		fmt.Sprintf("ALTER TABLE %s SET (autovacuum_vacuum_threshold = 50)", tableName),
		fmt.Sprintf("ALTER TABLE %s SET (autovacuum_analyze_threshold = 25)", tableName),
		fmt.Sprintf("ALTER TABLE %s SET (fillfactor = 90)", tableName),
	}

	for _, query := range optimizations {
		if _, err := pool.Exec(ctx, query); err != nil {
			logger.Warn("Failed to apply table optimization",
				zap.String("query", query),
				zap.Error(err))
			// Continue with other optimizations
		}
	}

	// Create composite indexes for MMO patterns
	if err := CreateCompositeIndexForMMO(ctx, pool, tableName, logger); err != nil {
		logger.Warn("Failed to create MMO composite indexes",
			zap.String("table", tableName),
			zap.Error(err))
	}

	logger.Info("MMO optimizations applied to table", zap.String("table", tableName))
	return nil
}
