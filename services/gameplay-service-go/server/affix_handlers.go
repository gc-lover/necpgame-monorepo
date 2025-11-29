package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/sirupsen/logrus"
)

type AffixHandlers struct {
	service AffixServiceInterface
	logger  *logrus.Logger
}

func NewAffixHandlers(service AffixServiceInterface) *AffixHandlers {
	return &AffixHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *AffixHandlers) GetActiveAffixes(w http.ResponseWriter, r *http.Request) {
	response, err := h.service.GetActiveAffixes(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get active affixes")
		h.respondError(w, http.StatusInternalServerError, "failed to get active affixes")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *AffixHandlers) GetAffix(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	affixID, err := uuid.Parse(vars["id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid affix id")
		return
	}

	affix, err := h.service.GetAffix(r.Context(), affixID)
	if err != nil {
		if err.Error() == "affix not found" {
			h.respondError(w, http.StatusNotFound, "affix not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get affix")
		h.respondError(w, http.StatusInternalServerError, "failed to get affix")
		return
	}

	h.respondJSON(w, http.StatusOK, affix)
}

func (h *AffixHandlers) GetInstanceAffixes(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid instance_id")
		return
	}

	response, err := h.service.GetInstanceAffixes(r.Context(), instanceID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get instance affixes")
		h.respondError(w, http.StatusInternalServerError, "failed to get instance affixes")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *AffixHandlers) GetRotationHistory(w http.ResponseWriter, r *http.Request) {
	weeksBack := 4
	if weeksBackStr := r.URL.Query().Get("weeks_back"); weeksBackStr != "" {
		if wb, err := strconv.Atoi(weeksBackStr); err == nil && wb >= 1 && wb <= 52 {
			weeksBack = wb
		}
	}

	limit := 20
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := h.service.GetRotationHistory(r.Context(), weeksBack, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get rotation history")
		h.respondError(w, http.StatusInternalServerError, "failed to get rotation history")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *AffixHandlers) TriggerRotation(w http.ResponseWriter, r *http.Request) {
	var req models.TriggerRotationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	rotation, err := h.service.TriggerRotation(r.Context(), req.Force, req.CustomAffixes)
	if err != nil {
		if err.Error() == "rotation already exists for this week" {
			h.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		if err.Error() == "custom_affixes must contain 8-10 affixes" {
			h.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		h.logger.WithError(err).Error("Failed to trigger rotation")
		h.respondError(w, http.StatusInternalServerError, "failed to trigger rotation")
		return
	}

	h.respondJSON(w, http.StatusOK, rotation)
}

func (h *AffixHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *AffixHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

