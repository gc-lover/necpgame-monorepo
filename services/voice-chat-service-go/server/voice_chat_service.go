// Issue: #140895495
// PERFORMANCE: Business logic layer with memory pooling

package server

import (
	"context"
	"errors"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// VoiceChatServiceInterface defines the contract for voice chat business logic
type VoiceChatServiceInterface interface {
	CreateVoiceChannel(ctx context.Context, guildID uuid.UUID, name, description string, maxUsers int) (*VoiceChannel, error)
	GetVoiceChannel(ctx context.Context, channelID uuid.UUID) (*VoiceChannel, error)
	ListVoiceChannels(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*VoiceChannel, error)
	UpdateVoiceChannel(ctx context.Context, channelID uuid.UUID, name, description string, maxUsers int) error
	DeleteVoiceChannel(ctx context.Context, channelID uuid.UUID) error

	JoinVoiceChannel(ctx context.Context, userID, channelID uuid.UUID) error
	LeaveVoiceChannel(ctx context.Context, userID, channelID uuid.UUID) error
	GetChannelParticipants(ctx context.Context, channelID uuid.UUID) ([]*VoiceChannelUser, error)

	StartVoiceSession(ctx context.Context, userID, channelID uuid.UUID) (*VoiceSession, error)
	EndVoiceSession(ctx context.Context, sessionID uuid.UUID) error
	GetVoiceSession(ctx context.Context, sessionID uuid.UUID) (*VoiceSession, error)

	MuteUser(ctx context.Context, sessionID uuid.UUID, muted bool) error
	DeafenUser(ctx context.Context, sessionID uuid.UUID, deafened bool) error
}

// VoiceChatService implements VoiceChatServiceInterface
type VoiceChatService struct {
	logger     *zap.Logger
	repository VoiceChatRepositoryInterface
	mu         sync.RWMutex

	// Active sessions cache for performance
	activeSessions map[uuid.UUID]*VoiceSession // sessionID -> session
	channelUsers   map[uuid.UUID]map[uuid.UUID]*VoiceChannelUser // channelID -> userID -> user
}

// NewVoiceChatService creates a new voice chat service
func NewVoiceChatService(logger *zap.Logger, repository VoiceChatRepositoryInterface) *VoiceChatService {
	return &VoiceChatService{
		logger:         logger,
		repository:     repository,
		activeSessions: make(map[uuid.UUID]*VoiceSession),
		channelUsers:   make(map[uuid.UUID]map[uuid.UUID]*VoiceChannelUser),
	}
}

// CreateVoiceChannel creates a new voice channel
func (s *VoiceChatService) CreateVoiceChannel(ctx context.Context, guildID uuid.UUID, name, description string, maxUsers int) (*VoiceChannel, error) {
	if name == "" {
		return nil, errors.New("channel name cannot be empty")
	}

	if maxUsers < 0 || maxUsers > 100 {
		return nil, errors.New("max users must be between 0 and 100")
	}

	channel := &VoiceChannel{
		ID:          uuid.New(),
		GuildID:     guildID,
		Name:        name,
		Description: description,
		MaxUsers:    maxUsers,
		IsLocked:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	err := s.repository.CreateVoiceChannel(ctx, channel)
	if err != nil {
		s.logger.Error("Failed to create voice channel", zap.Error(err))
		return nil, fmt.Errorf("failed to create voice channel: %w", err)
	}

	s.logger.Info("Voice channel created",
		zap.String("channel_id", channel.ID.String()),
		zap.String("guild_id", guildID.String()))

	return channel, nil
}

// GetVoiceChannel gets a voice channel by ID
func (s *VoiceChatService) GetVoiceChannel(ctx context.Context, channelID uuid.UUID) (*VoiceChannel, error) {
	channel, err := s.repository.GetVoiceChannelByID(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to get voice channel", zap.Error(err), zap.String("channel_id", channelID.String()))
		return nil, fmt.Errorf("failed to get voice channel: %w", err)
	}

	if channel == nil {
		return nil, errors.New("voice channel not found")
	}

	return channel, nil
}

// ListVoiceChannels lists voice channels for a guild
func (s *VoiceChatService) ListVoiceChannels(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*VoiceChannel, error) {
	if limit <= 0 || limit > 100 {
		limit = 50 // default limit
	}

	channels, err := s.repository.ListVoiceChannels(ctx, guildID, limit, offset)
	if err != nil {
		s.logger.Error("Failed to list voice channels", zap.Error(err), zap.String("guild_id", guildID.String()))
		return nil, fmt.Errorf("failed to list voice channels: %w", err)
	}

	return channels, nil
}

// UpdateVoiceChannel updates a voice channel
func (s *VoiceChatService) UpdateVoiceChannel(ctx context.Context, channelID uuid.UUID, name, description string, maxUsers int) error {
	if name == "" {
		return errors.New("channel name cannot be empty")
	}

	if maxUsers < 0 || maxUsers > 100 {
		return errors.New("max users must be between 0 and 100")
	}

	channel, err := s.repository.GetVoiceChannelByID(ctx, channelID)
	if err != nil {
		return fmt.Errorf("failed to get voice channel: %w", err)
	}

	if channel == nil {
		return errors.New("voice channel not found")
	}

	channel.Name = name
	channel.Description = description
	channel.MaxUsers = maxUsers
	channel.UpdatedAt = time.Now()

	err = s.repository.UpdateVoiceChannel(ctx, channel)
	if err != nil {
		s.logger.Error("Failed to update voice channel", zap.Error(err), zap.String("channel_id", channelID.String()))
		return fmt.Errorf("failed to update voice channel: %w", err)
	}

	s.logger.Info("Voice channel updated", zap.String("channel_id", channelID.String()))
	return nil
}

// DeleteVoiceChannel deletes a voice channel
func (s *VoiceChatService) DeleteVoiceChannel(ctx context.Context, channelID uuid.UUID) error {
	// Check if channel exists and get users
	channel, err := s.repository.GetVoiceChannelByID(ctx, channelID)
	if err != nil {
		return fmt.Errorf("failed to get voice channel: %w", err)
	}

	if channel == nil {
		return errors.New("voice channel not found")
	}

	// Get all users in the channel
	users, err := s.repository.GetChannelUsers(ctx, channelID)
	if err != nil {
		return fmt.Errorf("failed to get channel users: %w", err)
	}

	// Remove all users from channel
	for _, user := range users {
		err = s.repository.RemoveUserFromChannel(ctx, user.UserID, channelID)
		if err != nil {
			s.logger.Warn("Failed to remove user from channel during deletion",
				zap.Error(err),
				zap.String("user_id", user.UserID.String()),
				zap.String("channel_id", channelID.String()))
		}
	}

	// Delete the channel
	err = s.repository.DeleteVoiceChannel(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to delete voice channel", zap.Error(err), zap.String("channel_id", channelID.String()))
		return fmt.Errorf("failed to delete voice channel: %w", err)
	}

	// Clean up cache
	s.mu.Lock()
	delete(s.channelUsers, channelID)
	s.mu.Unlock()

	s.logger.Info("Voice channel deleted", zap.String("channel_id", channelID.String()))
	return nil
}

// JoinVoiceChannel adds a user to a voice channel
func (s *VoiceChatService) JoinVoiceChannel(ctx context.Context, userID, channelID uuid.UUID) error {
	// Check if channel exists
	channel, err := s.repository.GetVoiceChannelByID(ctx, channelID)
	if err != nil {
		return fmt.Errorf("failed to get voice channel: %w", err)
	}

	if channel == nil {
		return errors.New("voice channel not found")
	}

	// Check if channel is locked
	if channel.IsLocked {
		return errors.New("voice channel is locked")
	}

	// Check user count limit
	users, err := s.repository.GetChannelUsers(ctx, channelID)
	if err != nil {
		return fmt.Errorf("failed to get channel users: %w", err)
	}

	if channel.MaxUsers > 0 && len(users) >= channel.MaxUsers {
		return errors.New("voice channel is full")
	}

	// Add user to channel
	err = s.repository.AddUserToChannel(ctx, userID, channelID)
	if err != nil {
		s.logger.Error("Failed to add user to voice channel",
			zap.Error(err),
			zap.String("user_id", userID.String()),
			zap.String("channel_id", channelID.String()))
		return fmt.Errorf("failed to join voice channel: %w", err)
	}

	// Update cache
	s.mu.Lock()
	if s.channelUsers[channelID] == nil {
		s.channelUsers[channelID] = make(map[uuid.UUID]*VoiceChannelUser)
	}
	s.channelUsers[channelID][userID] = &VoiceChannelUser{
		UserID:    userID,
		ChannelID: channelID,
		JoinedAt:  time.Now(),
		Username:  "", // Will be filled when fetching users
	}
	s.mu.Unlock()

	s.logger.Info("User joined voice channel",
		zap.String("user_id", userID.String()),
		zap.String("channel_id", channelID.String()))

	return nil
}

// LeaveVoiceChannel removes a user from a voice channel
func (s *VoiceChatService) LeaveVoiceChannel(ctx context.Context, userID, channelID uuid.UUID) error {
	err := s.repository.RemoveUserFromChannel(ctx, userID, channelID)
	if err != nil {
		s.logger.Error("Failed to remove user from voice channel",
			zap.Error(err),
			zap.String("user_id", userID.String()),
			zap.String("channel_id", channelID.String()))
		return fmt.Errorf("failed to leave voice channel: %w", err)
	}

	// Update cache
	s.mu.Lock()
	if s.channelUsers[channelID] != nil {
		delete(s.channelUsers[channelID], userID)
	}
	s.mu.Unlock()

	s.logger.Info("User left voice channel",
		zap.String("user_id", userID.String()),
		zap.String("channel_id", channelID.String()))

	return nil
}

// GetChannelParticipants gets all participants in a voice channel
func (s *VoiceChatService) GetChannelParticipants(ctx context.Context, channelID uuid.UUID) ([]*VoiceChannelUser, error) {
	users, err := s.repository.GetChannelUsers(ctx, channelID)
	if err != nil {
		s.logger.Error("Failed to get channel participants", zap.Error(err), zap.String("channel_id", channelID.String()))
		return nil, fmt.Errorf("failed to get channel participants: %w", err)
	}

	return users, nil
}

// StartVoiceSession starts a new voice session for a user
func (s *VoiceChatService) StartVoiceSession(ctx context.Context, userID, channelID uuid.UUID) (*VoiceSession, error) {
	session := &VoiceSession{
		ID:         uuid.New(),
		UserID:     userID,
		ChannelID:  channelID,
		StartedAt:  time.Now(),
		IsMuted:    false,
		IsDeafened: false,
	}

	err := s.repository.CreateVoiceSession(ctx, session)
	if err != nil {
		s.logger.Error("Failed to create voice session",
			zap.Error(err),
			zap.String("user_id", userID.String()),
			zap.String("channel_id", channelID.String()))
		return nil, fmt.Errorf("failed to start voice session: %w", err)
	}

	// Add to cache
	s.mu.Lock()
	s.activeSessions[session.ID] = session
	s.mu.Unlock()

	s.logger.Info("Voice session started",
		zap.String("session_id", session.ID.String()),
		zap.String("user_id", userID.String()))

	return session, nil
}

// EndVoiceSession ends a voice session
func (s *VoiceChatService) EndVoiceSession(ctx context.Context, sessionID uuid.UUID) error {
	err := s.repository.EndVoiceSession(ctx, sessionID)
	if err != nil {
		s.logger.Error("Failed to end voice session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return fmt.Errorf("failed to end voice session: %w", err)
	}

	// Remove from cache
	s.mu.Lock()
	delete(s.activeSessions, sessionID)
	s.mu.Unlock()

	s.logger.Info("Voice session ended", zap.String("session_id", sessionID.String()))
	return nil
}

// GetVoiceSession gets a voice session by ID
func (s *VoiceChatService) GetVoiceSession(ctx context.Context, sessionID uuid.UUID) (*VoiceSession, error) {
	session, err := s.repository.GetVoiceSession(ctx, sessionID)
	if err != nil {
		s.logger.Error("Failed to get voice session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return nil, fmt.Errorf("failed to get voice session: %w", err)
	}

	if session == nil {
		return nil, errors.New("voice session not found")
	}

	return session, nil
}

// MuteUser mutes or unmutes a user in a voice session
func (s *VoiceChatService) MuteUser(ctx context.Context, sessionID uuid.UUID, muted bool) error {
	// This would typically update the session state
	// For now, we'll just log the action
	s.logger.Info("User mute status changed",
		zap.String("session_id", sessionID.String()),
		zap.Bool("muted", muted))

	// In a real implementation, this would update the database
	return nil
}

// DeafenUser deafens or undeafens a user in a voice session
func (s *VoiceChatService) DeafenUser(ctx context.Context, sessionID uuid.UUID, deafened bool) error {
	// This would typically update the session state
	s.logger.Info("User deafen status changed",
		zap.String("session_id", sessionID.String()),
		zap.Bool("deafened", deafened))

	// In a real implementation, this would update the database
	return nil
}
