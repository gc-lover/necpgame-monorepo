// Issue: #44
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/league-service-go/pkg/api"
)

type Handlers struct {
	service *LeagueService
}

func NewHandlers(service *LeagueService) *Handlers {
	return &Handlers{service: service}
}

// Реализация api.ServerInterface

// GetCurrentLeague получает текущую лигу
func (h *Handlers) GetCurrentLeague(w http.ResponseWriter, r *http.Request) {
	league, err := h.service.GetCurrentLeague(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, league)
}

// GetLeague получает информацию о лиге
func (h *Handlers) GetLeague(w http.ResponseWriter, r *http.Request, leagueId string) {
	league, err := h.service.GetLeagueByID(r.Context(), leagueId)
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, league)
}

// GetPlayerLeagueProgress получает прогресс игрока в лиге
func (h *Handlers) GetPlayerLeagueProgress(w http.ResponseWriter, r *http.Request, playerId string) {
	progress, err := h.service.GetPlayerProgress(r.Context(), playerId)
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, progress)
}

// Helper functions

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func respondError(w http.ResponseWriter, status int, message string) {
	error := api.Error{
		Code:    status,
		Message: message,
	}
	respondJSON(w, status, error)
}

