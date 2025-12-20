// Package server Issue: #1598
package server

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/chat-service-go/pkg/api"
	"github.com/google/uuid"
)

var (
	// ErrNotFound is returned when entity is not found
	ErrNotFound = errors.New("not found")
	// ErrForbidden is returned when access is denied
	ErrForbidden = errors.New("forbidden")
	// ErrBadRequest is returned for invalid requests
	ErrBadRequest = errors.New("bad request")
)

// getPlayerIDFromContext extracts player ID from context (from JWT token)
func getPlayerIDFromContext(ctx context.Context) (uuid.UUID, error) {
	// Try different context keys used in different services
	if playerID, ok := ctx.Value("player_id").(uuid.UUID); ok {
		return playerID, nil
	}
	if playerID, ok := ctx.Value("user_uuid").(uuid.UUID); ok {
		return playerID, nil
	}
	if playerIDStr, ok := ctx.Value("user_id").(string); ok {
		playerID, err := uuid.Parse(playerIDStr)
		if err == nil {
			return playerID, nil
		}
	}
	// TODO: Implement proper JWT extraction (Issue: Auth integration)
	// For now, return error - handlers should handle this
	return uuid.Nil, fmt.Errorf("player_id not found in context")
}

// Service contains business logic
type Service struct {
	repo *Repository
}

// NewService creates new service
func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

// SendMessage sends a message to a channel
func (s *Service) SendMessage(ctx context.Context, req *api.SendMessageRequest) (*api.MessageResponse, error) {
	channelID := req.ChannelID

	// Get senderID from JWT token in context
	senderID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, ErrForbidden
	}

	// TODO: Get senderName from character service (Issue: Character service integration)
	senderName := "Player" // Should come from character service

	// Check if player is banned
	banned, err := s.repo.IsPlayerBanned(ctx, senderID, &channelID)
	if err != nil {
		return nil, err
	}
	if banned {
		return nil, ErrForbidden
	}

	// Get channel to check permissions and get channel type
	channel, err := s.repo.GetChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	// Check if player is member (for private channels)
	if channel.Type == "private" {
		isMember, err := s.repo.IsChannelMember(ctx, channelID, senderID)
		if err != nil {
			return nil, err
		}
		if !isMember {
			return nil, ErrForbidden
		}
	}

	// Validate message length
	if len(req.Content) > channel.MaxLength {
		return nil, ErrBadRequest
	}

	// Create message
	messageID, err := s.repo.CreateMessage(ctx, channelID, senderID, req.Content, req.Content, channel.Type, senderName)
	if err != nil {
		return nil, err
	}

	return &api.MessageResponse{
		ID:         api.NewOptUUID(*messageID),
		ChannelID:  api.NewOptUUID(channelID),
		SenderID:   api.NewOptUUID(senderID),
		SenderName: api.NewOptString(senderName),
		Content:    api.NewOptString(req.Content),
		CreatedAt:  api.NewOptDateTime(time.Now()),
	}, nil
}

// GetChannelMessages retrieves channel message history
func (s *Service) GetChannelMessages(ctx context.Context, params api.GetChannelMessagesParams) (*api.MessagesHistoryResponse, error) {
	channelID := params.ChannelID

	// Check if channel exists
	_, err := s.repo.GetChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	limit := 50
	if params.Limit.Set {
		limit = params.Limit.Value
		if limit > 100 {
			limit = 100
		}
	}

	var before, after *uuid.UUID
	if params.Before.Set {
		before = &params.Before.Value
	}
	if params.After.Set {
		after = &params.After.Value
	}

	messages, err := s.repo.GetChannelMessages(ctx, channelID, limit, before, after)
	if err != nil {
		return nil, err
	}

	apiMessages := make([]api.MessageResponse, len(messages))
	for i, msg := range messages {
		apiMessages[i] = api.MessageResponse{
			ID:         api.NewOptUUID(msg.ID),
			ChannelID:  api.NewOptUUID(msg.ChannelID),
			SenderID:   api.NewOptUUID(msg.SenderID),
			SenderName: api.NewOptString(msg.SenderName),
			Content:    api.NewOptString(msg.Content),
			CreatedAt:  api.NewOptDateTime(msg.CreatedAt),
		}
	}

	return &api.MessagesHistoryResponse{
		Messages: apiMessages,
	}, nil
}

