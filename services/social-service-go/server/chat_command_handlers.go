package server

import (
	"bytes"
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type ChatCommandHandlers struct {
	service ChatCommandServiceInterface
	logger  *logrus.Logger
}

func NewChatCommandHandlers(service ChatCommandServiceInterface) *ChatCommandHandlers {
	return &ChatCommandHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ChatCommandHandlers) executeChatCommand(w http.ResponseWriter, r *http.Request) {
	var req models.ExecuteCommandRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	response, err := h.service.ExecuteCommand(r.Context(), &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to execute chat command")
		h.respondError(w, http.StatusInternalServerError, "failed to execute chat command")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ChatCommandHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	var buf bytes.Buffer
	if err := json.NewEncoder(&buf).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
		h.respondError(w, http.StatusInternalServerError, "Failed to encode JSON response")
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		h.logger.WithError(err).Error("Failed to write JSON response")
	}
}

func (h *ChatCommandHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

