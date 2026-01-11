// Database Index Management Library
// Issue: #2145
// PERFORMANCE: Index optimization, composite indexes, partial indexes

package database

import (
	"context"
	"fmt"
	"strings"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// IndexDefinition represents a database index
type IndexDefinition struct {
	Name       string
	Table      string
	Columns    []string
	Unique     bool
	Partial    string // WHERE clause for partial index
	Concurrent bool   // Use CREATE INDEX CONCURRENTLY
}

// CreateIndex creates a database index
func CreateIndex(ctx context.Context, pool *pgxpool.Pool, index IndexDefinition, logger *zap.Logger) error {
	var builder strings.Builder

	if index.Concurrent {
		builder.WriteString("CREATE INDEX CONCURRENTLY ")
	} else {
		builder.WriteString("CREATE ")
		if index.Unique {
			builder.WriteString("UNIQUE ")
		}
		builder.WriteString("INDEX ")
	}

	builder.WriteString(fmt.Sprintf("IF NOT EXISTS %s ON %s (", index.Name, index.Table))
	builder.WriteString(strings.Join(index.Columns, ", "))
	builder.WriteString(")")

	if index.Partial != "" {
		builder.WriteString(fmt.Sprintf(" WHERE %s", index.Partial))
	}

	query := builder.String()

	if _, err := pool.Exec(ctx, query); err != nil {
		return fmt.Errorf("failed to create index %s: %w", index.Name, err)
	}

	logger.Info("Index created",
		zap.String("name", index.Name),
		zap.String("table", index.Table),
		zap.Strings("columns", index.Columns))

	return nil
}

// DropIndex drops a database index
func DropIndex(ctx context.Context, pool *pgxpool.Pool, indexName string, concurrent bool, logger *zap.Logger) error {
	var query string
	if concurrent {
		query = fmt.Sprintf("DROP INDEX CONCURRENTLY IF EXISTS %s", indexName)
	} else {
		query = fmt.Sprintf("DROP INDEX IF EXISTS %s", indexName)
	}

	if _, err := pool.Exec(ctx, query); err != nil {
		return fmt.Errorf("failed to drop index %s: %w", indexName, err)
	}

	logger.Info("Index dropped", zap.String("name", indexName))
	return nil
}

// AnalyzeTable runs ANALYZE on a table to update statistics
func AnalyzeTable(ctx context.Context, pool *pgxpool.Pool, tableName string, logger *zap.Logger) error {
	query := fmt.Sprintf("ANALYZE %s", tableName)
	if _, err := pool.Exec(ctx, query); err != nil {
		return fmt.Errorf("failed to analyze table %s: %w", tableName, err)
	}

	logger.Info("Table analyzed", zap.String("table", tableName))
	return nil
}

// VacuumTable runs VACUUM on a table
func VacuumTable(ctx context.Context, pool *pgxpool.Pool, tableName string, analyze bool, logger *zap.Logger) error {
	var query string
	if analyze {
		query = fmt.Sprintf("VACUUM ANALYZE %s", tableName)
	} else {
		query = fmt.Sprintf("VACUUM %s", tableName)
	}

	if _, err := pool.Exec(ctx, query); err != nil {
		return fmt.Errorf("failed to vacuum table %s: %w", tableName, err)
	}

	logger.Info("Table vacuumed", zap.String("table", tableName), zap.Bool("analyze", analyze))
	return nil
}

// Common index patterns

// CreateCompositeIndex creates a composite index
func CreateCompositeIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns []string, logger *zap.Logger) error {
	return CreateIndex(ctx, pool, IndexDefinition{
		Name:    name,
		Table:   table,
		Columns: columns,
	}, logger)
}

// CreatePartialIndex creates a partial index with WHERE clause
func CreatePartialIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns []string, whereClause string, logger *zap.Logger) error {
	return CreateIndex(ctx, pool, IndexDefinition{
		Name:    name,
		Table:   table,
		Columns: columns,
		Partial: whereClause,
	}, logger)
}

// CreateUniqueIndex creates a unique index
func CreateUniqueIndex(ctx context.Context, pool *pgxpool.Pool, name, table string, columns []string, logger *zap.Logger) error {
	return CreateIndex(ctx, pool, IndexDefinition{
		Name:    name,
		Table:   table,
		Columns: columns,
		Unique:  true,
	}, logger)
}
