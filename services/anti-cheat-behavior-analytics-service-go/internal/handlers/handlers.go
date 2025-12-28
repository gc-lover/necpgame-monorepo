// Anti-Cheat Behavior Analytics HTTP Handlers
// Issue: #2212
// REST API handlers for anti-cheat analytics service

package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/service"
)

// Handlers handles HTTP requests
type Handlers struct {
	service *service.Service
	logger  *zap.SugaredLogger
}

// NewHandlers creates new handlers instance
func NewHandlers(svc *service.Service, logger *zap.SugaredLogger) *Handlers {
	return &Handlers{
		service: svc,
		logger:  logger,
	}
}

// HealthCheck handles health check requests
func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "anti-cheat-behavior-analytics",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)
}

// GetMetrics handles Prometheus metrics endpoint
func (h *Handlers) GetMetrics(w http.ResponseWriter, r *http.Request) {
	// In a real implementation, this would serve Prometheus metrics
	response := map[string]interface{}{
		"message": "Metrics endpoint - integrate with Prometheus",
		"status":  "not_implemented",
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)
}

// GetPlayerBehavior handles player behavior retrieval
func (h *Handlers) GetPlayerBehavior(w http.ResponseWriter, r *http.Request) {
	playerID := chi.URLParam(r, "playerId")
	if playerID == "" {
		h.sendError(w, "missing player_id parameter", http.StatusBadRequest)
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 10 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	ctx := r.Context()
	behaviors, err := h.service.GetPlayerBehavior(ctx, playerID, limit)
	if err != nil {
		h.logger.Errorf("Failed to get player behavior: %v", err)
		h.sendError(w, "failed to retrieve player behavior", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"player_id": playerID,
		"behaviors": behaviors,
		"count":     len(behaviors),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// GetPlayerRiskScore handles player risk score retrieval
func (h *Handlers) GetPlayerRiskScore(w http.ResponseWriter, r *http.Request) {
	playerID := chi.URLParam(r, "playerId")
	if playerID == "" {
		h.sendError(w, "missing player_id parameter", http.StatusBadRequest)
		return
	}

	ctx := r.Context()
	riskScore, err := h.service.GetPlayerRiskScore(ctx, playerID)
	if err != nil {
		h.logger.Errorf("Failed to get player risk score: %v", err)
		h.sendError(w, "failed to retrieve player risk score", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"player_id":  playerID,
		"risk_score": riskScore,
		"timestamp":  time.Now(),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// FlagPlayer handles player flagging for review
func (h *Handlers) FlagPlayer(w http.ResponseWriter, r *http.Request) {
	playerID := chi.URLParam(r, "playerId")
	if playerID == "" {
		h.sendError(w, "missing player_id parameter", http.StatusBadRequest)
		return
	}

	var request struct {
		Reason   string `json:"reason"`
		Severity string `json:"severity"`
	}

	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.sendError(w, "invalid request body", http.StatusBadRequest)
		return
	}

	if request.Reason == "" {
		request.Reason = "Manual review requested"
	}

	if request.Severity == "" {
		request.Severity = "medium"
	}

	ctx := r.Context()
	if err := h.service.FlagPlayer(ctx, playerID, request.Reason, request.Severity); err != nil {
		h.logger.Errorf("Failed to flag player: %v", err)
		h.sendError(w, "failed to flag player", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"player_id": playerID,
		"status":    "flagged",
		"reason":    request.Reason,
		"severity":  request.Severity,
		"timestamp": time.Now(),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// GetMatchAnalysis handles match analysis requests
func (h *Handlers) GetMatchAnalysis(w http.ResponseWriter, r *http.Request) {
	matchID := chi.URLParam(r, "matchId")
	if matchID == "" {
		h.sendError(w, "missing match_id parameter", http.StatusBadRequest)
		return
	}

	ctx := r.Context()
	analysis, err := h.service.GetMatchAnalysis(ctx, matchID)
	if err != nil {
		h.logger.Errorf("Failed to get match analysis: %v", err)
		h.sendError(w, "failed to retrieve match analysis", http.StatusInternalServerError)
		return
	}

	h.sendJSON(w, analysis, http.StatusOK)
}

// GetMatchAnomalies handles match anomalies retrieval
func (h *Handlers) GetMatchAnomalies(w http.ResponseWriter, r *http.Request) {
	matchID := chi.URLParam(r, "matchId")
	if matchID == "" {
		h.sendError(w, "missing match_id parameter", http.StatusBadRequest)
		return
	}

	ctx := r.Context()
	anomalies, err := h.service.GetMatchAnomalies(ctx, matchID)
	if err != nil {
		h.logger.Errorf("Failed to get match anomalies: %v", err)
		h.sendError(w, "failed to retrieve match anomalies", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"match_id":  matchID,
		"anomalies": anomalies,
		"count":     len(anomalies),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// GetStatisticsSummary handles statistics summary requests
func (h *Handlers) GetStatisticsSummary(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	stats, err := h.service.GetStatisticsSummary(ctx)
	if err != nil {
		h.logger.Errorf("Failed to get statistics summary: %v", err)
		h.sendError(w, "failed to retrieve statistics", http.StatusInternalServerError)
		return
	}

	h.sendJSON(w, stats, http.StatusOK)
}

// GetStatisticsTrends handles statistics trends requests
func (h *Handlers) GetStatisticsTrends(w http.ResponseWriter, r *http.Request) {
	hoursStr := r.URL.Query().Get("hours")
	hours := 24 // default
	if hoursStr != "" {
		if parsed, err := strconv.Atoi(hoursStr); err == nil && parsed > 0 && parsed <= 168 { // max 1 week
			hours = parsed
		}
	}

	ctx := r.Context()
	trends, err := h.service.GetStatisticsTrends(ctx, hours)
	if err != nil {
		h.logger.Errorf("Failed to get statistics trends: %v", err)
		h.sendError(w, "failed to retrieve statistics trends", http.StatusInternalServerError)
		return
	}

	h.sendJSON(w, trends, http.StatusOK)
}

// GetTopRiskyPlayers handles top risky players requests
func (h *Handlers) GetTopRiskyPlayers(w http.ResponseWriter, r *http.Request) {
	limitStr := r.URL.Query().Get("limit")
	limit := 10 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	ctx := r.Context()
	players, err := h.service.GetTopRiskyPlayers(ctx, limit)
	if err != nil {
		h.logger.Errorf("Failed to get top risky players: %v", err)
		h.sendError(w, "failed to retrieve risky players", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"players": players,
		"count":   len(players),
		"limit":   limit,
	}

	h.sendJSON(w, response, http.StatusOK)
}

// GetDetectionRules handles detection rules retrieval
func (h *Handlers) GetDetectionRules(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	rules, err := h.service.GetDetectionRules(ctx)
	if err != nil {
		h.logger.Errorf("Failed to get detection rules: %v", err)
		h.sendError(w, "failed to retrieve detection rules", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"rules": rules,
		"count": len(rules),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// CreateDetectionRule handles detection rule creation
func (h *Handlers) CreateDetectionRule(w http.ResponseWriter, r *http.Request) {
	var rule struct {
		ID          string                 `json:"id"`
		Name        string                 `json:"name"`
		Type        string                 `json:"type"`
		Description string                 `json:"description"`
		Config      map[string]interface{} `json:"config"`
		Enabled     bool                   `json:"enabled"`
	}

	if err := json.NewDecoder(r.Body).Decode(&rule); err != nil {
		h.sendError(w, "invalid request body", http.StatusBadRequest)
		return
	}

	if rule.ID == "" || rule.Name == "" || rule.Type == "" {
		h.sendError(w, "missing required fields: id, name, type", http.StatusBadRequest)
		return
	}

	detectionRule := &service.DetectionRule{
		ID:          rule.ID,
		Name:        rule.Name,
		Type:        rule.Type,
		Description: rule.Description,
		Config:      rule.Config,
		Enabled:     rule.Enabled,
	}

	ctx := r.Context()
	if err := h.service.CreateDetectionRule(ctx, detectionRule); err != nil {
		h.logger.Errorf("Failed to create detection rule: %v", err)
		h.sendError(w, "failed to create detection rule", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"rule":      detectionRule,
		"status":    "created",
		"timestamp": time.Now(),
	}

	h.sendJSON(w, response, http.StatusCreated)
}

// UpdateDetectionRule handles detection rule updates
func (h *Handlers) UpdateDetectionRule(w http.ResponseWriter, r *http.Request) {
	ruleID := chi.URLParam(r, "ruleId")
	if ruleID == "" {
		h.sendError(w, "missing rule_id parameter", http.StatusBadRequest)
		return
	}

	var config map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&config); err != nil {
		h.sendError(w, "invalid request body", http.StatusBadRequest)
		return
	}

	ctx := r.Context()
	if err := h.service.UpdateDetectionRule(ctx, ruleID, config); err != nil {
		h.logger.Errorf("Failed to update detection rule: %v", err)
		h.sendError(w, "failed to update detection rule", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"rule_id":   ruleID,
		"status":    "updated",
		"timestamp": time.Now(),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// DeleteDetectionRule handles detection rule deletion
func (h *Handlers) DeleteDetectionRule(w http.ResponseWriter, r *http.Request) {
	ruleID := chi.URLParam(r, "ruleId")
	if ruleID == "" {
		h.sendError(w, "missing rule_id parameter", http.StatusBadRequest)
		return
	}

	ctx := r.Context()
	if err := h.service.DeleteDetectionRule(ctx, ruleID); err != nil {
		h.logger.Errorf("Failed to delete detection rule: %v", err)
		h.sendError(w, "failed to delete detection rule", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"rule_id":   ruleID,
		"status":    "deleted",
		"timestamp": time.Now(),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// GetAlerts handles alerts retrieval
func (h *Handlers) GetAlerts(w http.ResponseWriter, r *http.Request) {
	status := r.URL.Query().Get("status")
	limitStr := r.URL.Query().Get("limit")
	limit := 50 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 1000 {
			limit = parsed
		}
	}

	ctx := r.Context()
	alerts, err := h.service.GetAlerts(ctx, status, limit)
	if err != nil {
		h.logger.Errorf("Failed to get alerts: %v", err)
		h.sendError(w, "failed to retrieve alerts", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"alerts": alerts,
		"count":  len(alerts),
		"status": status,
		"limit":  limit,
	}

	h.sendJSON(w, response, http.StatusOK)
}

// AcknowledgeAlert handles alert acknowledgment
func (h *Handlers) AcknowledgeAlert(w http.ResponseWriter, r *http.Request) {
	alertID := chi.URLParam(r, "alertId")
	if alertID == "" {
		h.sendError(w, "missing alert_id parameter", http.StatusBadRequest)
		return
	}

	ctx := r.Context()
	if err := h.service.AcknowledgeAlert(ctx, alertID); err != nil {
		h.logger.Errorf("Failed to acknowledge alert: %v", err)
		h.sendError(w, "failed to acknowledge alert", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"alert_id":  alertID,
		"status":    "acknowledged",
		"timestamp": time.Now(),
	}

	h.sendJSON(w, response, http.StatusOK)
}

// GetAlertDetails handles alert details retrieval
func (h *Handlers) GetAlertDetails(w http.ResponseWriter, r *http.Request) {
	alertID := chi.URLParam(r, "alertId")
	if alertID == "" {
		h.sendError(w, "missing alert_id parameter", http.StatusBadRequest)
		return
	}

	ctx := r.Context()
	alert, err := h.service.GetAlertDetails(ctx, alertID)
	if err != nil {
		h.logger.Errorf("Failed to get alert details: %v", err)
		h.sendError(w, "failed to retrieve alert details", http.StatusInternalServerError)
		return
	}

	h.sendJSON(w, alert, http.StatusOK)
}

// AuthMiddleware handles JWT authentication
func (h *Handlers) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Simplified auth - in production, validate JWT tokens
		// For now, just check for API key in header
		apiKey := r.Header.Get("X-API-Key")
		if apiKey == "" {
			h.sendError(w, "missing API key", http.StatusUnauthorized)
			return
		}

		// TODO: Validate API key against database/service
		if apiKey != "test-key" { // Placeholder validation
			h.sendError(w, "invalid API key", http.StatusUnauthorized)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// sendJSON sends JSON response
func (h *Handlers) sendJSON(w http.ResponseWriter, data interface{}, status int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

// sendError sends error response
func (h *Handlers) sendError(w http.ResponseWriter, message string, status int) {
	response := map[string]interface{}{
		"error":     message,
		"status":    status,
		"timestamp": time.Now(),
	}

	h.sendJSON(w, response, status)
}
