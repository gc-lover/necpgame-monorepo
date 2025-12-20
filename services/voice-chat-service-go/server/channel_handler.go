package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/sirupsen/logrus"
)

// CreateChannel OPTIMIZATION: Issue #2030 - Voice channel management with concurrent access
func (s *VoiceChatService) CreateChannel(w http.ResponseWriter, r *http.Request) {
	var req CreateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create channel request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	channel := &VoiceChannel{
		ChannelID:        generateChannelID(),
		Name:             req.Name,
		Type:             req.Type,
		Description:      req.Description,
		CreatorID:        userID,
		CreatedAt:        time.Now(),
		MaxParticipants:  req.MaxParticipants,
		ParticipantCount: 0,
		IsPrivate:        req.Password != "",
		Password:         req.Password,
		AudioSettings:    req.AudioSettings,
		Permissions:      req.Permissions,
		Participants:     make(map[string]*ChannelParticipant),
		LastActivity:     time.Now(),
	}

	s.channels.Store(channel.ChannelID, channel)
	s.metrics.ChannelOperations.Inc()
	s.metrics.ActiveChannels.Inc()

	resp := &CreateChannelResponse{
		ChannelID:    channel.ChannelID,
		Name:         channel.Name,
		Type:         channel.Type,
		CreatedAt:    channel.CreatedAt.Unix(),
		CreatorID:    channel.CreatorID,
		InviteCode:   generateInviteCode(),
		WebSocketURL: fmt.Sprintf("ws://%s/voice/stream/%s", s.config.WebSocketAddr, channel.ChannelID),
		Settings:     &ChannelSettings{}, // TODO: Convert audio settings
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"channel_id": channel.ChannelID,
		"name":       channel.Name,
		"creator_id": userID,
	}).Info("voice channel created successfully")
}

func (s *VoiceChatService) GetChannels(w http.ResponseWriter, r *http.Request) {
	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var channels []*ChannelSummary
	totalCount := 0

	s.channels.Range(func(key, value interface{}) bool {
		channel := value.(*VoiceChannel)
		totalCount++

		// TODO: Check user permissions for private channels

		summary := &ChannelSummary{
			ChannelID:        channel.ChannelID,
			Name:             channel.Name,
			Type:             channel.Type,
			ParticipantCount: channel.ParticipantCount,
			MaxParticipants:  channel.MaxParticipants,
			IsPrivate:        channel.IsPrivate,
			CreatorName:      "Unknown", // TODO: Get from user service
			CreatedAt:        channel.CreatedAt.Unix(),
			LastActivity:     channel.LastActivity.Unix(),
		}
		channels = append(channels, summary)
		return true
	})

	resp := &GetChannelsResponse{
		Channels:   channels,
		TotalCount: totalCount,
		Page:       1,
		PageSize:   len(channels),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *VoiceChatService) GetChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")
	userID := r.Header.Get("X-User-ID")

	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	// TODO: Check permissions for private channels

	var participants []*ChannelParticipant
	for _, participant := range channel.Participants {
		participants = append(participants, participant)
	}

	channelDetails := &ChannelDetails{
		ChannelID:        channel.ChannelID,
		Name:             channel.Name,
		Type:             channel.Type,
		Description:      channel.Description,
		ParticipantCount: channel.ParticipantCount,
		MaxParticipants:  channel.MaxParticipants,
		IsPrivate:        channel.IsPrivate,
		CreatorID:        channel.CreatorID,
		CreatorName:      "Unknown", // TODO: Get from user service
		CreatedAt:        channel.CreatedAt.Unix(),
		LastActivity:     channel.LastActivity.Unix(),
		Settings:         &ChannelSettings{},   // TODO: Convert settings
		Statistics:       &ChannelStatistics{}, // TODO: Calculate stats
	}

	resp := &GetChannelResponse{
		Channel:       channelDetails,
		Participants:  participants,
		IsParticipant: s.isUserInChannel(userID, channelID),
		CanJoin:       channel.ParticipantCount < channel.MaxParticipants,
		Permissions:   &UserPermissions{CanSpeak: true}, // TODO: Check actual permissions
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *VoiceChatService) UpdateChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")

	var req UpdateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode update channel request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	// TODO: Check if user can modify channel

	// Update fields
	if req.Name != "" {
		channel.Name = req.Name
	}
	if req.Description != "" {
		channel.Description = req.Description
	}
	if req.MaxParticipants > 0 {
		channel.MaxParticipants = req.MaxParticipants
	}
	if req.Password != "" {
		channel.Password = req.Password
		channel.IsPrivate = true
	}

	channel.LastActivity = time.Now()

	resp := &UpdateChannelResponse{
		ChannelID:     channelID,
		UpdatedFields: []string{"name", "description"}, // TODO: Track actual updates
		UpdatedAt:     channel.LastActivity.Unix(),
		UpdatedBy:     userID,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"channel_id": channelID,
		"user_id":    userID,
	}).Info("voice channel updated successfully")
}

