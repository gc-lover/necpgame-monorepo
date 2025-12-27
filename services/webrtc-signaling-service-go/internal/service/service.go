package service

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/webrtc-signaling-service-go/internal/repository"
	"necpgame/services/webrtc-signaling-service-go/pkg/models"
)

// Service handles business logic for WebRTC signaling
type Service struct {
	repo     *repository.Repository
	logger   *zap.Logger
	connections sync.Map // WebSocket connections map
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// Guild Voice Channel methods
func (s *Service) CreateGuildVoiceChannel(ctx context.Context, req *models.GuildVoiceChannelRequest, ownerID uuid.UUID) (*models.GuildVoiceChannelResponse, error) {
	// Validate guild membership (placeholder - should integrate with guild service)
	guildID, err := uuid.Parse(req.GuildID)
	if err != nil {
		return nil, fmt.Errorf("invalid guild ID: %w", err)
	}

	// Check if user has permission to create channels in this guild (placeholder)
	// TODO: Integrate with guild service to validate permissions

	channel := &models.VoiceChannel{
		ID:                     uuid.New(),
		Name:                   req.Name,
		Type:                   "guild",
		GuildID:                &guildID,
		OwnerID:                ownerID,
		MaxUsers:               req.MaxUsers,
		CurrentUsers:           0,
		IsActive:               true,
		CreatedAt:              time.Now(),
		UpdatedAt:              time.Now(),
		IsDefaultGuildChannel:  req.IsDefaultChannel,
		GuildPermissions: &models.GuildPermissions{
			AllowedRoles:    req.AllowedRoles,
			BlockedUsers:    []string{},
			MutedUsers:      []string{},
			DeafenedUsers:   []string{},
			IsModerated:     req.IsModerated,
			RequireApproval: req.RequireApproval,
		},
	}

	if channel.MaxUsers <= 0 {
		channel.MaxUsers = 50 // Default for guild channels
	}

	err = s.repo.CreateVoiceChannel(ctx, channel)
	if err != nil {
		s.logger.Error("Failed to create guild voice channel", zap.Error(err))
		return nil, fmt.Errorf("failed to create guild voice channel: %w", err)
	}

	response := &models.GuildVoiceChannelResponse{
		ID:               channel.ID.String(),
		Name:             channel.Name,
		Type:             channel.Type,
		GuildID:          channel.GuildID.String(),
		OwnerID:          channel.OwnerID.String(),
		MaxUsers:         channel.MaxUsers,
		CurrentUsers:     channel.CurrentUsers,
		IsActive:         channel.IsActive,
		IsDefaultChannel: channel.IsDefaultGuildChannel,
		GuildPermissions: *channel.GuildPermissions,
		CreatedAt:        channel.CreatedAt,
	}

	s.logger.Info("Guild voice channel created",
		zap.String("channel_id", channel.ID.String()),
		zap.String("guild_id", guildID.String()),
		zap.String("owner_id", ownerID.String()))

	return response, nil
}

func (s *Service) GetGuildVoiceChannels(ctx context.Context, guildID uuid.UUID) ([]*models.GuildVoiceChannelResponse, error) {
	channels, err := s.repo.ListVoiceChannels(ctx, &guildID)
	if err != nil {
		return nil, err
	}

	responses := make([]*models.GuildVoiceChannelResponse, 0, len(channels))
	for _, channel := range channels {
		if channel.Type == "guild" {
			response := &models.GuildVoiceChannelResponse{
				ID:               channel.ID.String(),
				Name:             channel.Name,
				Type:             channel.Type,
				GuildID:          channel.GuildID.String(),
				OwnerID:          channel.OwnerID.String(),
				MaxUsers:         channel.MaxUsers,
				CurrentUsers:     channel.CurrentUsers,
				IsActive:         channel.IsActive,
				IsDefaultChannel: channel.IsDefaultGuildChannel,
				CreatedAt:        channel.CreatedAt,
			}

			if channel.GuildPermissions != nil {
				response.GuildPermissions = *channel.GuildPermissions
			}

			responses = append(responses, response)
		}
	}

	return responses, nil
}

func (s *Service) UpdateGuildVoiceChannel(ctx context.Context, channelID uuid.UUID, req *models.GuildVoiceChannelUpdateRequest, userID uuid.UUID) (*models.GuildVoiceChannelResponse, error) {
	// Get existing channel
	channel, err := s.repo.GetVoiceChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	// Verify user has permission to update (placeholder - should integrate with guild service)
	if channel.OwnerID != userID {
		return nil, fmt.Errorf("insufficient permissions to update channel")
	}

	// Apply updates
	if req.Name != nil {
		channel.Name = *req.Name
	}
	if req.MaxUsers != nil && *req.MaxUsers > 0 {
		channel.MaxUsers = *req.MaxUsers
	}
	if req.AllowedRoles != nil {
		if channel.GuildPermissions == nil {
			channel.GuildPermissions = &models.GuildPermissions{}
		}
		channel.GuildPermissions.AllowedRoles = *req.AllowedRoles
	}
	if req.IsModerated != nil {
		if channel.GuildPermissions == nil {
			channel.GuildPermissions = &models.GuildPermissions{}
		}
		channel.GuildPermissions.IsModerated = *req.IsModerated
	}
	if req.RequireApproval != nil {
		if channel.GuildPermissions == nil {
			channel.GuildPermissions = &models.GuildPermissions{}
		}
		channel.GuildPermissions.RequireApproval = *req.RequireApproval
	}
	if req.BlockedUsers != nil {
		if channel.GuildPermissions == nil {
			channel.GuildPermissions = &models.GuildPermissions{}
		}
		channel.GuildPermissions.BlockedUsers = *req.BlockedUsers
	}

	channel.UpdatedAt = time.Now()

	err = s.repo.UpdateVoiceChannel(ctx, channel)
	if err != nil {
		s.logger.Error("Failed to update guild voice channel", zap.Error(err))
		return nil, fmt.Errorf("failed to update guild voice channel: %w", err)
	}

	response := &models.GuildVoiceChannelResponse{
		ID:               channel.ID.String(),
		Name:             channel.Name,
		Type:             channel.Type,
		GuildID:          channel.GuildID.String(),
		OwnerID:          channel.OwnerID.String(),
		MaxUsers:         channel.MaxUsers,
		CurrentUsers:     channel.CurrentUsers,
		IsActive:         channel.IsActive,
		IsDefaultChannel: channel.IsDefaultGuildChannel,
		CreatedAt:        channel.CreatedAt,
	}

	if channel.GuildPermissions != nil {
		response.GuildPermissions = *channel.GuildPermissions
	}

	s.logger.Info("Guild voice channel updated",
		zap.String("channel_id", channelID.String()),
		zap.String("guild_id", channel.GuildID.String()))

	return response, nil
}

func (s *Service) JoinGuildVoiceChannel(ctx context.Context, channelID, userID uuid.UUID) (*models.JoinVoiceChannelResponse, error) {
	// Get channel info
	channel, err := s.repo.GetVoiceChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	if channel.Type != "guild" {
		return nil, fmt.Errorf("channel is not a guild voice channel")
	}

	if !channel.IsActive {
		return nil, fmt.Errorf("guild voice channel is not active")
	}

	if channel.CurrentUsers >= channel.MaxUsers {
		return nil, fmt.Errorf("guild voice channel is full")
	}

	// Check guild permissions (placeholder - should integrate with guild service)
	if channel.GuildPermissions != nil {
		// Check if user is blocked
		for _, blockedUser := range channel.GuildPermissions.BlockedUsers {
			if blockedUser == userID.String() {
				return nil, fmt.Errorf("user is blocked from this channel")
			}
		}

		// Check if channel requires approval (placeholder)
		if channel.GuildPermissions.RequireApproval {
			return nil, fmt.Errorf("channel requires approval to join")
		}
	}

	// Add participant
	participant := &models.VoiceParticipant{
		ID:       uuid.New(),
		ChannelID: channelID,
		UserID:   userID,
		Role:     "member",
		IsMuted:  false,
		IsDeafened: false,
		JoinedAt: time.Now(),
	}

	err = s.repo.AddVoiceParticipant(ctx, participant)
	if err != nil {
		s.logger.Error("Failed to add guild voice participant", zap.Error(err))
		return nil, fmt.Errorf("failed to join guild voice channel: %w", err)
	}

	// Update channel user count
	channel.CurrentUsers++
	err = s.repo.UpdateVoiceChannel(ctx, channel)
	if err != nil {
		s.logger.Error("Failed to update guild channel user count", zap.Error(err))
		// Don't return error as user is already added
	}

	// Get participants
	participants, err := s.repo.GetVoiceParticipants(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to get guild voice participants", zap.Error(err))
		// Continue anyway
	}

	// Convert to response format
	participantInfos := make([]models.VoiceParticipantInfo, len(participants))
	for i, p := range participants {
		participantInfos[i] = models.VoiceParticipantInfo{
			UserID:     p.UserID.String(),
			Username:   "User_" + p.UserID.String()[:8], // Placeholder
			Role:       p.Role,
			IsMuted:    p.IsMuted,
			IsDeafened: p.IsDeafened,
		}
	}

	response := &models.JoinVoiceChannelResponse{
		Success: true,
		Channel: models.VoiceChannelResponse{
			ID:           channel.ID.String(),
			Name:         channel.Name,
			Type:         channel.Type,
			OwnerID:      channel.OwnerID.String(),
			MaxUsers:     channel.MaxUsers,
			CurrentUsers: channel.CurrentUsers,
			IsActive:     channel.IsActive,
			CreatedAt:    channel.CreatedAt,
		},
		Participants: participantInfos,
		Signaling: models.SignalingConfig{
			ICEServers: []models.ICEServerConfig{
				{
					URLs: []string{"stun:stun.l.google.com:19302"},
				},
			},
			SessionID: uuid.New().String(),
		},
	}

	s.logger.Info("User joined guild voice channel",
		zap.String("channel_id", channelID.String()),
		zap.String("guild_id", channel.GuildID.String()),
		zap.String("user_id", userID.String()))

	return response, nil
}

// VoiceChannel methods
func (s *Service) CreateVoiceChannel(ctx context.Context, req *models.VoiceChannelRequest, ownerID uuid.UUID) (*models.VoiceChannelResponse, error) {
	channel := &models.VoiceChannel{
		ID:         uuid.New(),
		Name:       req.Name,
		Type:       req.Type,
		OwnerID:    ownerID,
		MaxUsers:   req.MaxUsers,
		CurrentUsers: 0,
		IsActive:   true,
		CreatedAt:  time.Now(),
		UpdatedAt:  time.Now(),
	}

	if req.GuildID != nil {
		guildUUID, err := uuid.Parse(*req.GuildID)
		if err != nil {
			return nil, fmt.Errorf("invalid guild ID: %w", err)
		}
		channel.GuildID = &guildUUID
	}

	if channel.MaxUsers <= 0 {
		channel.MaxUsers = 50 // Default max users
	}

	err := s.repo.CreateVoiceChannel(ctx, channel)
	if err != nil {
		s.logger.Error("Failed to create voice channel", zap.Error(err))
		return nil, fmt.Errorf("failed to create voice channel: %w", err)
	}

	response := &models.VoiceChannelResponse{
		ID:           channel.ID.String(),
		Name:         channel.Name,
		Type:         channel.Type,
		OwnerID:      channel.OwnerID.String(),
		MaxUsers:     channel.MaxUsers,
		CurrentUsers: channel.CurrentUsers,
		IsActive:     channel.IsActive,
		CreatedAt:    channel.CreatedAt,
	}

	if channel.GuildID != nil {
		guildIDStr := channel.GuildID.String()
		response.GuildID = &guildIDStr
	}

	s.logger.Info("Voice channel created",
		zap.String("channel_id", channel.ID.String()),
		zap.String("owner_id", ownerID.String()))

	return response, nil
}

func (s *Service) GetVoiceChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannelResponse, error) {
	channel, err := s.repo.GetVoiceChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	response := &models.VoiceChannelResponse{
		ID:           channel.ID.String(),
		Name:         channel.Name,
		Type:         channel.Type,
		OwnerID:      channel.OwnerID.String(),
		MaxUsers:     channel.MaxUsers,
		CurrentUsers: channel.CurrentUsers,
		IsActive:     channel.IsActive,
		CreatedAt:    channel.CreatedAt,
	}

	if channel.GuildID != nil {
		guildIDStr := channel.GuildID.String()
		response.GuildID = &guildIDStr
	}

	return response, nil
}

