package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/referral-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          *mux.Router
	referralService ReferralService
	logger          *logrus.Logger
	server          *http.Server
	jwtValidator    *JwtValidator
	authEnabled     bool
}

func NewHTTPServer(addr string, referralService ReferralService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:            addr,
		router:          router,
		referralService: referralService,
		logger:          GetLogger(),
		jwtValidator:    jwtValidator,
		authEnabled:     authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/growth/referral").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.HandleFunc("/code", server.getReferralCode).Methods("GET")
	api.HandleFunc("/code/generate", server.generateReferralCode).Methods("POST")
	api.HandleFunc("/code/{code}/validate", server.validateReferralCode).Methods("GET")

	api.HandleFunc("/register", server.registerWithCode).Methods("POST")
	api.HandleFunc("/status/{player_id}", server.getReferralStatus).Methods("GET")

	api.HandleFunc("/milestones/{player_id}", server.getMilestones).Methods("GET")
	api.HandleFunc("/milestones/{milestone_id}/claim", server.claimMilestoneReward).Methods("POST")

	api.HandleFunc("/rewards/distribute", server.distributeRewards).Methods("POST")
	api.HandleFunc("/rewards/history/{player_id}", server.getRewardHistory).Methods("GET")

	api.HandleFunc("/stats/{player_id}", server.getReferralStats).Methods("GET")
	api.HandleFunc("/stats/public/{code}", server.getPublicReferralStats).Methods("GET")

	api.HandleFunc("/leaderboard", server.getLeaderboard).Methods("GET")
	api.HandleFunc("/leaderboard/{player_id}/position", server.getLeaderboardPosition).Methods("GET")

	api.HandleFunc("/events/{player_id}", server.getEvents).Methods("GET")

	router.HandleFunc("/health", server.healthCheck).Methods("GET")

	return server
}

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

func (s *HTTPServer) getReferralCode(w http.ResponseWriter, r *http.Request) {
	playerIDStr := r.URL.Query().Get("player_id")
	if playerIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "player_id is required")
		return
	}

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	code, err := s.referralService.GetReferralCode(r.Context(), playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get referral code")
		s.respondError(w, http.StatusInternalServerError, "failed to get referral code")
		return
	}

	if code == nil {
		s.respondError(w, http.StatusNotFound, "referral code not found")
		return
	}

	s.respondJSON(w, http.StatusOK, code)
}

func (s *HTTPServer) generateReferralCode(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID uuid.UUID `json:"player_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	code, err := s.referralService.GenerateReferralCode(r.Context(), req.PlayerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to generate referral code")
		s.respondError(w, http.StatusInternalServerError, "failed to generate referral code")
		return
	}

	s.respondJSON(w, http.StatusCreated, code)
}

func (s *HTTPServer) validateReferralCode(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	code := vars["code"]

	referralCode, err := s.referralService.ValidateReferralCode(r.Context(), code)
	if err != nil {
		s.respondJSON(w, http.StatusOK, map[string]interface{}{
			"code":      code,
			"is_valid":  false,
			"message":   err.Error(),
		})
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"code":     code,
		"is_valid": true,
		"player_id": referralCode.PlayerID.String(),
	})
}

func (s *HTTPServer) registerWithCode(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID    uuid.UUID `json:"player_id"`
		ReferralCode string   `json:"referral_code"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	referral, err := s.referralService.RegisterWithCode(r.Context(), req.PlayerID, req.ReferralCode)
	if err != nil {
		s.logger.WithError(err).Error("Failed to register with code")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusCreated, referral)
}

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

