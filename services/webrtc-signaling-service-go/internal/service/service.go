package service

import (
	"context"
	"fmt"
	"time"

	"go.uber.org/zap"

	"necpgame/services/webrtc-signaling-service-go/internal/repository"
	"necpgame/services/webrtc-signaling-service-go/pkg/models"
)

// Service handles business logic for WebRTC signaling
type Service struct {
	repo   *repository.Repository
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// CreateVoiceChannel creates a new voice channel
func (s *Service) CreateVoiceChannel(ctx context.Context, req models.CreateVoiceChannelRequest) (*models.VoiceChannel, error) {
	// Validate request
	if req.Name == "" {
		return nil, fmt.Errorf("channel name is required")
	}

	if req.Type == "" {
		req.Type = "party" // Default type
	}

	// Validate channel type
	validTypes := map[string]bool{"guild": true, "party": true, "global": true, "private": true}
	if !validTypes[req.Type] {
		return nil, fmt.Errorf("invalid channel type: %s", req.Type)
	}

	// Set default max participants
	if req.MaxParticipants == 0 {
		req.MaxParticipants = 10
	} else if req.MaxParticipants > 50 {
		req.MaxParticipants = 50 // Cap at maximum
	}

	channel, err := s.repo.CreateVoiceChannel(ctx, req)
	if err != nil {
		s.logger.Error("Failed to create voice channel",
			zap.String("name", req.Name),
			zap.String("type", req.Type),
			zap.Error(err))
		return nil, fmt.Errorf("failed to create voice channel: %w", err)
	}

	s.logger.Info("Voice channel created",
		zap.String("channel_id", channel.ID),
		zap.String("name", channel.Name))

	return channel, nil
}

// GetVoiceChannel retrieves a voice channel
func (s *Service) GetVoiceChannel(ctx context.Context, channelID string) (*models.VoiceChannel, error) {
	channel, err := s.repo.GetVoiceChannel(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to get voice channel",
			zap.String("channel_id", channelID),
			zap.Error(err))
		return nil, err
	}

	return channel, nil
}

// UpdateVoiceChannel updates a voice channel
func (s *Service) UpdateVoiceChannel(ctx context.Context, channelID string, req models.UpdateVoiceChannelRequest) (*models.VoiceChannel, error) {
	// Validate request
	if req.MaxParticipants != nil && (*req.MaxParticipants < 2 || *req.MaxParticipants > 50) {
		return nil, fmt.Errorf("max participants must be between 2 and 50")
	}

	if req.Status != nil {
		validStatuses := map[string]bool{"active": true, "inactive": true, "full": true, "closed": true}
		if !validStatuses[*req.Status] {
			return nil, fmt.Errorf("invalid status: %s", *req.Status)
		}
	}

	channel, err := s.repo.UpdateVoiceChannel(ctx, channelID, req)
	if err != nil {
		s.logger.Error("Failed to update voice channel",
			zap.String("channel_id", channelID),
			zap.Error(err))
		return nil, err
	}

	return channel, nil
}

// DeleteVoiceChannel deletes a voice channel
func (s *Service) DeleteVoiceChannel(ctx context.Context, channelID string) error {
	err := s.repo.DeleteVoiceChannel(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to delete voice channel",
			zap.String("channel_id", channelID),
			zap.Error(err))
		return err
	}

	return nil
}

// ListVoiceChannels lists voice channels with pagination
func (s *Service) ListVoiceChannels(ctx context.Context, limit, offset int, channelType, status string) (*models.VoiceChannelListResponse, error) {
	if limit <= 0 || limit > 100 {
		limit = 20 // Default limit
	}

	channels, total, err := s.repo.ListVoiceChannels(ctx, limit, offset, channelType, status)
	if err != nil {
		s.logger.Error("Failed to list voice channels",
			zap.Int("limit", limit),
			zap.Int("offset", offset),
			zap.Error(err))
		return nil, err
	}

	return &models.VoiceChannelListResponse{
		Channels: channels,
		Total:    total,
		HasMore:  offset+limit < total,
	}, nil
}

// JoinVoiceChannel handles joining a voice channel
func (s *Service) JoinVoiceChannel(ctx context.Context, channelID string, req models.JoinChannelRequest) (*models.JoinChannelResponse, error) {
	// Validate channel exists and is accessible
	channel, err := s.repo.GetVoiceChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	if channel.Status != "active" {
		return nil, fmt.Errorf("voice channel is not active")
	}

	if channel.CurrentParticipants >= channel.MaxParticipants {
		return nil, fmt.Errorf("voice channel is full")
	}

	// Join the channel
	err = s.repo.JoinVoiceChannel(ctx, channelID, req.UserID)
	if err != nil {
		return nil, err
	}

	// Get current participants
	participants, err := s.repo.GetChannelParticipants(ctx, channelID)
	if err != nil {
		s.logger.Warn("Failed to get participants, continuing",
			zap.String("channel_id", channelID), zap.Error(err))
		participants = []models.VoiceParticipant{}
	}

	// Generate ICE servers (mock implementation - in production, integrate with STUN/TURN service)
	iceServers := []models.ICEServer{
		{
			URLs: []string{"stun:stun.necpgame.com:19302"},
		},
		{
			URLs: []string{"turn:turn.necpgame.com:443?transport=tcp"},
			Username:   "necpgame-turn",
			Credential: "turn-secret-2024",
		},
	}

	// Generate session token (simplified - in production, use JWT)
	sessionToken := fmt.Sprintf("session_%s_%d", channelID, time.Now().Unix())

	response := &models.JoinChannelResponse{
		Channel:      *channel,
		ICEServers:   iceServers,
		SessionToken: sessionToken,
		Participants: participants,
	}

	s.logger.Info("User joined voice channel",
		zap.String("channel_id", channelID),
		zap.String("user_id", req.UserID),
		zap.Int("participants", len(participants)))

	return response, nil
}