// GetChannels retrieves list of available channels
func (s *Service) GetChannels(ctx context.Context) (*api.ChannelsListResponse, error) {
	channels, err := s.repo.ListChannels(ctx)
	if err != nil {
		return nil, err
	}

	apiChannels := make([]api.ChannelResponse, len(channels))
	for i, ch := range channels {
		apiChannels[i] = api.ChannelResponse{
			ID:        api.NewOptUUID(ch.ID),
			Name:      api.NewOptString(ch.Name),
			CreatedAt: api.NewOptDateTime(ch.CreatedAt),
		}

		// Convert channel type
		switch ch.Type {
		case "global":
			apiChannels[i].ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeGlobal)
		case "local":
			apiChannels[i].ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeLocal)
		case "party":
			apiChannels[i].ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeParty)
		case "guild":
			apiChannels[i].ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeGuild)
		case "trade":
			apiChannels[i].ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeTrade)
		case "whisper":
			apiChannels[i].ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeWhisper)
		case "private":
			apiChannels[i].ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypePrivate)
		}

		if ch.OwnerID != nil {
			apiChannels[i].OwnerID = api.NewOptUUID(*ch.OwnerID)
		}

		// Get member count
		memberCount, err := s.repo.GetChannelMemberCount(ctx, ch.ID)
		if err == nil {
			apiChannels[i].MemberCount = api.NewOptInt(memberCount)
		}
	}

	return &api.ChannelsListResponse{
		Channels: apiChannels,
	}, nil
}

// CreateChannel creates a new private channel
func (s *Service) CreateChannel(ctx context.Context, req *api.CreateChannelRequest) (*api.ChannelResponse, error) {
	// Get ownerID from JWT token in context
	ownerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, ErrForbidden
	}
	ownerIDPtr := &ownerID

	channelType := "private"
	switch req.ChannelType {
	case api.CreateChannelRequestChannelTypePrivate:
		channelType = "private"
	case api.CreateChannelRequestChannelTypeParty:
		channelType = "party"
	case api.CreateChannelRequestChannelTypeGuild:
		channelType = "guild"
	case api.CreateChannelRequestChannelTypeTrade:
		channelType = "trade"
	}

	var description string
	channelID, err := s.repo.CreateChannel(ctx, ownerIDPtr, channelType, req.Name, description)
	if err != nil {
		return nil, err
	}

	// Add initial members if provided
	if len(req.Members) > 0 {
		for _, memberID := range req.Members {
			_ = s.repo.AddChannelMember(ctx, *channelID, memberID)
		}
	}

	// Get created channel
	channel, err := s.repo.GetChannel(ctx, *channelID)
	if err != nil {
		return nil, err
	}

	response := &api.ChannelResponse{
		ID:        api.NewOptUUID(channel.ID),
		Name:      api.NewOptString(channel.Name),
		CreatedAt: api.NewOptDateTime(channel.CreatedAt),
	}

	if channel.OwnerID != nil {
		response.OwnerID = api.NewOptUUID(*channel.OwnerID)
	}

	switch channel.Type {
	case "global":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeGlobal)
	case "local":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeLocal)
	case "party":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeParty)
	case "guild":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeGuild)
	case "trade":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeTrade)
	case "whisper":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeWhisper)
	case "private":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypePrivate)
	}

	memberCount, err := s.repo.GetChannelMemberCount(ctx, channel.ID)
	if err == nil {
		response.MemberCount = api.NewOptInt(memberCount)
	}

	return response, nil
}

