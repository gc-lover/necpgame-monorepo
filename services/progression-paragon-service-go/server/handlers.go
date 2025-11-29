package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/progression-paragon-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type ParagonHandlers struct {
	service ParagonServiceInterface
	logger  *logrus.Logger
}

func NewParagonHandlers(service ParagonServiceInterface) *ParagonHandlers {
	return &ParagonHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ParagonHandlers) GetParagonLevels(w http.ResponseWriter, r *http.Request, params api.GetParagonLevelsParams) {
	characterID := uuid.UUID(params.CharacterId)

	levels, err := h.service.GetParagonLevels(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get paragon levels")
		h.respondError(w, http.StatusInternalServerError, "failed to get paragon levels")
		return
	}

	if levels == nil {
		h.respondError(w, http.StatusNotFound, "paragon levels not found")
		return
	}

	apiLevels := convertParagonLevelsToAPI(levels)
	h.respondJSON(w, http.StatusOK, apiLevels)
}

func (h *ParagonHandlers) DistributeParagonPoints(w http.ResponseWriter, r *http.Request, params api.DistributeParagonPointsParams) {
	characterID := uuid.UUID(params.CharacterId)

	var req api.DistributeParagonPointsJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	allocations := make([]ParagonAllocation, len(req.Allocations))
	for i, a := range req.Allocations {
		allocations[i] = ParagonAllocation{
			StatType:        string(a.StatType),
			PointsAllocated: a.Points,
		}
	}

	levels, err := h.service.DistributeParagonPoints(r.Context(), characterID, allocations)
	if err != nil {
		h.logger.WithError(err).Error("Failed to distribute paragon points")
		h.respondError(w, http.StatusInternalServerError, "failed to distribute paragon points")
		return
	}

	apiLevels := convertParagonLevelsToAPI(levels)
	h.respondJSON(w, http.StatusOK, apiLevels)
}

func (h *ParagonHandlers) GetParagonStats(w http.ResponseWriter, r *http.Request, params api.GetParagonStatsParams) {
	characterID := uuid.UUID(params.CharacterId)

	stats, err := h.service.GetParagonStats(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get paragon stats")
		h.respondError(w, http.StatusInternalServerError, "failed to get paragon stats")
		return
	}

	if stats == nil {
		h.respondError(w, http.StatusNotFound, "paragon stats not found")
		return
	}

	apiStats := convertParagonStatsToAPI(stats)
	h.respondJSON(w, http.StatusOK, apiStats)
}

func convertParagonLevelsToAPI(levels *ParagonLevels) api.ParagonLevels {
	characterID := openapi_types.UUID(levels.CharacterID)
	paragonLevel := levels.ParagonLevel
	paragonPointsTotal := levels.ParagonPointsTotal
	paragonPointsSpent := levels.ParagonPointsSpent
	paragonPointsAvailable := levels.ParagonPointsAvailable
	experienceCurrent := int(levels.ExperienceCurrent)
	experienceRequired := int(levels.ExperienceRequired)
	updatedAt := levels.UpdatedAt

	allocations := make([]api.ParagonAllocation, len(levels.Allocations))
	for i, a := range levels.Allocations {
		statType := api.ParagonAllocationStatType(a.StatType)
		pointsAllocated := a.PointsAllocated
		allocations[i] = api.ParagonAllocation{
			StatType:        &statType,
			PointsAllocated: &pointsAllocated,
		}
	}

	return api.ParagonLevels{
		CharacterId:            &characterID,
		ParagonLevel:            &paragonLevel,
		ParagonPointsTotal:      &paragonPointsTotal,
		ParagonPointsSpent:      &paragonPointsSpent,
		ParagonPointsAvailable: &paragonPointsAvailable,
		ExperienceCurrent:      &experienceCurrent,
		ExperienceRequired:     &experienceRequired,
		Allocations:            &allocations,
		UpdatedAt:              &updatedAt,
	}
}

func convertParagonStatsToAPI(stats *ParagonStats) api.ParagonStats {
	characterID := openapi_types.UUID(stats.CharacterID)
	totalParagonLevels := stats.TotalParagonLevels
	totalPointsEarned := stats.TotalPointsEarned
	totalPointsSpent := stats.TotalPointsSpent
	pointsByStat := stats.PointsByStat
	globalRank := stats.GlobalRank
	percentile := float32(stats.Percentile)

	return api.ParagonStats{
		CharacterId:        &characterID,
		TotalParagonLevels: &totalParagonLevels,
		TotalPointsEarned:  &totalPointsEarned,
		TotalPointsSpent:   &totalPointsSpent,
		PointsByStat:       &pointsByStat,
		GlobalRank:         &globalRank,
		Percentile:         &percentile,
	}
}

func (h *ParagonHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ParagonHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

