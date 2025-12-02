package server

import (
	"net/http"
	"github.com/google/uuid"
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
