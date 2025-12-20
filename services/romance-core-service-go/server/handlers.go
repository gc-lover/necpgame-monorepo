// Issue: #140876112
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"go.uber.org/zap"
)

// CalculateChemistryHandler обрабатывает POST /api/v1/romance/calculate-chemistry
func (s *RomanceCoreServer) CalculateChemistryHandler(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerTraits map[string]interface{} `json:"player_traits"`
		TargetTraits map[string]interface{} `json:"target_traits"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	chemistry, err := s.romanceService.CalculateChemistry(r.Context(), req.PlayerTraits, req.TargetTraits)
	if err != nil {
		s.logger.Error("Failed to calculate chemistry", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"chemistry_score": chemistry,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// CalculateEventScoreHandler обрабатывает POST /api/v1/romance/calculate-event-score
func (s *RomanceCoreServer) CalculateEventScoreHandler(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Event   RomanceEvent   `json:"event"`
		Context RomanceContext `json:"context"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	score, err := s.romanceService.CalculateFinalEventScore(r.Context(), req.Event, req.Context)
	if err != nil {
		s.logger.Error("Failed to calculate event score", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"event_id":    req.Event.ID,
		"final_score": score,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// SelectEventsHandler обрабатывает POST /api/v1/romance/select-events
func (s *RomanceCoreServer) SelectEventsHandler(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Context         RomanceContext `json:"context"`
		AvailableEvents []RomanceEvent `json:"available_events"`
		Count           int            `json:"count"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	if req.Count <= 0 || req.Count > 20 {
		req.Count = 5 // default
	}

	events, err := s.romanceService.SelectNextEvents(r.Context(), req.Context, req.AvailableEvents, req.Count)
	if err != nil {
		s.logger.Error("Failed to select events", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"selected_events": events,
		"count":           len(events),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// AdaptEventHandler обрабатывает POST /api/v1/romance/adapt-event
func (s *RomanceCoreServer) AdaptEventHandler(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Event   RomanceEvent   `json:"event"`
		Context RomanceContext `json:"context"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	adaptedEvent, err := s.romanceService.AdaptEventToCulture(r.Context(), req.Event, req.Context)
	if err != nil {
		s.logger.Error("Failed to adapt event", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"original_event": req.Event,
		"adapted_event":  adaptedEvent,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// ValidateTriggersHandler обрабатывает POST /api/v1/romance/validate-triggers
func (s *RomanceCoreServer) ValidateTriggersHandler(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Event   RomanceEvent   `json:"event"`
		Context RomanceContext `json:"context"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	valid, reason, err := s.romanceService.ValidateEventTriggers(r.Context(), req.Event, req.Context)
	if err != nil {
		s.logger.Error("Failed to validate triggers", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"event_id": req.Event.ID,
		"is_valid": valid,
		"reason":   reason,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// GetRomanceStatsHandler обрабатывает GET /api/v1/romance/stats
func (s *RomanceCoreServer) GetRomanceStatsHandler(w http.ResponseWriter, r *http.Request) {
	playerID := r.URL.Query().Get("player_id")
	if playerID == "" {
		http.Error(w, "player_id is required", http.StatusBadRequest)
		return
	}

	// Получаем статистику из БД
	stats, err := s.getRomanceStatsFromDB(r.Context(), playerID)
	if err != nil {
		s.logger.Error("Failed to get romance stats", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(stats)
}

// getRomanceStatsFromDB получает статистику романтики из БД
func (s *RomanceCoreServer) getRomanceStatsFromDB(ctx context.Context, playerID string) (map[string]interface{}, error) {
	query := `
		SELECT
			COUNT(*) as total_relationships,
			COUNT(CASE WHEN is_romantic THEN 1 END) as romantic_relationships,
			COUNT(CASE WHEN relationship_stage = 'dating' THEN 1 END) as dating_relationships,
			COUNT(CASE WHEN relationship_stage = 'in_relationship' THEN 1 END) as in_relationship_count,
			AVG(relationship_score) as avg_relationship_score,
			AVG(chemistry_score) as avg_chemistry_score,
			AVG(trust_score) as avg_trust_score
		FROM social.romance_relationships
		WHERE player_id = $1 AND is_active = true
	`

	var stats struct {
		TotalRelationships    int     `json:"total_relationships"`
		RomanticRelationships int     `json:"romantic_relationships"`
		DatingRelationships   int     `json:"dating_relationships"`
		InRelationshipCount   int     `json:"in_relationship_count"`
		AvgRelationshipScore  float64 `json:"avg_relationship_score"`
		AvgChemistryScore     float64 `json:"avg_chemistry_score"`
		AvgTrustScore         float64 `json:"avg_trust_score"`
	}

	err := s.db.QueryRowContext(ctx, query, playerID).Scan(
		&stats.TotalRelationships,
		&stats.RomanticRelationships,
		&stats.DatingRelationships,
		&stats.InRelationshipCount,
		&stats.AvgRelationshipScore,
		&stats.AvgChemistryScore,
		&stats.AvgTrustScore,
	)

	if err != nil {
		return nil, err
	}

	// Конвертируем в map для JSON ответа
	result := map[string]interface{}{
		"total_relationships":    stats.TotalRelationships,
		"romantic_relationships": stats.RomanticRelationships,
		"dating_relationships":   stats.DatingRelationships,
		"in_relationship_count":  stats.InRelationshipCount,
		"avg_relationship_score": stats.AvgRelationshipScore,
		"avg_chemistry_score":    stats.AvgChemistryScore,
		"avg_trust_score":        stats.AvgTrustScore,
	}

	return result, nil
}

// HealthCheckHandler проверяет здоровье сервиса
func (s *RomanceCoreServer) HealthCheckHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "healthy", "service": "romance-core-service"}`))
}

// ReadinessCheckHandler проверяет готовность сервиса
func (s *RomanceCoreServer) ReadinessCheckHandler(w http.ResponseWriter, r *http.Request) {
	// Проверяем подключение к БД
	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()

	if err := s.db.PingContext(ctx); err != nil {
		s.logger.Error("Database not ready", zap.Error(err))
		http.Error(w, "Service not ready", http.StatusServiceUnavailable)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "ready", "service": "romance-core-service"}`))
}

// MetricsHandler предоставляет метрики сервиса
func (s *RomanceCoreServer) MetricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"service": "romance-core-service", "version": "1.0.0", "algorithms": ["scoring", "filtering", "chemistry", "cultural_adaptation", "trigger_validation"]}`))
}
