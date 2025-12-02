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

	rangeSize := 5
	if rangeStr := r.URL.Query().Get("range"); rangeStr != "" {
		if r, err := strconv.Atoi(rangeStr); err == nil && r > 0 && r <= 50 {
			rangeSize = r
		}
	}

	var seasonID *string
	if seasonIDStr := r.URL.Query().Get("season_id"); seasonIDStr != "" {
		seasonID = &seasonIDStr
	}

	entries, err := s.leaderboardService.GetRankNeighbors(r.Context(), characterID, metric, scope, rangeSize, seasonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get rank neighbors")
		s.respondError(w, http.StatusInternalServerError, "failed to get rank neighbors")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"entries": entries,
		"total":   len(entries),
	})
}



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



func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}



func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}



func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}



func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		next.ServeHTTP(w, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
		}).Info("HTTP request")
	})
}



func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, http.StatusText(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}



func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}



func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.authEnabled || s.jwtValidator == nil {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid or expired token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		ctx = context.WithValue(ctx, "user_id", claims.Subject)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}



