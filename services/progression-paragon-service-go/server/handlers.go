package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
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

func (h *ParagonHandlers) GetParagonLevels(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

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

	h.respondJSON(w, http.StatusOK, levels)
}

func (h *ParagonHandlers) DistributeParagonPoints(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var req struct {
		Allocations []struct {
			StatType string `json:"stat_type"`
			Points   int    `json:"points"`
		} `json:"allocations"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	allocations := make([]ParagonAllocation, len(req.Allocations))
	for i, a := range req.Allocations {
		allocations[i] = ParagonAllocation{
			StatType:       a.StatType,
			PointsAllocated: a.Points,
		}
	}

	levels, err := h.service.DistributeParagonPoints(r.Context(), characterID, allocations)
	if err != nil {
		h.logger.WithError(err).Error("Failed to distribute paragon points")
		h.respondError(w, http.StatusInternalServerError, "failed to distribute paragon points")
		return
	}

	h.respondJSON(w, http.StatusOK, levels)
}

func (h *ParagonHandlers) GetParagonStats(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

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

	h.respondJSON(w, http.StatusOK, stats)
}

func (h *ParagonHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ParagonHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	}
	h.respondJSON(w, status, errorResponse)
}

