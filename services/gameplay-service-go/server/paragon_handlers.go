package server

import (
	"bytes"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
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

func (h *ParagonHandlers) getParagonLevels(w http.ResponseWriter, r *http.Request) {
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

	paragon, err := h.service.GetParagonLevels(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get paragon levels")
		h.respondError(w, http.StatusInternalServerError, "failed to get paragon levels")
		return
	}

	if paragon == nil {
		h.respondError(w, http.StatusNotFound, "paragon levels not found")
		return
	}

	h.respondJSON(w, http.StatusOK, paragon)
}

func (h *ParagonHandlers) distributeParagonPoints(w http.ResponseWriter, r *http.Request) {
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

	var req models.DistributeParagonPointsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if len(req.Allocations) == 0 {
		h.respondError(w, http.StatusBadRequest, "allocations are required")
		return
	}

	paragon, err := h.service.DistributeParagonPoints(r.Context(), characterID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to distribute paragon points")
		if err.Error() == "not enough paragon points available" || 
		   err.Error() == "stat_type cannot exceed 100 points" ||
		   err.Error() == "points must be at least 1" {
			h.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			h.respondError(w, http.StatusInternalServerError, "failed to distribute paragon points")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, paragon)
}

func (h *ParagonHandlers) getParagonStats(w http.ResponseWriter, r *http.Request) {
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
	var buf bytes.Buffer
	if err := json.NewEncoder(&buf).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
		h.respondError(w, http.StatusInternalServerError, "Failed to encode JSON response")
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		h.logger.WithError(err).Error("Failed to write JSON response")
	}
}

func (h *ParagonHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