// ExchangeSignalingMessage processes WebRTC signaling messages
func (s *Service) ExchangeSignalingMessage(ctx context.Context, channelID string, msg models.SignalingMessage) (*models.SignalingResponse, error) {
	// Validate message
	if msg.Type == "" || msg.SenderID == "" || msg.TargetID == "" {
		return nil, fmt.Errorf("invalid signaling message: missing required fields")
	}

	// Validate message type
	validTypes := map[string]bool{
		"offer": true, "answer": true, "ice_candidate": true, "hangup": true,
	}
	if !validTypes[msg.Type] {
		return nil, fmt.Errorf("invalid message type: %s", msg.Type)
	}

	// Set timestamp if not provided
	if msg.Timestamp.IsZero() {
		msg.Timestamp = time.Now()
	}

	// Record the signaling message for analytics
	err := s.repo.RecordSignalingMessage(ctx, msg)
	if err != nil {
		s.logger.Warn("Failed to record signaling message",
			zap.String("channel_id", channelID),
			zap.String("type", msg.Type),
			zap.Error(err))
		// Don't fail the operation for analytics issues
	}

	// In a real implementation, you would:
	// 1. Validate that both sender and target are in the channel
	// 2. Route the message to the target peer
	// 3. Handle ICE candidate validation
	// 4. Implement signaling message queuing for offline peers

	response := &models.SignalingResponse{
		Success:    true,
		MessageID:  fmt.Sprintf("msg_%d", time.Now().UnixNano()),
		DeliveredAt: time.Now(),
	}

	s.logger.Info("Signaling message processed",
		zap.String("channel_id", channelID),
		zap.String("type", msg.Type),
		zap.String("sender_id", msg.SenderID),
		zap.String("target_id", msg.TargetID))

	return response, nil
}

// LeaveVoiceChannel handles leaving a voice channel
func (s *Service) LeaveVoiceChannel(ctx context.Context, channelID, userID string) (*models.LeaveChannelResponse, error) {
	err := s.repo.LeaveVoiceChannel(ctx, channelID, userID)
	if err != nil {
		return nil, err
	}

	// Calculate session duration (simplified)
	sessionDuration := 1250 // seconds - in production, calculate from join time

	// Calculate quality score (simplified)
	qualityScore := 0.92 // percentage - in production, aggregate from quality reports

	response := &models.LeaveChannelResponse{
		Success:         true,
		SessionDuration: sessionDuration,
		QualityScore:    qualityScore,
	}

	s.logger.Info("User left voice channel",
		zap.String("channel_id", channelID),
		zap.String("user_id", userID),
		zap.Int("duration", sessionDuration))

	return response, nil
}

// ReportVoiceQuality processes voice quality reports
func (s *Service) ReportVoiceQuality(ctx context.Context, channelID string, report models.VoiceQualityReport) (*models.VoiceQualityResponse, error) {
	// Validate report
	if report.UserID == "" {
		return nil, fmt.Errorf("user_id is required")
	}

	// Set timestamp if not provided
	if report.Timestamp.IsZero() {
		report.Timestamp = time.Now()
	}

	// Record the quality report
	err := s.repo.RecordVoiceQualityReport(ctx, channelID, report)
	if err != nil {
		s.logger.Warn("Failed to record quality report",
			zap.String("channel_id", channelID),
			zap.String("user_id", report.UserID),
			zap.Error(err))
		// Don't fail the operation for analytics issues
	}

	// Analyze metrics and provide recommendations
	recommendedSettings := models.VoiceQualitySettings{
		Bitrate:           64000,
		SampleRate:        48000,
		Channels:          1,
		EchoCancellation:  true,
		NoiseSuppression:  true,
	}

	// Adjust recommendations based on metrics
	if report.Metrics.LatencyMs > 200 {
		recommendedSettings.Bitrate = 32000 // Reduce bitrate for high latency
	}

	if report.Metrics.PacketLossPercent > 5 {
		recommendedSettings.SampleRate = 24000 // Reduce sample rate for packet loss
	}

	nextInterval := 30 // seconds
	if report.Metrics.LatencyMs > 100 {
		nextInterval = 60 // Report less frequently for stable connections
	}

	response := &models.VoiceQualityResponse{
		Acknowledged:       true,
		RecommendedSettings: recommendedSettings,
		NextReportInterval:  nextInterval,
	}

	s.logger.Info("Voice quality report processed",
		zap.String("channel_id", channelID),
		zap.String("user_id", report.UserID),
		zap.Float64("latency", report.Metrics.LatencyMs),
		zap.Float64("packet_loss", report.Metrics.PacketLossPercent))

	return response, nil
}
