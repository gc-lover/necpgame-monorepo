// Issue: #1598, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-history-service-go/pkg/api"
	"github.com/google/uuid"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout = 50 * time.Millisecond
)

// ChatHistoryHandlers implements api.Handler interface (ogen typed handlers!)
type ChatHistoryHandlers struct{}

func NewChatHistoryHandlers() *ChatHistoryHandlers {
	return &ChatHistoryHandlers{}
}

// GetChatHistory - TYPED response!
func (h *ChatHistoryHandlers) GetChatHistory(ctx context.Context, params api.GetChatHistoryParams) (api.GetChatHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	messageId1 := uuid.New()
	messageId2 := uuid.New()
	senderId1 := uuid.New()
	senderId2 := uuid.New()
	channelId1 := uuid.New()
	channelId2 := uuid.New()
	now := time.Now()

	// Convert GetChatHistoryChannelType to ChatMessageChannelType
	channelTypeMsg := api.ChatMessageChannelType(params.ChannelType)

	messages := []api.ChatMessage{
		{
			ID:           api.NewOptUUID(messageId1),
			ChannelID:    api.NewOptUUID(channelId1),
			ChannelType:  api.NewOptChatMessageChannelType(channelTypeMsg),
			SenderID:     api.NewOptUUID(senderId1),
			SenderName:   api.NewOptString("Player1"),
			Content:      api.NewOptString("History message 1"),
			MessageType:  api.NewOptChatMessageMessageType(api.ChatMessageMessageTypeText),
			CreatedAt:    api.NewOptDateTime(now),
			UpdatedAt:    api.NewOptNilDateTime(now),
			ExpiresAt:    api.OptNilDateTime{},
			IsDeleted:    api.NewOptBool(false),
			IsEdited:     api.NewOptBool(false),
			FormattedText: api.OptString{},
		},
		{
			ID:           api.NewOptUUID(messageId2),
			ChannelID:    api.NewOptUUID(channelId2),
			ChannelType:  api.NewOptChatMessageChannelType(channelTypeMsg),
			SenderID:     api.NewOptUUID(senderId2),
			SenderName:   api.NewOptString("Player2"),
			Content:      api.NewOptString("History message 2"),
			MessageType:  api.NewOptChatMessageMessageType(api.ChatMessageMessageTypeText),
			CreatedAt:    api.NewOptDateTime(now),
			UpdatedAt:    api.NewOptNilDateTime(now),
			ExpiresAt:    api.OptNilDateTime{},
			IsDeleted:    api.NewOptBool(false),
			IsEdited:     api.NewOptBool(false),
			FormattedText: api.OptString{},
		},
	}

	total := len(messages)
	limit := 50
	offset := 0

	if params.Limit.Set {
		limit = params.Limit.Value
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	channelTypeStr := string(params.ChannelType)

	response := &api.ChatHistoryResponse{
		ChannelType: api.NewOptString(channelTypeStr),
		Items:       messages,
		Total:       total,
		Limit:       api.NewOptInt(limit),
		Offset:      api.NewOptInt(offset),
		HasMore:     api.NewOptBool(false),
	}

	return response, nil
}

// SearchChatHistory - TYPED response!
func (h *ChatHistoryHandlers) SearchChatHistory(ctx context.Context, params api.SearchChatHistoryParams) (api.SearchChatHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	messageId1 := uuid.New()
	messageId2 := uuid.New()
	senderId1 := uuid.New()
	senderId2 := uuid.New()
	channelId1 := uuid.New()
	channelId2 := uuid.New()
	now := time.Now()

	channelType := api.ChatMessageChannelTypeGLOBAL
	if params.ChannelType.Set {
		channelType = api.ChatMessageChannelType(params.ChannelType.Value)
	}

	messages := []api.ChatMessage{
		{
			ID:           api.NewOptUUID(messageId1),
			ChannelID:    api.NewOptUUID(channelId1),
			ChannelType:  api.NewOptChatMessageChannelType(channelType),
			SenderID:     api.NewOptUUID(senderId1),
			SenderName:   api.NewOptString("Player1"),
			Content:      api.NewOptString("Search result 1"),
			MessageType:  api.NewOptChatMessageMessageType(api.ChatMessageMessageTypeText),
			CreatedAt:    api.NewOptDateTime(now),
			UpdatedAt:    api.NewOptNilDateTime(now),
			ExpiresAt:    api.OptNilDateTime{},
			IsDeleted:    api.NewOptBool(false),
			IsEdited:     api.NewOptBool(false),
			FormattedText: api.OptString{},
		},
		{
			ID:           api.NewOptUUID(messageId2),
			ChannelID:    api.NewOptUUID(channelId2),
			ChannelType:  api.NewOptChatMessageChannelType(channelType),
			SenderID:     api.NewOptUUID(senderId2),
			SenderName:   api.NewOptString("Player2"),
			Content:      api.NewOptString("Search result 2"),
			MessageType:  api.NewOptChatMessageMessageType(api.ChatMessageMessageTypeText),
			CreatedAt:    api.NewOptDateTime(now),
			UpdatedAt:    api.NewOptNilDateTime(now),
			ExpiresAt:    api.OptNilDateTime{},
			IsDeleted:    api.NewOptBool(false),
			IsEdited:     api.NewOptBool(false),
			FormattedText: api.OptString{},
		},
	}

	total := len(messages)
	limit := 50
	offset := 0

	if params.Limit.Set {
		limit = params.Limit.Value
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	query := params.Query

	response := &api.ChatSearchResponse{
		Query:   api.NewOptString(query),
		Items:   messages,
		Total:   total,
		Limit:   api.NewOptInt(limit),
		Offset:  api.NewOptInt(offset),
		HasMore: api.NewOptBool(false),
	}

	return response, nil
}
