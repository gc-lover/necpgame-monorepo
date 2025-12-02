// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/necpgame/gameplay-service-go/pkg/weaponcombatapi"
	"github.com/sirupsen/logrus"
)

type WeaponCombatHandlers struct {
	service WeaponMechanicsServiceInterface
	logger  *logrus.Logger
}

func NewWeaponCombatHandlers(service WeaponMechanicsServiceInterface) *WeaponCombatHandlers {
	return &WeaponCombatHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *WeaponCombatHandlers) PlaceDeployableWeapon(w http.ResponseWriter, r *http.Request) {
	var req weaponcombatapi.PlaceDeployableWeaponRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position := map[string]float64{
		"x": float64(req.Position.X),
		"y": float64(req.Position.Y),
		"z": float64(req.Position.Z),
	}

	activationRadius := float64(5.0)
	if req.ActivationRadius != nil {
		activationRadius = float64(*req.ActivationRadius)
	}

	deploymentID, err := h.service.PlaceDeployableWeapon(
		r.Context(),
		uuid.UUID(req.CharacterId),
		string(req.WeaponType),
		position,
		activationRadius,
		req.AmmoRemaining,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to place deployable weapon")
		h.respondError(w, http.StatusInternalServerError, "failed to place deployable weapon")
		return
	}

	weaponTypeStr := string(req.WeaponType)
	response := weaponcombatapi.DeployableWeapon{
		Id:               (*openapi_types.UUID)(&deploymentID),
		CharacterId:      &req.CharacterId,
		WeaponType:       &weaponTypeStr,
		Position:         &req.Position,
		ActivationRadius: req.ActivationRadius,
		AmmoRemaining:    req.AmmoRemaining,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *WeaponCombatHandlers) GetDeployableWeapon(w http.ResponseWriter, r *http.Request, deploymentId openapi_types.UUID) {
	deployment, err := h.service.GetDeployableWeapon(r.Context(), uuid.UUID(deploymentId))
	if err != nil {
		h.logger.WithError(err).Error("Failed to get deployable weapon")
		h.respondError(w, http.StatusInternalServerError, "failed to get deployable weapon")
		return
	}

	response := weaponcombatapi.DeployableWeapon{}
	if id, ok := deployment["deployment_id"].(string); ok {
		if idUUID, err := uuid.Parse(id); err == nil {
			response.Id = (*openapi_types.UUID)(&idUUID)
		}
	}
	if charID, ok := deployment["character_id"].(string); ok {
		if charUUID, err := uuid.Parse(charID); err == nil {
			response.CharacterId = (*openapi_types.UUID)(&charUUID)
		}
	}
	if weaponType, ok := deployment["weapon_type"].(string); ok {
		response.WeaponType = &weaponType
	}
	if position, ok := deployment["position"].(map[string]interface{}); ok {
		pos := weaponcombatapi.Position3D{}
		if x, ok := position["x"].(float64); ok {
			pos.X = float32(x)
		}
		if y, ok := position["y"].(float64); ok {
			pos.Y = float32(y)
		}
		if z, ok := position["z"].(float64); ok {
			pos.Z = float32(z)
		}
		response.Position = &pos
	}
	if activationRadius, ok := deployment["activation_radius"].(float64); ok {
		ar := float32(activationRadius)
		response.ActivationRadius = &ar
	}
	if ammoRemaining, ok := deployment["ammo_remaining"].(int); ok {
		response.AmmoRemaining = &ammoRemaining
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCombatHandlers) FireLaser(w http.ResponseWriter, r *http.Request) {
	var req weaponcombatapi.FireLaserRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	direction := map[string]float64{
		"x": float64(req.Direction.X),
		"y": float64(req.Direction.Y),
		"z": float64(req.Direction.Z),
	}

	var duration *float64
	if req.Duration != nil {
		d := float64(*req.Duration)
		duration = &d
	}

	var chargeLevel *float64
	if req.ChargeLevel != nil {
		cl := float64(*req.ChargeLevel)
		chargeLevel = &cl
	}

	result, err := h.service.FireLaser(
		r.Context(),
		uuid.UUID(req.WeaponId),
		uuid.UUID(req.CharacterId),
		string(req.LaserType),
		direction,
		duration,
		chargeLevel,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to fire laser")
		h.respondError(w, http.StatusInternalServerError, "failed to fire laser")
		return
	}

	response := weaponcombatapi.LaserFireResponse{
		WeaponId: &req.WeaponId,
	}
	if heatLevel, ok := result["heat_level"].(float64); ok {
		hl := float32(heatLevel)
		response.HeatLevel = &hl
	}
	if maxHeat, ok := result["max_heat"].(float64); ok {
		mh := float32(maxHeat)
		response.MaxHeat = &mh
	}
	if isOverheated, ok := result["is_overheated"].(bool); ok {
		response.IsOverheated = &isOverheated
	}
	if targetsHit, ok := result["targets_hit"].([]interface{}); ok {
		hits := make([]weaponcombatapi.LaserHit, 0, len(targetsHit))
		for _, th := range targetsHit {
			if hitMap, ok := th.(map[string]interface{}); ok {
				hit := weaponcombatapi.LaserHit{}
				if targetID, ok := hitMap["target_id"].(string); ok {
					if targetUUID, err := uuid.Parse(targetID); err == nil {
						hit.TargetId = (*openapi_types.UUID)(&targetUUID)
					}
				}
				if damage, ok := hitMap["damage"].(float64); ok {
					d := float32(damage)
					hit.Damage = &d
				}
				hits = append(hits, hit)
			}
		}
		response.TargetsHit = &hits
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCombatHandlers) PerformMeleeAttack(w http.ResponseWriter, r *http.Request) {
	var req weaponcombatapi.MeleeAttackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	attackID, damage, hits, isCritical, err := h.service.PerformMeleeAttack(
		r.Context(),
		uuid.UUID(req.CharacterId),
		uuid.UUID(req.TargetId),
		string(req.WeaponType),
		string(req.AttackType),
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to perform melee attack")
		h.respondError(w, http.StatusInternalServerError, "failed to perform melee attack")
		return
	}

	damageFloat := float32(damage)
	comboCount := hits
	response := weaponcombatapi.MeleeAttackResponse{
		AttackId:   (*openapi_types.UUID)(&attackID),
		Damage:     &damageFloat,
		ComboCount: &comboCount,
		IsCritical: &isCritical,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Issue: #141886468
func (h *WeaponCombatHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *WeaponCombatHandlers) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(map[string]string{"error": message}); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON error response")
	}
}







