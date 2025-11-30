// Issue: #142109884, #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/pkg/damageapi"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type DamageHandlers struct {
	service DamageServiceInterface
	logger  *logrus.Logger
}

func NewDamageHandlers(service DamageServiceInterface) *DamageHandlers {
	return &DamageHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *DamageHandlers) CalculateDamage(w http.ResponseWriter, r *http.Request) {
	var req damageapi.CalculateDamageJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	attackerID := uuid.UUID(req.AttackerId)
	targetID := uuid.UUID(req.TargetId)
	if attackerID == uuid.Nil || targetID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "attacker_id and target_id are required")
		return
	}

	if req.BaseDamage == nil || *req.BaseDamage < 0 {
		h.respondError(w, http.StatusBadRequest, "base_damage must be non-negative")
		return
	}

	var modifiers *DamageModifiers
	if req.Modifiers != nil {
		modifiers = &DamageModifiers{
			IsCritical:   req.Modifiers.IsCritical != nil && *req.Modifiers.IsCritical,
			WeakSpot:     req.Modifiers.WeakSpot != nil && *req.Modifiers.WeakSpot,
			RangeModifier: 1.0,
		}
		if req.Modifiers.RangeModifier != nil {
			if *req.Modifiers.RangeModifier < 0.5 || *req.Modifiers.RangeModifier > 1.5 {
				h.respondError(w, http.StatusBadRequest, "range_modifier must be between 0.5 and 1.5")
				return
			}
			modifiers.RangeModifier = float32(*req.Modifiers.RangeModifier)
		}
	}

	result, err := h.service.CalculateDamage(r.Context(), attackerID, targetID, *req.BaseDamage, string(req.DamageType), modifiers)
	if err != nil {
		h.logger.WithError(err).Error("Failed to calculate damage")
		h.respondError(w, http.StatusInternalServerError, "failed to calculate damage")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *DamageHandlers) ApplyEffects(w http.ResponseWriter, r *http.Request) {
	var req damageapi.ApplyEffectsJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	targetID := uuid.UUID(req.TargetId)
	if targetID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "target_id is required")
		return
	}

	if req.Effects == nil || len(*req.Effects) == 0 {
		h.respondError(w, http.StatusBadRequest, "effects are required")
		return
	}

	var effects []EffectRequest
	for _, effect := range *req.Effects {
		if effect.EffectType == nil || effect.EffectName == nil || effect.Duration == nil || effect.Value == nil {
			continue
		}
		effects = append(effects, EffectRequest{
			EffectType: string(*effect.EffectType),
			EffectName: *effect.EffectName,
			Duration:   *effect.Duration,
			Value:      *effect.Value,
		})
	}

	if len(effects) == 0 {
		h.respondError(w, http.StatusBadRequest, "valid effects are required")
		return
	}

	result, err := h.service.ApplyEffects(r.Context(), targetID, effects)
	if err != nil {
		h.logger.WithError(err).Error("Failed to apply effects")
		h.respondError(w, http.StatusInternalServerError, "failed to apply effects")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *DamageHandlers) RemoveEffect(w http.ResponseWriter, r *http.Request, effectId openapi_types.UUID) {
	effectUUID := uuid.UUID(effectId)
	if effectUUID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "invalid effect_id")
		return
	}

	err := h.service.RemoveEffect(r.Context(), effectUUID)
	if err != nil {
		if err.Error() == "effect not found" {
			h.respondError(w, http.StatusNotFound, "effect not found")
			return
		}
		h.logger.WithError(err).Error("Failed to remove effect")
		h.respondError(w, http.StatusInternalServerError, "failed to remove effect")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "removed"})
}

func (h *DamageHandlers) ExtendEffect(w http.ResponseWriter, r *http.Request, effectId openapi_types.UUID) {
	effectUUID := uuid.UUID(effectId)
	if effectUUID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "invalid effect_id")
		return
	}

	var req struct {
		AdditionalTurns *int `json:"additional_turns"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.AdditionalTurns == nil || *req.AdditionalTurns < 1 || *req.AdditionalTurns > 10 {
		h.respondError(w, http.StatusBadRequest, "additional_turns must be between 1 and 10")
		return
	}

	err := h.service.ExtendEffect(r.Context(), effectUUID, *req.AdditionalTurns)
	if err != nil {
		if err.Error() == "effect not found" {
			h.respondError(w, http.StatusNotFound, "effect not found")
			return
		}
		h.logger.WithError(err).Error("Failed to extend effect")
		h.respondError(w, http.StatusInternalServerError, "failed to extend effect")
		return
	}

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
	errorResponse := damageapi.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}



