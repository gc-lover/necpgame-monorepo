// Issue: #1598, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-channels-service-go/pkg/api"
	"github.com/google/uuid"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// ChatChannelsHandlers implements api.Handler interface (ogen typed handlers!)
type ChatChannelsHandlers struct{}

func NewChatChannelsHandlers() *ChatChannelsHandlers {
	return &ChatChannelsHandlers{}
}

// GetChannels - TYPED response!
func (h *ChatChannelsHandlers) GetChannels(ctx context.Context, params api.GetChannelsParams) (api.GetChannelsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	channelId1 := uuid.New()
	channelId2 := uuid.New()
	now := time.Now()

	channels := []api.ChatChannel{
		{
			ID:               api.NewOptUUID(channelId1),
			ChannelType:      api.NewOptChatChannelChannelType(api.ChatChannelChannelTypeGLOBAL),
			OwnerID:          api.OptNilUUID{},
			Name:             api.NewOptString("Global Chat"),
			Description:      api.OptNilString{},
			ReadPermission:   api.OptNilChatChannelReadPermission{},
			WritePermission:  api.OptNilChatChannelWritePermission{},
			CooldownSeconds:  api.NewOptInt(0),
			MaxMessageLength: api.NewOptInt(500),
			IsActive:         api.NewOptBool(true),
			CreatedAt:        api.NewOptDateTime(now),
		},
		{
			ID:               api.NewOptUUID(channelId2),
			ChannelType:      api.NewOptChatChannelChannelType(api.ChatChannelChannelTypeTRADE),
			OwnerID:          api.OptNilUUID{},
			Name:             api.NewOptString("Trade Chat"),
			Description:      api.OptNilString{},
			ReadPermission:   api.OptNilChatChannelReadPermission{},
			WritePermission:  api.OptNilChatChannelWritePermission{},
			CooldownSeconds:  api.NewOptInt(0),
			MaxMessageLength: api.NewOptInt(500),
			IsActive:         api.NewOptBool(true),
			CreatedAt:        api.NewOptDateTime(now),
		},
	}

	response := &api.ChannelListResponse{
		Channels: channels,
		Total:    api.NewOptInt(len(channels)),
	}

	return response, nil
}

// GetChannel - TYPED response!
func (h *ChatChannelsHandlers) GetChannel(ctx context.Context, params api.GetChannelParams) (api.GetChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	now := time.Now()

	response := &api.ChatChannel{
		ID:               api.NewOptUUID(params.ChannelID),
		ChannelType:      api.NewOptChatChannelChannelType(api.ChatChannelChannelTypeGLOBAL),
		OwnerID:          api.OptNilUUID{},
		Name:             api.NewOptString("Global Chat"),
		Description:      api.OptNilString{},
		ReadPermission:   api.OptNilChatChannelReadPermission{},
		WritePermission:  api.OptNilChatChannelWritePermission{},
		CooldownSeconds:  api.NewOptInt(0),
		MaxMessageLength: api.NewOptInt(500),
		IsActive:         api.NewOptBool(true),
		CreatedAt:        api.NewOptDateTime(now),
	}

	return response, nil
}

// JoinChannel - TYPED response!
func (h *ChatChannelsHandlers) JoinChannel(ctx context.Context, req *api.JoinChannelRequest, params api.JoinChannelParams) (api.JoinChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	now := time.Now()

	response := &api.ChatChannel{
		ID:               api.NewOptUUID(params.ChannelID),
		ChannelType:      api.NewOptChatChannelChannelType(api.ChatChannelChannelTypeGLOBAL),
		OwnerID:          api.OptNilUUID{},
		Name:             api.NewOptString("Global Chat"),
		Description:      api.OptNilString{},
		ReadPermission:   api.OptNilChatChannelReadPermission{},
		WritePermission:  api.OptNilChatChannelWritePermission{},
		CooldownSeconds:  api.NewOptInt(0),
		MaxMessageLength: api.NewOptInt(500),
		IsActive:         api.NewOptBool(true),
		CreatedAt:        api.NewOptDateTime(now),
	}

	return response, nil
}

// LeaveChannel - TYPED response!
func (h *ChatChannelsHandlers) LeaveChannel(ctx context.Context, req *api.LeaveChannelRequest, params api.LeaveChannelParams) (api.LeaveChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	status := "left"
	response := &api.StatusResponse{
		Status: api.NewOptString(status),
	}

	return response, nil
}

// GetChannelMembers - TYPED response!
func (h *ChatChannelsHandlers) GetChannelMembers(ctx context.Context, params api.GetChannelMembersParams) (api.GetChannelMembersRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	memberId1 := uuid.New()
	memberId2 := uuid.New()
	members := []uuid.UUID{memberId1, memberId2}
	total := len(members)

	limit := 50
	offset := 0
	if params.Limit.Set {
		limit = params.Limit.Value
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	response := &api.ChannelMembersResponse{
		ChannelID: api.NewOptUUID(params.ChannelID),
		Members:   members,
		Total:     api.NewOptInt(total),
		Limit:     api.NewOptInt(limit),
		Offset:    api.NewOptInt(offset),
	}

	return response, nil
}
