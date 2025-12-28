// Package cdn provides Content Delivery Network for MMOFPS game assets
package cdn

import (
	"context"
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"io"
	"net/http"
	"os"
	"path/filepath"
	"strings"
	"sync"
	"time"

	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// CDNManager manages content delivery for game assets
type CDNManager struct {
	config       *CDNConfig
	logger       *errorhandling.Logger
	assets       map[string]*AssetInfo
	regions      map[string]*CDNRegion
	mu           sync.RWMutex

	// Performance tracking
	totalRequests    int64
	cacheHits        int64
	cacheMisses      int64
	bytesServed      int64

	// Background processes
	shutdownChan chan struct{}
	wg           sync.WaitGroup
}

// CDNConfig holds CDN configuration
type CDNConfig struct {
	AssetRoot         string            `json:"asset_root"`
	CacheTTL          time.Duration     `json:"cache_ttl"`
	MaxAssetSize      int64             `json:"max_asset_size"`
	SupportedFormats  []string          `json:"supported_formats"`
	CompressionTypes  []string          `json:"compression_types"`
	Regions           map[string]string `json:"regions"` // region -> endpoint
	EnableEdgeCaching bool              `json:"enable_edge_caching"`
	PrefetchEnabled   bool              `json:"prefetch_enabled"`
}

// AssetInfo contains metadata about a game asset
type AssetInfo struct {
	Path         string            `json:"path"`
	Hash         string            `json:"hash"`
	Size         int64             `json:"size"`
	ContentType  string            `json:"content_type"`
	LastModified time.Time         `json:"last_modified"`
	Version      string            `json:"version"`
	Regions      []string          `json:"regions"` // regions where asset is cached
	AccessCount  int64             `json:"access_count"`
	LastAccessed time.Time         `json:"last_accessed"`
	Tags         map[string]string `json:"tags,omitempty"`
}

// CDNRegion represents a CDN region/edge location
type CDNRegion struct {
	ID       string    `json:"id"`
	Endpoint string    `json:"endpoint"`
	Status   RegionStatus `json:"status"`
	LastSync time.Time `json:"last_sync"`
	Assets   map[string]bool `json:"assets"` // asset hash -> exists
}

// RegionStatus represents the status of a CDN region
type RegionStatus string

const (
	RegionStatusActive    RegionStatus = "active"
	RegionStatusInactive  RegionStatus = "inactive"
	RegionStatusSyncing   RegionStatus = "syncing"
	RegionStatusError     RegionStatus = "error"
)

// AssetRequest represents a request for an asset
type AssetRequest struct {
	Path           string            `json:"path"`
	Version        string            `json:"version,omitempty"`
	ClientRegion   string            `json:"client_region,omitempty"`
	AcceptEncoding string            `json:"accept_encoding,omitempty"`
	UserAgent      string            `json:"user_agent,omitempty"`
	Headers        map[string]string `json:"headers,omitempty"`
}

// AssetResponse represents the response for an asset request
type AssetResponse struct {
	Asset       *AssetInfo      `json:"asset,omitempty"`
	Content     io.ReadSeeker   `json:"-"`
	ContentType string          `json:"content_type"`
	Size        int64           `json:"size"`
	StatusCode  int             `json:"status_code"`
	Headers     map[string]string `json:"headers,omitempty"`
	Error       error           `json:"-"`
}

// PrefetchRequest represents a prefetch request for assets
type PrefetchRequest struct {
	Patterns  []string          `json:"patterns"`
	Regions   []string          `json:"regions"`
	Priority  PrefetchPriority  `json:"priority"`
	ExpiresAt *time.Time        `json:"expires_at,omitempty"`
}

// PrefetchPriority represents prefetch priority levels
type PrefetchPriority string

const (
	PrefetchPriorityLow    PrefetchPriority = "low"
	PrefetchPriorityNormal PrefetchPriority = "normal"
	PrefetchPriorityHigh   PrefetchPriority = "high"
)

// NewCDNManager creates a new CDN manager
func NewCDNManager(config *CDNConfig, logger *errorhandling.Logger) (*CDNManager, error) {
	if config == nil {
		config = &CDNConfig{
			AssetRoot:         "/assets",
			CacheTTL:          24 * time.Hour,
			MaxAssetSize:      100 * 1024 * 1024, // 100MB
			SupportedFormats:  []string{".png", ".jpg", ".jpeg", ".webp", ".mp3", ".wav", ".ogg", ".json", ".yaml"},
			CompressionTypes:  []string{"gzip", "br"},
			EnableEdgeCaching: true,
			PrefetchEnabled:   true,
		}
	}

	cdn := &CDNManager{
		config:       config,
		logger:       logger,
		assets:       make(map[string]*AssetInfo),
		regions:      make(map[string]*CDNRegion),
		shutdownChan: make(chan struct{}),
	}

	// Initialize regions
	for regionID, endpoint := range config.Regions {
		cdn.regions[regionID] = &CDNRegion{
			ID:       regionID,
			Endpoint: endpoint,
			Status:   RegionStatusActive,
			LastSync: time.Now(),
			Assets:   make(map[string]bool),
		}
	}

	// Start background processes
	cdn.startBackgroundProcesses()

	// Scan existing assets
	if err := cdn.scanAssets(); err != nil {
		logger.Warnw("Failed to scan existing assets", "error", err)
	}

	logger.Infow("CDN manager initialized",
		"asset_root", config.AssetRoot,
		"regions", len(config.Regions),
		"edge_caching", config.EnableEdgeCaching)

	return cdn, nil
}

// GetAsset retrieves an asset for the given request
func (cdn *CDNManager) GetAsset(ctx context.Context, req *AssetRequest) (*AssetResponse, error) {
	atomic.AddInt64(&cdn.totalRequests, 1)

	// Find asset in cache
	assetKey := cdn.getAssetKey(req.Path, req.Version)
	cdn.mu.RLock()
	asset, exists := cdn.assets[assetKey]
	cdn.mu.RUnlock()

	if !exists {
		atomic.AddInt64(&cdn.cacheMisses, 1)
		return &AssetResponse{
			StatusCode: http.StatusNotFound,
			Error:      errorhandling.NewNotFoundError("ASSET_NOT_FOUND", "Asset not found"),
		}, nil
	}

	atomic.AddInt64(&cdn.cacheHits, 1)
	atomic.AddInt64(&cdn.bytesServed, asset.Size)

	// Update access statistics
	cdn.mu.Lock()
	asset.AccessCount++
	asset.LastAccessed = time.Now()
	cdn.mu.Unlock()

	// Determine best region for client
	region := cdn.selectBestRegion(req.ClientRegion, asset)

	// Load asset content
	content, err := cdn.loadAssetContent(asset.Path)
	if err != nil {
		return &AssetResponse{
			StatusCode: http.StatusInternalServerError,
			Error:      errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "ASSET_LOAD_FAILED", "Failed to load asset content"),
		}, nil
	}

	// Prepare response headers
	headers := map[string]string{
		"Content-Type":  asset.ContentType,
		"Content-Length": fmt.Sprintf("%d", asset.Size),
		"ETag":          fmt.Sprintf(`"%s"`, asset.Hash),
		"Cache-Control": fmt.Sprintf("max-age=%d", int(cdn.config.CacheTTL.Seconds())),
		"Last-Modified": asset.LastModified.Format(http.TimeFormat),
	}

	// Add compression headers if supported
	if cdn.shouldCompress(req.AcceptEncoding, asset.ContentType) {
		headers["Content-Encoding"] = cdn.getBestCompression(req.AcceptEncoding)
	}

	// Add CDN-specific headers
	if region != "" {
		headers["X-CDN-Region"] = region
		headers["X-CDN-Cache"] = "HIT"
	}

	return &AssetResponse{
		Asset:       asset,
		Content:     content,
		ContentType: asset.ContentType,
		Size:        asset.Size,
		StatusCode:  http.StatusOK,
		Headers:     headers,
	}, nil
}

