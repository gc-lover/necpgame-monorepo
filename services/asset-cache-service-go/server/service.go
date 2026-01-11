package server

import (
	"context"
	"crypto/md5"
	"fmt"
	"io"
	"log/slog"
	"os"
	"path/filepath"
	"time"

	"github.com/google/uuid"
)

// AssetCacheService provides high-performance asset caching with memory mapping
type AssetCacheService struct {
	repo          Repository
	cacheManager  *MemoryMappedCacheManager
	cacheDir      string
	maxFileSize   int64
}

// NewAssetCacheService creates a new asset cache service
func NewAssetCacheService(repo Repository, cacheManager *MemoryMappedCacheManager) *AssetCacheService {
	return &AssetCacheService{
		repo:         repo,
		cacheManager: cacheManager,
		cacheDir:     cacheManager.cacheDir,
		maxFileSize:  int64(cacheManager.config.MaxFileSizeMB) * 1024 * 1024,
	}
}

// LoadAsset loads an asset from cache or filesystem
func (s *AssetCacheService) LoadAsset(ctx context.Context, assetID string) ([]byte, error) {
	start := time.Now()

	// Try to get asset metadata
	assetUUID, err := uuid.Parse(assetID)
	if err != nil {
		return nil, fmt.Errorf("invalid asset ID: %w", err)
	}

	metadata, err := s.repo.GetAssetMetadata(ctx, assetUUID)
	if err != nil {
		// Asset not in cache, need to load from filesystem
		return s.loadAssetFromFilesystem(ctx, assetID)
	}

	// Check if file still exists
	if _, err := os.Stat(metadata.FilePath); os.IsNotExist(err) {
		// File was deleted, remove from cache
		s.repo.DeleteAssetMetadata(ctx, assetUUID)
		return s.loadAssetFromFilesystem(ctx, assetID)
	}

	// Try to load from memory-mapped cache
	data, err := s.cacheManager.LoadFile(metadata.FilePath)
	if err != nil {
		slog.Warn("Failed to load from memory cache, falling back to file read",
			"asset_id", assetID, "error", err)
		return s.loadAssetDirectly(metadata.FilePath)
	}

	// Update access time
	s.repo.UpdateAssetAccessTime(ctx, assetUUID)

	loadTime := time.Since(start)
	slog.Info("Asset loaded from memory cache",
		"asset_id", assetID,
		"file_size", len(data),
		"load_time_ms", loadTime.Milliseconds())

	return data, nil
}

// StoreAsset stores an asset in the cache
func (s *AssetCacheService) StoreAsset(ctx context.Context, assetID string, data []byte, contentType string) error {
	start := time.Now()

	// Generate checksum
	checksum := fmt.Sprintf("%x", md5.Sum(data))

	// Determine file path in cache
	fileName := fmt.Sprintf("%s_%s", assetID, checksum[:8])
	filePath := filepath.Join(s.cacheDir, fileName)

	// Check if file size exceeds limit
	if int64(len(data)) > s.maxFileSize {
		return fmt.Errorf("asset too large for caching: %d bytes > %d bytes", len(data), s.maxFileSize)
	}

	// Write to cache file
	if err := os.WriteFile(filePath, data, 0644); err != nil {
		return fmt.Errorf("failed to write asset to cache: %w", err)
	}

	// Load into memory-mapped cache
	if _, err := s.cacheManager.LoadFile(filePath); err != nil {
		slog.Warn("Failed to memory map asset file", "asset_id", assetID, "error", err)
		// Continue anyway, file is still cached on disk
	}

	// Store metadata
	metadata := &AssetMetadata{
		ID:             uuid.New(),
		AssetID:        assetID,
		FilePath:       filePath,
		FileSize:       int64(len(data)),
		ContentType:    contentType,
		Checksum:       checksum,
		LastAccessedAt: time.Now(),
		CreatedAt:      time.Now(),
		AccessCount:    1,
		CachePriority:  s.calculatePriority(contentType, len(data)),
		IsPreloaded:    false,
	}

	if err := s.repo.StoreAssetMetadata(ctx, metadata); err != nil {
		// Clean up file if metadata storage fails
		os.Remove(filePath)
		return fmt.Errorf("failed to store asset metadata: %w", err)
	}

	storeTime := time.Since(start)
	slog.Info("Asset stored in cache",
		"asset_id", assetID,
		"file_size", len(data),
		"store_time_ms", storeTime.Milliseconds())

	return nil
}

