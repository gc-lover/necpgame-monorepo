package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/combat-damage-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type DamageHandlers struct {
	logger *logrus.Logger
}

func NewDamageHandlers() *DamageHandlers {
	return &DamageHandlers{
		logger: GetLogger(),
	}
}

func (h *DamageHandlers) CalculateDamage(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.DamageCalculationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode CalculateDamage request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"attacker_id": req.AttackerId,
		"target_id":   req.TargetId,
		"base_damage": req.BaseDamage,
		"damage_type": req.DamageType,
	}).Info("CalculateDamage request")

	finalDamage := req.BaseDamage
	if req.Modifiers != nil {
		if req.Modifiers.IsCritical != nil && *req.Modifiers.IsCritical {
			finalDamage = finalDamage * 2
		}
		if req.Modifiers.WeakSpot != nil && *req.Modifiers.WeakSpot {
			finalDamage = int(float32(finalDamage) * 1.5)
		}
		if req.Modifiers.RangeModifier != nil {
			finalDamage = int(float32(finalDamage) * *req.Modifiers.RangeModifier)
		}
	}

	damageType := api.DamageCalculationResultDamageType(req.DamageType)
	wasCritical := false
	if req.Modifiers != nil && req.Modifiers.IsCritical != nil {
		wasCritical = *req.Modifiers.IsCritical
	}

	response := api.DamageCalculationResult{
		AttackerId:      &req.AttackerId,
		TargetId:        &req.TargetId,
		BaseDamage:      &req.BaseDamage,
		FinalDamage:     &finalDamage,
		DamageType:      &damageType,
		WasCritical:     &wasCritical,
		WasBlocked:      new(bool),
		DamageReduction: new(int),
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *DamageHandlers) ApplyEffects(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.ApplyEffectsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ApplyEffects request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"target_id": req.TargetId,
		"effects":   len(req.Effects),
	}).Info("ApplyEffects request")

	effects := make([]api.CombatEffect, 0, len(req.Effects))
	for _, effectReq := range req.Effects {
		effectType := api.CombatEffectEffectType(effectReq.EffectType)
		effect := api.CombatEffect{
			EffectName:     &effectReq.EffectName,
			EffectType:     &effectType,
			Value:          &effectReq.Value,
			Duration:       &effectReq.Duration,
			RemainingTurns: &effectReq.Duration,
		}
		effects = append(effects, effect)
	}

	type ApplyEffectsResponse struct {
		Effects []api.CombatEffect `json:"effects"`
	}

	response := ApplyEffectsResponse{
		Effects: effects,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *DamageHandlers) RemoveEffect(w http.ResponseWriter, r *http.Request, effectId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("effect_id", effectId).Info("RemoveEffect request")

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "removed"})
}

func (h *DamageHandlers) ExtendEffect(w http.ResponseWriter, r *http.Request, effectId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.ExtendEffectJSONBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ExtendEffect request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"effect_id":         effectId,
		"additional_turns":  req.AdditionalTurns,
	}).Info("ExtendEffect request")

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "extended"})
}

func (h *DamageHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *DamageHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