// UploadAsset uploads a new asset to the CDN
func (cdn *CDNManager) UploadAsset(ctx context.Context, path string, content io.Reader, metadata map[string]string) (*AssetInfo, error) {
	// Validate asset
	if err := cdn.validateAsset(path, content); err != nil {
		return nil, err
	}

	// Calculate hash and size
	hash, size, err := cdn.calculateAssetHash(content)
	if err != nil {
		return nil, errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "HASH_CALCULATION_FAILED", "Failed to calculate asset hash")
	}

	// Create asset info
	asset := &AssetInfo{
		Path:         path,
		Hash:         hash,
		Size:         size,
		ContentType:  cdn.getContentType(path),
		LastModified: time.Now(),
		Version:      "1.0",
		Regions:      []string{},
		AccessCount:  0,
		LastAccessed: time.Now(),
		Tags:         metadata,
	}

	// Store asset locally
	if err := cdn.storeAssetLocally(path, content); err != nil {
		return nil, errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "ASSET_STORAGE_FAILED", "Failed to store asset locally")
	}

	// Distribute to regions if edge caching enabled
	if cdn.config.EnableEdgeCaching {
		go cdn.distributeToRegions(asset)
	}

	// Register asset
	assetKey := cdn.getAssetKey(path, asset.Version)
	cdn.mu.Lock()
	cdn.assets[assetKey] = asset
	cdn.mu.Unlock()

	cdn.logger.Infow("Asset uploaded successfully",
		"path", path,
		"size", size,
		"hash", hash)

	return asset, nil
}

