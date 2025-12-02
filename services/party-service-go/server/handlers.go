// Issue: #139
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/party-service-go/pkg/api"
	"github.com/oapi-codegen/runtime/types"
)

// PartyHandlers implements api.ServerInterface
type PartyHandlers struct {
	service *PartyService
}

// NewPartyHandlers creates handlers with DI
func NewPartyHandlers(service *PartyService) *PartyHandlers {
	return &PartyHandlers{
		service: service,
	}
}

// CreateParty implements api.ServerInterface
func (h *PartyHandlers) CreateParty(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.CreatePartyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	// TODO: Get player ID from auth context
	leaderID := "player-001"

	party, err := h.service.CreateParty(ctx, leaderID, req.Name, string(*req.LootMode))
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to create party", err)
		return
	}

	respondJSON(w, http.StatusCreated, party)
}

// GetParty implements api.ServerInterface
func (h *PartyHandlers) GetParty(w http.ResponseWriter, r *http.Request, partyId api.PartyId) {
	ctx := r.Context()

	party, err := h.service.GetParty(ctx, string(partyId))
	if err != nil {
		respondError(w, http.StatusNotFound, "Party not found", err)
		return
	}

	respondJSON(w, http.StatusOK, party)
}

// DisbandParty implements api.ServerInterface
func (h *PartyHandlers) DisbandParty(w http.ResponseWriter, r *http.Request, partyId api.PartyId) {
	ctx := r.Context()

	if err := h.service.DisbandParty(ctx, string(partyId)); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to disband party", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Party disbanded successfully",
	})
}

// InvitePlayer implements api.ServerInterface
func (h *PartyHandlers) InvitePlayer(w http.ResponseWriter, r *http.Request, partyId api.PartyId) {
	ctx := r.Context()

	var req api.InviteRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	invite, err := h.service.InvitePlayer(ctx, string(partyId), req.PlayerId.String())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to invite player", err)
		return
	}

	respondJSON(w, http.StatusOK, invite)
}

// AcceptInvite implements api.ServerInterface
func (h *PartyHandlers) AcceptInvite(w http.ResponseWriter, r *http.Request, inviteId types.UUID) {
	ctx := r.Context()

	// TODO: Get player ID from auth context
	playerID := "player-001"

	party, err := h.service.AcceptInvite(ctx, inviteId.String(), playerID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to accept invite", err)
		return
	}

	respondJSON(w, http.StatusOK, party)
}

// DeclineInvite implements api.ServerInterface
func (h *PartyHandlers) DeclineInvite(w http.ResponseWriter, r *http.Request, inviteId types.UUID) {
	ctx := r.Context()

	if err := h.service.DeclineInvite(ctx, inviteId.String()); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to decline invite", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Invite declined successfully",
	})
}

// LeaveParty implements api.ServerInterface
func (h *PartyHandlers) LeaveParty(w http.ResponseWriter, r *http.Request, partyId api.PartyId) {
	ctx := r.Context()

	// TODO: Get player ID from auth context
	playerID := "player-001"

	if err := h.service.LeaveParty(ctx, string(partyId), playerID); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to leave party", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Left party successfully",
	})
}

// KickMember implements api.ServerInterface
func (h *PartyHandlers) KickMember(w http.ResponseWriter, r *http.Request, partyId api.PartyId) {
	ctx := r.Context()

	var req struct {
		PlayerId string `json:"playerId"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	if err := h.service.KickMember(ctx, string(partyId), req.PlayerId); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to kick member", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Member kicked successfully",
	})
}

// UpdateSettings implements api.ServerInterface
func (h *PartyHandlers) UpdateSettings(w http.ResponseWriter, r *http.Request, partyId api.PartyId) {
	ctx := r.Context()

	var req api.PartySettingsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	if err := h.service.UpdateSettings(ctx, string(partyId), &req); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to update settings", err)
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{
		"message": "Settings updated successfully",
	})
}

// RollForLoot implements api.ServerInterface
func (h *PartyHandlers) RollForLoot(w http.ResponseWriter, r *http.Request, partyId api.PartyId) {
	ctx := r.Context()

	var req api.LootRollRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	// TODO: Get player ID from auth context
	playerID := "player-001"

	result, err := h.service.RollForLoot(ctx, string(partyId), playerID, req.ItemId.String(), string(req.RollType))
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to roll for loot", err)
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// Helper functions
func respondJSON(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, statusCode int, message string, err error) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(map[string]interface{}{
		"error": map[string]interface{}{
			"code":    statusCode,
			"message": message,
			"details": err.Error(),
		},
	})
}
