package server

import (
	"net/http"
	"time"

	"github.com/necpgame/social-chat-history-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
)

type ChatHistoryHandlers struct{}

func NewChatHistoryHandlers() *ChatHistoryHandlers {
	return &ChatHistoryHandlers{}
}

func (h *ChatHistoryHandlers) GetChatHistory(w http.ResponseWriter, r *http.Request, channelType api.GetChatHistoryParamsChannelType, params api.GetChatHistoryParams) {
	messageId1 := openapi_types.UUID(uuid.New())
	messageId2 := openapi_types.UUID(uuid.New())
	senderId1 := openapi_types.UUID(uuid.New())
	senderId2 := openapi_types.UUID(uuid.New())
	channelId1 := openapi_types.UUID(uuid.New())
	channelId2 := openapi_types.UUID(uuid.New())
	channelTypePtr := api.ChatMessageChannelType(channelType)
	messageType := api.Text
	content1 := "History message 1"
	content2 := "History message 2"
	senderName1 := "Player1"
	senderName2 := "Player2"
	now := time.Now()
	isDeleted := false
	isEdited := false
	channelTypeStr := string(channelType)

	messages := []api.ChatMessage{
		{
			Id:           &messageId1,
			ChannelId:    &channelId1,
			ChannelType:  &channelTypePtr,
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
			ChannelType:  &channelTypePtr,
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

	response := api.ChatHistoryResponse{
		ChannelType: &channelTypeStr,
		Items:       messages,
		Total:       total,
		Limit:       &limit,
		Offset:      &offset,
		HasMore:     &hasMore,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatHistoryHandlers) SearchChatHistory(w http.ResponseWriter, r *http.Request, params api.SearchChatHistoryParams) {
	messageId1 := openapi_types.UUID(uuid.New())
	messageId2 := openapi_types.UUID(uuid.New())
	senderId1 := openapi_types.UUID(uuid.New())
	senderId2 := openapi_types.UUID(uuid.New())
	channelId1 := openapi_types.UUID(uuid.New())
	channelId2 := openapi_types.UUID(uuid.New())
	channelType := api.ChatMessageChannelTypeGLOBAL
	messageType := api.Text
	content1 := "Search result 1"
	content2 := "Search result 2"
	senderName1 := "Player1"
	senderName2 := "Player2"
	now := time.Now()
	isDeleted := false
	isEdited := false
	query := params.Query

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

	response := api.ChatSearchResponse{
		Query:   &query,
		Items:   messages,
		Total:   total,
		Limit:   &limit,
		Offset:  &offset,
		HasMore: &hasMore,
	}

	respondJSON(w, http.StatusOK, response)
}

