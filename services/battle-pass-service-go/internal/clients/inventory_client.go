package clients

import (
	"fmt"

	"go.uber.org/zap"
)

// InventoryClient handles communication with the Inventory Service
type InventoryClient struct {
	*HTTPClient
}

// NewInventoryClient creates a new Inventory Service client
func NewInventoryClient(baseURL string, logger *zap.Logger) *InventoryClient {
	return &InventoryClient{
		HTTPClient: NewHTTPClient(baseURL, logger),
	}
}

// InventoryItem represents an item in player inventory
type InventoryItem struct {
	ID       string                 `json:"id"`
	PlayerID string                 `json:"playerId"`
	ItemType string                 `json:"itemType"`
	ItemID   string                 `json:"itemId"`
	Quantity int                    `json:"quantity"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}

// AddItemRequest represents a request to add an item to inventory
type AddItemRequest struct {
	PlayerID string                 `json:"playerId"`
	ItemType string                 `json:"itemType"`
	ItemID   string                 `json:"itemId"`
	Quantity int                    `json:"quantity"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}

// AddItemToInventory adds an item to player inventory
func (c *InventoryClient) AddItemToInventory(request AddItemRequest) (*InventoryItem, error) {
	path := "/inventory/items"

	resp, err := c.Post(path, request, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to add item to inventory: %w", err)
	}

	var item InventoryItem
	if err := c.ReadJSONResponse(resp, &item); err != nil {
		return nil, fmt.Errorf("failed to read add item response: %w", err)
	}

	return &item, nil
}

// GrantReward grants a reward by adding it to player inventory
func (c *InventoryClient) GrantReward(playerID, rewardType, rewardID string, metadata map[string]interface{}) (string, error) {
	request := AddItemRequest{
		PlayerID: playerID,
		ItemType: rewardType,
		ItemID:   rewardID,
		Quantity: 1,
		Metadata: metadata,
	}

	item, err := c.AddItemToInventory(request)
	if err != nil {
		return "", fmt.Errorf("failed to grant reward: %w", err)
	}

	return item.ID, nil
}

// GetPlayerInventory retrieves player inventory items
func (c *InventoryClient) GetPlayerInventory(playerID string, itemType string) ([]InventoryItem, error) {
	path := fmt.Sprintf("/inventory/players/%s/items", playerID)
	if itemType != "" {
		path += fmt.Sprintf("?type=%s", itemType)
	}

	resp, err := c.Get(path, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to get player inventory: %w", err)
	}

	var items []InventoryItem
	if err := c.ReadJSONResponse(resp, &items); err != nil {
		return nil, fmt.Errorf("failed to read inventory response: %w", err)
	}

	return items, nil
}

// RemoveItemFromInventory removes an item from player inventory (for returns/refunds)
func (c *InventoryClient) RemoveItemFromInventory(playerID, itemID string) error {
	path := fmt.Sprintf("/inventory/items/%s", itemID)

	resp, err := c.Delete(path, nil)
	if err != nil {
		return fmt.Errorf("failed to remove item from inventory: %w", err)
	}

	if resp.StatusCode != 204 {
		return fmt.Errorf("failed to remove item: HTTP %d", resp.StatusCode)
	}

	return nil
}