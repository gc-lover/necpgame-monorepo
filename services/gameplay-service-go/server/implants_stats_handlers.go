// Issue: #142109960
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/pkg/implantsstatsapi"
	"github.com/sirupsen/logrus"
)

type ImplantsStatsHandlers struct {
	service ImplantsStatsServiceInterface
	logger  *logrus.Logger
}

func NewImplantsStatsHandlers(service ImplantsStatsServiceInterface) *ImplantsStatsHandlers {
	return &ImplantsStatsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ImplantsStatsHandlers) GetEnergyStatus(w http.ResponseWriter, r *http.Request, params implantsstatsapi.GetEnergyStatusParams) {
	var characterID *uuid.UUID
	if params.CharacterId != nil {
		id := uuid.UUID(*params.CharacterId)
		characterID = &id
	}

	status, err := h.service.GetEnergyStatus(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get energy status")
		h.respondError(w, http.StatusInternalServerError, "failed to get energy status")
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

func (h *ImplantsStatsHandlers) GetHumanityStatus(w http.ResponseWriter, r *http.Request, params implantsstatsapi.GetHumanityStatusParams) {
	var characterID *uuid.UUID
	if params.CharacterId != nil {
		id := uuid.UUID(*params.CharacterId)
		characterID = &id
	}

	status, err := h.service.GetHumanityStatus(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get humanity status")
		h.respondError(w, http.StatusInternalServerError, "failed to get humanity status")
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

func (h *ImplantsStatsHandlers) CheckCompatibility(w http.ResponseWriter, r *http.Request, params implantsstatsapi.CheckCompatibilityParams) {
	var characterID *uuid.UUID
	if params.CharacterId != nil {
		id := uuid.UUID(*params.CharacterId)
		characterID = &id
	}

	implantID := uuid.UUID(params.ImplantId)
	if implantID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "implant_id is required")
		return
	}

	result, err := h.service.CheckCompatibility(r.Context(), characterID, implantID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to check compatibility")
		h.respondError(w, http.StatusInternalServerError, "failed to check compatibility")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *ImplantsStatsHandlers) GetSetBonuses(w http.ResponseWriter, r *http.Request, params implantsstatsapi.GetSetBonusesParams) {
	var characterID *uuid.UUID
	if params.CharacterId != nil {
		id := uuid.UUID(*params.CharacterId)
		characterID = &id
	}

	bonuses, err := h.service.GetSetBonuses(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get set bonuses")
		h.respondError(w, http.StatusInternalServerError, "failed to get set bonuses")
		return
	}

	h.respondJSON(w, http.StatusOK, bonuses)
}

func (h *ImplantsStatsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ImplantsStatsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := implantsstatsapi.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

