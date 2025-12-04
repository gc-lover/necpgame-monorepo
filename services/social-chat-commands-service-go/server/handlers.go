// Issue: #1604
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/social-chat-commands-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

type ChatCommandsHandlers struct{}

func NewChatCommandsHandlers() *ChatCommandsHandlers {
	return &ChatCommandsHandlers{}
}

func (h *ChatCommandsHandlers) ExecuteChatCommand(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	var req api.ExecuteCommandRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	success := true
	result := "Command executed successfully"
	command := req.Command

	response := api.CommandResponse{
		Success: &success,
		Command: &command,
		Result:  &result,
		Error:   nil,
	}

	respondJSON(w, http.StatusOK, response)
}




















