package server

import (
	"encoding/json"
	"net/http"

	"github.com/sirupsen/logrus"
)

// TODO: After running `make generate-weapon-combinations-api`, uncomment the import:
// "github.com/necpgame/economy-service-go/pkg/weaponcombinationsapi"

type WeaponCombinationsHandlers struct {
	service WeaponCombinationsServiceInterface
	logger  *logrus.Logger
}

func NewWeaponCombinationsHandlers(service WeaponCombinationsServiceInterface) *WeaponCombinationsHandlers {
	return &WeaponCombinationsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

// TODO: After running `make generate-weapon-combinations-api`, implement these handlers
// to match the generated weaponcombinationsapi.ServerInterface interface
func (h *WeaponCombinationsHandlers) GenerateWeaponCombination(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-combinations-api' first")
}

func (h *WeaponCombinationsHandlers) GetWeaponCombinationMatrix(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-combinations-api' first")
}

func (h *WeaponCombinationsHandlers) GetWeaponModifiers(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-combinations-api' first")
}

func (h *WeaponCombinationsHandlers) ApplyWeaponModifier(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-combinations-api' first")
}

func (h *WeaponCombinationsHandlers) GetCorporations(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-combinations-api' first")
}

func (h *WeaponCombinationsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *WeaponCombinationsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]string{"error": message})
}