// PrefetchAssets prefetches assets to edge locations
func (cdn *CDNManager) PrefetchAssets(ctx context.Context, req *PrefetchRequest) error {
	cdn.logger.Infow("Starting asset prefetch",
		"patterns", len(req.Patterns),
		"regions", len(req.Regions),
		"priority", req.Priority)

	// Find matching assets
	var assetsToPrefetch []*AssetInfo

	cdn.mu.RLock()
	for _, asset := range cdn.assets {
		if cdn.matchesPatterns(asset.Path, req.Patterns) {
			assetsToPrefetch = append(assetsToPrefetch, asset)
		}
	}
	cdn.mu.RUnlock()

	if len(assetsToPrefetch) == 0 {
		return errorhandling.NewValidationError("NO_MATCHING_ASSETS", "No assets match the provided patterns")
	}

	// Prefetch to specified regions
	for _, regionID := range req.Regions {
		if region, exists := cdn.regions[regionID]; exists {
			go cdn.prefetchToRegion(region, assetsToPrefetch, req.Priority)
		}
	}

	cdn.logger.Infow("Asset prefetch initiated",
		"assets_count", len(assetsToPrefetch),
		"regions_count", len(req.Regions))

	return nil
}

// GetAssetStats returns CDN performance statistics
func (cdn *CDNManager) GetAssetStats() map[string]interface{} {
	cdn.mu.RLock()
	defer cdn.mu.RUnlock()

	totalAssets := len(cdn.assets)
	totalRegions := len(cdn.regions)

	// Calculate cache hit rate
	hitRate := float64(0)
	totalRequests := atomic.LoadInt64(&cdn.totalRequests)
	if totalRequests > 0 {
		hitRate = float64(atomic.LoadInt64(&cdn.cacheHits)) / float64(totalRequests) * 100
	}

	// Calculate most popular assets
	type assetStats struct {
		path  string
		count int64
		size  int64
	}

	var stats []assetStats
	for _, asset := range cdn.assets {
		stats = append(stats, assetStats{
			path:  asset.Path,
			count: asset.AccessCount,
			size:  asset.Size,
		})
	}

	// Sort by access count
	sort.Slice(stats, func(i, j int) bool {
		return stats[i].count > stats[j].count
	})

	topAssets := stats
	if len(stats) > 10 {
		topAssets = stats[:10]
	}

	return map[string]interface{}{
		"total_assets":         totalAssets,
		"total_regions":        totalRegions,
		"total_requests":       atomic.LoadInt64(&cdn.totalRequests),
		"cache_hits":          atomic.LoadInt64(&cdn.cacheHits),
		"cache_misses":        atomic.LoadInt64(&cdn.cacheMisses),
		"cache_hit_rate_percent": hitRate,
		"bytes_served":        atomic.LoadInt64(&cdn.bytesServed),
		"top_assets":          topAssets,
		"regions_status":      cdn.getRegionsStatus(),
	}
}

