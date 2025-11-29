package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/combat-sandevistan-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type SandevistanHandlers struct {
	logger *logrus.Logger
}

func NewSandevistanHandlers() *SandevistanHandlers {
	return &SandevistanHandlers{
		logger: GetLogger(),
	}
}

func (h *SandevistanHandlers) ActivateSandevistan(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("player_id", playerId).Info("ActivateSandevistan request")

	activationId := openapi_types.UUID{}
	startedAt := time.Now()
	expiresAt := startedAt.Add(10 * time.Second)
	phase := api.SandevistanActivationPhase("active")
	actionBudgetRemaining := 100

	response := api.SandevistanActivation{
		ActivationId:          activationId,
		StartedAt:             startedAt,
		ExpiresAt:             expiresAt,
		Phase:                 phase,
		ActionBudgetRemaining: &actionBudgetRemaining,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) DeactivateSandevistan(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("player_id", playerId).Info("DeactivateSandevistan request")

	status := "deactivated"
	response := api.StatusResponse{
		Status: &status,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) GetSandevistanStatus(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("player_id", playerId).Info("GetSandevistanStatus request")

	isActive := false
	phase := api.SandevistanStatusPhase("idle")
	cooldownRemaining := 0
	actionBudgetRemaining := 100
	heatStacks := 0
	temporalMarksCount := 0

	response := api.SandevistanStatus{
		IsActive:              isActive,
		Phase:                 phase,
		CooldownRemaining:     cooldownRemaining,
		ActionBudgetRemaining: &actionBudgetRemaining,
		HeatStacks:            &heatStacks,
		TemporalMarksCount:    &temporalMarksCount,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) UseActionBudget(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.UseActionBudgetJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"player_id": playerId,
		"actions":   req.Actions,
	}).Info("UseActionBudget request")

	budgetRemaining := 100
	executedActions := []struct {
		ActionType *string    `json:"action_type,omitempty"`
		Success    *bool      `json:"success,omitempty"`
		Timestamp  *time.Time `json:"timestamp,omitempty"`
	}{}

	response := api.ActionBudgetResult{
		BudgetRemaining:  budgetRemaining,
		ExecutedActions: executedActions,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) SetTemporalMarks(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.SetTemporalMarksJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"player_id":  playerId,
		"target_ids": req.TargetIds,
	}).Info("SetTemporalMarks request")

	status := "marks_set"
	response := api.StatusResponse{
		Status: &status,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) GetTemporalMarks(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("player_id", playerId).Info("GetTemporalMarks request")

	marks := []api.TemporalMark{}

	response := map[string]interface{}{
		"marks": marks,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) ApplyCoolingCartridge(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.ApplyCoolingCartridgeJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	h.logger.WithField("player_id", playerId).Info("ApplyCoolingCartridge request")

	heatStacksRemoved := 5
	newHeatLevel := 0
	cooldownReduced := 10
	cyberpsychosisRisk := float32(0.1)

	response := api.CoolingResult{
		HeatStacksRemoved:  heatStacksRemoved,
		NewHeatLevel:       newHeatLevel,
		CooldownReduced:    &cooldownReduced,
		CyberpsychosisRisk: &cyberpsychosisRisk,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) GetHeatStatus(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("player_id", playerId).Info("GetHeatStatus request")

	currentStacks := 0
	maxStacks := 10
	isOverstress := false
	cyberpsychosisRisk := float32(0.0)

	response := api.HeatStatus{
		CurrentStacks:     currentStacks,
		MaxStacks:         maxStacks,
		IsOverstress:      isOverstress,
		CyberpsychosisRisk: &cyberpsychosisRisk,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) ApplyCounterplay(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.ApplyCounterplayJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	h.logger.WithField("player_id", playerId).Info("ApplyCounterplay request")

	sandevistanInterrupted := false
	effectApplied := api.CounterplayResultEffectApplied("stunned")
	phaseEnded := false
	actionBudgetReduced := 10

	response := api.CounterplayResult{
		SandevistanInterrupted: sandevistanInterrupted,
		EffectApplied:          effectApplied,
		PhaseEnded:             &phaseEnded,
		ActionBudgetReduced:    &actionBudgetReduced,
	}

	respondJSON(w, http.StatusOK, response)
}

