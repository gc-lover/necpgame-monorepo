package clients

import (
	"fmt"

	"go.uber.org/zap"
)

// PlayerClient handles communication with the Player Service
type PlayerClient struct {
	*HTTPClient
}

// NewPlayerClient creates a new Player Service client
func NewPlayerClient(baseURL string, logger *zap.Logger) *PlayerClient {
	return &PlayerClient{
		HTTPClient: NewHTTPClient(baseURL, logger),
	}
}

// PlayerInfo represents player information from the Player Service
type PlayerInfo struct {
	ID       string `json:"id"`
	Username string `json:"username"`
	Email    string `json:"email"`
	Status   string `json:"status"` // active, banned, suspended
	Level    int    `json:"level"`
}

// GetPlayerInfo retrieves player information
func (c *PlayerClient) GetPlayerInfo(playerID string) (*PlayerInfo, error) {
	path := fmt.Sprintf("/players/%s", playerID)

	resp, err := c.Get(path, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to get player info: %w", err)
	}

	var player PlayerInfo
	if err := c.ReadJSONResponse(resp, &player); err != nil {
		return nil, fmt.Errorf("failed to read player info response: %w", err)
	}

	return &player, nil
}

// ValidatePlayerExists checks if a player exists and is active
func (c *PlayerClient) ValidatePlayerExists(playerID string) error {
	player, err := c.GetPlayerInfo(playerID)
	if err != nil {
		return err
	}

	if player.Status != "active" {
		return fmt.Errorf("player is not active: status %s", player.Status)
	}

	return nil
}

// GetPlayerLevel retrieves the current level of a player
func (c *PlayerClient) GetPlayerLevel(playerID string) (int, error) {
	player, err := c.GetPlayerInfo(playerID)
	if err != nil {
		return 0, err
	}

	return player.Level, nil
}