// getAssetKey generates a cache key for an asset
func (cdn *CDNManager) getAssetKey(path, version string) string {
	if version == "" {
		version = "latest"
	}
	return fmt.Sprintf("%s:%s", path, version)
}

// selectBestRegion selects the best CDN region for the client
func (cdn *CDNManager) selectBestRegion(clientRegion string, asset *AssetInfo) string {
	// If client region is specified and asset is cached there, use it
	if clientRegion != "" {
		if region, exists := cdn.regions[clientRegion]; exists {
			if region.Assets[asset.Hash] {
				return clientRegion
			}
		}
	}

	// Find any region that has the asset
	for regionID, region := range cdn.regions {
		if region.Status == RegionStatusActive && region.Assets[asset.Hash] {
			return regionID
		}
	}

	// Fallback to first active region
	for regionID, region := range cdn.regions {
		if region.Status == RegionStatusActive {
			return regionID
		}
	}

	return ""
}

// loadAssetContent loads asset content from disk
func (cdn *CDNManager) loadAssetContent(path string) (io.ReadSeeker, error) {
	fullPath := filepath.Join(cdn.config.AssetRoot, path)
	file, err := os.Open(fullPath)
	if err != nil {
		return nil, err
	}
	return file, nil
}

// validateAsset validates an asset before upload
func (cdn *CDNManager) validateAsset(path string, content io.Reader) error {
	// Check file extension
	ext := strings.ToLower(filepath.Ext(path))
	validExt := false
	for _, supported := range cdn.config.SupportedFormats {
		if ext == supported {
			validExt = true
			break
		}
	}
	if !validExt {
		return errorhandling.NewValidationError("UNSUPPORTED_FORMAT", fmt.Sprintf("File format %s is not supported", ext))
	}

	// Check file size
	if seeker, ok := content.(io.Seeker); ok {
		size, err := seeker.Seek(0, io.SeekEnd)
		if err != nil {
			return errorhandling.WrapError(err, errorhandling.ErrorTypeInternal, "SIZE_CHECK_FAILED", "Failed to check file size")
		}
		seeker.Seek(0, io.SeekStart)

		if size > cdn.config.MaxAssetSize {
			return errorhandling.NewValidationError("FILE_TOO_LARGE", fmt.Sprintf("File size %d exceeds maximum %d", size, cdn.config.MaxAssetSize))
		}
	}

	return nil
}

// calculateAssetHash calculates MD5 hash and size of asset content
func (cdn *CDNManager) calculateAssetHash(content io.Reader) (string, int64, error) {
	hash := md5.New()
	size, err := io.Copy(hash, content)
	if err != nil {
		return "", 0, err
	}

	hashStr := hex.EncodeToString(hash.Sum(nil))
	return hashStr, size, nil
}

// getContentType determines content type from file extension
func (cdn *CDNManager) getContentType(path string) string {
	ext := strings.ToLower(filepath.Ext(path))
	switch ext {
	case ".png":
		return "image/png"
	case ".jpg", ".jpeg":
		return "image/jpeg"
	case ".webp":
		return "image/webp"
	case ".mp3":
		return "audio/mpeg"
	case ".wav":
		return "audio/wav"
	case ".ogg":
		return "audio/ogg"
	case ".json":
		return "application/json"
	case ".yaml", ".yml":
		return "application/yaml"
	default:
		return "application/octet-stream"
	}
}

// shouldCompress determines if content should be compressed
func (cdn *CDNManager) shouldCompress(acceptEncoding, contentType string) bool {
	if acceptEncoding == "" {
		return false
	}

	// Only compress text-based content
	compressibleTypes := []string{
		"application/json",
		"application/yaml",
		"text/",
	}

	for _, compressible := range compressibleTypes {
		if strings.Contains(contentType, compressible) {
			// Check if client accepts compression
			for _, encoding := range strings.Split(acceptEncoding, ",") {
				encoding = strings.TrimSpace(encoding)
				if encoding == "gzip" || encoding == "br" {
					return true
				}
			}
		}
	}

	return false
}

