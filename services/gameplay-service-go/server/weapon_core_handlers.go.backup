// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/necpgame/gameplay-service-go/pkg/weaponcoreapi"
	"github.com/sirupsen/logrus"
)

type WeaponCoreHandlers struct {
	service WeaponMechanicsServiceInterface
	logger  *logrus.Logger
}

func NewWeaponCoreHandlers(service WeaponMechanicsServiceInterface) *WeaponCoreHandlers {
	return &WeaponCoreHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *WeaponCoreHandlers) ApplySpecialMechanics(w http.ResponseWriter, r *http.Request) {
	var req weaponcoreapi.ApplySpecialMechanicsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	mechanicData := make(map[string]interface{})
	if req.MechanicData != nil {
		mechanicData = *req.MechanicData
	}

	effectID, err := h.service.ApplySpecialMechanics(
		r.Context(),
		uuid.UUID(req.WeaponId),
		uuid.UUID(req.CharacterId),
		uuid.UUID(req.TargetId),
		string(req.MechanicType),
		mechanicData,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to apply special mechanics")
		h.respondError(w, http.StatusInternalServerError, "failed to apply special mechanics")
		return
	}

	now := time.Now()
	mechanicTypeStr := string(req.MechanicType)
	response := weaponcoreapi.ApplySpecialMechanicsResponse{
		EffectId:     (*openapi_types.UUID)(&effectID),
		MechanicType: &mechanicTypeStr,
		AppliedAt:    &now,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCoreHandlers) GetWeaponSpecialMechanics(w http.ResponseWriter, r *http.Request, weaponId openapi_types.UUID) {
	mechanics, err := h.service.GetWeaponSpecialMechanics(r.Context(), uuid.UUID(weaponId))
	if err != nil {
		h.logger.WithError(err).Error("Failed to get weapon special mechanics")
		h.respondError(w, http.StatusInternalServerError, "failed to get weapon special mechanics")
		return
	}

	weaponMechanics := make([]weaponcoreapi.WeaponMechanic, 0, len(mechanics))
	for _, m := range mechanics {
		mechanic := weaponcoreapi.WeaponMechanic{
			MechanicData: &m,
		}
		if id, ok := m["id"].(string); ok {
			if idUUID, err := uuid.Parse(id); err == nil {
				mechanic.Id = (*openapi_types.UUID)(&idUUID)
			}
		}
		if mechanicType, ok := m["mechanic_type"].(string); ok {
			mechanic.MechanicType = &mechanicType
		}
		if createdAt, ok := m["created_at"].(string); ok {
			if t, err := time.Parse(time.RFC3339, createdAt); err == nil {
				mechanic.CreatedAt = &t
			}
		}
		weaponMechanics = append(weaponMechanics, mechanic)
	}

	weaponIdPtr := &weaponId
	response := weaponcoreapi.WeaponSpecialMechanicsResponse{
		WeaponId:  weaponIdPtr,
		Mechanics: &weaponMechanics,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCoreHandlers) CreatePersistentEffect(w http.ResponseWriter, r *http.Request) {
	var req weaponcoreapi.CreatePersistentEffectRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position := map[string]float64{
		"x": float64(req.Position.X),
		"y": float64(req.Position.Y),
		"z": float64(req.Position.Z),
	}

	var remainingTicks int
	if req.RemainingTicks != nil {
		remainingTicks = *req.RemainingTicks
	}

	effectID, err := h.service.CreatePersistentEffect(
		r.Context(),
		uuid.UUID(req.TargetId),
		string(req.ProjectileType),
		position,
		float64(req.DamagePerTick),
		float64(req.TickInterval),
		remainingTicks,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create persistent effect")
		h.respondError(w, http.StatusInternalServerError, "failed to create persistent effect")
		return
	}

	targetIdPtr := &req.TargetId
	projectileTypeStr := string(req.ProjectileType)
	positionPtr := &req.Position
	damagePerTick := req.DamagePerTick
	tickInterval := req.TickInterval
	response := weaponcoreapi.PersistentEffect{
		Id:             (*openapi_types.UUID)(&effectID),
		TargetId:       targetIdPtr,
		ProjectileType: &projectileTypeStr,
		Position:       positionPtr,
		DamagePerTick:  &damagePerTick,
		TickInterval:   &tickInterval,
		RemainingTicks: req.RemainingTicks,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *WeaponCoreHandlers) GetPersistentEffects(w http.ResponseWriter, r *http.Request, targetId openapi_types.UUID) {
	effects, err := h.service.GetPersistentEffects(r.Context(), uuid.UUID(targetId))
	if err != nil {
		h.logger.WithError(err).Error("Failed to get persistent effects")
		h.respondError(w, http.StatusInternalServerError, "failed to get persistent effects")
		return
	}

	persistentEffects := make([]weaponcoreapi.PersistentEffect, 0, len(effects))
	for _, e := range effects {
		targetIdPtr := &targetId
		effect := weaponcoreapi.PersistentEffect{
			TargetId: targetIdPtr,
		}
		if id, ok := e["effect_id"].(string); ok {
			if idUUID, err := uuid.Parse(id); err == nil {
				effect.Id = (*openapi_types.UUID)(&idUUID)
			}
		}
		if projectileType, ok := e["projectile_type"].(string); ok {
			projectileTypeStr := projectileType
			effect.ProjectileType = &projectileTypeStr
		}
		if position, ok := e["position"].(map[string]interface{}); ok {
			pos := weaponcoreapi.Position3D{}
			if x, ok := position["x"].(float64); ok {
				pos.X = float32(x)
			}
			if y, ok := position["y"].(float64); ok {
				pos.Y = float32(y)
			}
			if z, ok := position["z"].(float64); ok {
				pos.Z = float32(z)
			}
			effect.Position = &pos
		}
		if damagePerTick, ok := e["damage_per_tick"].(float64); ok {
			dpt := float32(damagePerTick)
			effect.DamagePerTick = &dpt
		}
		if tickInterval, ok := e["tick_interval"].(float64); ok {
			ti := float32(tickInterval)
			effect.TickInterval = &ti
		}
		if remainingTicks, ok := e["remaining_ticks"].(int); ok {
			effect.RemainingTicks = &remainingTicks
		}
		persistentEffects = append(persistentEffects, effect)
	}

	h.respondJSON(w, http.StatusOK, persistentEffects)
}

func (h *WeaponCoreHandlers) CalculateChainDamage(w http.ResponseWriter, r *http.Request) {
	var req weaponcoreapi.CalculateChainDamageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	maxJumps := 3
	if req.MaxJumps != nil {
		maxJumps = *req.MaxJumps
	}

	jumpDamageReduction := float32(0.2)
	if req.JumpDamageReduction != nil {
		jumpDamageReduction = *req.JumpDamageReduction
	}

	targets, totalDamage, err := h.service.CalculateChainDamage(
		r.Context(),
		uuid.UUID(req.SourceTargetId),
		uuid.UUID(req.WeaponId),
		string(req.DamageType),
		float64(req.BaseDamage),
		maxJumps,
		float64(jumpDamageReduction),
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to calculate chain damage")
		h.respondError(w, http.StatusInternalServerError, "failed to calculate chain damage")
		return
	}

	jumps := make([]weaponcoreapi.ChainDamageJump, 0, len(targets))
	for _, t := range targets {
		jump := weaponcoreapi.ChainDamageJump{}
		if targetID, ok := t["target_id"].(string); ok {
			if targetUUID, err := uuid.Parse(targetID); err == nil {
				jump.TargetId = (*openapi_types.UUID)(&targetUUID)
			}
		}
		if damage, ok := t["damage"].(float64); ok {
			damageFloat := float32(damage)
			jump.Damage = &damageFloat
		}
		if jumpNumber, ok := t["jump_number"].(int); ok {
			jump.JumpNumber = &jumpNumber
		}
		jumps = append(jumps, jump)
	}

	totalDamageFloat := float32(totalDamage)
	response := weaponcoreapi.ChainDamageResponse{
		Jumps:       &jumps,
		TotalDamage: &totalDamageFloat,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCoreHandlers) DestroyEnvironment(w http.ResponseWriter, r *http.Request) {
	var req weaponcoreapi.DestroyEnvironmentRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	explosionPosition := map[string]float64{
		"x": float64(req.ExplosionPosition.X),
		"y": float64(req.ExplosionPosition.Y),
		"z": float64(req.ExplosionPosition.Z),
	}

	affectedTargets, destroyedObjects, err := h.service.DestroyEnvironment(
		r.Context(),
		explosionPosition,
		float64(req.ExplosionRadius),
		uuid.UUID(req.WeaponId),
		float64(req.Damage),
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to destroy environment")
		h.respondError(w, http.StatusInternalServerError, "failed to destroy environment")
		return
	}

	affected := make([]weaponcoreapi.AffectedTarget, 0, len(affectedTargets))
	for _, t := range affectedTargets {
		target := weaponcoreapi.AffectedTarget{}
		if targetID, ok := t["target_id"].(string); ok {
			if targetUUID, err := uuid.Parse(targetID); err == nil {
				target.TargetId = (*openapi_types.UUID)(&targetUUID)
			}
		}
		if damage, ok := t["damage"].(float64); ok {
			damageFloat := float32(damage)
			target.Damage = &damageFloat
		}
		if bodyParts, ok := t["body_parts_destroyed"].([]interface{}); ok {
			parts := make([]string, 0, len(bodyParts))
			for _, part := range bodyParts {
				if s, ok := part.(string); ok {
					parts = append(parts, s)
				}
			}
			target.BodyPartsDestroyed = &parts
		}
		affected = append(affected, target)
	}

	destroyed := make([]weaponcoreapi.DestroyedObject, 0, len(destroyedObjects))
	for _, obj := range destroyedObjects {
		destroyedObj := weaponcoreapi.DestroyedObject{}
		if objID, ok := obj["object_id"].(string); ok {
			if objUUID, err := uuid.Parse(objID); err == nil {
				destroyedObj.ObjectId = (*openapi_types.UUID)(&objUUID)
			}
		}
		if destructionType, ok := obj["destruction_type"].(string); ok {
			dt := weaponcoreapi.DestroyedObjectDestructionType(destructionType)
			destroyedObj.DestructionType = &dt
		}
		if position, ok := obj["position"].(map[string]interface{}); ok {
			pos := weaponcoreapi.Position3D{}
			if x, ok := position["x"].(float64); ok {
				pos.X = float32(x)
			}
			if y, ok := position["y"].(float64); ok {
				pos.Y = float32(y)
			}
			if z, ok := position["z"].(float64); ok {
				pos.Z = float32(z)
			}
			destroyedObj.Position = &pos
		}
		destroyed = append(destroyed, destroyedObj)
	}

	response := weaponcoreapi.EnvironmentDestructionResponse{
		AffectedTargets:  &affected,
		DestroyedObjects: &destroyed,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCoreHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *WeaponCoreHandlers) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(map[string]string{"error": message}); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON error response")
	}
}

// TODO: After running `make generate-all-weapon-apis`, implement converter functions
// using the generated types from weaponcoreapi package

