package server

import (
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/leaderboard-service-go/models"
)

func (s *HTTPServer) listLeaderboards(w http.ResponseWriter, r *http.Request) {
	var leaderboardType *models.LeaderboardType
	if typeStr := r.URL.Query().Get("type"); typeStr != "" {
		lt := models.LeaderboardType(typeStr)
		leaderboardType = &lt
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

	result, err := s.leaderboardService.GetLeaderboards(r.Context(), leaderboardType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list leaderboards")
		s.respondError(w, http.StatusInternalServerError, "failed to list leaderboards")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getLeaderboard(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	result, err := s.leaderboardService.GetLeaderboard(r.Context(), leaderboardID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard")
		return
	}

	if result == nil {
		s.respondError(w, http.StatusNotFound, "leaderboard not found")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getLeaderboardTop(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	limit := 100
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 1000 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	result, err := s.leaderboardService.GetLeaderboardTop(r.Context(), leaderboardID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard top")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard top")
		return
	}

	if result == nil {
		s.respondError(w, http.StatusNotFound, "leaderboard not found")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getLeaderboardPlayerRank(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]
	playerIDStr := vars["playerId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	rank, err := s.leaderboardService.GetLeaderboardPlayerRank(r.Context(), leaderboardID, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard player rank")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard player rank")
		return
	}

	if rank == nil {
		s.respondError(w, http.StatusNotFound, "player rank not found")
		return
	}

	s.respondJSON(w, http.StatusOK, rank)
}

func (s *HTTPServer) getLeaderboardRankAround(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]
	playerIDStr := vars["playerId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	rangeSize := 5
	if rangeStr := r.URL.Query().Get("range"); rangeStr != "" {
		if r, err := strconv.Atoi(rangeStr); err == nil && r > 0 && r <= 50 {
			rangeSize = r
		}
	}

	entries, err := s.leaderboardService.GetLeaderboardRankAround(r.Context(), leaderboardID, playerID, rangeSize)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard rank around")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard rank around")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"entries": entries,
		"total":   len(entries),
	})
}

