package server

import (
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

func (s *HTTPServer) getReferralStats(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	stats, err := s.referralService.GetReferralStats(r.Context(), playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get referral stats")
		s.respondError(w, http.StatusInternalServerError, "failed to get referral stats")
		return
	}

	s.respondJSON(w, http.StatusOK, stats)
}

func (s *HTTPServer) getPublicReferralStats(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	code := vars["code"]

	stats, err := s.referralService.GetPublicReferralStats(r.Context(), code)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get public referral stats")
		s.respondError(w, http.StatusNotFound, "referral code not found")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"code":              code,
		"total_referrals":   stats.TotalReferrals,
		"active_referrals":  stats.ActiveReferrals,
		"level_10_referrals": stats.Level10Referrals,
		"current_milestone":  stats.CurrentMilestone,
	})
}

