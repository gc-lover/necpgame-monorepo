package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/necpgame/character-engram-compatibility-service-go/pkg/api"
)

type Handlers struct{}

func (h *Handlers) GetEngramCompatibility(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	overallCompatibility := api.CompatibilityMatrixOverallCompatibilityNeutral
	synergyBonus := float32(0.0)
	response := api.CompatibilityMatrix{
		Engrams:             []openapi_types.UUID{},
		CompatibilityPairs:  []api.CompatibilityPair{},
		OverallCompatibility: &overallCompatibility,
		SynergyBonus:         &synergyBonus,
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) CheckEngramCompatibility(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	var req api.CheckCompatibilityRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	compatibilityLevel := api.CompatibilityResultCompatibilityLevelNeutral
	canInstall := true
	response := api.CompatibilityResult{
		EngramIds:            req.EngramIds,
		CompatibilityLevel:   compatibilityLevel,
		CompatibilityPercentage: 0.0,
		CanInstall:           &canInstall,
		Pairs:                nil,
		Warnings:             nil,
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetEngramConflicts(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	response := []api.EngramConflict{}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) ResolveEngramConflict(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	var req api.ResolveConflictRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	resolvedAt := time.Now()
	response := api.ResolveConflictResponse{
		ConflictId:     req.ConflictId,
		Success:        true,
		ResolvedAt:     &resolvedAt,
		InfluenceChanges: nil,
		NewBalance:     nil,
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) CreateConflictEvent(w http.ResponseWriter, r *http.Request, characterId api.CharacterId) {
	var req api.CreateConflictEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	eventId := openapi_types.UUID(uuid.New())
	engramIds := []openapi_types.UUID{req.Engram1Id, req.Engram2Id}
	conflictType := api.ConflictEventConflictType(req.ConflictType)
	response := api.ConflictEvent{
		EventId:     eventId,
		CharacterId: characterId,
		ConflictType: conflictType,
		EngramIds:   &engramIds,
		EventData:   req.EventData,
		CreatedAt:   time.Now(),
	}
	respondJSON(w, http.StatusOK, response)
}

