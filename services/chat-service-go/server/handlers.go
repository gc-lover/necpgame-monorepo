// Issue: #172
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/chat-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type Handlers struct {
	service Service
}

func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) SendMessage(w http.ResponseWriter, r *http.Request) {
	var req api.SendMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.SendMessage(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetChannelMessages(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID, params api.GetChannelMessagesParams) {
	response, err := h.service.GetChannelMessages(r.Context(), channelId.String(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetChannels(w http.ResponseWriter, r *http.Request) {
	response, err := h.service.GetChannels(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) CreateChannel(w http.ResponseWriter, r *http.Request) {
	var req api.CreateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.CreateChannel(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetChannel(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID) {
	response, err := h.service.GetChannel(r.Context(), channelId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Channel not found")
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) UpdateChannel(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID) {
	var req api.UpdateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.UpdateChannel(r.Context(), channelId.String(), &req)
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) DeleteChannel(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID) {
	err := h.service.DeleteChannel(r.Context(), channelId.String())
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "deleted"})
}

func (h *Handlers) AddChannelMember(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID) {
	var req api.AddMemberRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	err := h.service.AddChannelMember(r.Context(), channelId.String(), &req)
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "added"})
}

func (h *Handlers) RemoveChannelMember(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID, playerId openapi_types.UUID) {
	err := h.service.RemoveChannelMember(r.Context(), channelId.String(), playerId.String())
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "removed"})
}

func (h *Handlers) BanPlayer(w http.ResponseWriter, r *http.Request) {
	var req api.BanPlayerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.BanPlayer(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) UnbanPlayer(w http.ResponseWriter, r *http.Request, banId openapi_types.UUID) {
	err := h.service.UnbanPlayer(r.Context(), banId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "unbanned"})
}

func (h *Handlers) DeleteMessage(w http.ResponseWriter, r *http.Request, messageId openapi_types.UUID) {
	err := h.service.DeleteMessage(r.Context(), messageId.String())
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "deleted"})
}

func respondJSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, code int, message string) {
	respondJSON(w, code, map[string]string{"error": message})
}

