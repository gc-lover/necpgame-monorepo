package server

import (
	"encoding/json"
	"log/slog"
	"net/http"
	"strconv"
	"time"

	"github.com/labstack/echo/v4"
)

// FeatureFlagHandler handles HTTP requests for feature flag operations
type FeatureFlagHandler struct {
	service *FeatureFlagService
}

// NewFeatureFlagHandler creates a new feature flag handler
func NewFeatureFlagHandler(service *FeatureFlagService) *FeatureFlagHandler {
	return &FeatureFlagHandler{service: service}
}

// EvaluateFeature handles POST /api/v1/flags/{flagName}/evaluate
func (h *FeatureFlagHandler) EvaluateFeature(c echo.Context) error {
	flagName := c.Param("flagName")
	if flagName == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "flag name is required",
		})
	}

	var userCtx UserContext
	if err := json.NewDecoder(c.Request().Body).Decode(&userCtx); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "invalid request body",
		})
	}

	// Set defaults
	if userCtx.Timestamp.IsZero() {
		userCtx.Timestamp = time.Now()
	}
	if userCtx.Environment == "" {
		userCtx.Environment = "production"
	}

	ctx := c.Request().Context()
	result, err := h.service.EvaluateFeature(ctx, flagName, &userCtx)
	if err != nil {
		slog.Error("Failed to evaluate feature", "flag_name", flagName, "user_id", userCtx.UserID, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to evaluate feature",
		})
	}

	return c.JSON(http.StatusOK, result)
}

// CreateFeatureFlag handles POST /api/v1/flags
func (h *FeatureFlagHandler) CreateFeatureFlag(c echo.Context) error {
	var flag FeatureFlag
	if err := json.NewDecoder(c.Request().Body).Decode(&flag); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "invalid request body",
		})
	}

	if flag.Name == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "flag name is required",
		})
	}

	ctx := c.Request().Context()
	createdFlag, err := h.service.CreateFeatureFlag(ctx, &flag)
	if err != nil {
		slog.Error("Failed to create feature flag", "flag_name", flag.Name, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to create feature flag",
		})
	}

	return c.JSON(http.StatusCreated, createdFlag)
}

// GetFeatureFlag handles GET /api/v1/flags/{flagName}
func (h *FeatureFlagHandler) GetFeatureFlag(c echo.Context) error {
	flagName := c.Param("flagName")
	if flagName == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "flag name is required",
		})
	}

	ctx := c.Request().Context()
	flag, err := h.service.(*FeatureFlagService).getFeatureFlag(ctx, flagName)
	if err != nil {
		slog.Error("Failed to get feature flag", "flag_name", flagName, "error", err)
		return c.JSON(http.StatusNotFound, map[string]string{
			"error": "feature flag not found",
		})
	}

	return c.JSON(http.StatusOK, flag)
}

// UpdateFeatureFlag handles PUT /api/v1/flags/{flagName}
func (h *FeatureFlagHandler) UpdateFeatureFlag(c echo.Context) error {
	flagName := c.Param("flagName")
	if flagName == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "flag name is required",
		})
	}

	var updates FeatureFlag
	if err := json.NewDecoder(c.Request().Body).Decode(&updates); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "invalid request body",
		})
	}

	ctx := c.Request().Context()
	updatedFlag, err := h.service.UpdateFeatureFlag(ctx, flagName, &updates)
	if err != nil {
		slog.Error("Failed to update feature flag", "flag_name", flagName, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to update feature flag",
		})
	}

	return c.JSON(http.StatusOK, updatedFlag)
}

// ListFeatureFlags handles GET /api/v1/flags
func (h *FeatureFlagHandler) ListFeatureFlags(c echo.Context) error {
	ctx := c.Request().Context()

	// Get flags from service (accessing private method - this is a design issue)
	// In production, you'd expose this through the service interface
	flags := make([]*FeatureFlag, 0)
	h.service.(*FeatureFlagService).mu.RLock()
	for _, flag := range h.service.(*FeatureFlagService).flagCache {
		flags = append(flags, flag)
	}
	h.service.(*FeatureFlagService).mu.RUnlock()

	return c.JSON(http.StatusOK, map[string]interface{}{
		"flags": flags,
		"count": len(flags),
	})
}

