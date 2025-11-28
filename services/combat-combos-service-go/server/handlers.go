package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/combat-ai-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *Service
	logger  *logrus.Logger
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *Handlers) GetAIProfiles(w http.ResponseWriter, r *http.Request, params api.GetAIProfilesParams) {
	limit := 50
	offset := 0

	var tier, faction *string
	if params.DifficultyLayer != nil {
		tierStr := string(*params.DifficultyLayer)
		tier = &tierStr
	}
	if params.FactionId != nil {
		faction = params.FactionId
	}

	profiles, total, err := h.service.GetAIProfiles(r.Context(), tier, faction, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get AI profiles")
		h.respondError(w, http.StatusInternalServerError, "Failed to get AI profiles")
		return
	}

	response := map[string]interface{}{
		"profiles": profiles,
		"total":    total,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetAIProfile(w http.ResponseWriter, r *http.Request, profileId openapi_types.UUID) {
	profile, err := h.service.GetAIProfile(r.Context(), profileId.String())
	if err != nil {
		if err.Error() == "profile not found" {
			h.respondError(w, http.StatusNotFound, "Profile not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get AI profile")
		h.respondError(w, http.StatusInternalServerError, "Failed to get AI profile")
		return
	}

	h.respondJSON(w, http.StatusOK, profile)
}

func (h *Handlers) CreateEncounter(w http.ResponseWriter, r *http.Request) {
	var req api.CreateEncounterRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	encounter, err := h.service.CreateEncounter(r.Context(), req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create encounter")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusCreated, encounter)
}

func (h *Handlers) GetEncounter(w http.ResponseWriter, r *http.Request, encounterId openapi_types.UUID) {
	encounter, err := h.service.GetEncounter(r.Context(), encounterId.String())
	if err != nil {
		if err.Error() == "encounter not found" {
			h.respondError(w, http.StatusNotFound, "Encounter not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get encounter")
		h.respondError(w, http.StatusInternalServerError, "Failed to get encounter")
		return
	}

	h.respondJSON(w, http.StatusOK, encounter)
}

func (h *Handlers) StartEncounter(w http.ResponseWriter, r *http.Request, encounterId openapi_types.UUID) {
	if err := h.service.StartEncounter(r.Context(), encounterId.String()); err != nil {
		h.logger.WithError(err).Error("Failed to start encounter")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "started"})
}

func (h *Handlers) EndEncounter(w http.ResponseWriter, r *http.Request, encounterId openapi_types.UUID) {
	if err := h.service.EndEncounter(r.Context(), encounterId.String()); err != nil {
		h.logger.WithError(err).Error("Failed to end encounter")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "completed"})
}

func (h *Handlers) AdvanceRaidPhase(w http.ResponseWriter, r *http.Request, raidId openapi_types.UUID) {
	var req api.RaidPhaseRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := h.service.AdvanceRaidPhase(r.Context(), raidId.String(), req); err != nil {
		h.logger.WithError(err).Error("Failed to advance raid phase")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "advanced"})
}

func (h *Handlers) GetProfileTelemetry(w http.ResponseWriter, r *http.Request, profileId openapi_types.UUID) {
	telemetry, err := h.service.GetProfileTelemetry(r.Context(), profileId.String())
	if err != nil {
		if err.Error() == "telemetry not found" {
			h.respondError(w, http.StatusNotFound, "Telemetry not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get telemetry")
		h.respondError(w, http.StatusInternalServerError, "Failed to get telemetry")
		return
	}

	h.respondJSON(w, http.StatusOK, telemetry)
}

func (h *Handlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *Handlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}
