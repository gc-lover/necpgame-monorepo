package server

import (
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/referral-service-go/models"
)

func (s *HTTPServer) getLeaderboard(w http.ResponseWriter, r *http.Request) {
	leaderboardTypeStr := r.URL.Query().Get("leaderboard_type")
	if leaderboardTypeStr == "" {
		s.respondError(w, http.StatusBadRequest, "leaderboard_type is required")
		return
	}

	leaderboardType := models.ReferralLeaderboardType(leaderboardTypeStr)
	if leaderboardType != models.LeaderboardTypeTopReferrers &&
		leaderboardType != models.LeaderboardTypeTopMilestone &&
		leaderboardType != models.LeaderboardTypeTopRewards {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_type")
		return
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

	entries, total, err := s.referralService.GetLeaderboard(r.Context(), leaderboardType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"leaderboard_type": leaderboardType,
		"entries":          entries,
		"total":            total,
		"limit":            limit,
		"offset":           offset,
	})
}

func (s *HTTPServer) getLeaderboardPosition(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	leaderboardTypeStr := r.URL.Query().Get("leaderboard_type")
	if leaderboardTypeStr == "" {
		s.respondError(w, http.StatusBadRequest, "leaderboard_type is required")
		return
	}

	leaderboardType := models.ReferralLeaderboardType(leaderboardTypeStr)
	if leaderboardType != models.LeaderboardTypeTopReferrers &&
		leaderboardType != models.LeaderboardTypeTopMilestone &&
		leaderboardType != models.LeaderboardTypeTopRewards {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_type")
		return
	}

	entry, position, err := s.referralService.GetLeaderboardPosition(r.Context(), playerID, leaderboardType)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard position")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard position")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id":       playerID,
		"leaderboard_type": leaderboardType,
		"position":        position,
		"entry":           entry,
	})
}