func (s *VoiceChatService) DeleteChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")

	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	// TODO: Check if user can delete channel
	if channel.CreatorID != userID {
		http.Error(w, "Insufficient permissions", http.StatusForbidden)
		return
	}

	// Remove all participants
	for userID := range channel.Participants {
		delete(channel.Participants, userID)
	}

	s.channels.Delete(channelID)
	s.metrics.ActiveChannels.Dec()
	s.metrics.ChannelOperations.Inc()

	w.WriteHeader(http.StatusNoContent)

	s.logger.WithFields(logrus.Fields{
		"channel_id": channelID,
		"user_id":    userID,
	}).Info("voice channel deleted successfully")
}

func (s *VoiceChatService) JoinChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")

	var req JoinChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode join channel request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	// Check password for private channels
	if channel.IsPrivate && req.Password != channel.Password {
		http.Error(w, "Invalid password", http.StatusForbidden)
		return
	}

	// Check participant limit
	if channel.ParticipantCount >= channel.MaxParticipants {
		http.Error(w, "Channel full", http.StatusTooManyRequests)
		return
	}

	// Add user to channel
	participant := &ChannelParticipant{
		UserID:      userID,
		Username:    "Unknown", // TODO: Get from user service
		DisplayName: "Unknown", // TODO: Get from user service
		JoinedAt:    time.Now().Unix(),
		IsMuted:     false,
		IsDeafened:  false,
		Speaking:    false,
		VolumeLevel: 1.0,
		Role:        "participant",
	}

	channel.Participants[userID] = participant
	channel.ParticipantCount++
	channel.LastActivity = time.Now()

	s.metrics.ChannelOperations.Inc()

	resp := &JoinChannelResponse{
		ChannelID:    channelID,
		WebSocketURL: fmt.Sprintf("ws://%s/voice/stream/%s", s.config.WebSocketAddr, channelID),
		SessionToken: generateSessionToken(),
		AudioConfig: &AudioConfig{
			SampleRate: channel.AudioSettings.SampleRate,
			Channels:   channel.AudioSettings.Channels,
			Codec:      channel.AudioSettings.Codec,
			Bitrate:    channel.AudioSettings.Bitrate,
			BufferSize: s.config.AudioBufferSize,
			ICEServers: []string{"stun:stun.l.google.com:19302"},
		},
		JoinedAt: time.Now().Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"user_id":    userID,
		"channel_id": channelID,
	}).Info("user joined voice channel")
}

func (s *VoiceChatService) LeaveChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")
	userID := r.Header.Get("X-User-ID")

	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found or not a member", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	if _, isParticipant := channel.Participants[userID]; !isParticipant {
		http.Error(w, "Channel not found or not a member", http.StatusNotFound)
		return
	}

	// Remove user from channel
	delete(channel.Participants, userID)
	channel.ParticipantCount--
	channel.LastActivity = time.Now()

	s.metrics.ChannelOperations.Inc()

	resp := &LeaveChannelResponse{
		ChannelID:       channelID,
		LeftAt:          time.Now().Unix(),
		SessionDuration: 0, // TODO: Calculate actual duration
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"user_id":    userID,
		"channel_id": channelID,
	}).Info("user left voice channel")
}

// Helper methods
func (s *VoiceChatService) isUserInChannel(userID, channelID string) bool {
	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		return false
	}

	channel := channelValue.(*VoiceChannel)
	_, isParticipant := channel.Participants[userID]
	return isParticipant
}
