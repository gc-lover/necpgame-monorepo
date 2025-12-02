// Issue: #131
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/economy-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type TradeHandlers struct {
	tradeService TradeServiceInterface
	logger       *logrus.Logger
}

func NewTradeHandlers(tradeService TradeServiceInterface) *TradeHandlers {
	return &TradeHandlers{
		tradeService: tradeService,
		logger:       GetLogger(),
	}
}

func (h *TradeHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if data != nil {
		if err := json.NewEncoder(w).Encode(data); err != nil {
			h.logger.WithError(err).Error("Failed to encode JSON response")
		}
	}
}

func (h *TradeHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, api.Error{
		Error:   http.StatusText(status),
		Message: message,
	})
}

func (h *TradeHandlers) CreateTradeSession(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	initiatorID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req api.CreateTradeSessionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	createReq := convertCreateTradeSessionRequestFromAPI(req)
	session, err := h.tradeService.CreateTrade(r.Context(), initiatorID, createReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create trade session")
		h.respondError(w, http.StatusInternalServerError, "failed to create trade session")
		return
	}

	if session == nil {
		h.respondError(w, http.StatusBadRequest, "cannot create trade session")
		return
	}

	h.respondJSON(w, http.StatusOK, convertTradeSessionToAPI(session))
}

func (h *TradeHandlers) CancelTradeSession(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	sessionUUID := uuid.UUID(sessionId)
	err = h.tradeService.CancelTrade(r.Context(), sessionUUID, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to cancel trade session")
		h.respondError(w, http.StatusInternalServerError, "failed to cancel trade session")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *TradeHandlers) GetTradeSession(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	sessionUUID := uuid.UUID(sessionId)
	session, err := h.tradeService.GetTrade(r.Context(), sessionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get trade session")
		h.respondError(w, http.StatusInternalServerError, "failed to get trade session")
		return
	}

	if session == nil {
		h.respondError(w, http.StatusNotFound, "trade session not found")
		return
	}

	h.respondJSON(w, http.StatusOK, convertTradeSessionToAPI(session))
}

func (h *TradeHandlers) GetTradeSessionAudit(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	sessionUUID := uuid.UUID(sessionId)
	session, err := h.tradeService.GetTrade(r.Context(), sessionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get trade session audit")
		h.respondError(w, http.StatusInternalServerError, "failed to get trade session audit")
		return
	}

	if session == nil {
		h.respondError(w, http.StatusNotFound, "trade session not found")
		return
	}

	audit := convertTradeSessionToAudit(session)
	h.respondJSON(w, http.StatusOK, audit)
}

func (h *TradeHandlers) GetTradeConfirmationStatus(w http.ResponseWriter, r *http.Request, sessionId openapi_types.UUID) {
	sessionUUID := uuid.UUID(sessionId)
	session, err := h.tradeService.GetTrade(r.Context(), sessionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get trade confirmation status")
		h.respondError(w, http.StatusInternalServerError, "failed to get trade confirmation status")
		return
	}

	if session == nil {
		h.respondError(w, http.StatusNotFound, "trade session not found")
		return
	}

	status := convertTradeSessionToConfirmationStatus(session)
	h.respondJSON(w, http.StatusOK, status)
}

func (h *TradeHandlers) GetActiveTrades(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	sessions, err := h.tradeService.GetActiveTrades(r.Context(), characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get active trades")
		h.respondError(w, http.StatusInternalServerError, "failed to get active trades")
		return
	}

	tradeSessions := make([]api.TradeSession, 0, len(sessions))
	for i := range sessions {
		apiSession := convertTradeSessionToAPI(&sessions[i])
		if apiSession != nil {
			tradeSessions = append(tradeSessions, *apiSession)
		}
	}

	response := api.ActiveTradesResponse{
		Trades: tradeSessions,
		Total:  len(tradeSessions),
	}

	h.respondJSON(w, http.StatusOK, response)
}
