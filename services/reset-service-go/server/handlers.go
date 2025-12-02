// Issue: #1364, #141886477, #141886485
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/reset-service-go/models"
	"github.com/necpgame/reset-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type ResetHandlers struct {
	service ResetServiceInterface
	logger  *logrus.Logger
}

func NewResetHandlers(service ResetServiceInterface) *ResetHandlers {
	return &ResetHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ResetHandlers) GetResetStats(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	stats, err := h.service.GetResetStats(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get reset stats")
		h.respondError(w, http.StatusInternalServerError, "failed to get reset stats")
		return
	}

	h.respondJSON(w, http.StatusOK, stats)
}

func (h *ResetHandlers) GetResetHistory(w http.ResponseWriter, r *http.Request, params api.GetResetHistoryParams) {
	ctx := r.Context()

	var resetType *models.ResetType
	if params.Type != nil {
		rt := models.ResetType(*params.Type)
		resetType = &rt
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 {
		if *params.Limit > 100 {
			limit = 100
		} else {
			limit = *params.Limit
		}
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	response, err := h.service.GetResetHistory(ctx, resetType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get reset history")
		h.respondError(w, http.StatusInternalServerError, "failed to get reset history")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ResetHandlers) TriggerReset(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.TriggerResetRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	resetType := models.ResetType(req.Type)
	if resetType != models.ResetTypeDaily && resetType != models.ResetTypeWeekly {
		h.respondError(w, http.StatusBadRequest, "invalid reset type")
		return
	}

	err := h.service.TriggerReset(ctx, resetType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to trigger reset")
		h.respondError(w, http.StatusInternalServerError, "failed to trigger reset")
		return
	}

	response := api.SuccessResponse{
		Status: stringPtr("success"),
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *ResetHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	
	jsonData, err := json.Marshal(data)
	if err != nil {
		h.logger.WithError(err).Error("Failed to marshal JSON response")
		w.WriteHeader(http.StatusInternalServerError)
		errorResponse := api.Error{
			Error:   http.StatusText(http.StatusInternalServerError),
			Message: "failed to encode response",
		}
		if encodeErr := json.NewEncoder(w).Encode(errorResponse); encodeErr != nil {
			h.logger.WithError(encodeErr).Error("Failed to encode error response")
		}
		return
	}
	
	w.WriteHeader(status)
	if _, err := w.Write(jsonData); err != nil {
		h.logger.WithError(err).Error("Failed to write JSON response")
	}
}

func (h *ResetHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

func stringPtr(s string) *string {
	return &s
}
