// Issue: #140876112, #141888018
package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type RomanceCoreHandlers struct {
	service RomanceCoreServiceInterface
	logger  *logrus.Logger
}

func NewRomanceCoreHandlers(service RomanceCoreServiceInterface) *RomanceCoreHandlers {
	return &RomanceCoreHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *RomanceCoreHandlers) GetRomanceTypes(w http.ResponseWriter, r *http.Request) {
	types, err := h.service.GetRomanceTypes(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get romance types")
		h.respondError(w, http.StatusInternalServerError, "failed to get romance types")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"types": types,
	})
}

func (h *RomanceCoreHandlers) GetPlayerRomanceRelationships(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	limit := 20
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := parseInt(limitStr); err == nil {
			limit = parsedLimit
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := parseInt(offsetStr); err == nil {
			offset = parsedOffset
		}
	}

	relationships, total, err := h.service.GetPlayerRomanceRelationships(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player romance relationships")
		h.respondError(w, http.StatusInternalServerError, "failed to get player romance relationships")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"relationships": relationships,
		"total":         total,
	})
}

func (h *RomanceCoreHandlers) GetPlayerRomanceRelationshipsByType(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]
	romanceType := vars["type"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	limit := 20
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := parseInt(limitStr); err == nil {
			limit = parsedLimit
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := parseInt(offsetStr); err == nil {
			offset = parsedOffset
		}
	}

	relationships, total, err := h.service.GetPlayerRomanceRelationshipsByType(r.Context(), playerID, romanceType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player romance relationships by type")
		h.respondError(w, http.StatusInternalServerError, "failed to get player romance relationships by type")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"relationships": relationships,
		"total":         total,
	})
}

func (h *RomanceCoreHandlers) InitiatePlayerPlayerRomance(w http.ResponseWriter, r *http.Request) {
	var req struct {
		TargetPlayerID  uuid.UUID              `json:"target_player_id"`
		Message         string                 `json:"message,omitempty"`
		PrivacySettings map[string]interface{} `json:"privacy_settings,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// Get player ID from auth context (simplified - should come from JWT)
	playerID := uuid.MustParse("00000000-0000-0000-0000-000000000000") // TODO: get from auth context

	relationship, err := h.service.InitiatePlayerPlayerRomance(r.Context(), playerID, req.TargetPlayerID, req.Message, req.PrivacySettings)
	if err != nil {
		h.logger.WithError(err).Error("Failed to initiate player-player romance")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, relationship)
}

func (h *RomanceCoreHandlers) AcceptPlayerPlayerRomance(w http.ResponseWriter, r *http.Request) {
	var req struct {
		RelationshipID uuid.UUID `json:"relationship_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// Get player ID from auth context
	playerID := uuid.MustParse("00000000-0000-0000-0000-000000000000") // TODO: get from auth context

	relationship, err := h.service.AcceptPlayerPlayerRomance(r.Context(), req.RelationshipID, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to accept player-player romance")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, relationship)
}

func (h *RomanceCoreHandlers) RejectPlayerPlayerRomance(w http.ResponseWriter, r *http.Request) {
	var req struct {
		RelationshipID uuid.UUID `json:"relationship_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// Get player ID from auth context
	playerID := uuid.MustParse("00000000-0000-0000-0000-000000000000") // TODO: get from auth context

	if err := h.service.RejectPlayerPlayerRomance(r.Context(), req.RelationshipID, playerID); err != nil {
		h.logger.WithError(err).Error("Failed to reject player-player romance")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *RomanceCoreHandlers) BreakupPlayerPlayerRomance(w http.ResponseWriter, r *http.Request) {
	var req struct {
		RelationshipID uuid.UUID `json:"relationship_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// Get player ID from auth context
	playerID := uuid.MustParse("00000000-0000-0000-0000-000000000000") // TODO: get from auth context

	if err := h.service.BreakupPlayerPlayerRomance(r.Context(), req.RelationshipID, playerID); err != nil {
		h.logger.WithError(err).Error("Failed to breakup player-player romance")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *RomanceCoreHandlers) GetPlayerPlayerRomance(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID1Str := vars["player_id1"]
	playerID2Str := vars["player_id2"]

	playerID1, err := uuid.Parse(playerID1Str)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id1")
		return
	}

	playerID2, err := uuid.Parse(playerID2Str)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id2")
		return
	}

	relationship, err := h.service.GetPlayerPlayerRomance(r.Context(), playerID1, playerID2)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player-player romance")
		h.respondError(w, http.StatusInternalServerError, "failed to get player-player romance")
		return
	}

	if relationship == nil {
		h.respondError(w, http.StatusNotFound, "romance relationship not found")
		return
	}

	h.respondJSON(w, http.StatusOK, relationship)
}

func (h *RomanceCoreHandlers) GetRomanceCompatibility(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]
	targetIDStr := vars["target_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	targetID, err := uuid.Parse(targetIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid target_id")
		return
	}

	result, err := h.service.GetRomanceCompatibility(r.Context(), playerID, targetID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get romance compatibility")
		h.respondError(w, http.StatusInternalServerError, "failed to get romance compatibility")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *RomanceCoreHandlers) UpdateRomancePrivacy(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID    uuid.UUID              `json:"player_id"`
		RomanceType string                 `json:"romance_type"`
		Settings    map[string]interface{} `json:"settings"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if err := h.service.UpdateRomancePrivacy(r.Context(), req.PlayerID, req.RomanceType, req.Settings); err != nil {
		h.logger.WithError(err).Error("Failed to update romance privacy")
		h.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *RomanceCoreHandlers) GetRomanceNotifications(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	limit := 20
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := parseInt(limitStr); err == nil {
			limit = parsedLimit
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := parseInt(offsetStr); err == nil {
			offset = parsedOffset
		}
	}

	notifications, total, err := h.service.GetRomanceNotifications(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get romance notifications")
		h.respondError(w, http.StatusInternalServerError, "failed to get romance notifications")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"notifications": notifications,
		"total":         total,
	})
}

func (h *RomanceCoreHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *RomanceCoreHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

func parseInt(s string) (int, error) {
	return strconv.Atoi(s)
}

