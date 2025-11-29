package server

import (
	"encoding/json"
	"net/http"

	"github.com/sirupsen/logrus"
)

// TODO: After running `make generate-all-weapon-apis`, uncomment the import:
// "github.com/necpgame/gameplay-service-go/pkg/weaponeffectsapi"

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

// TODO: After running `make generate-all-weapon-apis`, implement these handlers
// to match the generated weaponeffectsapi.ServerInterface interface
func (h *WeaponEffectsHandlers) ApplyElementalEffect(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponEffectsHandlers) ApplyTemporalEffect(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponEffectsHandlers) ApplyControl(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
}

func (h *WeaponEffectsHandlers) CreateSummon(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-all-weapon-apis' first")
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