// DeleteAsset removes an asset from cache
func (s *AssetCacheService) DeleteAsset(ctx context.Context, assetID string) error {
	assetUUID, err := uuid.Parse(assetID)
	if err != nil {
		return fmt.Errorf("invalid asset ID: %w", err)
	}

	metadata, err := s.repo.GetAssetMetadata(ctx, assetUUID)
	if err != nil {
		return fmt.Errorf("asset not found: %w", err)
	}

	// Remove from memory cache
	if err := s.cacheManager.UnloadFile(metadata.FilePath); err != nil {
		slog.Warn("Failed to unload from memory cache", "asset_id", assetID, "error", err)
	}

	// Remove file
	if err := os.Remove(metadata.FilePath); err != nil && !os.IsNotExist(err) {
		slog.Warn("Failed to remove cache file", "file_path", metadata.FilePath, "error", err)
	}

	// Remove metadata
	if err := s.repo.DeleteAssetMetadata(ctx, assetUUID); err != nil {
		return fmt.Errorf("failed to delete asset metadata: %w", err)
	}

	slog.Info("Asset deleted from cache", "asset_id", assetID)
	return nil
}

// GetCacheStats returns cache performance statistics
func (s *AssetCacheService) GetCacheStats(ctx context.Context) (*CacheStatsResponse, error) {
	stats := s.cacheManager.GetStats()

	dbStats, err := s.repo.GetCacheStats(ctx)
	if err != nil {
		slog.Warn("Failed to get database stats", "error", err)
		dbStats = &CacheStatistics{}
	}

	return &CacheStatsResponse{
		MemoryStats: stats,
		DatabaseStats: CacheDatabaseStats{
			TotalAssets:         dbStats.TotalAssets,
			TotalSizeBytes:      dbStats.TotalSizeBytes,
			CacheHits:           dbStats.CacheHits,
			CacheMisses:         dbStats.CacheMisses,
			AverageAccessTimeMs: dbStats.AverageAccessTimeMs,
		},
	}, nil
}

// PreloadAssets preloads frequently accessed assets into memory
func (s *AssetCacheService) PreloadAssets(ctx context.Context, assetIDs []string) error {
	slog.Info("Starting asset preload", "count", len(assetIDs))

	for _, assetID := range assetIDs {
		if err := s.preloadAsset(ctx, assetID); err != nil {
			slog.Warn("Failed to preload asset", "asset_id", assetID, "error", err)
			continue
		}
	}

	slog.Info("Asset preload completed", "count", len(assetIDs))
	return nil
}

// CleanupExpiredAssets removes assets that haven't been accessed for a long time
func (s *AssetCacheService) CleanupExpiredAssets(ctx context.Context, maxAge time.Duration) error {
	slog.Info("Starting expired asset cleanup", "max_age", maxAge)

	expiredAssets, err := s.repo.GetExpiredAssets(ctx, maxAge)
	if err != nil {
		return fmt.Errorf("failed to get expired assets: %w", err)
	}

	cleanedCount := 0
	for _, assetID := range expiredAssets {
		if err := s.DeleteAsset(ctx, assetID.String()); err != nil {
			slog.Warn("Failed to delete expired asset", "asset_id", assetID, "error", err)
			continue
		}
		cleanedCount++
	}

	slog.Info("Expired asset cleanup completed", "cleaned_count", cleanedCount)
	return nil
}

