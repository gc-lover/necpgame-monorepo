package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2030 - Voice chat moderation and abuse reporting
func (s *VoiceChatService) ReportVoiceAbuse(w http.ResponseWriter, r *http.Request) {
	var req ReportAbuseRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode abuse report")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	s.metrics.ModerationActions.Inc()

	resp := &ReportAbuseResponse{
		ReportID:         generateReportID(),
		ReportedUserID:   req.ReportedUserID,
		ChannelID:        req.ChannelID,
		AbuseType:        req.AbuseType,
		Status:           "submitted",
		SubmittedAt:      time.Now().Unix(),
		EstimatedReviewTime: "2 hours",
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"reporter_id":     userID,
		"reported_user_id": req.ReportedUserID,
		"abuse_type":      req.AbuseType,
	}).Info("voice abuse reported")
}

func (s *VoiceChatService) MuteUser(w http.ResponseWriter, r *http.Request) {
	var req MuteUserRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode mute user request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	// TODO: Check if user has moderation permissions
	// For now, allow any authenticated user to mute (should be restricted)

	channelValue, exists := s.channels.Load(req.ChannelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	participant, exists := channel.Participants[req.UserID]
	if !exists {
		http.Error(w, "User not found in channel", http.StatusNotFound)
		return
	}

	// Apply mute
	if req.MuteType == "voice" || req.MuteType == "both" {
		participant.IsMuted = true
	}
	if req.MuteType == "text" || req.MuteType == "both" {
		// TODO: Implement text muting if applicable
	}

	duration := time.Duration(req.DurationMinutes) * time.Minute
	expiresAt := time.Now().Add(duration)

	s.metrics.ModerationActions.Inc()

	resp := &MuteUserResponse{
		UserID:          req.UserID,
		ChannelID:       req.ChannelID,
		MuteType:        req.MuteType,
		DurationMinutes: req.DurationMinutes,
		MutedBy:         userID,
		MutedAt:         time.Now().Unix(),
		ExpiresAt:       expiresAt.Unix(),
		Reason:          req.Reason,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"muted_user_id": req.UserID,
		"muted_by":      userID,
		"channel_id":    req.ChannelID,
		"mute_type":     req.MuteType,
		"duration_min":  req.DurationMinutes,
	}).Info("user muted in voice channel")
}
