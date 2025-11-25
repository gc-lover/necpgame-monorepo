package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/pkg/api/chat"
	"github.com/sirupsen/logrus"
)

type ChatServiceInterface interface {
	GetChannel(ctx context.Context, channelID uuid.UUID) (*chat.ChatChannel, error)
	ListChannels(ctx context.Context, params chat.ListChannelsParams) ([]chat.ChatChannel, error)
	CreateChannel(ctx context.Context, req *chat.CreateChannelRequest) (*chat.ChatChannel, error)
	DeleteChannel(ctx context.Context, channelID uuid.UUID) error
	SendMessage(ctx context.Context, channelID uuid.UUID, req *chat.SendMessageRequest) (*chat.ChatMessage, error)
	GetMessages(ctx context.Context, channelID uuid.UUID, params chat.GetMessagesParams) ([]chat.ChatMessage, error)
	DeleteMessage(ctx context.Context, channelID, messageID uuid.UUID) error
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

func (h *ChatHandlers) GetChannel(w http.ResponseWriter, r *http.Request, channelId chat.ChannelId) {
	ctx := r.Context()
	
	channel, err := h.service.GetChannel(ctx, channelId)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get channel")
		h.respondError(w, http.StatusInternalServerError, "failed to get channel")
		return
	}
	
	if channel == nil {
		h.respondError(w, http.StatusNotFound, "channel not found")
		return
	}
	
	h.respondJSON(w, http.StatusOK, channel)
}

func (h *ChatHandlers) ListChannels(w http.ResponseWriter, r *http.Request, params chat.ListChannelsParams) {
	ctx := r.Context()
	
	channels, err := h.service.ListChannels(ctx, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list channels")
		h.respondError(w, http.StatusInternalServerError, "failed to list channels")
		return
	}
	
	response := chat.ChannelListResponse{
		Channels: &channels,
		Total:    new(int),
	}
	*response.Total = len(channels)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChatHandlers) CreateChannel(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	var req chat.CreateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	channel, err := h.service.CreateChannel(ctx, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create channel")
		h.respondError(w, http.StatusInternalServerError, "failed to create channel")
		return
	}
	
	h.respondJSON(w, http.StatusCreated, channel)
}

func (h *ChatHandlers) DeleteChannel(w http.ResponseWriter, r *http.Request, channelId chat.ChannelId) {
	ctx := r.Context()
	
	if err := h.service.DeleteChannel(ctx, channelId); err != nil {
		h.logger.WithError(err).Error("Failed to delete channel")
		h.respondError(w, http.StatusInternalServerError, "failed to delete channel")
		return
	}
	
	w.WriteHeader(http.StatusNoContent)
}

func (h *ChatHandlers) SendMessage(w http.ResponseWriter, r *http.Request, channelId chat.ChannelId) {
	ctx := r.Context()
	
	var req chat.SendMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	message, err := h.service.SendMessage(ctx, channelId, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to send message")
		h.respondError(w, http.StatusInternalServerError, "failed to send message")
		return
	}
	
	h.respondJSON(w, http.StatusCreated, message)
}

func (h *ChatHandlers) GetMessages(w http.ResponseWriter, r *http.Request, channelId chat.ChannelId, params chat.GetMessagesParams) {
	ctx := r.Context()
	
	messages, err := h.service.GetMessages(ctx, channelId, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get messages")
		h.respondError(w, http.StatusInternalServerError, "failed to get messages")
		return
	}
	
	response := chat.MessageListResponse{
		Messages: &messages,
		Total:    new(int),
	}
	*response.Total = len(messages)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChatHandlers) DeleteMessage(w http.ResponseWriter, r *http.Request, channelId chat.ChannelId, messageId chat.MessageId) {
	ctx := r.Context()
	
	if err := h.service.DeleteMessage(ctx, channelId, messageId); err != nil {
		h.logger.WithError(err).Error("Failed to delete message")
		h.respondError(w, http.StatusInternalServerError, "failed to delete message")
		return
	}
	
	w.WriteHeader(http.StatusNoContent)
}

func (h *ChatHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ChatHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := chat.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

