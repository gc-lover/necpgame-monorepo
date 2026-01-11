package server

import (
	"context"
	"database/sql"
	"fmt"
	"log/slog"
	"time"

	"github.com/google/uuid"
)

// Repository handles database operations for asset cache
type Repository interface {
	// Asset metadata operations
	StoreAssetMetadata(ctx context.Context, metadata *AssetMetadata) error
	GetAssetMetadata(ctx context.Context, assetID uuid.UUID) (*AssetMetadata, error)
	UpdateAssetAccessTime(ctx context.Context, assetID uuid.UUID) error
	DeleteAssetMetadata(ctx context.Context, assetID uuid.UUID) error

	// Cache statistics
	GetCacheStats(ctx context.Context) (*CacheStatistics, error)
	UpdateCacheStats(ctx context.Context, stats *CacheStatistics) error

	// Bulk operations for performance
	GetExpiredAssets(ctx context.Context, maxAge time.Duration) ([]uuid.UUID, error)
}

// PostgresRepository implements Repository for PostgreSQL
type PostgresRepository struct {
	db *sql.DB
}

// AssetMetadata represents asset metadata stored in database
type AssetMetadata struct {
	ID               uuid.UUID `db:"id"`
	AssetID          string    `db:"asset_id"`
	FilePath         string    `db:"file_path"`
	FileSize         int64     `db:"file_size"`
	ContentType      string    `db:"content_type"`
	Checksum         string    `db:"checksum"`
	CompressionType  string    `db:"compression_type"`
	OriginalSize     int64     `db:"original_size"`
	CompressedSize   int64     `db:"compressed_size"`
	LastAccessedAt   time.Time `db:"last_accessed_at"`
	CreatedAt        time.Time `db:"created_at"`
	AccessCount      int64     `db:"access_count"`
	CachePriority    int       `db:"cache_priority"`
	IsPreloaded      bool      `db:"is_preloaded"`
}

// CacheStatistics represents cache performance statistics
type CacheStatistics struct {
	ID                    uuid.UUID `db:"id"`
	TotalAssets           int64     `db:"total_assets"`
	TotalSizeBytes        int64     `db:"total_size_bytes"`
	CacheHits             int64     `db:"cache_hits"`
	CacheMisses           int64     `db:"cache_misses"`
	MemoryUsedBytes       int64     `db:"memory_used_bytes"`
	MaxMemoryBytes        int64     `db:"max_memory_bytes"`
	AverageAccessTimeMs   float64   `db:"average_access_time_ms"`
	LastUpdatedAt         time.Time `db:"last_updated_at"`
}

// NewPostgresRepository creates a new PostgreSQL repository
func NewPostgresRepository(db *sql.DB) Repository {
	return &PostgresRepository{db: db}
}

// StoreAssetMetadata stores asset metadata in database
func (r *PostgresRepository) StoreAssetMetadata(ctx context.Context, metadata *AssetMetadata) error {
	query := `
		INSERT INTO asset_cache.asset_metadata (
			id, asset_id, file_path, file_size, content_type, checksum,
			compression_type, original_size, compressed_size, last_accessed_at,
			created_at, access_count, cache_priority, is_preloaded
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)
		ON CONFLICT (asset_id) DO UPDATE SET
			file_path = EXCLUDED.file_path,
			file_size = EXCLUDED.file_size,
			content_type = EXCLUDED.content_type,
			checksum = EXCLUDED.checksum,
			compression_type = EXCLUDED.compression_type,
			original_size = EXCLUDED.original_size,
			compressed_size = EXCLUDED.compressed_size,
			last_accessed_at = EXCLUDED.last_accessed_at,
			access_count = asset_cache.asset_metadata.access_count + 1,
			cache_priority = EXCLUDED.cache_priority,
			is_preloaded = EXCLUDED.is_preloaded
	`

	_, err := r.db.ExecContext(ctx, query,
		metadata.ID, metadata.AssetID, metadata.FilePath, metadata.FileSize,
		metadata.ContentType, metadata.Checksum, metadata.CompressionType,
		metadata.OriginalSize, metadata.CompressedSize, metadata.LastAccessedAt,
		metadata.CreatedAt, metadata.AccessCount, metadata.CachePriority, metadata.IsPreloaded,
	)

	if err != nil {
		slog.Error("Failed to store asset metadata", "asset_id", metadata.AssetID, "error", err)
		return fmt.Errorf("failed to store asset metadata: %w", err)
	}

	slog.Debug("Asset metadata stored", "asset_id", metadata.AssetID, "file_size", metadata.FileSize)
	return nil
}

// GetAssetMetadata retrieves asset metadata from database
func (r *PostgresRepository) GetAssetMetadata(ctx context.Context, assetID uuid.UUID) (*AssetMetadata, error) {
	query := `
		SELECT id, asset_id, file_path, file_size, content_type, checksum,
			   compression_type, original_size, compressed_size, last_accessed_at,
			   created_at, access_count, cache_priority, is_preloaded
		FROM asset_cache.asset_metadata
		WHERE id = $1
	`

	var metadata AssetMetadata
	err := r.db.QueryRowContext(ctx, query, assetID).Scan(
		&metadata.ID, &metadata.AssetID, &metadata.FilePath, &metadata.FileSize,
		&metadata.ContentType, &metadata.Checksum, &metadata.CompressionType,
		&metadata.OriginalSize, &metadata.CompressedSize, &metadata.LastAccessedAt,
		&metadata.CreatedAt, &metadata.AccessCount, &metadata.CachePriority, &metadata.IsPreloaded,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("asset metadata not found: %s", assetID)
		}
		slog.Error("Failed to get asset metadata", "asset_id", assetID, "error", err)
		return nil, fmt.Errorf("failed to get asset metadata: %w", err)
	}

	return &metadata, nil
}