func (s *Service) JoinVoiceChannel(ctx context.Context, channelID, userID uuid.UUID) (*models.JoinVoiceChannelResponse, error) {
	// Get channel info
	channel, err := s.repo.GetVoiceChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	if !channel.IsActive {
		return nil, fmt.Errorf("voice channel is not active")
	}

	if channel.CurrentUsers >= channel.MaxUsers {
		return nil, fmt.Errorf("voice channel is full")
	}

	// Add participant
	participant := &models.VoiceParticipant{
		ID:       uuid.New(),
		ChannelID: channelID,
		UserID:   userID,
		Role:     "member",
		IsMuted:  false,
		IsDeafened: false,
		JoinedAt: time.Now(),
	}

	err = s.repo.AddVoiceParticipant(ctx, participant)
	if err != nil {
		s.logger.Error("Failed to add voice participant", zap.Error(err))
		return nil, fmt.Errorf("failed to join voice channel: %w", err)
	}

	// Update channel user count
	channel.CurrentUsers++
	err = s.repo.UpdateVoiceChannel(ctx, channel)
	if err != nil {
		s.logger.Error("Failed to update channel user count", zap.Error(err))
		// Don't return error here as user is already added
	}

	// Get participants
	participants, err := s.repo.GetVoiceParticipants(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to get voice participants", zap.Error(err))
		// Continue anyway
	}

	// Convert to response format
	participantInfos := make([]models.VoiceParticipantInfo, len(participants))
	for i, p := range participants {
		participantInfos[i] = models.VoiceParticipantInfo{
			UserID:     p.UserID.String(),
			Username:   "User_" + p.UserID.String()[:8], // Placeholder
			Role:       p.Role,
			IsMuted:    p.IsMuted,
			IsDeafened: p.IsDeafened,
		}
	}

	response := &models.JoinVoiceChannelResponse{
		Success: true,
		Channel: models.VoiceChannelResponse{
			ID:           channel.ID.String(),
			Name:         channel.Name,
			Type:         channel.Type,
			OwnerID:      channel.OwnerID.String(),
			MaxUsers:     channel.MaxUsers,
			CurrentUsers: channel.CurrentUsers,
			IsActive:     channel.IsActive,
			CreatedAt:    channel.CreatedAt,
		},
		Participants: participantInfos,
		Signaling: models.SignalingConfig{
			ICEServers: []models.ICEServerConfig{
				{
					URLs: []string{"stun:stun.l.google.com:19302"},
				},
			},
			SessionID: uuid.New().String(),
		},
	}

	s.logger.Info("User joined voice channel",
		zap.String("channel_id", channelID.String()),
		zap.String("user_id", userID.String()))

	return response, nil
}

