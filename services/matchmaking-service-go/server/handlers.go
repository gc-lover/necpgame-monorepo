// Issue: #150
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/matchmaking-service-go/pkg/api"
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

// EnterQueue добавляет игрока в очередь
func (h *Handlers) EnterQueue(w http.ResponseWriter, r *http.Request) {
	var req api.EnterQueueRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	response, err := h.service.EnterQueue(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// GetQueueStatus получает статус очереди
func (h *Handlers) GetQueueStatus(w http.ResponseWriter, r *http.Request, queueId types.UUID) {
	response, err := h.service.GetQueueStatus(r.Context(), queueId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Queue not found")
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// LeaveQueue удаляет игрока из очереди
func (h *Handlers) LeaveQueue(w http.ResponseWriter, r *http.Request, queueId types.UUID) {
	response, err := h.service.LeaveQueue(r.Context(), queueId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Queue not found")
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// GetPlayerRating получает рейтинг игрока
func (h *Handlers) GetPlayerRating(w http.ResponseWriter, r *http.Request, playerId api.PlayerId) {
	response, err := h.service.GetPlayerRating(r.Context(), playerId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Player not found")
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// GetLeaderboard получает таблицу лидеров
func (h *Handlers) GetLeaderboard(w http.ResponseWriter, r *http.Request, activityType string, params api.GetLeaderboardParams) {
	response, err := h.service.GetLeaderboard(r.Context(), activityType, params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// AcceptMatch принимает матч
func (h *Handlers) AcceptMatch(w http.ResponseWriter, r *http.Request, matchId types.UUID) {
	err := h.service.AcceptMatch(r.Context(), matchId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{"status": "accepted"})
}

// DeclineMatch отклоняет матч
func (h *Handlers) DeclineMatch(w http.ResponseWriter, r *http.Request, matchId types.UUID) {
	err := h.service.DeclineMatch(r.Context(), matchId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, map[string]string{"status": "declined"})
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

