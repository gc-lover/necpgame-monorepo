// SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package models

type ExecuteCommandRequest struct {
	Command string   `json:"command"`
	Args    []string `json:"args,omitempty"`
}

type CommandResponse struct {
	Success bool    `json:"success"`
	Command string  `json:"command"`
	Result  *string `json:"result,omitempty"`
	Error   *string `json:"error,omitempty"`
}
