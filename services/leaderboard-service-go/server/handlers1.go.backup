package server

import (
	"net/http"
	"github.com/google/uuid"
	"strconv"
)

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}



func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}



func (s *HTTPServer) getGlobalLeaderboard(w http.ResponseWriter, r *http.Request) {
	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
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

	result, err := s.leaderboardService.GetGlobalLeaderboard(r.Context(), metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get global leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get global leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}



func (s *HTTPServer) getSeasonalLeaderboard(w http.ResponseWriter, r *http.Request) {
	seasonID := r.URL.Query().Get("season_id")
	if seasonID == "" {
		s.respondError(w, http.StatusBadRequest, "season_id is required")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
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

	result, err := s.leaderboardService.GetSeasonalLeaderboard(r.Context(), seasonID, metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get seasonal leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get seasonal leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}



func (s *HTTPServer) getClassLeaderboard(w http.ResponseWriter, r *http.Request) {
	classIDStr := r.URL.Query().Get("class_id")
	if classIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "class_id is required")
		return
	}

	classID, err := uuid.Parse(classIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid class_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
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

	result, err := s.leaderboardService.GetClassLeaderboard(r.Context(), classID, metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get class leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get class leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}



func (s *HTTPServer) getFriendsLeaderboard(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	result, err := s.leaderboardService.GetFriendsLeaderboard(r.Context(), characterID, metric, limit)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get friends leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get friends leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}



func (s *HTTPServer) getGuildLeaderboard(w http.ResponseWriter, r *http.Request) {
	guildIDStr := r.URL.Query().Get("guild_id")
	if guildIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "guild_id is required")
		return
	}

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
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

	result, err := s.leaderboardService.GetGuildLeaderboard(r.Context(), guildID, metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get guild leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get guild leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}



func (s *HTTPServer) getPlayerRank(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	scope := models.ScopeGlobal
	if scopeStr := r.URL.Query().Get("scope"); scopeStr != "" {
		scope = models.LeaderboardScope(scopeStr)
	}

	var seasonID *string
	if seasonIDStr := r.URL.Query().Get("season_id"); seasonIDStr != "" {
		seasonID = &seasonIDStr
	}

	rank, err := s.leaderboardService.GetPlayerRank(r.Context(), characterID, metric, scope, seasonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player rank")
		s.respondError(w, http.StatusInternalServerError, "failed to get player rank")
		return
	}

	if rank == nil {
		s.respondError(w, http.StatusNotFound, "player rank not found")
		return
	}

	s.respondJSON(w, http.StatusOK, rank)
}



func (s *HTTPServer) getRankNeighbors(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