// CreateExperiment handles POST /api/v1/experiments
func (h *FeatureFlagHandler) CreateExperiment(c echo.Context) error {
	var experiment Experiment
	if err := json.NewDecoder(c.Request().Body).Decode(&experiment); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "invalid request body",
		})
	}

	if experiment.Name == "" || experiment.FeatureFlagID == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "experiment name and feature flag ID are required",
		})
	}

	ctx := c.Request().Context()
	createdExperiment, err := h.service.CreateExperiment(ctx, &experiment)
	if err != nil {
		slog.Error("Failed to create experiment", "experiment_name", experiment.Name, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to create experiment",
		})
	}

	return c.JSON(http.StatusCreated, createdExperiment)
}

// StartExperiment handles POST /api/v1/experiments/{experimentId}/start
func (h *FeatureFlagHandler) StartExperiment(c echo.Context) error {
	experimentID := c.Param("experimentId")
	if experimentID == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "experiment ID is required",
		})
	}

	ctx := c.Request().Context()
	if err := h.service.StartExperiment(ctx, experimentID); err != nil {
		slog.Error("Failed to start experiment", "experiment_id", experimentID, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to start experiment",
		})
	}

	return c.JSON(http.StatusOK, map[string]string{
		"message": "experiment started successfully",
	})
}

// GetExperimentResults handles GET /api/v1/experiments/{experimentId}/results
func (h *FeatureFlagHandler) GetExperimentResults(c echo.Context) error {
	experimentID := c.Param("experimentId")
	if experimentID == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "experiment ID is required",
		})
	}

	ctx := c.Request().Context()
	results, err := h.service.GetExperimentResults(ctx, experimentID)
	if err != nil {
		slog.Error("Failed to get experiment results", "experiment_id", experimentID, "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to get experiment results",
		})
	}

	return c.JSON(http.StatusOK, results)
}

// HealthCheck handles GET /health
func (h *FeatureFlagHandler) HealthCheck(c echo.Context) error {
	ctx := c.Request().Context()
	stats, err := h.service.GetEncryptionStats(ctx) // Reuse for basic health check
	if err != nil {
		return c.JSON(http.StatusServiceUnavailable, map[string]interface{}{
			"status": "unhealthy",
			"error":  err.Error(),
		})
	}

	return c.JSON(http.StatusOK, map[string]interface{}{
		"status":    "healthy",
		"service":   "feature-flag-service",
		"timestamp": time.Now(),
	})
}

// BulkEvaluate handles POST /api/v1/flags/evaluate/bulk
func (h *FeatureFlagHandler) BulkEvaluate(c echo.Context) error {
	var request struct {
		FlagNames []string    `json:"flag_names"`
		UserContext UserContext `json:"user_context"`
	}

	if err := json.NewDecoder(c.Request().Body).Decode(&request); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "invalid request body",
		})
	}

	if len(request.FlagNames) == 0 {
		return c.JSON(http.StatusBadRequest, map[string]string{
			"error": "flag_names array is required and cannot be empty",
		})
	}

	if request.UserContext.Timestamp.IsZero() {
		request.UserContext.Timestamp = time.Now()
	}
	if request.UserContext.Environment == "" {
		request.UserContext.Environment = "production"
	}

	ctx := c.Request().Context()
	results := make(map[string]*EvaluationResult)

	for _, flagName := range request.FlagNames {
		result, err := h.service.EvaluateFeature(ctx, flagName, &request.UserContext)
		if err != nil {
			slog.Warn("Failed to evaluate feature in bulk", "flag_name", flagName, "error", err)
			results[flagName] = &EvaluationResult{
				FlagName:  flagName,
				Value:     false,
				Timestamp: time.Now(),
			}
		} else {
			results[flagName] = result
		}
	}

	return c.JSON(http.StatusOK, map[string]interface{}{
		"results": results,
		"count":   len(results),
	})
}

// GetServiceStats handles GET /api/v1/stats
func (h *FeatureFlagHandler) GetServiceStats(c echo.Context) error {
	ctx := c.Request().Context()
	stats, err := h.service.GetEncryptionStats(ctx)
	if err != nil {
		slog.Error("Failed to get service stats", "error", err)
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"error": "failed to get service statistics",
		})
	}

	return c.JSON(http.StatusOK, stats)
}