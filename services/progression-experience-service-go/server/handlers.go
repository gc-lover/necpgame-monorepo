// Issue: #164
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/progression-experience-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type Handlers struct{}

func NewHandlers() *Handlers {
	return &Handlers{}
}

// Реализация api.ServerInterface

// ValidateProgression валидирует прогрессию
func (h *Handlers) ValidateProgression(w http.ResponseWriter, r *http.Request) {
	var req api.ValidateProgressionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Реализовать валидацию прогрессии
	valid := true
	response := api.ProgressionValidationResponse{
		Valid:  &valid,
		Issues: nil,
	}
	respondJSON(w, http.StatusOK, response)
}

// GetCharacterProgression получает прогрессию персонажа
func (h *Handlers) GetCharacterProgression(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	// TODO: Реализовать получение прогрессии из БД
	progression := api.CharacterProgression{
		CharacterId:              &characterId,
		Level:                    intPtr(1),
		Experience:               intPtr(0),
		ExperienceToNextLevel:    intPtr(1000),
		AvailableAttributePoints: intPtr(5),
		AvailableSkillPoints:     intPtr(3),
		Attributes:               nil,
		Skills:                   nil,
	}

	respondJSON(w, http.StatusOK, progression)
}

// DistributeAttributePoints распределяет очки атрибутов
func (h *Handlers) DistributeAttributePoints(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	var req api.DistributeAttributePointsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Реализовать распределение атрибутов в БД
	respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

// AddExperience начисляет опыт персонажу
func (h *Handlers) AddExperience(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	var req api.AddExperienceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Реализовать начисление опыта в БД
	respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

// DistributeSkillPoints распределяет очки навыков
func (h *Handlers) DistributeSkillPoints(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	var req api.DistributeSkillPointsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Реализовать распределение навыков в БД
	respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func intPtr(i int) *int {
	return &i
}

// Helper functions

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	code := http.StatusText(status)
	respondJSON(w, status, api.Error{
		Code:    &code,
		Message: message,
		Error:   "error",
	})
}

