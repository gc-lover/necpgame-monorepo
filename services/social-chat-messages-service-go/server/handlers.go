package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/social-chat-messages-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
)

type ChatMessagesHandlers struct{}

func NewChatMessagesHandlers() *ChatMessagesHandlers {
	return &ChatMessagesHandlers{}
}

func (h *ChatMessagesHandlers) GetMessages(w http.ResponseWriter, r *http.Request, params api.GetMessagesParams) {
	messageId1 := openapi_types.UUID(uuid.New())
	messageId2 := openapi_types.UUID(uuid.New())
	senderId1 := openapi_types.UUID(uuid.New())
	senderId2 := openapi_types.UUID(uuid.New())
	channelId1 := openapi_types.UUID(uuid.New())
	channelId2 := openapi_types.UUID(uuid.New())
	channelType := api.ChatMessageChannelTypeGLOBAL
	messageType := api.Text
	content1 := "Hello, world!"
	content2 := "How are you?"
	senderName1 := "Player1"
	senderName2 := "Player2"
	now := time.Now()
	isDeleted := false
	isEdited := false

	messages := []api.ChatMessage{
		{
			Id:           &messageId1,
			ChannelId:    &channelId1,
			ChannelType:  &channelType,
			SenderId:     &senderId1,
			SenderName:   &senderName1,
			Content:      &content1,
			MessageType:  &messageType,
			CreatedAt:    &now,
			UpdatedAt:    &now,
			ExpiresAt:    nil,
			IsDeleted:    &isDeleted,
			IsEdited:     &isEdited,
			FormattedText: nil,
		},
		{
			Id:           &messageId2,
			ChannelId:    &channelId2,
			ChannelType:  &channelType,
			SenderId:     &senderId2,
			SenderName:   &senderName2,
			Content:      &content2,
			MessageType:  &messageType,
			CreatedAt:    &now,
			UpdatedAt:    &now,
			ExpiresAt:    nil,
			IsDeleted:    &isDeleted,
			IsEdited:     &isEdited,
			FormattedText: nil,
		},
	}

	total := len(messages)
	limit := 50
	offset := 0
	hasMore := false

	if params.Limit != nil {
		limit = int(*params.Limit)
	}
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	response := api.MessageListResponse{
		Items:   messages,
		Total:   total,
		Limit:   &limit,
		Offset:  &offset,
		HasMore: &hasMore,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatMessagesHandlers) GetChannelMessages(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID, params api.GetChannelMessagesParams) {
	messageId1 := openapi_types.UUID(uuid.New())
	messageId2 := openapi_types.UUID(uuid.New())
	senderId1 := openapi_types.UUID(uuid.New())
	senderId2 := openapi_types.UUID(uuid.New())
	channelType := api.ChatMessageChannelTypeGLOBAL
	messageType := api.Text
	content1 := "Channel message 1"
	content2 := "Channel message 2"
	senderName1 := "Player1"
	senderName2 := "Player2"
	now := time.Now()
	isDeleted := false
	isEdited := false

	messages := []api.ChatMessage{
		{
			Id:           &messageId1,
			ChannelId:    &channelId,
			ChannelType:  &channelType,
			SenderId:     &senderId1,
			SenderName:   &senderName1,
			Content:      &content1,
			MessageType:  &messageType,
			CreatedAt:    &now,
			UpdatedAt:    &now,
			ExpiresAt:    nil,
			IsDeleted:    &isDeleted,
			IsEdited:     &isEdited,
			FormattedText: nil,
		},
		{
			Id:           &messageId2,
			ChannelId:    &channelId,
			ChannelType:  &channelType,
			SenderId:     &senderId2,
			SenderName:   &senderName2,
			Content:      &content2,
			MessageType:  &messageType,
			CreatedAt:    &now,
			UpdatedAt:    &now,
			ExpiresAt:    nil,
			IsDeleted:    &isDeleted,
			IsEdited:     &isEdited,
			FormattedText: nil,
		},
	}

	total := len(messages)
	limit := 50
	offset := 0
	hasMore := false

	if params.Limit != nil {
		limit = int(*params.Limit)
	}
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	response := api.MessageListResponse{
		Items:   messages,
		Total:   total,
		Limit:   &limit,
		Offset:  &offset,
		HasMore: &hasMore,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatMessagesHandlers) SendChatMessage(w http.ResponseWriter, r *http.Request) {
	var req api.SendMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	messageId := openapi_types.UUID(uuid.New())
	senderId := openapi_types.UUID(uuid.New())
	channelType := api.ChatMessageChannelType(req.ChannelType)
	messageType := api.Text
	senderName := "Player1"
	now := time.Now()
	isDeleted := false
	isEdited := false

	response := api.ChatMessage{
		Id:           &messageId,
		ChannelId:    &req.ChannelId,
		ChannelType:  &channelType,
		SenderId:     &senderId,
		SenderName:   &senderName,
		Content:      &req.Content,
		MessageType:  &messageType,
		CreatedAt:    &now,
		UpdatedAt:    &now,
		ExpiresAt:    nil,
		IsDeleted:    &isDeleted,
		IsEdited:     &isEdited,
		FormattedText: nil,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatMessagesHandlers) ProcessChatMessage(w http.ResponseWriter, r *http.Request) {
	var req api.ProcessMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	messageId := req.MessageId
	senderId := openapi_types.UUID(uuid.New())
	channelId := openapi_types.UUID(uuid.New())
	channelType := api.ChatMessageChannelTypeGLOBAL
	messageType := api.Text
	content := "Processed message"
	senderName := "Player1"
	now := time.Now()
	isDeleted := false
	isEdited := false
	formattingApplied := true
	moderationApplied := false

	message := api.ChatMessage{
		Id:           &messageId,
		ChannelId:    &channelId,
		ChannelType:  &channelType,
		SenderId:     &senderId,
		SenderName:   &senderName,
		Content:      &content,
		MessageType:  &messageType,
		CreatedAt:    &now,
		UpdatedAt:    &now,
		ExpiresAt:    nil,
		IsDeleted:    &isDeleted,
		IsEdited:     &isEdited,
		FormattedText: nil,
	}

	response := api.ProcessedMessageResponse{
		Message:           &message,
		FormattingApplied: &formattingApplied,
		ModerationApplied: &moderationApplied,
	}

	respondJSON(w, http.StatusOK, response)
}












