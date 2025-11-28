package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-service-go/pkg/api/chat"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type ChatServiceInterface interface {
	GetMessages(ctx context.Context, params chat.GetMessagesParams) (*chat.MessageListResponse, error)
	ProcessChatMessage(ctx context.Context, req *chat.ProcessMessageRequest) (*chat.ProcessedMessageResponse, error)
	SendChatMessage(ctx context.Context, req *chat.SendMessageRequest) (*chat.ChatMessage, error)
	GetChannelMessages(ctx context.Context, channelID openapi_types.UUID, params chat.GetChannelMessagesParams) (*chat.MessageListResponse, error)
}

type ChatHandlers struct {
	service ChatServiceInterface
	logger  *logrus.Logger
}

func NewChatHandlers(service ChatServiceInterface) *ChatHandlers {
	return &ChatHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ChatHandlers) GetMessages(w http.ResponseWriter, r *http.Request, params chat.GetMessagesParams) {
	ctx := r.Context()
	
	response, err := h.service.GetMessages(ctx, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get messages")
		h.respondError(w, http.StatusInternalServerError, "failed to get messages")
		return
	}
	
	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChatHandlers) ProcessChatMessage(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	var req chat.ProcessMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	response, err := h.service.ProcessChatMessage(ctx, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to process message")
		h.respondError(w, http.StatusInternalServerError, "failed to process message")
		return
	}
	
	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChatHandlers) SendChatMessage(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	var req chat.SendMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	message, err := h.service.SendChatMessage(ctx, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to send message")
		h.respondError(w, http.StatusInternalServerError, "failed to send message")
		return
	}
	
	h.respondJSON(w, http.StatusCreated, message)
}

func (h *ChatHandlers) GetChannelMessages(w http.ResponseWriter, r *http.Request, channelId openapi_types.UUID, params chat.GetChannelMessagesParams) {
	ctx := r.Context()
	
	response, err := h.service.GetChannelMessages(ctx, channelId, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get channel messages")
		h.respondError(w, http.StatusInternalServerError, "failed to get channel messages")
		return
	}
	
	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChatHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *ChatHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := chat.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}
