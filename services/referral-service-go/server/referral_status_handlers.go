package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/referral-service-go/models"
)

func (s *HTTPServer) getReferralStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var status *models.ReferralStatus
	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		st := models.ReferralStatus(statusStr)
		status = &st
	}

	limit := 50
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

	referrals, total, err := s.referralService.GetReferralStatus(r.Context(), playerID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get referral status")
		s.respondError(w, http.StatusInternalServerError, "failed to get referral status")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id": playerID,
		"referrals": referrals,
		"total":     total,
		"limit":     limit,
		"offset":    offset,
	})
}

func (s *HTTPServer) getMilestones(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	milestones, currentMilestone, err := s.referralService.GetMilestones(r.Context(), playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get milestones")
		s.respondError(w, http.StatusInternalServerError, "failed to get milestones")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id":        playerID,
		"milestones":       milestones,
		"current_milestone": currentMilestone,
	})
}

func (s *HTTPServer) claimMilestoneReward(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	milestoneIDStr := vars["milestone_id"]

	milestoneID, err := uuid.Parse(milestoneIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid milestone_id")
		return
	}

	var req struct {
		PlayerID uuid.UUID `json:"player_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	milestone, err := s.referralService.ClaimMilestoneReward(r.Context(), req.PlayerID, milestoneID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to claim milestone reward")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"success":        true,
		"milestone_id":  milestone.ID.String(),
		"reward_amount": 1000,
		"currency_type": "credits",
	})
}

