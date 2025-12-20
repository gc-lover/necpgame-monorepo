// Package server Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/sirupsen/logrus"
)

// TODO: After running `make generate-weapon-effects-api`, uncomment the import:
// "github.com/necpgame/client-service-go/pkg/weaponeffectsapi"

type WeaponEffectsHandlers struct {
	service WeaponEffectsServiceInterface
	logger  *logrus.Logger
}

// TriggerVisualEffect TODO: After running `make generate-weapon-effects-api`, implement these handlers
// to match the generated weaponeffectsapi.ServerInterface interface
func (h *WeaponEffectsHandlers) TriggerVisualEffect(w http.ResponseWriter) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-effects-api' first")
}

func (h *WeaponEffectsHandlers) TriggerAudioEffect(w http.ResponseWriter) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-effects-api' first")
}

func (h *WeaponEffectsHandlers) GetEffect(w http.ResponseWriter) {
	h.respondError(w, http.StatusNotImplemented, "handler not implemented - run 'make generate-weapon-effects-api' first")
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
