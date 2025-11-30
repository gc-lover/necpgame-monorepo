// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type EngramRomanceHandlers struct {
	romanceService EngramRomanceServiceInterface
	logger         *logrus.Logger
}

func NewEngramRomanceHandlers(romanceService EngramRomanceServiceInterface) *EngramRomanceHandlers {
	return &EngramRomanceHandlers{
		romanceService: romanceService,
		logger:         GetLogger(),
	}
}

func (h *EngramRomanceHandlers) EngramRomanceComment(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	engramIDStr := vars["engram_id"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	var req struct {
		CharacterID      uuid.UUID              `json:"character_id"`
		RomanceEventType string                 `json:"romance_event_type"`
		PartnerID        *uuid.UUID             `json:"partner_id,omitempty"`
		EventContext     map[string]interface{} `json:"event_context,omitempty"`
		InfluenceLevel   float64                `json:"influence_level"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.CharacterID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	if req.RomanceEventType == "" {
		h.respondError(w, http.StatusBadRequest, "romance_event_type is required")
		return
	}

	if req.InfluenceLevel == 0 {
		req.InfluenceLevel = 10.0
	}

	result, err := h.romanceService.CreateRomanceComment(r.Context(), engramID, req.CharacterID, req.RomanceEventType, req.PartnerID, req.EventContext, req.InfluenceLevel)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create romance comment")
		h.respondError(w, http.StatusInternalServerError, "Failed to create romance comment")
		return
	}

	response := map[string]interface{}{
		"comment_id":         result.CommentID.String(),
		"engram_id":          result.EngramID.String(),
		"character_id":       result.CharacterID.String(),
		"comment_text":       result.CommentText,
		"romance_event_type": result.RomanceEventType,
		"influence_level":    result.InfluenceLevel,
		"created_at":         result.CreatedAt.Format("2006-01-02T15:04:05Z07:00"),
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EngramRomanceHandlers) GetEngramRomanceInfluence(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	engramIDStr := vars["engram_id"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	var relationshipID *uuid.UUID
	if relIDStr := r.URL.Query().Get("relationship_id"); relIDStr != "" {
		relID, err := uuid.Parse(relIDStr)
		if err == nil {
			relationshipID = &relID
		}
	}

	result, err := h.romanceService.GetEngramRomanceInfluence(r.Context(), engramID, relationshipID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get engram romance influence")
		h.respondError(w, http.StatusInternalServerError, "Failed to get engram romance influence")
		return
	}

	response := map[string]interface{}{
		"engram_id":          result.EngramID.String(),
		"influence_level":    result.InfluenceLevel,
		"influence_category": result.InfluenceCategory,
		"special_events":     result.SpecialEvents,
	}

	if result.EngramType != nil {
		response["engram_type"] = *result.EngramType
	}

	if result.RelationshipImpact != nil {
		response["relationship_impact"] = map[string]interface{}{
			"helps_relationship":      result.RelationshipImpact.HelpsRelationship,
			"interferes_relationship": result.RelationshipImpact.InterferesRelationship,
			"impact_percentage":       result.RelationshipImpact.ImpactPercentage,
		}
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EngramRomanceHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *EngramRomanceHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}



