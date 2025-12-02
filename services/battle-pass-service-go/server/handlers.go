// Issue: #227
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-service-go/pkg/api"
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetCurrentSeason implements GET /api/v1/economy/battle-pass/current
func (h *Handlers) GetCurrentSeason(w http.ResponseWriter, r *http.Request) {
	season, err := h.service.GetCurrentSeason(r.Context())
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "No active season")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, season)
}

// GetPlayerProgress implements GET /api/v1/economy/battle-pass/progress
func (h *Handlers) GetPlayerProgress(w http.ResponseWriter, r *http.Request, params api.GetPlayerProgressParams) {
	progress, err := h.service.GetPlayerProgress(r.Context(), *params.PlayerId)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Player progress not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, progress)
}

// ClaimReward implements POST /api/v1/economy/battle-pass/claim
func (h *Handlers) ClaimReward(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID string `json:"player_id"`
		Level    int    `json:"level"`
		Track    string `json:"track"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.ClaimReward(r.Context(), req.PlayerID, req.Level, api.RewardTrack(req.Track))
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Reward not found")
			return
		}
		if err == ErrAlreadyClaimed {
			respondError(w, http.StatusBadRequest, "Reward already claimed")
			return
		}
		if err == ErrPremiumRequired {
			respondError(w, http.StatusBadRequest, "Premium required")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// PurchasePremium implements POST /api/v1/economy/battle-pass/purchase-premium
func (h *Handlers) PurchasePremium(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID string `json:"player_id"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.PurchasePremium(r.Context(), req.PlayerID)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Player not found")
			return
		}
		if err == ErrAlreadyPremium {
			respondError(w, http.StatusBadRequest, "Premium already purchased")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// GetWeeklyChallenges implements GET /api/v1/economy/battle-pass/challenges/weekly
func (h *Handlers) GetWeeklyChallenges(w http.ResponseWriter, r *http.Request, params api.GetWeeklyChallengesParams) {
	challenges, err := h.service.GetWeeklyChallenges(r.Context(), *params.PlayerId)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, challenges)
}

// CompleteChallenge implements POST /api/v1/economy/battle-pass/challenges/{challengeId}/complete
func (h *Handlers) CompleteChallenge(w http.ResponseWriter, r *http.Request, challengeId string) {
	var req struct {
		PlayerID string `json:"player_id"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.CompleteChallenge(r.Context(), req.PlayerID, challengeId)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Challenge not found")
			return
		}
		if err == ErrAlreadyCompleted {
			respondError(w, http.StatusBadRequest, "Challenge already completed")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

// AddXP implements POST /api/v1/economy/battle-pass/xp/add
func (h *Handlers) AddXP(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID  string `json:"player_id"`
		XPAmount  int    `json:"xp_amount"`
		Source    string `json:"source"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.AddXP(r.Context(), req.PlayerID, req.XPAmount, req.Source)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, result)
}

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	respondJSON(w, status, map[string]string{"error": message})
}
