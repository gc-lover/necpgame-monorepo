// Issue: #1598, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-messages-service-go/pkg/api"
	"github.com/google/uuid"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// ChatMessagesHandlers implements api.Handler interface (ogen typed handlers!)
type ChatMessagesHandlers struct{}

func NewChatMessagesHandlers() *ChatMessagesHandlers {
	return &ChatMessagesHandlers{}
}

// GetMessages - TYPED response!
func (h *ChatMessagesHandlers) GetMessages(ctx context.Context, params api.GetMessagesParams) (api.GetMessagesRes, error) {
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

	messages := []api.ChatMessage{
		{
			ID:           api.NewOptUUID(messageId1),
			ChannelID:    api.NewOptUUID(channelId1),
			ChannelType:  api.NewOptChatMessageChannelType(api.ChatMessageChannelTypeGLOBAL),
			SenderID:     api.NewOptUUID(senderId1),
			SenderName:   api.NewOptString("Player1"),
			Content:      api.NewOptString("Hello, world!"),
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
			ChannelType:  api.NewOptChatMessageChannelType(api.ChatMessageChannelTypeGLOBAL),
			SenderID:     api.NewOptUUID(senderId2),
			SenderName:   api.NewOptString("Player2"),
			Content:      api.NewOptString("How are you?"),
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

	response := &api.MessageListResponse{
		Items:   messages,
		Total:   total,
		Limit:   api.NewOptInt(limit),
		Offset:  api.NewOptInt(offset),
		HasMore: api.NewOptBool(false),
	}

	return response, nil
}

// GetChannelMessages - TYPED response!
func (h *ChatMessagesHandlers) GetChannelMessages(ctx context.Context, params api.GetChannelMessagesParams) (api.GetChannelMessagesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	messageId1 := uuid.New()
	messageId2 := uuid.New()
	senderId1 := uuid.New()
	senderId2 := uuid.New()
	now := time.Now()

	messages := []api.ChatMessage{
		{
			ID:           api.NewOptUUID(messageId1),
			ChannelID:    api.NewOptUUID(params.ChannelID),
			ChannelType:  api.NewOptChatMessageChannelType(api.ChatMessageChannelTypeGLOBAL),
			SenderID:     api.NewOptUUID(senderId1),
			SenderName:   api.NewOptString("Player1"),
			Content:      api.NewOptString("Channel message 1"),
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
			ChannelID:    api.NewOptUUID(params.ChannelID),
			ChannelType:  api.NewOptChatMessageChannelType(api.ChatMessageChannelTypeGLOBAL),
			SenderID:     api.NewOptUUID(senderId2),
			SenderName:   api.NewOptString("Player2"),
			Content:      api.NewOptString("Channel message 2"),
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

	response := &api.MessageListResponse{
		Items:   messages,
		Total:   total,
		Limit:   api.NewOptInt(limit),
		Offset:  api.NewOptInt(offset),
		HasMore: api.NewOptBool(false),
	}

	return response, nil
}

// SendChatMessage - TYPED response!
func (h *ChatMessagesHandlers) SendChatMessage(ctx context.Context, req *api.SendMessageRequest) (api.SendChatMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	messageId := uuid.New()
	senderId := uuid.New()
	now := time.Now()

	channelType := api.ChatMessageChannelType(req.ChannelType)

	response := &api.ChatMessage{
		ID:           api.NewOptUUID(messageId),
		ChannelID:    api.NewOptUUID(req.ChannelID),
		ChannelType:  api.NewOptChatMessageChannelType(channelType),
		SenderID:     api.NewOptUUID(senderId),
		SenderName:   api.NewOptString("Player1"),
		Content:      api.NewOptString(req.Content),
		MessageType:  api.NewOptChatMessageMessageType(api.ChatMessageMessageTypeText),
		CreatedAt:    api.NewOptDateTime(now),
		UpdatedAt:    api.NewOptNilDateTime(now),
		ExpiresAt:    api.OptNilDateTime{},
		IsDeleted:    api.NewOptBool(false),
		IsEdited:     api.NewOptBool(false),
		FormattedText: api.OptString{},
	}

	return response, nil
}

// ProcessChatMessage - TYPED response!
func (h *ChatMessagesHandlers) ProcessChatMessage(ctx context.Context, req *api.ProcessMessageRequest) (api.ProcessChatMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	messageId := req.MessageID
	senderId := uuid.New()
	channelId := uuid.New()
	now := time.Now()

	message := api.ChatMessage{
		ID:           api.NewOptUUID(messageId),
		ChannelID:    api.NewOptUUID(channelId),
		ChannelType:  api.NewOptChatMessageChannelType(api.ChatMessageChannelTypeGLOBAL),
		SenderID:     api.NewOptUUID(senderId),
		SenderName:   api.NewOptString("Player1"),
		Content:      api.NewOptString("Processed message"),
		MessageType:  api.NewOptChatMessageMessageType(api.ChatMessageMessageTypeText),
		CreatedAt:    api.NewOptDateTime(now),
		UpdatedAt:    api.NewOptNilDateTime(now),
		ExpiresAt:    api.OptNilDateTime{},
		IsDeleted:    api.NewOptBool(false),
		IsEdited:     api.NewOptBool(false),
		FormattedText: api.OptString{},
	}

	response := &api.ProcessedMessageResponse{
		Message:           api.NewOptChatMessage(message),
		FormattingApplied: api.NewOptBool(true),
		ModerationApplied: api.NewOptBool(false),
	}

	return response, nil
}
