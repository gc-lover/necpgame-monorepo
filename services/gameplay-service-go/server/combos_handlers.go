package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/pkg/combosapi"
	"github.com/sirupsen/logrus"
)

type ComboHandlers struct {
	service ComboServiceInterface
	logger  *logrus.Logger
}

func NewComboHandlers(service ComboServiceInterface) *ComboHandlers {
	return &ComboHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ComboHandlers) GetComboLoadout(w http.ResponseWriter, r *http.Request, params combosapi.GetComboLoadoutParams) {
	characterID := uuid.UUID(params.CharacterId)

	loadout, err := h.service.GetLoadout(r.Context(), characterID)
	if err != nil {
		if err.Error() == "loadout not found" {
			h.respondError(w, http.StatusNotFound, "loadout not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get combo loadout")
		h.respondError(w, http.StatusInternalServerError, "failed to get combo loadout")
		return
	}

	apiLoadout := convertLoadoutToAPI(loadout)
	h.respondJSON(w, http.StatusOK, apiLoadout)
}

func (h *ComboHandlers) UpdateComboLoadout(w http.ResponseWriter, r *http.Request) {
	var req combosapi.UpdateComboLoadoutJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.CharacterId)

	updateReq := convertUpdateLoadoutRequestFromAPI(&req)
	loadout, err := h.service.UpdateLoadout(r.Context(), characterID, updateReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update combo loadout")
		h.respondError(w, http.StatusInternalServerError, "failed to update combo loadout")
		return
	}

	apiLoadout := convertLoadoutToAPI(loadout)
	h.respondJSON(w, http.StatusOK, apiLoadout)
}

func (h *ComboHandlers) SubmitComboScore(w http.ResponseWriter, r *http.Request) {
	var req combosapi.SubmitComboScoreJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	submitReq := convertSubmitScoreRequestFromAPI(&req)
	response, err := h.service.SubmitScore(r.Context(), submitReq)
	if err != nil {
		if err.Error() == "activation not found" {
			h.respondError(w, http.StatusNotFound, "activation not found")
			return
		}
		h.logger.WithError(err).Error("Failed to submit combo score")
		h.respondError(w, http.StatusInternalServerError, "failed to submit combo score")
		return
	}

	apiResponse := convertScoreSubmissionResponseToAPI(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *ComboHandlers) GetComboAnalytics(w http.ResponseWriter, r *http.Request, params combosapi.GetComboAnalyticsParams) {
	var comboID *uuid.UUID
	if params.ComboId != nil {
		id := uuid.UUID(*params.ComboId)
		comboID = &id
	}

	var characterID *uuid.UUID
	if params.CharacterId != nil {
		id := uuid.UUID(*params.CharacterId)
		characterID = &id
	}

	limit := 20
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	response, err := h.service.GetAnalytics(r.Context(), comboID, characterID, params.PeriodStart, params.PeriodEnd, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get combo analytics")
		h.respondError(w, http.StatusInternalServerError, "failed to get combo analytics")
		return
	}

	apiResponse := convertAnalyticsResponseToAPI(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *ComboHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ComboHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := combosapi.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

