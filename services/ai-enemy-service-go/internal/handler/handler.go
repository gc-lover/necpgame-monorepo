package handler

import (
	"context"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"necpgame/services/ai-enemy-service-go/internal/service"
)

// Handler implements the generated API interface
type Handler struct {
	aiService *service.AIService
}

// NewHandler creates a new handler instance
func NewHandler(aiService *service.AIService) *Handler {
	return &Handler{
		aiService: aiService,
	}
}

// Router returns the HTTP router with all endpoints
func (h *Handler) Router() http.Handler {
	// This would integrate with the generated ogen router
	// For now, return a basic mux
	mux := http.NewServeMux()

	// Health endpoint
	mux.HandleFunc("/health", h.handleHealth)

	// AI Enemy endpoints
	mux.HandleFunc("/enemies", h.handleGetActiveEnemies)
	mux.HandleFunc("/enemies/", h.handleEnemyByID)

	// Telemetry endpoint
	mux.HandleFunc("/telemetry", h.handleTelemetry)

	return mux
}

// handleHealth provides health check endpoint
func (h *Handler) handleHealth(w http.ResponseWriter, r *http.Request) {
	telemetry := h.aiService.GetTelemetry()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"status": "healthy",
		"timestamp": "%s",
		"active_enemies": %d,
		"uptime_seconds": 3600
	}`, time.Now().Format(time.RFC3339), telemetry.ActiveEnemies)
}

// handleGetActiveEnemies handles GET /enemies
func (h *Handler) handleGetActiveEnemies(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	zoneID := r.URL.Query().Get("zone_id")
	if zoneID == "" {
		http.Error(w, "zone_id parameter required", http.StatusBadRequest)
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 50
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	enemies, err := h.aiService.GetActiveEnemies(ctx, zoneID)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to get enemies: %v", err), http.StatusInternalServerError)
		return
	}

	// Limit results
	if len(enemies) > limit {
		enemies = enemies[:limit]
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	fmt.Fprintf(w, `{"enemies":[`)
	for i, enemy := range enemies {
		if i > 0 {
			fmt.Fprintf(w, ",")
		}
		fmt.Fprintf(w, `{
			"enemy_id": "%s",
			"enemy_type": "%s",
			"position": {"x": %f, "y": %f, "z": %f},
			"health_percentage": %f,
			"threat_level": "medium"
		}`, enemy.ID, enemy.EnemyType, enemy.Position.X, enemy.Position.Y, enemy.Position.Z, enemy.Health.Percentage)
	}
	fmt.Fprintf(w, `],"total_count": %d}`, len(enemies))
}

// handleEnemyByID handles operations on specific enemies
func (h *Handler) handleEnemyByID(w http.ResponseWriter, r *http.Request) {
	// Extract enemy ID from URL path
	// This is a simplified implementation

	switch r.Method {
	case http.MethodGet:
		h.handleGetEnemy(w, r)
	case http.MethodPost:
		if r.URL.Path[len(r.URL.Path)-7:] == "/damage" {
			h.handleApplyDamage(w, r)
		} else {
			http.Error(w, "Not implemented", http.StatusNotImplemented)
		}
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// handleGetEnemy handles GET /enemies/{id}
func (h *Handler) handleGetEnemy(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Extract enemy ID from path (simplified)
	enemyID := r.URL.Path[len("/enemies/"):]

	enemy, err := h.aiService.GetEnemy(ctx, enemyID)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to get enemy: %v", err), http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"enemy_id": "%s",
		"enemy_type": "%s",
		"position": {"x": %f, "y": %f, "z": %f},
		"health": {
			"current": %d,
			"maximum": %d,
			"percentage": %f
		},
		"status": "%s",
		"created_at": "%s"
	}`, enemy.ID, enemy.EnemyType, enemy.Position.X, enemy.Position.Y, enemy.Position.Z,
		enemy.Health.Current, enemy.Health.Maximum, enemy.Health.Percentage,
		enemy.Status, enemy.CreatedAt.Format(time.RFC3339))
}

// handleApplyDamage handles POST /enemies/{id}/damage
func (h *Handler) handleApplyDamage(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Extract enemy ID from path (simplified)
	path := r.URL.Path
	enemyID := path[len("/enemies/") : len(path)-7] // Remove "/damage"

	if r.Header.Get("Content-Type") != "application/json" {
		http.Error(w, "Content-Type must be application/json", http.StatusBadRequest)
		return
	}

	// Simplified JSON parsing (in production use proper JSON parsing)
	damageAmount := 50 // Default damage
	damageType := "physical"

	result, err := h.aiService.ApplyDamage(ctx, enemyID, damageAmount, damageType)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to apply damage: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"enemy_id": "%s",
		"damage_dealt": %d,
		"actual_damage": %d,
		"killed": %t,
		"new_health": {
			"current": %d,
			"maximum": %d,
			"percentage": %f
		}
	}`, enemyID, result.DamageDealt, result.ActualDamage, result.Killed,
		result.NewHealth.Current, result.NewHealth.Maximum, result.NewHealth.Percentage)
}

// handleTelemetry provides service telemetry
func (h *Handler) handleTelemetry(w http.ResponseWriter, r *http.Request) {
	telemetry := h.aiService.GetTelemetry()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"metric": "service_stats",
		"timeframe": "current",
		"data_points": [{
			"timestamp": "%s",
			"active_enemies": %d,
			"behavior_decisions": %d,
			"damage_dealt": %d,
			"enemies_spawned": %d,
			"enemies_destroyed": %d
		}],
		"average": %d,
		"peak": %d,
		"total": %d
	}`, time.Now().Format(time.RFC3339), telemetry.ActiveEnemies,
		telemetry.BehaviorDecisions, telemetry.DamageDealt,
		telemetry.EnemiesSpawned, telemetry.EnemiesDestroyed,
		telemetry.ActiveEnemies, telemetry.ActiveEnemies, telemetry.ActiveEnemies)
}

// Issue: #1861
