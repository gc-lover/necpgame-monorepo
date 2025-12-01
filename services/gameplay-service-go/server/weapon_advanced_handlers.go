// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/necpgame/gameplay-service-go/pkg/weaponadvancedapi"
	"github.com/sirupsen/logrus"
)

type WeaponAdvancedHandlers struct {
	service WeaponMechanicsServiceInterface
	logger  *logrus.Logger
}

func NewWeaponAdvancedHandlers(service WeaponMechanicsServiceInterface) *WeaponAdvancedHandlers {
	return &WeaponAdvancedHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *WeaponAdvancedHandlers) CalculateClassSynergy(w http.ResponseWriter, r *http.Request) {
	var req weaponadvancedapi.CalculateClassSynergyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	synergyBonuses, exclusiveAbilities, err := h.service.CalculateClassSynergy(
		r.Context(),
		uuid.UUID(req.CharacterId),
		uuid.UUID(req.WeaponId),
		string(req.ClassId),
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to calculate class synergy")
		h.respondError(w, http.StatusInternalServerError, "failed to calculate class synergy")
		return
	}

	classIdStr := string(req.ClassId)
	response := weaponadvancedapi.ClassSynergyResponse{
		WeaponId:         &req.WeaponId,
		ClassId:          &classIdStr,
		SynergyBonuses:   &synergyBonuses,
		ExclusiveAbilities: &exclusiveAbilities,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponAdvancedHandlers) FireDualWielding(w http.ResponseWriter, r *http.Request) {
	var req weaponadvancedapi.DualWieldingFireRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	var targetID *uuid.UUID
	if req.TargetId != nil {
		tid := uuid.UUID(*req.TargetId)
		targetID = &tid
	}

	leftFired, rightFired, leftDamage, rightDamage, err := h.service.FireDualWielding(
		r.Context(),
		uuid.UUID(req.CharacterId),
		uuid.UUID(req.LeftWeaponId),
		uuid.UUID(req.RightWeaponId),
		string(req.FiringMode),
		targetID,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to fire dual wielding")
		h.respondError(w, http.StatusInternalServerError, "failed to fire dual wielding")
		return
	}

	response := weaponadvancedapi.DualWieldingFireResponse{
		LeftWeaponFired:  &leftFired,
		RightWeaponFired: &rightFired,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponAdvancedHandlers) CalculateProjectileForm(w http.ResponseWriter, r *http.Request) {
	var req weaponadvancedapi.CalculateProjectileFormRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	formData := make(map[string]interface{})
	if req.FormData != nil {
		formData = *req.FormData
	}

	projectiles, totalCount, err := h.service.CalculateProjectileForm(
		r.Context(),
		uuid.UUID(req.WeaponId),
		string(req.FormType),
		formData,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to calculate projectile form")
		h.respondError(w, http.StatusInternalServerError, "failed to calculate projectile form")
		return
	}

	trajectories := make([]weaponadvancedapi.ProjectileTrajectory, 0, len(projectiles))
	for _, p := range projectiles {
		trajectory := weaponadvancedapi.ProjectileTrajectory{}
		if position, ok := p["position"].(map[string]interface{}); ok {
			pos := weaponadvancedapi.Position3D{}
			if x, ok := position["x"].(float64); ok {
				pos.X = float32(x)
			}
			if y, ok := position["y"].(float64); ok {
				pos.Y = float32(y)
			}
			if z, ok := position["z"].(float64); ok {
				pos.Z = float32(z)
			}
			trajectory.StartPosition = &pos
		}
		if velocity, ok := p["velocity"].(map[string]interface{}); ok {
			vel := weaponadvancedapi.Direction3D{}
			if x, ok := velocity["x"].(float64); ok {
				vel.X = float32(x)
			}
			if y, ok := velocity["y"].(float64); ok {
				vel.Y = float32(y)
			}
			if z, ok := velocity["z"].(float64); ok {
				vel.Z = float32(z)
			}
			trajectory.Direction = &vel
		}
		if speed, ok := p["speed"].(float64); ok {
			s := float32(speed)
			trajectory.Speed = &s
		}
		trajectories = append(trajectories, trajectory)
	}

	formTypeStr := string(req.FormType)
	response := weaponadvancedapi.ProjectileFormResponse{
		FormType:        &formTypeStr,
		Trajectories:    &trajectories,
		ProjectileCount: &totalCount,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Issue: #141886468
func (h *WeaponAdvancedHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *WeaponAdvancedHandlers) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(map[string]string{"error": message}); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON error response")
	}
}






