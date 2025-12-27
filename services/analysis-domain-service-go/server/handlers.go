// Issue: #implement-analysis-domain-service
// HTTP Handlers for Analysis Domain Service

package server

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"go.uber.org/zap"

	"analysis-domain-service-go/pkg/service"
)

// APIHandler implements HTTP handlers for the analysis API
type APIHandler struct {
	service service.ServiceInterface
	logger  *zap.Logger
}

// NewAPIHandler creates a new API handler
func NewAPIHandler(svc service.ServiceInterface, logger *zap.Logger) *APIHandler {
	return &APIHandler{
		service: svc,
		logger:  logger,
	}
}

// GetNetworkLatency handles network latency monitoring requests
func (h *APIHandler) GetNetworkLatency(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	region := r.URL.Query().Get("region")

	if region == "" {
		h.writeError(w, http.StatusBadRequest, "region parameter is required")
		return
	}

	metrics, err := h.service.GetNetworkLatency(ctx, region)
	if err != nil {
		h.logger.Error("Failed to get network latency",
			zap.String("region", region),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get network latency")
		return
	}

	h.writeJSON(w, http.StatusOK, metrics)
}

// GetNetworkBottlenecks handles network bottlenecks analysis requests
func (h *APIHandler) GetNetworkBottlenecks(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	bottlenecks, err := h.service.GetNetworkBottlenecks(ctx)
	if err != nil {
		h.logger.Error("Failed to get network bottlenecks", zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get network bottlenecks")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"bottlenecks": bottlenecks,
		"timestamp":   time.Now(),
	})
}

// GetScalabilityAnalysis handles scalability analysis requests
func (h *APIHandler) GetScalabilityAnalysis(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	serviceName := r.URL.Query().Get("service")

	if serviceName == "" {
		serviceName = "analysis-service" // Default
	}

	analysis, err := h.service.GetScalabilityAnalysis(ctx, serviceName)
	if err != nil {
		h.logger.Error("Failed to get scalability analysis",
			zap.String("service", serviceName),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get scalability analysis")
		return
	}

	h.writeJSON(w, http.StatusOK, analysis)
}

// GetSecurityThreats handles security threats analysis requests
func (h *APIHandler) GetSecurityThreats(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	threats, err := h.service.GetSecurityThreats(ctx)
	if err != nil {
		h.logger.Error("Failed to get security threats", zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get security threats")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"threats":   threats,
		"timestamp": time.Now(),
	})
}

// GetPlayerBehaviorMetrics handles player behavior metrics requests
func (h *APIHandler) GetPlayerBehaviorMetrics(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	period := r.URL.Query().Get("period")

	if period == "" {
		period = "daily" // Default
	}

	metrics, err := h.service.GetPlayerBehaviorMetrics(ctx, period)
	if err != nil {
		h.logger.Error("Failed to get player behavior metrics",
			zap.String("period", period),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get player behavior metrics")
		return
	}

	h.writeJSON(w, http.StatusOK, metrics)
}

// GetPlayerRetention handles player retention analysis requests
func (h *APIHandler) GetPlayerRetention(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	cohort := r.URL.Query().Get("cohort")

	daysStr := r.URL.Query().Get("days")
	days := 7 // Default
	if daysStr != "" {
		if d, err := strconv.Atoi(daysStr); err == nil && d > 0 {
			days = d
		}
	}

	retention, err := h.service.GetPlayerRetention(ctx, cohort, days)
	if err != nil {
		h.logger.Error("Failed to get player retention",
			zap.String("cohort", cohort),
			zap.Int("days", days),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get player retention")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"cohort":    cohort,
		"days":      days,
		"retention": retention,
		"timestamp": time.Now(),
	})
}

// GetPlayerChurn handles player churn analysis requests
func (h *APIHandler) GetPlayerChurn(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	daysStr := r.URL.Query().Get("days")
	days := 30 // Default
	if daysStr != "" {
		if d, err := strconv.Atoi(daysStr); err == nil && d > 0 {
			days = d
		}
	}

	churnRate, err := h.service.GetPlayerChurn(ctx, days)
	if err != nil {
		h.logger.Error("Failed to get player churn",
			zap.Int("days", days),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get player churn")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"days":       days,
		"churn_rate": churnRate,
		"timestamp":  time.Now(),
	})
}

// GetPlayerEngagement handles player engagement analysis requests
func (h *APIHandler) GetPlayerEngagement(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	period := r.URL.Query().Get("period")

	if period == "" {
		period = "weekly" // Default
	}

	engagement, err := h.service.GetPlayerEngagement(ctx, period)
	if err != nil {
		h.logger.Error("Failed to get player engagement",
			zap.String("period", period),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get player engagement")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"period":     period,
		"engagement": engagement,
		"timestamp":  time.Now(),
	})
}

// GetPlayerSegmentation handles player segmentation analysis requests
func (h *APIHandler) GetPlayerSegmentation(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	segmentation, err := h.service.GetPlayerSegmentation(ctx)
	if err != nil {
		h.logger.Error("Failed to get player segmentation", zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get player segmentation")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"segmentation": segmentation,
		"timestamp":    time.Now(),
	})
}

// GetPerformanceDashboard handles performance dashboard requests
func (h *APIHandler) GetPerformanceDashboard(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	dashboard, err := h.service.GetPerformanceDashboard(ctx)
	if err != nil {
		h.logger.Error("Failed to get performance dashboard", zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get performance dashboard")
		return
	}

	h.writeJSON(w, http.StatusOK, dashboard)
}

// GetPerformanceAlerts handles performance alerts requests
func (h *APIHandler) GetPerformanceAlerts(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	alerts, err := h.service.GetPerformanceAlerts(ctx)
	if err != nil {
		h.logger.Error("Failed to get performance alerts", zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get performance alerts")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"alerts":    alerts,
		"timestamp": time.Now(),
	})
}

// GetPerformanceMetrics handles detailed performance metrics requests
func (h *APIHandler) GetPerformanceMetrics(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	serviceName := r.URL.Query().Get("service")

	if serviceName == "" {
		serviceName = "analysis-service" // Default
	}

	metrics, err := h.service.GetPerformanceMetrics(ctx, serviceName)
	if err != nil {
		h.logger.Error("Failed to get performance metrics",
			zap.String("service", serviceName),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get performance metrics")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"service":  serviceName,
		"metrics":  metrics,
		"timestamp": time.Now(),
	})
}

// GetResearchInsights handles research insights requests
func (h *APIHandler) GetResearchInsights(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	category := r.URL.Query().Get("category")

	insights, err := h.service.GetResearchInsights(ctx, category)
	if err != nil {
		h.logger.Error("Failed to get research insights",
			zap.String("category", category),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get research insights")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"category":  category,
		"insights":  insights,
		"timestamp": time.Now(),
	})
}

// GetResearchTrends handles research trends analysis requests
func (h *APIHandler) GetResearchTrends(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	category := r.URL.Query().Get("category")

	daysStr := r.URL.Query().Get("days")
	days := 30 // Default
	if daysStr != "" {
		if d, err := strconv.Atoi(daysStr); err == nil && d > 0 {
			days = d
		}
	}

	trends, err := h.service.GetResearchTrends(ctx, category, days)
	if err != nil {
		h.logger.Error("Failed to get research trends",
			zap.String("category", category),
			zap.Int("days", days),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to get research trends")
		return
	}

	h.writeJSON(w, http.StatusOK, map[string]interface{}{
		"category": category,
		"days":     days,
		"trends":   trends,
		"timestamp": time.Now(),
	})
}

// TestHypothesis handles hypothesis testing requests
func (h *APIHandler) TestHypothesis(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req struct {
		Hypothesis string                 `json:"hypothesis"`
		TestData   map[string]interface{} `json:"test_data"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, http.StatusBadRequest, "Invalid JSON payload")
		return
	}

	if req.Hypothesis == "" {
		h.writeError(w, http.StatusBadRequest, "Hypothesis is required")
		return
	}

	test, err := h.service.TestHypothesis(ctx, req.Hypothesis, req.TestData)
	if err != nil {
		h.logger.Error("Failed to test hypothesis",
			zap.String("hypothesis", req.Hypothesis),
			zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "Failed to test hypothesis")
		return
	}

	h.writeJSON(w, http.StatusOK, test)
}

// Helper methods

func (h *APIHandler) writeJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

func (h *APIHandler) writeError(w http.ResponseWriter, status int, message string) {
	h.writeJSON(w, status, map[string]interface{}{
		"error":     message,
		"timestamp": time.Now(),
	})
}
