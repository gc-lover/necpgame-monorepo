package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/referral-service-go/models"
)

func (s *HTTPServer) distributeRewards(w http.ResponseWriter, r *http.Request) {
	var req struct {
		ReferralID uuid.UUID              `json:"referral_id"`
		RewardType models.ReferralRewardType `json:"reward_type"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if err := s.referralService.DistributeRewards(r.Context(), req.ReferralID, req.RewardType); err != nil {
		s.logger.WithError(err).Error("Failed to distribute rewards")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"success":            true,
		"rewards_distributed": []interface{}{},
	})
}

func (s *HTTPServer) getRewardHistory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var rewardType *models.ReferralRewardType
	if rewardTypeStr := r.URL.Query().Get("reward_type"); rewardTypeStr != "" {
		rt := models.ReferralRewardType(rewardTypeStr)
		rewardType = &rt
	}

	limit := 20
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	rewards, total, err := s.referralService.GetRewardHistory(r.Context(), playerID, rewardType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get reward history")
		s.respondError(w, http.StatusInternalServerError, "failed to get reward history")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id": playerID,
		"rewards":   rewards,
		"total":     total,
		"limit":     limit,
		"offset":    offset,
	})
}

