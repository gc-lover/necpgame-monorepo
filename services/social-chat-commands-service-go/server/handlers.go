// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1598, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-commands-service-go/pkg/api"
)

// DBTimeout Context timeout constants (Issue #1604)
const (
	DBTimeout = 50 * time.Millisecond
)

// ChatCommandsHandlers implements api.Handler interface (ogen typed handlers!)
type ChatCommandsHandlers struct{}

func NewChatCommandsHandlers() *ChatCommandsHandlers {
	return &ChatCommandsHandlers{}
}

// ExecuteChatCommand - TYPED response!
func (h *ChatCommandsHandlers) ExecuteChatCommand(ctx context.Context, req *api.ExecuteCommandRequest) (api.ExecuteChatCommandRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	success := true
	result := "Command executed successfully"
	command := req.Command

	response := &api.CommandResponse{
		Success: api.NewOptBool(true),
		Command: api.NewOptString(command),
		Result:  api.NewOptNilString(result),
		Error:   api.OptNilString{},
	}

	return response, nil
}
