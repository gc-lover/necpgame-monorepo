package server

import (
	"encoding/json"
	"net/http"

	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

// TODO: After running `make generate-all-weapon-apis`, uncomment the import:
// "github.com/necpgame/gameplay-service-go/pkg/weaponcombatapi"

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

// TODO: After running `make generate-all-weapon-apis`, implement these handlers
// to match the generated weaponcombatapi.ServerInterface interface
func (h *WeaponCombatHandlers) PlaceDeployableWeapon(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponCombatHandlers) GetDeployableWeapon(w http.ResponseWriter, r *http.Request, deploymentId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponCombatHandlers) FireLaser(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponCombatHandlers) PerformMeleeAttack(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
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