// loadAssetFromFilesystem loads an asset from the filesystem (not cached yet)
func (s *AssetCacheService) loadAssetFromFilesystem(ctx context.Context, assetID string) ([]byte, error) {
	// This would typically search for the asset in configured asset directories
	// For now, assume assets are in a specific directory structure
	assetPath := s.findAssetPath(assetID)
	if assetPath == "" {
		return nil, fmt.Errorf("asset not found: %s", assetID)
	}

	data, err := s.loadAssetDirectly(assetPath)
	if err != nil {
		return nil, err
	}

	// Cache the asset for future use
	contentType := s.detectContentType(assetPath)
	if err := s.StoreAsset(ctx, assetID, data, contentType); err != nil {
		slog.Warn("Failed to cache asset after loading", "asset_id", assetID, "error", err)
		// Continue anyway, asset is loaded
	}

	return data, nil
}

// loadAssetDirectly loads an asset directly from file (bypassing cache)
func (s *AssetCacheService) loadAssetDirectly(filePath string) ([]byte, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return nil, fmt.Errorf("failed to open asset file: %w", err)
	}
	defer file.Close()

	data, err := io.ReadAll(file)
	if err != nil {
		return nil, fmt.Errorf("failed to read asset file: %w", err)
	}

	return data, nil
}

// preloadAsset loads an asset into memory cache for faster access
func (s *AssetCacheService) preloadAsset(ctx context.Context, assetID string) error {
	_, err := s.LoadAsset(ctx, assetID)
	return err
}

// findAssetPath finds the filesystem path for an asset
func (s *AssetCacheService) findAssetPath(assetID string) string {
	// This is a simplified implementation
	// In production, this would search through configured asset directories
	possiblePaths := []string{
		filepath.Join("assets", assetID),
		filepath.Join("assets", "textures", assetID),
		filepath.Join("assets", "models", assetID),
		filepath.Join("assets", "audio", assetID),
	}

	for _, path := range possiblePaths {
		if _, err := os.Stat(path); err == nil {
			return path
		}
	}

	return ""
}

// detectContentType detects the content type of an asset
func (s *AssetCacheService) detectContentType(filePath string) string {
	ext := filepath.Ext(filePath)
	switch ext {
	case ".png", ".jpg", ".jpeg", ".gif", ".webp":
		return "image/" + ext[1:]
	case ".obj", ".fbx", ".gltf", ".glb":
		return "model/" + ext[1:]
	case ".wav", ".mp3", ".ogg":
		return "audio/" + ext[1:]
	case ".mp4", ".webm":
		return "video/" + ext[1:]
	default:
		return "application/octet-stream"
	}
}

// calculatePriority calculates cache priority based on content type and size
func (s *AssetCacheService) calculatePriority(contentType string, size int) int {
	// Higher priority for smaller, frequently accessed assets
	basePriority := 3

	// Boost priority for textures and UI assets
	if contentType == "image/png" || contentType == "image/jpeg" {
		basePriority += 2
	}

	// Reduce priority for large files
	if size > 1024*1024 { // > 1MB
		basePriority--
	}
	if size > 10*1024*1024 { // > 10MB
		basePriority--
	}

	// Ensure priority is within bounds
	if basePriority < 1 {
		basePriority = 1
	}
	if basePriority > 5 {
		basePriority = 5
	}

	return basePriority
}

// CacheStatsResponse represents cache statistics response
type CacheStatsResponse struct {
	MemoryStats   CacheStats         `json:"memory_stats"`
	DatabaseStats CacheDatabaseStats `json:"database_stats"`
}

// CacheDatabaseStats represents database-level cache statistics
type CacheDatabaseStats struct {
	TotalAssets         int64   `json:"total_assets"`
	TotalSizeBytes      int64   `json:"total_size_bytes"`
	CacheHits           int64   `json:"cache_hits"`
	CacheMisses         int64   `json:"cache_misses"`
	AverageAccessTimeMs float64 `json:"average_access_time_ms"`
}