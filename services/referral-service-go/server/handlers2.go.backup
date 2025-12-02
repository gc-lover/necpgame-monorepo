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



func (s *HTTPServer) getEvents(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var eventType *models.ReferralEventType
	if eventTypeStr := r.URL.Query().Get("event_type"); eventTypeStr != "" {
		et := models.ReferralEventType(eventTypeStr)
		eventType = &et
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

	events, total, err := s.referralService.GetEvents(r.Context(), playerID, eventType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get events")
		s.respondError(w, http.StatusInternalServerError, "failed to get events")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"events": events,
		"total":  total,
		"limit":  limit,
		"offset": offset,
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



