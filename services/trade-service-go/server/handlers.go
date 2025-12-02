// Issue: #131
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/trade-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type Handlers struct {
	service Service
}

func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) CreateTradeSession(w http.ResponseWriter, r *http.Request) {
	var req api.CreateTradeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.CreateTradeSession(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetTradeSession(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	response, err := h.service.GetTradeSession(r.Context(), sessionId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Trade session not found")
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) CancelTradeSession(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	err := h.service.CancelTradeSession(r.Context(), sessionId.String())
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "cancelled"})
}

func (h *Handlers) AddTradeItems(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	var req api.AddItemsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.AddTradeItems(r.Context(), sessionId.String(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) AddTradeCurrency(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	var req api.AddCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.AddTradeCurrency(r.Context(), sessionId.String(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) SetTradeReady(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	var req api.ReadyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.SetTradeReady(r.Context(), sessionId.String(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) CompleteTrade(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	response, err := h.service.CompleteTrade(r.Context(), sessionId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetTradeHistory(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetTradeHistoryParams) {
	response, err := h.service.GetTradeHistory(r.Context(), playerId.String(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func respondJSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, code int, message string) {
	respondJSON(w, code, map[string]string{"error": message})
}

