package server

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) createMessage(w http.ResponseWriter, r *http.Request) {
	var req models.CreateMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Content == "" {
		s.respondError(w, http.StatusBadRequest, "content is required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	senderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	formatted := FormatMessage(req.Content)

	message := &models.ChatMessage{
		ID:          uuid.New(),
		ChannelID:   req.ChannelID,
		ChannelType: req.ChannelType,
		SenderID:    senderID,
		SenderName:  r.Context().Value("username").(string),
		Content:     req.Content,
		Formatted:   formatted,
		CreatedAt:   time.Now(),
	}

	message, err = s.socialService.CreateMessage(r.Context(), message)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create chat message")
		s.respondError(w, http.StatusInternalServerError, "failed to create message")
		return
	}

	s.respondJSON(w, http.StatusCreated, message)
}

func (s *HTTPServer) getMessages(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelIDStr := vars["channelId"]

	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel id")
		return
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	messages, total, err := s.socialService.GetMessages(r.Context(), channelID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chat messages")
		s.respondError(w, http.StatusInternalServerError, "failed to get messages")
		return
	}

	response := models.MessageListResponse{
		Messages: messages,
		Total:    total,
		HasMore:  offset+limit < total,
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getChannels(w http.ResponseWriter, r *http.Request) {
	channelTypeStr := r.URL.Query().Get("type")
	var channelType *models.ChannelType

	if channelTypeStr != "" {
		ct := models.ChannelType(channelTypeStr)
		channelType = &ct
	}

	channels, err := s.socialService.GetChannels(r.Context(), channelType)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chat channels")
		s.respondError(w, http.StatusInternalServerError, "failed to get channels")
		return
	}

	response := models.ChannelListResponse{
		Channels: channels,
		Total:    len(channels),
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getChannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelIDStr := vars["id"]

	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel id")
		return
	}

	channel, err := s.socialService.GetChannel(r.Context(), channelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chat channel")
		s.respondError(w, http.StatusInternalServerError, "failed to get channel")
		return
	}

	if channel == nil {
		s.respondError(w, http.StatusNotFound, "channel not found")
		return
	}

	s.respondJSON(w, http.StatusOK, channel)
}

