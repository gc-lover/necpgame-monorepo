// HTTP handlers for Dynamic Quests Service
// Issue: #2244
// Agent: Backend

package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"necpgame/services/dynamic-quests-service-go/internal/repository"
	"necpgame/services/dynamic-quests-service-go/internal/service"
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

// Health handles health check endpoint
func (h *Handlers) Health(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]string{
		"status":    "healthy",
		"service":   "dynamic-quests-service",
		"timestamp": time.Now().Format(time.RFC3339),
	})
}

// Ready handles readiness check endpoint
func (h *Handlers) Ready(w http.ResponseWriter, r *http.Request) {
	// In production, check database connectivity
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]string{
		"status":    "ready",
		"service":   "dynamic-quests-service",
		"timestamp": time.Now().Format(time.RFC3339),
	})
}

// ListQuests handles GET /api/v1/quests - list available quests
func (h *Handlers) ListQuests(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Parse query parameters
	playerLevelStr := r.URL.Query().Get("player_level")
	playerLevel := 1 // default
	if playerLevelStr != "" {
		if level, err := strconv.Atoi(playerLevelStr); err == nil {
			playerLevel = level
		}
	}

	playerID := r.URL.Query().Get("player_id")
	var reputation *repository.PlayerReputation
	if playerID != "" {
		rep, err := h.service.GetPlayerReputation(ctx, playerID)
		if err != nil {
			h.logger.Errorf("Failed to get player reputation: %v", err)
			// Continue without reputation filtering
		} else {
			reputation = rep
		}
	}

	quests, err := h.service.ListAvailableQuests(ctx, playerLevel, reputation)
	if err != nil {
		h.logger.Errorf("Failed to list quests: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to list quests")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"quests": quests,
		"count":  len(quests),
	})
}

// CreateQuest handles POST /api/v1/quests - create new quest definition
func (h *Handlers) CreateQuest(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var quest repository.QuestDefinition
	if err := json.NewDecoder(r.Body).Decode(&quest); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate required fields
	if quest.QuestID == "" || quest.Title == "" {
		h.respondError(w, http.StatusBadRequest, "Quest ID and title are required")
		return
	}

	if err := h.service.repo.CreateQuestDefinition(ctx, &quest); err != nil {
		h.logger.Errorf("Failed to create quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to create quest")
		return
	}

	h.respondJSON(w, http.StatusCreated, map[string]string{
		"message":    "Quest created successfully",
		"quest_id":   quest.QuestID,
	})
}

// GetQuest handles GET /api/v1/quests/{questId} - get quest definition
func (h *Handlers) GetQuest(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	questID := chi.URLParam(r, "questId")

	quest, err := h.service.repo.GetQuestDefinition(ctx, questID)
	if err != nil {
		h.logger.Errorf("Failed to get quest: %v", err)
		h.respondError(w, http.StatusNotFound, "Quest not found")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// UpdateQuest handles PUT /api/v1/quests/{questId} - update quest definition
func (h *Handlers) UpdateQuest(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	questID := chi.URLParam(r, "questId")

	var quest repository.QuestDefinition
	if err := json.NewDecoder(r.Body).Decode(&quest); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	quest.QuestID = questID // Ensure ID matches URL param

	if err := h.service.repo.CreateQuestDefinition(ctx, &quest); err != nil {
		h.logger.Errorf("Failed to update quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to update quest")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{
		"message":  "Quest updated successfully",
		"quest_id": questID,
	})
}

// DeleteQuest handles DELETE /api/v1/quests/{questId} - delete quest definition
func (h *Handlers) DeleteQuest(w http.ResponseWriter, r *http.Request) {
	// Note: In production, soft delete would be implemented
	h.respondError(w, http.StatusNotImplemented, "Delete operation not implemented")
}

// StartQuest handles POST /api/v1/quests/{questId}/start - start quest for player
func (h *Handlers) StartQuest(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	questID := chi.URLParam(r, "questId")

	var req struct {
		PlayerID string `json:"player_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.PlayerID == "" {
		h.respondError(w, http.StatusBadRequest, "Player ID is required")
		return
	}

	if err := h.service.StartQuest(ctx, req.PlayerID, questID); err != nil {
		h.logger.Errorf("Failed to start quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to start quest")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{
		"message":  "Quest started successfully",
		"player_id": req.PlayerID,
		"quest_id": questID,
	})
}

// GetQuestState handles GET /api/v1/quests/{questId}/state - get player quest state
func (h *Handlers) GetQuestState(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	questID := chi.URLParam(r, "questId")
	playerID := r.URL.Query().Get("player_id")

	if playerID == "" {
		h.respondError(w, http.StatusBadRequest, "Player ID is required")
		return
	}

	state, err := h.service.GetPlayerQuestState(ctx, playerID, questID)
	if err != nil {
		h.logger.Errorf("Failed to get quest state: %v", err)
		h.respondError(w, http.StatusNotFound, "Quest state not found")
		return
	}

	h.respondJSON(w, http.StatusOK, state)
}

// MakeChoice handles POST /api/v1/quests/{questId}/choices - process player choice
func (h *Handlers) MakeChoice(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	questID := chi.URLParam(r, "questId")

	var req struct {
		PlayerID string `json:"player_id"`
		Choice   service.QuestChoice `json:"choice"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.PlayerID == "" || req.Choice.ChoicePoint == "" {
		h.respondError(w, http.StatusBadRequest, "Player ID and choice are required")
		return
	}

	result, err := h.service.ProcessChoice(ctx, req.PlayerID, questID, req.Choice)
	if err != nil {
		h.logger.Errorf("Failed to process choice: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to process choice")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

// CompleteQuest handles POST /api/v1/quests/{questId}/complete - complete quest
func (h *Handlers) CompleteQuest(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "Manual completion not implemented")
}

// GetPlayerQuests handles GET /api/v1/players/{playerId}/quests - get player's quests
func (h *Handlers) GetPlayerQuests(w http.ResponseWriter, r *http.Request) {
	playerID := chi.URLParam(r, "playerId")

	// In a full implementation, this would query the database for all player quests
	// For now, return a placeholder response
	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id": playerID,
		"quests":    []interface{}{}, // Placeholder
		"message":   "Player quests endpoint - implementation pending",
	})
}

// GetPlayerReputation handles GET /api/v1/players/{playerId}/reputation - get player reputation
func (h *Handlers) GetPlayerReputation(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	playerID := chi.URLParam(r, "playerId")

	reputation, err := h.service.GetPlayerReputation(ctx, playerID)
	if err != nil {
		h.logger.Errorf("Failed to get player reputation: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to get reputation")
		return
	}

	h.respondJSON(w, http.StatusOK, reputation)
}

// ImportQuests handles POST /api/v1/admin/import - import quests from YAML files
func (h *Handlers) ImportQuests(w http.ResponseWriter, r *http.Request) {
	// This would integrate with the quest import script
	// For now, return a placeholder response
	h.respondJSON(w, http.StatusOK, map[string]string{
		"message": "Quest import functionality - implementation pending",
		"status":  "placeholder",
	})
}

// ResetPlayerProgress handles POST /api/v1/admin/reset - reset player progress (admin only)
func (h *Handlers) ResetPlayerProgress(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "Reset functionality not implemented")
}

// respondJSON sends a JSON response
func (h *Handlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Errorf("Failed to encode JSON response: %v", err)
	}
}

// respondError sends an error response
func (h *Handlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{
		"error":   message,
		"status":  http.StatusText(status),
		"code":    strconv.Itoa(status),
	})
}