// getBestCompression selects the best compression method
func (cdn *CDNManager) getBestCompression(acceptEncoding string) string {
	encodings := strings.Split(acceptEncoding, ",")
	for _, encoding := range encodings {
		encoding = strings.TrimSpace(strings.Split(encoding, ";")[0])
		if encoding == "br" {
			return "br"
		}
		if encoding == "gzip" {
			return "gzip"
		}
	}
	return "gzip"
}

// storeAssetLocally stores asset on local disk
func (cdn *CDNManager) storeAssetLocally(path string, content io.Reader) error {
	fullPath := filepath.Join(cdn.config.AssetRoot, path)

	// Create directory if it doesn't exist
	dir := filepath.Dir(fullPath)
	if err := os.MkdirAll(dir, 0755); err != nil {
		return err
	}

	file, err := os.Create(fullPath)
	if err != nil {
		return err
	}
	defer file.Close()

	_, err = io.Copy(file, content)
	return err
}

// distributeToRegions distributes asset to all CDN regions
func (cdn *CDNManager) distributeToRegions(asset *AssetInfo) {
	for regionID, region := range cdn.regions {
		if region.Status == RegionStatusActive {
			go cdn.syncAssetToRegion(regionID, asset)
		}
	}
}

// syncAssetToRegion syncs a single asset to a specific region
func (cdn *CDNManager) syncAssetToRegion(regionID string, asset *AssetInfo) {
	region := cdn.regions[regionID]
	if region == nil {
		return
	}

	// Mark region as syncing
	region.Status = RegionStatusSyncing

	// Simulate sync delay (in real implementation, this would upload to cloud storage)
	time.Sleep(100 * time.Millisecond)

	// Mark asset as available in region
	cdn.mu.Lock()
	region.Assets[asset.Hash] = true
	region.LastSync = time.Now()
	region.Status = RegionStatusActive
	cdn.mu.Unlock()

	cdn.logger.Debugw("Asset synced to region",
		"asset", asset.Path,
		"region", regionID,
		"hash", asset.Hash)
}

// prefetchToRegion prefetches assets to a specific region
func (cdn *CDNManager) prefetchToRegion(region *CDNRegion, assets []*AssetInfo, priority PrefetchPriority) {
	region.Status = RegionStatusSyncing

	// Simulate prefetch delay based on priority
	var delay time.Duration
	switch priority {
	case PrefetchPriorityHigh:
		delay = 50 * time.Millisecond
	case PrefetchPriorityNormal:
		delay = 100 * time.Millisecond
	default:
		delay = 200 * time.Millisecond
	}

	time.Sleep(delay * time.Duration(len(assets)))

	// Mark assets as prefetched
	cdn.mu.Lock()
	for _, asset := range assets {
		region.Assets[asset.Hash] = true
	}
	region.LastSync = time.Now()
	region.Status = RegionStatusActive
	cdn.mu.Unlock()

	cdn.logger.Infow("Assets prefetched to region",
		"region", region.ID,
		"assets_count", len(assets),
		"priority", priority)
}

// matchesPatterns checks if a path matches any of the given patterns
func (cdn *CDNManager) matchesPatterns(path string, patterns []string) bool {
	for _, pattern := range patterns {
		if matched, _ := filepath.Match(pattern, path); matched {
			return true
		}
		// Also check if path contains the pattern
		if strings.Contains(path, pattern) {
			return true
		}
	}
	return false
}

