// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1490
package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type ChatCommandHandlers struct {
	service ChatCommandServiceInterface
	logger  *logrus.Logger
}

func NewChatCommandHandlers(service ChatCommandServiceInterface, logger *logrus.Logger) *ChatCommandHandlers {
	return &ChatCommandHandlers{
		service: service,
		logger:  logger,
	}
}

func (h *ChatCommandHandlers) ExecuteChatCommand(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), CacheTimeout)
	defer cancel()

	var req models.ExecuteCommandRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	response, err := h.service.ExecuteCommand(ctx, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to execute chat command")
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(response); err != nil {
		h.logger.WithError(err).Error("Failed to encode response")
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}
}
