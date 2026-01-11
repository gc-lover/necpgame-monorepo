package server

import (
	"encoding/json"
	"io"
	"log/slog"
	"net/http"
	"strconv"
	"time"

	"github.com/labstack/echo/v4"
)

// AssetCacheHandler handles HTTP requests for asset cache operations
type AssetCacheHandler struct {
	service *AssetCacheService
}

// NewAssetCacheHandler creates a new asset cache handler
func NewAssetCacheHandler(service *AssetCacheService) *AssetCacheHandler {
	return &AssetCacheHandler{service: service}
}

// GetAsset handles GET /assets/{id}
func (h *AssetCacheHandler) GetAsset(c echo.Context) error {
	assetID := c.Param("id")
	if assetID == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "asset ID is required",
		})
	}

	ctx := c.Request().Context()
	data, err := h.service.LoadAsset(ctx, assetID)
	if err != nil {
		slog.Error("Failed to load asset", "asset_id", assetID, "error", err)
		return c.JSON(http.StatusNotFound, map[string]string{
			"error": "asset not found",
		})
	}

	// Set appropriate content type based on asset type detection
	contentType := h.detectContentTypeFromData(data)
	c.Response().Header().Set("Content-Type", contentType)
	c.Response().Header().Set("Cache-Control", "public, max-age=3600")

	return c.Blob(http.StatusOK, contentType, data)
}

// PutAsset handles PUT /assets/{id}
func (h *AssetCacheHandler) PutAsset(c echo.Context) error {
	assetID := c.Param("id")
	if assetID == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "asset ID is required",
		})
	}

	data, err := io.ReadAll(c.Request().Body)
	if err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "failed to read request body",
		})
	}

	contentType := c.Request().Header.Get("Content-Type")
	if contentType == "" {
		contentType = "application/octet-stream"
	}

	ctx := c.Request().Context()
	if err := h.service.StoreAsset(ctx, assetID, data, contentType); err != nil {
		slog.Error("Failed to store asset", "asset_id", assetID, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to store asset",
		})
	}

	return c.JSON(http.StatusCreated, map[string]string{
		"message": "asset stored successfully",
		"asset_id": assetID,
	})
}

// DeleteAsset handles DELETE /assets/{id}
func (h *AssetCacheHandler) DeleteAsset(c echo.Context) error {
	assetID := c.Param("id")
	if assetID == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "asset ID is required",
		})
	}

	ctx := c.Request().Context()
	if err := h.service.DeleteAsset(ctx, assetID); err != nil {
		slog.Error("Failed to delete asset", "asset_id", assetID, "error", err)
		return c.JSON(http.StatusNotFound, map[string]string{
			"error": "asset not found or could not be deleted",
		})
	}

	return c.JSON(http.StatusOK, map[string]string{
		"message": "asset deleted successfully",
		"asset_id": assetID,
	})
}

// GetCacheStats handles GET /cache/stats
func (h *AssetCacheHandler) GetCacheStats(c echo.Context) error {
	ctx := c.Request().Context()
	stats, err := h.service.GetCacheStats(ctx)
	if err != nil {
		slog.Error("Failed to get cache stats", "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to get cache statistics",
		})
	}

	return c.JSON(http.StatusOK, stats)
}

// PreloadAssets handles POST /cache/preload
func (h *AssetCacheHandler) PreloadAssets(c echo.Context) error {
	var request struct {
		AssetIDs []string `json:"asset_ids"`
	}

	if err := json.NewDecoder(c.Request().Body).Decode(&request); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "invalid request body",
		})
	}

	if len(request.AssetIDs) == 0 {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "asset_ids array is required and cannot be empty",
		})
	}

	ctx := c.Request().Context()
	if err := h.service.PreloadAssets(ctx, request.AssetIDs); err != nil {
		slog.Error("Failed to preload assets", "count", len(request.AssetIDs), "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to preload assets",
		})
	}

	return c.JSON(http.StatusOK, map[string]interface{}{
		"message": "assets preloaded successfully",
		"count":   len(request.AssetIDs),
	})
}

// CleanupExpiredAssets handles POST /cache/cleanup
func (h *AssetCacheHandler) CleanupExpiredAssets(c echo.Context) error {
	var request struct {
		MaxAgeHours int `json:"max_age_hours"`
	}

	if err := json.NewDecoder(c.Request().Body).Decode(&request); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "invalid request body",
		})
	}

	if request.MaxAgeHours <= 0 {
		request.MaxAgeHours = 24 // Default 24 hours
	}

	maxAge := time.Duration(request.MaxAgeHours) * time.Hour
	ctx := c.Request().Context()

	if err := h.service.CleanupExpiredAssets(ctx, maxAge); err != nil {
		slog.Error("Failed to cleanup expired assets", "max_age", maxAge, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to cleanup expired assets",
		})
	}

	return c.JSON(http.StatusOK, map[string]interface{}{
		"message": "expired assets cleaned up successfully",
		"max_age_hours": request.MaxAgeHours,
	})
}

// HealthCheck handles GET /health
func (h *AssetCacheHandler) HealthCheck(c echo.Context) error {
	ctx := c.Request().Context()
	stats, err := h.service.GetCacheStats(ctx)
	if err != nil {
		return c.JSON(http.StatusServiceUnavailable, map[string]interface{}{
			"status": "unhealthy",
			"error":  err.Error(),
		})
	}

	return c.JSON(http.StatusOK, map[string]interface{}{
		"status":      "healthy",
		"service":     "asset-cache-service",
		"memory_used": stats.MemoryStats.MemoryUsedMB,
		"max_memory":  stats.MemoryStats.MaxMemoryMB,
		"files_mapped": stats.MemoryStats.FilesMapped,
	})
}

// detectContentTypeFromData attempts to detect content type from data
func (h *AssetCacheHandler) detectContentTypeFromData(data []byte) string {
	// Simple content type detection based on file signatures
	if len(data) < 4 {
		return "application/octet-stream"
	}

	// PNG
	if data[0] == 0x89 && data[1] == 0x50 && data[2] == 0x4E && data[3] == 0x47 {
		return "image/png"
	}

	// JPEG
	if data[0] == 0xFF && data[1] == 0xD8 {
		return "image/jpeg"
	}

	// GIF
	if data[0] == 0x47 && data[1] == 0x49 && data[2] == 0x46 {
		return "image/gif"
	}

	// Default
	return "application/octet-stream"
}