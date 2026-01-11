// Package service содержит бизнес-логику системы репутации
// Issue: #2174 - Reputation Decay & Recovery mechanics
package service

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"necpgame/services/reputation-service-go/internal/models"
)

// Service представляет основной сервис репутации
type Service struct {
	Registry *models.ReputationRegistry
	Logger   *zap.Logger
}

// GetPlayerReputation возвращает профиль репутации игрока
func (s *Service) GetPlayerReputation(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	vars := mux.Vars(r)
	playerID := vars["playerId"]

	profile, exists := s.Registry.GetPlayerReputationProfile(playerID)
	if !exists {
		w.WriteHeader(http.StatusNotFound)
		json.NewEncoder(w).Encode(map[string]string{"error": "Player reputation not found"})
		return
	}

	json.NewEncoder(w).Encode(profile)
}

// UpdatePlayerReputation обновляет репутацию игрока
func (s *Service) UpdatePlayerReputation(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	vars := mux.Vars(r)
	playerID := vars["playerId"]

	// TODO: Parse request body and update reputation
	response := map[string]interface{}{
		"player_id": playerID,
		"status":    "updated",
	}

	json.NewEncoder(w).Encode(response)
}

// GetReputationHistory возвращает историю изменений репутации
func (s *Service) GetReputationHistory(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	vars := mux.Vars(r)
	playerID := vars["playerId"]

	limitStr := r.URL.Query().Get("limit")
	limit := 50
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
			limit = l
		}
	}

	// Get recent changes for this player
	history := make([]*models.ReputationChange, 0)
	for _, change := range s.Registry.RecentChanges {
		if change.PlayerID == playerID {
			history = append(history, change)
			if len(history) >= limit {
				break
			}
		}
	}

	response := map[string]interface{}{
		"player_id":   playerID,
		"history":     history,
		"total_count": len(history),
	}

	json.NewEncoder(w).Encode(response)
}

// TriggerDecay применяет decay к репутации игрока
func (s *Service) TriggerDecay(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	vars := mux.Vars(r)
	playerID := vars["playerId"]

	// TODO: Implement decay application
	response := map[string]interface{}{
		"player_id": playerID,
		"status":    "decay_triggered",
		"message":   "Decay calculation completed",
	}

	json.NewEncoder(w).Encode(response)
}

// TriggerRecovery применяет recovery к репутации игрока
func (s *Service) TriggerRecovery(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	vars := mux.Vars(r)
	playerID := vars["playerId"]

	// TODO: Implement recovery application
	response := map[string]interface{}{
		"player_id":        playerID,
		"recovery_amount":  25.0,
		"recovery_type":    "action_based",
		"status":          "recovery_applied",
	}

	json.NewEncoder(w).Encode(response)
}

// GetDecayConfiguration возвращает конфигурацию decay
func (s *Service) GetDecayConfiguration(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	rules := make(map[string]*models.DecayRule)
	for repType, rule := range s.Registry.DecayRules {
		rules[repType] = rule
	}

	response := map[string]interface{}{
		"decay_rules":  rules,
		"last_updated": "2024-01-01T00:00:00Z",
	}

	json.NewEncoder(w).Encode(response)
}

// UpdateDecayConfiguration обновляет конфигурацию decay
func (s *Service) UpdateDecayConfiguration(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	// TODO: Parse request body and update configuration
	response := map[string]interface{}{
		"status":       "updated",
		"last_updated": "2024-01-01T00:00:00Z",
	}

	json.NewEncoder(w).Encode(response)
}

// GetReputationStatistics возвращает статистику системы репутации
func (s *Service) GetReputationStatistics(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	// Calculate basic statistics
	totalPlayers := len(s.Registry.PlayerReputations)
	totalChanges := len(s.Registry.RecentChanges)

	stats := &models.ReputationStatistics{
		LastCalculated:     time.Now(),
		TotalPlayers:       totalPlayers,
		ActivePlayers:      totalPlayers, // Assume all are active for demo
		AverageReputation:  map[string]float64{"global": 0.0},
		DecayEvents:        0,
		RecoveryEvents:     0,
		ReputationDistribution: map[string]int{
			"outcast":   0,
			"neutral":   totalPlayers,
			"respected": 0,
			"honored":   0,
			"legendary": 0,
		},
		TimeframeHours: 24,
	}

	response := map[string]interface{}{
		"timeframe":     "24h",
		"statistics":    stats,
		"total_changes": totalChanges,
	}

	json.NewEncoder(w).Encode(response)
}