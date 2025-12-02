// Issue: #130

package server

import (
	"encoding/json"
	"net/http"

	"github.com/combat-sessions-service-go/pkg/api"
)

// CombatSessionHandlers implements api.ServerInterface
type CombatSessionHandlers struct {
	service *CombatSessionService
}

// NewCombatSessionHandlers creates new handlers
func NewCombatSessionHandlers(service *CombatSessionService) *CombatSessionHandlers {
	return &CombatSessionHandlers{
		service: service,
	}
}

// CreateCombatSession implements POST /gameplay/combat/sessions
func (h *CombatSessionHandlers) CreateCombatSession(w http.ResponseWriter, r *http.Request) {
	var req api.CreateSessionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	session, err := h.service.CreateSession(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusCreated, session)
}

// ListCombatSessions implements GET /gameplay/combat/sessions
func (h *CombatSessionHandlers) ListCombatSessions(w http.ResponseWriter, r *http.Request, params api.ListCombatSessionsParams) {
	sessions, err := h.service.ListSessions(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, sessions)
}

// GetCombatSession implements GET /gameplay/combat/sessions/{sessionId}
func (h *CombatSessionHandlers) GetCombatSession(w http.ResponseWriter, r *http.Request, sessionId api.SessionId) {
	session, err := h.service.GetSession(r.Context(), sessionId.String())
	if err != nil {
		if err == ErrSessionNotFound {
			respondError(w, http.StatusNotFound, "session not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, session)
}

// EndCombatSession implements DELETE /gameplay/combat/sessions/{sessionId}
func (h *CombatSessionHandlers) EndCombatSession(w http.ResponseWriter, r *http.Request, sessionId api.SessionId) {
	playerID := r.Context().Value("player_id").(string)

	result, err := h.service.EndSession(r.Context(), sessionId.String(), playerID)
	if err != nil {
		if err == ErrUnauthorized {
			respondError(w, http.StatusForbidden, "not authorized to end session")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// ExecuteCombatAction implements POST /gameplay/combat/sessions/{sessionId}/actions
func (h *CombatSessionHandlers) ExecuteCombatAction(w http.ResponseWriter, r *http.Request, sessionId api.SessionId) {
	var req api.ActionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID := r.Context().Value("player_id").(string)

	result, err := h.service.ExecuteAction(r.Context(), sessionId.String(), playerID, &req)
	if err != nil {
		if err == ErrActionRejected {
			respondError(w, http.StatusForbidden, "action rejected by anti-cheat")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// GetCombatState implements GET /gameplay/combat/sessions/{sessionId}/state
func (h *CombatSessionHandlers) GetCombatState(w http.ResponseWriter, r *http.Request, sessionId api.SessionId) {
	state, err := h.service.GetSessionState(r.Context(), sessionId.String())
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, state)
}

// GetCombatLogs implements GET /gameplay/combat/sessions/{sessionId}/logs
func (h *CombatSessionHandlers) GetCombatLogs(w http.ResponseWriter, r *http.Request, sessionId api.SessionId, params api.GetCombatLogsParams) {
	logs, err := h.service.GetSessionLogs(r.Context(), sessionId.String(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, logs)
}

// GetCombatStats implements GET /gameplay/combat/sessions/{sessionId}/stats
func (h *CombatSessionHandlers) GetCombatStats(w http.ResponseWriter, r *http.Request, sessionId api.SessionId) {
	stats, err := h.service.GetSessionStats(r.Context(), sessionId.String())
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, stats)
}

// Helper functions
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	respondJSON(w, status, map[string]string{"error": message})
}

