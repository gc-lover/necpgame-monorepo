// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/necpgame/gameplay-service-go/pkg/weaponeffectsapi"
	"github.com/sirupsen/logrus"
)

type WeaponEffectsHandlers struct {
	service WeaponMechanicsServiceInterface
	logger  *logrus.Logger
}

func NewWeaponEffectsHandlers(service WeaponMechanicsServiceInterface) *WeaponEffectsHandlers {
	return &WeaponEffectsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *WeaponEffectsHandlers) ApplyElementalEffect(w http.ResponseWriter, r *http.Request) {
	var req weaponeffectsapi.ApplyElementalEffectRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	var duration *float64
	if req.Duration != nil {
		d := float64(*req.Duration)
		duration = &d
	}

	effectID, err := h.service.ApplyElementalEffect(
		r.Context(),
		uuid.UUID(req.TargetId),
		string(req.ElementType),
		float64(req.Damage),
		duration,
		req.Stacks,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to apply elemental effect")
		h.respondError(w, http.StatusInternalServerError, "failed to apply elemental effect")
		return
	}

	elementTypeStr := string(req.ElementType)
	response := weaponeffectsapi.ElementalEffectResponse{
		EffectId:    (*openapi_types.UUID)(&effectID),
		ElementType: &elementTypeStr,
		DamagePerTick: &req.Damage,
		Stacks:      req.Stacks,
	}
	if req.Duration != nil {
		expiresAt := time.Now().Add(time.Duration(*req.Duration) * time.Second)
		response.ExpiresAt = &expiresAt
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponEffectsHandlers) ApplyTemporalEffect(w http.ResponseWriter, r *http.Request) {
	var req weaponeffectsapi.ApplyTemporalEffectRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	effectID, err := h.service.ApplyTemporalEffect(
		r.Context(),
		uuid.UUID(req.TargetId),
		string(req.EffectType),
		req.ModifierValue,
		float64(req.Duration),
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to apply temporal effect")
		h.respondError(w, http.StatusInternalServerError, "failed to apply temporal effect")
		return
	}

	effectTypeStr := string(req.EffectType)
	expiresAt := time.Now().Add(time.Duration(req.Duration) * time.Second)
	response := weaponeffectsapi.TemporalEffectResponse{
		EffectId:      (*openapi_types.UUID)(&effectID),
		EffectType:    &effectTypeStr,
		ModifierValue: &req.ModifierValue,
		ExpiresAt:     &expiresAt,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponEffectsHandlers) ApplyControl(w http.ResponseWriter, r *http.Request) {
	var req weaponeffectsapi.ApplyControlRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	effectID, err := h.service.ApplyControl(
		r.Context(),
		uuid.UUID(req.TargetId),
		string(req.ControlType),
		req.ControlData,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to apply control")
		h.respondError(w, http.StatusInternalServerError, "failed to apply control")
		return
	}

	controlTypeStr := string(req.ControlType)
	response := weaponeffectsapi.ControlResponse{
		EffectId:    (*openapi_types.UUID)(&effectID),
		ControlType: &controlTypeStr,
		ControlData: &req.ControlData,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponEffectsHandlers) CreateSummon(w http.ResponseWriter, r *http.Request) {
	var req weaponeffectsapi.CreateSummonRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position := map[string]float64{
		"x": float64(req.Position.X),
		"y": float64(req.Position.Y),
		"z": float64(req.Position.Z),
	}

	var duration *float64
	if req.Duration != nil {
		d := float64(*req.Duration)
		duration = &d
	}

	summonID, err := h.service.CreateSummon(
		r.Context(),
		uuid.UUID(req.CharacterId),
		string(req.SummonType),
		position,
		duration,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create summon")
		h.respondError(w, http.StatusInternalServerError, "failed to create summon")
		return
	}

	now := time.Now()
	response := weaponeffectsapi.Summon{
		Id:          (*openapi_types.UUID)(&summonID),
		CharacterId: &req.CharacterId,
		Position:    &req.Position,
		CreatedAt:   &now,
	}
	if req.Duration != nil {
		expiresAt := time.Now().Add(time.Duration(*req.Duration) * time.Second)
		response.ExpiresAt = &expiresAt
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *WeaponEffectsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *WeaponEffectsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(map[string]string{"error": message}); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON error response")
	}
}