// UpdateAssetAccessTime updates the last accessed time for an asset
func (r *PostgresRepository) UpdateAssetAccessTime(ctx context.Context, assetID uuid.UUID) error {
	query := `
		UPDATE asset_cache.asset_metadata
		SET last_accessed_at = NOW(), access_count = access_count + 1
		WHERE id = $1
	`

	result, err := r.db.ExecContext(ctx, query, assetID)
	if err != nil {
		slog.Error("Failed to update asset access time", "asset_id", assetID, "error", err)
		return fmt.Errorf("failed to update asset access time: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("asset not found: %s", assetID)
	}

	return nil
}

// DeleteAssetMetadata removes asset metadata from database
func (r *PostgresRepository) DeleteAssetMetadata(ctx context.Context, assetID uuid.UUID) error {
	query := `DELETE FROM asset_cache.asset_metadata WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, assetID)
	if err != nil {
		slog.Error("Failed to delete asset metadata", "asset_id", assetID, "error", err)
		return fmt.Errorf("failed to delete asset metadata: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("asset not found: %s", assetID)
	}

	slog.Info("Asset metadata deleted", "asset_id", assetID)
	return nil
}

// GetCacheStats retrieves cache statistics
func (r *PostgresRepository) GetCacheStats(ctx context.Context) (*CacheStatistics, error) {
	query := `
		SELECT id, total_assets, total_size_bytes, cache_hits, cache_misses,
			   memory_used_bytes, max_memory_bytes, average_access_time_ms, last_updated_at
		FROM asset_cache.cache_statistics
		WHERE id = (SELECT id FROM asset_cache.cache_statistics ORDER BY last_updated_at DESC LIMIT 1)
	`

	var stats CacheStatistics
	err := r.db.QueryRowContext(ctx, query).Scan(
		&stats.ID, &stats.TotalAssets, &stats.TotalSizeBytes, &stats.CacheHits,
		&stats.CacheMisses, &stats.MemoryUsedBytes, &stats.MaxMemoryBytes,
		&stats.AverageAccessTimeMs, &stats.LastUpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Return default stats if no record exists
			return &CacheStatistics{
				ID:                uuid.New(),
				LastUpdatedAt:     time.Now(),
			}, nil
		}
		slog.Error("Failed to get cache stats", "error", err)
		return nil, fmt.Errorf("failed to get cache stats: %w", err)
	}

	return &stats, nil
}

// UpdateCacheStats updates cache statistics
func (r *PostgresRepository) UpdateCacheStats(ctx context.Context, stats *CacheStatistics) error {
	query := `
		INSERT INTO asset_cache.cache_statistics (
			id, total_assets, total_size_bytes, cache_hits, cache_misses,
			memory_used_bytes, max_memory_bytes, average_access_time_ms, last_updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (id) DO UPDATE SET
			total_assets = EXCLUDED.total_assets,
			total_size_bytes = EXCLUDED.total_size_bytes,
			cache_hits = EXCLUDED.cache_hits,
			cache_misses = EXCLUDED.cache_misses,
			memory_used_bytes = EXCLUDED.memory_used_bytes,
			max_memory_bytes = EXCLUDED.max_memory_bytes,
			average_access_time_ms = EXCLUDED.average_access_time_ms,
			last_updated_at = EXCLUDED.last_updated_at
	`

	_, err := r.db.ExecContext(ctx, query,
		stats.ID, stats.TotalAssets, stats.TotalSizeBytes, stats.CacheHits,
		stats.CacheMisses, stats.MemoryUsedBytes, stats.MaxMemoryBytes,
		stats.AverageAccessTimeMs, stats.LastUpdatedAt,
	)

	if err != nil {
		slog.Error("Failed to update cache stats", "error", err)
		return fmt.Errorf("failed to update cache stats: %w", err)
	}

	return nil
}

// GetExpiredAssets finds assets that haven't been accessed for the specified duration
func (r *PostgresRepository) GetExpiredAssets(ctx context.Context, maxAge time.Duration) ([]uuid.UUID, error) {
	query := `
		SELECT id FROM asset_cache.asset_metadata
		WHERE last_accessed_at < $1 AND cache_priority < 5
		ORDER BY last_accessed_at ASC
		LIMIT 100
	`

	cutoffTime := time.Now().Add(-maxAge)
	rows, err := r.db.QueryContext(ctx, query, cutoffTime)
	if err != nil {
		slog.Error("Failed to get expired assets", "error", err)
		return nil, fmt.Errorf("failed to get expired assets: %w", err)
	}
	defer rows.Close()

	var assetIDs []uuid.UUID
	for rows.Next() {
		var assetID uuid.UUID
		if err := rows.Scan(&assetID); err != nil {
			slog.Error("Failed to scan asset ID", "error", err)
			continue
		}
		assetIDs = append(assetIDs, assetID)
	}

	return assetIDs, nil
}