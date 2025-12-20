// Package server Issue: #1598, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-format-service-go/pkg/api"
	"github.com/go-faster/jx"
	"github.com/google/uuid"
)

// DBTimeout Context timeout constants (Issue #1604)
const (
	DBTimeout = 50 * time.Millisecond
)

// ChatFormatHandlers implements api.Handler interface (ogen typed handlers!)
type ChatFormatHandlers struct{}

func NewChatFormatHandlers() *ChatFormatHandlers {
	return &ChatFormatHandlers{}
}

// FormatChatMessage - TYPED response!
func (h *ChatFormatHandlers) FormatChatMessage(ctx context.Context, req *api.FormatMessageRequest) (api.FormatChatMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	original := req.Content
	formatted := req.Content
	var mentions []uuid.UUID

	response := &api.FormattedMessageResponse{
		Original:  api.NewOptString(original),
		Formatted: api.NewOptString(formatted),
		Mentions:  mentions,
	}

	return response, nil
}

// GetChatEvents - TYPED response!
func (h *ChatFormatHandlers) GetChatEvents(ctx context.Context, params api.GetChatEventsParams) (api.GetChatEventsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	eventId1 := uuid.New()
	eventId2 := uuid.New()
	channelId := uuid.New()
	now := time.Now()

	// Event data as jx.Raw maps
	messageId1JSON := jx.Raw(`"` + uuid.New().String() + `"`)
	playerId1JSON := jx.Raw(`"` + uuid.New().String() + `"`)
	messageId2JSON := jx.Raw(`"` + uuid.New().String() + `"`)
	playerId2JSON := jx.Raw(`"` + uuid.New().String() + `"`)

	eventData1 := api.ChatEventEventData{
		"message_id": messageId1JSON,
		"player_id":  playerId1JSON,
	}
	eventData2 := api.ChatEventEventData{
		"message_id": messageId2JSON,
		"player_id":  playerId2JSON,
	}

	events := []api.ChatEvent{
		{
			ID:        api.NewOptUUID(eventId1),
			EventType: api.NewOptChatEventEventType(api.ChatEventEventTypeChatMessageSent),
			ChannelID: api.NewOptNilUUID(channelId),
			EventData: api.NewOptChatEventEventData(eventData1),
			CreatedAt: api.NewOptDateTime(now),
		},
		{
			ID:        api.NewOptUUID(eventId2),
			EventType: api.NewOptChatEventEventType(api.ChatEventEventTypeChatMessageReceived),
			ChannelID: api.NewOptNilUUID(channelId),
			EventData: api.NewOptChatEventEventData(eventData2),
			CreatedAt: api.NewOptDateTime(now),
		},
	}

	total := len(events)
	limit := 50
	offset := 0

	if params.Limit.Set {
		limit = params.Limit.Value
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	response := &api.ChatEventsResponse{
		Events: events,
		Total:  api.NewOptInt(total),
		Limit:  api.NewOptInt(limit),
		Offset: api.NewOptInt(offset),
	}

	return response, nil
}
