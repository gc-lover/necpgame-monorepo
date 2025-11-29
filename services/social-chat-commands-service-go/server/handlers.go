package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-chat-commands-service-go/pkg/api"
)

type ChatCommandsHandlers struct{}

func NewChatCommandsHandlers() *ChatCommandsHandlers {
	return &ChatCommandsHandlers{}
}

func (h *ChatCommandsHandlers) ExecuteChatCommand(w http.ResponseWriter, r *http.Request) {
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



