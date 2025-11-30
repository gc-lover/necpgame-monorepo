package server

import (
	"encoding/json"
	"net/http"

	"github.com/sirupsen/logrus"
)

// TODO: After running `make generate-all-weapon-apis`, uncomment the import:
// "github.com/necpgame/gameplay-service-go/pkg/weaponadvancedapi"

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

// TODO: After running `make generate-all-weapon-apis`, implement these handlers
// to match the generated weaponadvancedapi.ServerInterface interface
func (h *WeaponAdvancedHandlers) CalculateProjectileForm(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponAdvancedHandlers) CalculateClassSynergy(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponAdvancedHandlers) FireDualWielding(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
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






