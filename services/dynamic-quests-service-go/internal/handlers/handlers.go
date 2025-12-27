// HTTP handlers for Dynamic Quests Service
// Issue: #2244
// Agent: Backend

package handlers

import (
	"encoding/json"
	"io"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"necpgame/services/dynamic-quests-service-go/internal/repository"
	"necpgame/services/dynamic-quests-service-go/internal/service"
	"necpgame/services/dynamic-quests-service-go/pkg/models"
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

	if err := h.service.CreateQuestDefinition(ctx, &quest); err != nil {
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

	quest, err := h.service.GetQuestDefinition(ctx, questID)
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

	if err := h.service.CreateQuestDefinition(ctx, &quest); err != nil {
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
	ctx := r.Context()

	// Read YAML data from request body
	yamlData, err := io.ReadAll(r.Body)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Failed to read request body")
		return
	}

	if len(yamlData) == 0 {
		h.respondError(w, http.StatusBadRequest, "YAML data is required")
		return
	}

	// Process YAML import
	if err := h.service.ImportQuestsFromYAML(ctx, yamlData); err != nil {
		h.logger.Errorf("Failed to import quests from YAML: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to import quests")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{
		"message": "Quests imported successfully from YAML",
		"status":  "success",
	})
}

// ResetPlayerProgress handles POST /api/v1/admin/reset - reset player progress (admin only)
func (h *Handlers) ResetPlayerProgress(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "Reset functionality not implemented")
}

// GetQuestAnalytics handles GET /api/v1/admin/quests/{questId}/analytics - get quest analytics
func (h *Handlers) GetQuestAnalytics(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	questID := chi.URLParam(r, "questId")

	analytics, err := h.service.GenerateQuestAnalytics(ctx, questID)
	if err != nil {
		h.logger.Errorf("Failed to generate quest analytics: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to generate analytics")
		return
	}

	h.respondJSON(w, http.StatusOK, analytics)
}

