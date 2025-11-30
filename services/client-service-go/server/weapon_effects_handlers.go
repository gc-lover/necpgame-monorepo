package server

import (
	"encoding/json"
	"net/http"

	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

// TODO: After running `make generate-weapon-effects-api`, uncomment the import:
// "github.com/necpgame/client-service-go/pkg/weaponeffectsapi"

type WeaponEffectsHandlers struct {
	service WeaponEffectsServiceInterface
	logger  *logrus.Logger
}

func NewWeaponEffectsHandlers(service WeaponEffectsServiceInterface) *WeaponEffectsHandlers {
	return &WeaponEffectsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

// TODO: After running `make generate-weapon-effects-api`, implement these handlers
// to match the generated weaponeffectsapi.ServerInterface interface
func (h *WeaponEffectsHandlers) TriggerVisualEffect(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-effects-api' first")
}

func (h *WeaponEffectsHandlers) TriggerAudioEffect(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-effects-api' first")
}

func (h *WeaponEffectsHandlers) GetEffect(w http.ResponseWriter, r *http.Request, effectId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-effects-api' first")
}

func (h *WeaponEffectsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *WeaponEffectsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]string{"error": message})
}