// scanAssets scans the asset directory for existing assets
func (cdn *CDNManager) scanAssets() error {
	return filepath.Walk(cdn.config.AssetRoot, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}

		if info.IsDir() {
			return nil
		}

		// Make path relative to asset root
		relPath, err := filepath.Rel(cdn.config.AssetRoot, path)
		if err != nil {
			return err
		}

		// Validate file
		file, err := os.Open(path)
		if err != nil {
			return err
		}
		defer file.Close()

		if err := cdn.validateAsset(relPath, file); err != nil {
			cdn.logger.Debugw("Skipping invalid asset", "path", relPath, "error", err)
			return nil
		}

		// Calculate hash
		file.Seek(0, 0)
		hash, size, err := cdn.calculateAssetHash(file)
		if err != nil {
			return err
		}

		// Create asset info
		asset := &AssetInfo{
			Path:         relPath,
			Hash:         hash,
			Size:         size,
			ContentType:  cdn.getContentType(relPath),
			LastModified: info.ModTime(),
			Version:      "1.0",
			Regions:      []string{"local"},
			AccessCount:  0,
			LastAccessed: time.Now(),
		}

		assetKey := cdn.getAssetKey(relPath, asset.Version)
		cdn.mu.Lock()
		cdn.assets[assetKey] = asset
		cdn.mu.Unlock()

		return nil
	})
}

// getRegionsStatus returns status of all regions
func (cdn *CDNManager) getRegionsStatus() map[string]interface{} {
	status := make(map[string]interface{})
	for regionID, region := range cdn.regions {
		status[regionID] = map[string]interface{}{
			"status":     region.Status,
			"endpoint":   region.Endpoint,
			"last_sync":  region.LastSync,
			"assets_count": len(region.Assets),
		}
	}
	return status
}

// startBackgroundProcesses starts background cleanup and sync processes
func (cdn *CDNManager) startBackgroundProcesses() {
	// Region sync process
	cdn.wg.Add(1)
	go func() {
		defer cdn.wg.Done()
		ticker := time.NewTicker(5 * time.Minute)
		defer ticker.Stop()

		for {
			select {
			case <-ticker.C:
				cdn.syncRegions()
			case <-cdn.shutdownChan:
				return
			}
		}
	}()

	// Asset cleanup process
	cdn.wg.Add(1)
	go func() {
		defer cdn.wg.Done()
		ticker := time.NewTicker(1 * time.Hour)
		defer ticker.Stop()

		for {
			select {
			case <-ticker.C:
				cdn.cleanupExpiredAssets()
			case <-cdn.shutdownChan:
				return
			}
		}
	}()
}

// syncRegions synchronizes regions with central asset registry
func (cdn *CDNManager) syncRegions() {
	cdn.mu.RLock()
	assets := make(map[string]*AssetInfo)
	for k, v := range cdn.assets {
		assets[k] = v
	}
	cdn.mu.RUnlock()

	for regionID, region := range cdn.regions {
		if region.Status == RegionStatusActive {
			go cdn.syncRegionAssets(regionID, assets)
		}
	}
}

// syncRegionAssets syncs asset list to a specific region
func (cdn *CDNManager) syncRegionAssets(regionID string, assets map[string]*AssetInfo) {
	region := cdn.regions[regionID]
	if region == nil {
		return
	}

	// In real implementation, this would sync asset metadata to the region
	cdn.logger.Debugw("Synced assets to region",
		"region", regionID,
		"assets_count", len(assets))
}

// cleanupExpiredAssets removes expired cached assets
func (cdn *CDNManager) cleanupExpiredAssets() {
	cutoff := time.Now().Add(-cdn.config.CacheTTL)
	removed := 0

	cdn.mu.Lock()
	for key, asset := range cdn.assets {
		if asset.LastAccessed.Before(cutoff) && asset.AccessCount == 0 {
			delete(cdn.assets, key)
			removed++
		}
	}
	cdn.mu.Unlock()

	if removed > 0 {
		cdn.logger.Infow("Cleaned up expired assets", "removed_count", removed)
	}
}

// Shutdown gracefully shuts down the CDN manager
func (cdn *CDNManager) Shutdown(ctx context.Context) error {
	close(cdn.shutdownChan)

	done := make(chan struct{})
	go func() {
		cdn.wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		cdn.logger.Info("CDN manager shut down gracefully")
		return nil
	case <-ctx.Done():
		cdn.logger.Warn("CDN manager shutdown timed out")
		return ctx.Err()
	}
}
