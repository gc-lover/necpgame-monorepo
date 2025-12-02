// Issue: #139
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/party-service-go/pkg/api"
	"github.com/oapi-codegen/runtime/types"
)

// Handlers реализует api.ServerInterface
type Handlers struct {
	service Service
}

// NewHandlers создает handlers с dependency injection
func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

// CreateParty создает группу
func (h *Handlers) CreateParty(w http.ResponseWriter, r *http.Request) {
	var req api.CreatePartyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	
	response, err := h.service.CreateParty(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, response)
}

// GetParty получает информацию о группе
func (h *Handlers) GetParty(w http.ResponseWriter, r *http.Request, partyId types.UUID) {
	response, err := h.service.GetParty(r.Context(), partyId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Party not found")
		return
	}
	
	respondJSON(w, http.StatusOK, response)
}

// DeleteParty распускает группу
func (h *Handlers) DeleteParty(w http.ResponseWriter, r *http.Request, partyId types.UUID) {
	err := h.service.DeleteParty(r.Context(), partyId.String())
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, map[string]string{"status": "deleted"})
}

// InviteToParty приглашает игрока
func (h *Handlers) InviteToParty(w http.ResponseWriter, r *http.Request, partyId types.UUID) {
	var req api.InviteRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	
	response, err := h.service.InviteToParty(r.Context(), partyId.String(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, response)
}

// AcceptInvite принимает приглашение
func (h *Handlers) AcceptInvite(w http.ResponseWriter, r *http.Request, inviteId types.UUID) {
	err := h.service.AcceptInvite(r.Context(), inviteId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, map[string]string{"status": "accepted"})
}

// DeclineInvite отклоняет приглашение
func (h *Handlers) DeclineInvite(w http.ResponseWriter, r *http.Request, inviteId types.UUID) {
	err := h.service.DeclineInvite(r.Context(), inviteId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, map[string]string{"status": "declined"})
}

// LeaveParty выйти из группы
func (h *Handlers) LeaveParty(w http.ResponseWriter, r *http.Request, partyId types.UUID) {
	err := h.service.LeaveParty(r.Context(), partyId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, map[string]string{"status": "left"})
}

// KickMember кикнуть участника
func (h *Handlers) KickMember(w http.ResponseWriter, r *http.Request, partyId types.UUID, memberId types.UUID) {
	err := h.service.KickMember(r.Context(), partyId.String(), memberId.String())
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, map[string]string{"status": "kicked"})
}

// UpdatePartySettings обновить настройки группы
func (h *Handlers) UpdatePartySettings(w http.ResponseWriter, r *http.Request, partyId types.UUID) {
	var req api.PartySettingsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	
	response, err := h.service.UpdateSettings(r.Context(), partyId.String(), &req)
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, response)
}

// RollForLoot сделать roll на лут
func (h *Handlers) RollForLoot(w http.ResponseWriter, r *http.Request, partyId types.UUID) {
	var req api.LootRollRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	
	response, err := h.service.RollForLoot(r.Context(), partyId.String(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, response)
}

// PassLootRoll пропустить roll
func (h *Handlers) PassLootRoll(w http.ResponseWriter, r *http.Request, partyId types.UUID, rollId types.UUID) {
	err := h.service.PassLootRoll(r.Context(), partyId.String(), rollId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}
	
	respondJSON(w, http.StatusOK, map[string]string{"status": "passed"})
}

// Response helpers
func respondJSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, code int, message string) {
	respondJSON(w, code, map[string]string{"error": message})
}