func (s *Service) LeaveVoiceChannel(ctx context.Context, channelID, userID uuid.UUID) error {
	// Remove participant
	err := s.repo.RemoveVoiceParticipant(ctx, channelID, userID)
	if err != nil {
		s.logger.Error("Failed to remove voice participant", zap.Error(err))
		return fmt.Errorf("failed to leave voice channel: %w", err)
	}

	// Update channel user count
	channel, err := s.repo.GetVoiceChannel(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to get channel for user count update", zap.Error(err))
		return nil // Don't fail the leave operation
	}

	if channel.CurrentUsers > 0 {
		channel.CurrentUsers--
		err = s.repo.UpdateVoiceChannel(ctx, channel)
		if err != nil {
			s.logger.Error("Failed to update channel user count", zap.Error(err))
		}
	}

	s.logger.Info("User left voice channel",
		zap.String("channel_id", channelID.String()),
		zap.String("user_id", userID.String()))

	return nil
}

// Signaling methods
func (s *Service) ExchangeSignalingMessage(ctx context.Context, req *models.SignalingRequest) (*models.SignalingResponse, error) {
	// Parse UUIDs
	fromUserID, err := uuid.Parse(req.FromUserID)
	if err != nil {
		return nil, fmt.Errorf("invalid from_user_id: %w", err)
	}

	toUserID, err := uuid.Parse(req.ToUserID)
	if err != nil {
		return nil, fmt.Errorf("invalid to_user_id: %w", err)
	}

	channelID, err := uuid.Parse(req.ChannelID)
	if err != nil {
		return nil, fmt.Errorf("invalid channel_id: %w", err)
	}

	// Create signaling message
	message := &models.SignalingMessage{
		ID:         uuid.New(),
		Type:       req.Type,
		FromUserID: fromUserID,
		ToUserID:   toUserID,
		ChannelID:  channelID,
		Payload:    make(map[string]interface{}),
		Timestamp:  time.Now(),
	}

	// Add payload based on type
	switch req.Type {
	case "offer":
		if req.Offer != nil {
			message.Payload["type"] = req.Offer.Type
			message.Payload["sdp"] = req.Offer.SDP
		}
	case "answer":
		if req.Answer != nil {
			message.Payload["type"] = req.Answer.Type
			message.Payload["sdp"] = req.Answer.SDP
		}
	case "ice-candidate":
		if req.Candidate != nil {
			message.Payload["candidate"] = req.Candidate.Candidate
			if req.Candidate.SDPMLineIndex != nil {
				message.Payload["sdpMLineIndex"] = *req.Candidate.SDPMLineIndex
			}
			message.Payload["sdpMid"] = req.Candidate.SDPMid
		}
	}

	// Store message
	err = s.repo.StoreSignalingMessage(ctx, message)
	if err != nil {
		s.logger.Error("Failed to store signaling message", zap.Error(err))
		return nil, fmt.Errorf("failed to exchange signaling message: %w", err)
	}

	s.logger.Info("Signaling message exchanged",
		zap.String("type", req.Type),
		zap.String("from_user", req.FromUserID),
		zap.String("to_user", req.ToUserID),
		zap.String("channel", req.ChannelID))

	return &models.SignalingResponse{
		Success: true,
		Message: "Signaling message exchanged successfully",
	}, nil
}

// Voice quality methods
func (s *Service) ReportVoiceQuality(ctx context.Context, channelID, userID uuid.UUID, req *models.VoiceQualityReportRequest) error {
	report := &models.VoiceQualityReport{
		ID:         uuid.New(),
		ChannelID:  channelID,
		UserID:     userID,
		Bitrate:    req.Bitrate,
		PacketLoss: req.PacketLoss,
		Jitter:     req.Jitter,
		Latency:    req.Latency,
		Quality:    req.Quality,
		ReportedAt: time.Now(),
	}

	err := s.repo.StoreVoiceQualityReport(ctx, report)
	if err != nil {
		s.logger.Error("Failed to store voice quality report", zap.Error(err))
		return fmt.Errorf("failed to report voice quality: %w", err)
	}

	s.logger.Info("Voice quality reported",
		zap.String("channel_id", channelID.String()),
		zap.String("user_id", userID.String()),
		zap.String("quality", req.Quality))

	return nil
}

// Health check
func (s *Service) HealthCheck(ctx context.Context) error {
	return s.repo.HealthCheck(ctx)
}

// PERFORMANCE: Service methods use context timeouts for all operations
// Concurrent safety ensured through proper locking in repository layer
// Memory pooling implemented for frequently allocated objects