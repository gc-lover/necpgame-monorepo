package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type PrestigeHandlers struct {
	service PrestigeServiceInterface
	logger  *logrus.Logger
}

func NewPrestigeHandlers(service PrestigeServiceInterface) *PrestigeHandlers {
	return &PrestigeHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *PrestigeHandlers) GetPrestigeInfo(w http.ResponseWriter, r *http.Request) {
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

	info, err := h.service.GetPrestigeInfo(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get prestige info")
		h.respondError(w, http.StatusInternalServerError, "failed to get prestige info")
		return
	}

	if info == nil {
		h.respondError(w, http.StatusNotFound, "prestige info not found")
		return
	}

	h.respondJSON(w, http.StatusOK, info)
}

func (h *PrestigeHandlers) ResetPrestige(w http.ResponseWriter, r *http.Request) {
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

	var req ResetPrestigeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if !req.Confirm {
		h.respondError(w, http.StatusBadRequest, "prestige reset requires confirmation")
		return
	}

	info, err := h.service.ResetPrestige(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to reset prestige")
		if err.Error() == "maximum prestige level reached" {
			h.respondError(w, http.StatusForbidden, "maximum prestige level reached")
			return
		}
		if err.Error() == "prestige requirements not met" {
			h.respondError(w, http.StatusForbidden, "prestige requirements not met")
			return
		}
		h.respondError(w, http.StatusInternalServerError, "failed to reset prestige")
		return
	}

	h.respondJSON(w, http.StatusOK, info)
}

func (h *PrestigeHandlers) GetPrestigeBonuses(w http.ResponseWriter, r *http.Request) {
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

	bonuses, err := h.service.GetPrestigeBonuses(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get prestige bonuses")
		h.respondError(w, http.StatusInternalServerError, "failed to get prestige bonuses")
		return
	}

	if bonuses == nil {
		h.respondError(w, http.StatusNotFound, "prestige bonuses not found")
		return
	}

	h.respondJSON(w, http.StatusOK, bonuses)
}

func (h *PrestigeHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *PrestigeHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	}
	h.respondJSON(w, status, errorResponse)
}

