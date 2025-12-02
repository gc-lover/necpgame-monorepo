// Issue: #164
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/gameplay-progression-core-service-go/pkg/api"
)

type Handlers struct{}

func NewHandlers() *Handlers {
	return &Handlers{}
}

// Реализация api.ServerInterface

// GetCharacterProgression получает прогрессию персонажа
func (h *Handlers) GetCharacterProgression(w http.ResponseWriter, r *http.Request, characterId int64) {
	// TODO: Реализовать получение прогрессии из БД
	progression := api.CharacterProgression{
		CharacterId:      characterId,
		Level:            1,
		Experience:       0,
		ExperienceToNext: 1000,
		AttributePoints:  5,
		SkillPoints:      3,
	}

	respondJSON(w, http.StatusOK, progression)
}

// UpdateCharacterProgression обновляет прогрессию персонажа
func (h *Handlers) UpdateCharacterProgression(w http.ResponseWriter, r *http.Request, characterId int64) {
	var req api.UpdateProgressionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Реализовать обновление прогрессии в БД
	progression := api.CharacterProgression{
		CharacterId:      characterId,
		Level:            1,
		Experience:       0,
		ExperienceToNext: 1000,
		AttributePoints:  5,
		SkillPoints:      3,
	}

	respondJSON(w, http.StatusOK, progression)
}

// Helper functions

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	respondJSON(w, status, api.Error{
		Code:    int32(status),
		Message: message,
	})
}