// GenerateDynamicQuest handles POST /api/v1/admin/quests/generate - generate dynamic quest
func (h *Handlers) GenerateDynamicQuest(w http.ResponseWriter, r *http.Request) {
	var req models.QuestGenerationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Placeholder for dynamic quest generation
	response := models.QuestGenerationResponse{
		Success: false,
		Error:   "Dynamic quest generation not implemented yet",
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Detroit Quests Handlers
// Issues: #140927952, #140927958, #140927959, #140927961, #140927963

// GetConeyIslandHotDogsQuest handles GET /quests/detroit/coney-island-hot-dogs
func (h *Handlers) GetConeyIslandHotDogsQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Coney Island Hot Dogs quest request")

	quest, err := h.service.GetConeyIslandHotDogsQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Coney Island Hot Dogs quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// Get1967RiotsLegacyQuest handles GET /quests/detroit/1967-riots-legacy
func (h *Handlers) Get1967RiotsLegacyQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get 1967 Riots Legacy quest request")

	quest, err := h.service.Get1967RiotsLegacyQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get 1967 Riots Legacy quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// Get8MileRoadJourneyQuest handles GET /quests/detroit/8-mile-road-journey
func (h *Handlers) Get8MileRoadJourneyQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get 8 Mile Road Journey quest request")

	quest, err := h.service.Get8MileRoadJourneyQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get 8 Mile Road Journey quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetRedWingsHockeyQuest handles GET /quests/detroit/red-wings-hockey
func (h *Handlers) GetRedWingsHockeyQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Red Wings Hockey quest request")

	quest, err := h.service.GetRedWingsHockeyQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Red Wings Hockey quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetRevivalHopeQuest handles GET /quests/detroit/revival-hope
func (h *Handlers) GetRevivalHopeQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Revival and Hope quest request")

	quest, err := h.service.GetRevivalHopeQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Revival and Hope quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetLakeMichiganQuest handles GET /quests/chicago/lake-michigan
// Issue: #140928958
func (h *Handlers) GetLakeMichiganQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Lake Michigan quest request")

	quest, err := h.service.GetLakeMichiganQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Lake Michigan quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetCubsWrigleyFieldQuest handles GET /quests/chicago/cubs-wrigley-field
// Issue: #140928959
func (h *Handlers) GetCubsWrigleyFieldQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Chicago Cubs Wrigley Field quest request")

	quest, err := h.service.GetCubsWrigleyFieldQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Chicago Cubs Wrigley Field quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetWillisTowerQuest handles GET /quests/chicago/willis-tower
// Issue: #140928947
func (h *Handlers) GetWillisTowerQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Willis Tower quest request")

	quest, err := h.service.GetWillisTowerQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Willis Tower quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetDeepDishPizzaQuest handles GET /quests/chicago/deep-dish-pizza
// Issue: #140928949
func (h *Handlers) GetDeepDishPizzaQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Deep Dish Pizza quest request")

	quest, err := h.service.GetDeepDishPizzaQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Deep Dish Pizza quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetCapitalBuildingQuest handles GET /quests/denver/capital-building
// Issue: #140928923
func (h *Handlers) GetCapitalBuildingQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Capital Building quest request")

	quest, err := h.service.GetCapitalBuildingQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Capital Building quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetOutdoorLifestyleQuest handles GET /quests/denver/outdoor-lifestyle
// Issue: #140928921
func (h *Handlers) GetOutdoorLifestyleQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Outdoor Lifestyle quest request")

	quest, err := h.service.GetOutdoorLifestyleQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Outdoor Lifestyle quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetTangoDanceQuest handles GET /quests/buenos-aires/tango-dance
// Issue: #140929841
func (h *Handlers) GetTangoDanceQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Tango Dance quest request")

	quest, err := h.service.GetTangoDanceQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Tango Dance quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetLaBocaCaminitoQuest handles GET /quests/buenos-aires/la-boca-caminito
// Issue: #140929844
func (h *Handlers) GetLaBocaCaminitoQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get La Boca Caminito quest request")

	quest, err := h.service.GetLaBocaCaminitoQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get La Boca Caminito quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetAsadoBBQQuest handles GET /quests/buenos-aires/asado-bbq
// Issue: #140929848
func (h *Handlers) GetAsadoBBQQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Asado BBQ quest request")

	quest, err := h.service.GetAsadoBBQQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Asado BBQ quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetRecoletaCemeteryQuest handles GET /quests/buenos-aires/recoleta-cemetery
// Issue: #140929854
func (h *Handlers) GetRecoletaCemeteryQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Recoleta Cemetery quest request")

	quest, err := h.service.GetRecoletaCemeteryQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Recoleta Cemetery quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetParisOfTheSouthQuest handles GET /quests/buenos-aires/paris-of-the-south
// Issue: #140929855
func (h *Handlers) GetParisOfTheSouthQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Paris of the South quest request")

	quest, err := h.service.GetParisOfTheSouthQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Paris of the South quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetEconomicCrisisQuest handles GET /quests/buenos-aires/economic-crisis
// Issue: #140929878
func (h *Handlers) GetEconomicCrisisQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Economic Crisis quest request")

	quest, err := h.service.GetEconomicCrisisQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Economic Crisis quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetOilLegacyQuest handles GET /quests/dallas/oil-legacy
// Issue: #140928929
func (h *Handlers) GetOilLegacyQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Oil Legacy quest request")

	quest, err := h.service.GetOilLegacyQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Oil Legacy quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
}

// GetEverythingBiggerQuest handles GET /quests/dallas/everything-bigger
// Issue: #140928943
func (h *Handlers) GetEverythingBiggerQuest(w http.ResponseWriter, r *http.Request) {
	h.logger.Info("Handling get Everything Bigger quest request")

	quest, err := h.service.GetEverythingBiggerQuest(r.Context())
	if err != nil {
		h.logger.Errorf("Failed to get Everything Bigger quest: %v", err)
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve quest")
		return
	}

	h.respondJSON(w, http.StatusOK, quest)
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

