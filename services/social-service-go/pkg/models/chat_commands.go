// Chat Commands Models
// Issue: #1490 - Chat Commands Service: ogen handlers implementation
// PERFORMANCE: Optimized structs for chat command processing

package models

// ExecuteCommandRequest represents a request to execute a chat command
type ExecuteCommandRequest struct {
	Command string   `json:"command"`
	Args    []string `json:"args,omitempty"`
}

// CommandResponse represents the response from executing a chat command
type CommandResponse struct {
	Command string `json:"command"`
	Result  *string `json:"result,omitempty"`
	Error   *string `json:"error,omitempty"`
	Success bool    `json:"success"`
}

// ChatCommand represents a chat command definition
type ChatCommand struct {
	Name        string   `json:"name"`
	Description string   `json:"description"`
	Args        []string `json:"args,omitempty"`
	Permissions []string `json:"permissions,omitempty"`
}

// CommandResult represents the result of command execution
type CommandResult struct {
	Success bool
	Result  string
	Error   string
}
