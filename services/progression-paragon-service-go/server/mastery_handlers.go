package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type MasteryHandlers struct {
	service MasteryServiceInterface
	logger  *logrus.Logger
}

func NewMasteryHandlers(service MasteryServiceInterface) *MasteryHandlers {
	return &MasteryHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *MasteryHandlers) GetMasteryLevels(w http.ResponseWriter, r *http.Request) {
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

	levels, err := h.service.GetMasteryLevels(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get mastery levels")
		h.respondError(w, http.StatusInternalServerError, "failed to get mastery levels")
		return
	}

	if levels == nil {
		h.respondError(w, http.StatusNotFound, "mastery levels not found")
		return
	}

	h.respondJSON(w, http.StatusOK, levels)
}

func (h *MasteryHandlers) GetMasteryProgress(w http.ResponseWriter, r *http.Request) {
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

	vars := mux.Vars(r)
	masteryType := vars["type"]
	if masteryType == "" {
		h.respondError(w, http.StatusBadRequest, "mastery type is required")
		return
	}

	progress, err := h.service.GetMasteryProgress(r.Context(), characterID, masteryType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get mastery progress")
		if err.Error() == "invalid mastery type: "+masteryType {
			h.respondError(w, http.StatusBadRequest, "invalid mastery type")
			return
		}
		h.respondError(w, http.StatusInternalServerError, "failed to get mastery progress")
		return
	}

	if progress == nil {
		h.respondError(w, http.StatusNotFound, "mastery progress not found")
		return
	}

	h.respondJSON(w, http.StatusOK, progress)
}

func (h *MasteryHandlers) GetMasteryRewards(w http.ResponseWriter, r *http.Request) {
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

	var masteryType *string
	masteryTypeStr := r.URL.Query().Get("mastery_type")
	if masteryTypeStr != "" {
		masteryType = &masteryTypeStr
	}

	rewards, err := h.service.GetMasteryRewards(r.Context(), characterID, masteryType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get mastery rewards")
		if err.Error() == "invalid mastery type: "+*masteryType {
			h.respondError(w, http.StatusBadRequest, "invalid mastery type")
			return
		}
		h.respondError(w, http.StatusInternalServerError, "failed to get mastery rewards")
		return
	}

	if rewards == nil {
		h.respondError(w, http.StatusNotFound, "mastery rewards not found")
		return
	}

	h.respondJSON(w, http.StatusOK, rewards)
}

func (h *MasteryHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *MasteryHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	}
	h.respondJSON(w, status, errorResponse)
}

