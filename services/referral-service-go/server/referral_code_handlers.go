package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

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