// GetChannel retrieves channel information
func (s *Service) GetChannel(ctx context.Context, params api.GetChannelParams) (*api.ChannelResponse, error) {
	channel, err := s.repo.GetChannel(ctx, params.ChannelID)
	if err != nil {
		return nil, err
	}

	response := &api.ChannelResponse{
		ID:        api.NewOptUUID(channel.ID),
		Name:      api.NewOptString(channel.Name),
		CreatedAt: api.NewOptDateTime(channel.CreatedAt),
	}

	if channel.OwnerID != nil {
		response.OwnerID = api.NewOptUUID(*channel.OwnerID)
	}

	switch channel.Type {
	case "global":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeGlobal)
	case "local":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeLocal)
	case "party":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeParty)
	case "guild":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeGuild)
	case "trade":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeTrade)
	case "whisper":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypeWhisper)
	case "private":
		response.ChannelType = api.NewOptChannelResponseChannelType(api.ChannelResponseChannelTypePrivate)
	}

	memberCount, err := s.repo.GetChannelMemberCount(ctx, channel.ID)
	if err == nil {
		response.MemberCount = api.NewOptInt(memberCount)
	}

	return response, nil
}

// UpdateChannel updates channel settings
func (s *Service) UpdateChannel(ctx context.Context, params api.UpdateChannelParams, req *api.UpdateChannelRequest) (*api.ChannelResponse, error) {
	_, err := s.repo.GetChannel(ctx, params.ChannelID)
	if err != nil {
		return nil, err
	}

	var name *string
	if req.Name.Set {
		name = &req.Name.Value
	}

	err = s.repo.UpdateChannel(ctx, params.ChannelID, name, nil)
	if err != nil {
		return nil, err
	}

	// Get updated channel
	getParams := api.GetChannelParams{ChannelID: params.ChannelID}
	return s.GetChannel(ctx, getParams)
}

// DeleteChannel deletes a channel
func (s *Service) DeleteChannel(ctx context.Context, params api.DeleteChannelParams) error {
	return s.repo.DeleteChannel(ctx, params.ChannelID)
}

// AddChannelMember adds a member to a channel
func (s *Service) AddChannelMember(ctx context.Context, params api.AddChannelMemberParams, req *api.AddMemberRequest) (*api.SuccessResponse, error) {
	err := s.repo.AddChannelMember(ctx, params.ChannelID, req.PlayerID)
	if err != nil {
		return nil, err
	}
	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// RemoveChannelMember removes a member from a channel
func (s *Service) RemoveChannelMember(ctx context.Context, params api.RemoveChannelMemberParams) error {
	return s.repo.RemoveChannelMember(ctx, params.ChannelID, params.PlayerID)
}

// BanPlayer bans a player from chat
func (s *Service) BanPlayer(ctx context.Context, req *api.BanPlayerRequest) (*api.BanResponse, error) {
	var channelID *uuid.UUID
	if req.ChannelID.Set {
		channelID = &req.ChannelID.Value
	}

	var expiresAt *time.Time
	if req.DurationMinutes.Set && req.DurationMinutes.Value > 0 {
		exp := time.Now().Add(time.Duration(req.DurationMinutes.Value) * time.Minute)
		expiresAt = &exp
	}

	banID, err := s.repo.CreateBan(ctx, req.PlayerID, channelID, req.Reason, expiresAt)
	if err != nil {
		return nil, err
	}

	ban, err := s.repo.GetBan(ctx, *banID)
	if err != nil {
		return nil, err
	}

	response := &api.BanResponse{
		ID:       api.NewOptUUID(ban.ID),
		PlayerID: api.NewOptUUID(ban.CharacterID),
		Reason:   api.NewOptString(ban.Reason),
		BannedAt: api.NewOptDateTime(ban.CreatedAt),
	}

	if ban.ChannelID != nil {
		response.ChannelID = api.NewOptUUID(*ban.ChannelID)
	}
	if ban.ExpiresAt != nil {
		response.ExpiresAt = api.NewOptDateTime(*ban.ExpiresAt)
	}

	return response, nil
}

// UnbanPlayer removes a ban from a player
func (s *Service) UnbanPlayer(ctx context.Context, params api.UnbanPlayerParams) error {
	return s.repo.DeleteBan(ctx, params.BanID)
}

// DeleteMessage deletes a message
func (s *Service) DeleteMessage(ctx context.Context, params api.DeleteMessageParams) error {
	return s.repo.DeleteMessage(ctx, params.MessageID)
}
