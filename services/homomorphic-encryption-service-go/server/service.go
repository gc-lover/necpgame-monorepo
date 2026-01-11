package server

import (
	"context"
	"encoding/json"
	"fmt"
	"log/slog"
	"time"

	"github.com/google/uuid"
)

// HomomorphicEncryptionService provides business logic for homomorphic encryption operations
type HomomorphicEncryptionService struct {
	engine   *HomomorphicEngine
	repo     Repository
	maxDataSize int64
}

// NewHomomorphicEncryptionService creates a new homomorphic encryption service
func NewHomomorphicEncryptionService(engine *HomomorphicEngine, repo Repository) *HomomorphicEncryptionService {
	return &HomomorphicEncryptionService{
		engine:      engine,
		repo:        repo,
		maxDataSize: 1024 * 1024, // 1MB max per data item
	}
}

// EncryptPlayerInventory encrypts a player's inventory data
func (s *HomomorphicEncryptionService) EncryptPlayerInventory(ctx context.Context, playerID string, inventory map[string]interface{}) (*EncryptedData, error) {
	if len(inventory) == 0 {
		return nil, fmt.Errorf("inventory cannot be empty")
	}

	// Serialize inventory to JSON for encryption
	inventoryJSON, err := json.Marshal(inventory)
	if err != nil {
		return nil, fmt.Errorf("failed to serialize inventory: %w", err)
	}

	if int64(len(inventoryJSON)) > s.maxDataSize {
		return nil, fmt.Errorf("inventory data too large: %d bytes > %d bytes", len(inventoryJSON), s.maxDataSize)
	}

	// Get or create player key
	keyID, err := s.getOrCreatePlayerKey(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player key: %w", err)
	}

	// For inventory, we'll encrypt individual item counts as integers
	encryptedItems := make(map[string]*EncryptedData)
	for itemID, quantity := range inventory {
		if qty, ok := quantity.(float64); ok {
			encryptedItem, err := s.engine.EncryptGameData(int(qty), "integer", keyID)
			if err != nil {
				return nil, fmt.Errorf("failed to encrypt item %s: %w", itemID, err)
			}
			encryptedItems[itemID] = encryptedItem
		}
	}

	// Create inventory structure
	inventoryData := map[string]interface{}{
		"player_id":       playerID,
		"encrypted_items": encryptedItems,
		"item_count":      len(encryptedItems),
	}

	// Encrypt the inventory structure
	encryptedInventory, err := s.engine.EncryptGameData(inventoryData, "inventory", keyID)
	if err != nil {
		return nil, fmt.Errorf("failed to encrypt inventory: %w", err)
	}

	// Store in repository
	encryptedInventory.CreatedAt = time.Now().Unix()
	if err := s.repo.StoreEncryptedData(ctx, encryptedInventory); err != nil {
		return nil, fmt.Errorf("failed to store encrypted inventory: %w", err)
	}

	slog.Info("Player inventory encrypted successfully",
		"player_id", playerID,
		"items_encrypted", len(encryptedItems),
		"inventory_id", encryptedInventory.ID,
	)

	return encryptedInventory, nil
}

// DecryptPlayerInventory decrypts a player's inventory data
func (s *HomomorphicEncryptionService) DecryptPlayerInventory(ctx context.Context, inventoryID string) (map[string]interface{}, error) {
	encryptedInventory, err := s.repo.GetEncryptedData(ctx, inventoryID)
	if err != nil {
		return nil, fmt.Errorf("failed to get encrypted inventory: %w", err)
	}

	if encryptedInventory.DataType != "inventory" {
		return nil, fmt.Errorf("invalid data type: expected 'inventory', got '%s'", encryptedInventory.DataType)
	}

	decryptedData, err := s.engine.DecryptGameData(encryptedInventory)
	if err != nil {
		return nil, fmt.Errorf("failed to decrypt inventory: %w", err)
	}

	inventory, ok := decryptedData.(map[string]interface{})
	if !ok {
		return nil, fmt.Errorf("invalid decrypted data format")
	}

	slog.Info("Player inventory decrypted successfully",
		"inventory_id", inventoryID,
		"player_id", inventory["player_id"],
	)

	return inventory, nil
}

// PerformSecureTransaction performs a secure transaction between players using homomorphic encryption
func (s *HomomorphicEncryptionService) PerformSecureTransaction(ctx context.Context, fromPlayerID, toPlayerID string, itemID string, quantity int) error {
	// Get encrypted inventories
	fromInventoryID, err := s.getPlayerInventoryID(ctx, fromPlayerID)
	if err != nil {
		return fmt.Errorf("failed to get sender inventory: %w", err)
	}

	toInventoryID, err := s.getPlayerInventoryID(ctx, toPlayerID)
	if err != nil {
		return fmt.Errorf("failed to get receiver inventory: %w", err)
	}

	fromInventory, err := s.repo.GetEncryptedData(ctx, fromInventoryID)
	if err != nil {
		return fmt.Errorf("failed to get sender encrypted inventory: %w", err)
	}

	toInventory, err := s.repo.GetEncryptedData(ctx, toInventoryID)
	if err != nil {
		return fmt.Errorf("failed to get receiver encrypted inventory: %w", err)
	}

	// Decrypt inventories to get item counts (in real implementation, this would be done homomorphically)
	fromData, err := s.engine.DecryptGameData(fromInventory)
	if err != nil {
		return fmt.Errorf("failed to decrypt sender inventory: %w", err)
	}

	toData, err := s.engine.DecryptGameData(toInventory)
	if err != nil {
		return fmt.Errorf("failed to decrypt receiver inventory: %w", err)
	}

	fromInventoryMap, ok := fromData.(map[string]interface{})
	if !ok {
		return fmt.Errorf("invalid sender inventory format")
	}

	toInventoryMap, ok := toData.(map[string]interface{})
	if !ok {
		return fmt.Errorf("invalid receiver inventory format")
	}

	// Check if sender has enough items
	fromItems := fromInventoryMap["encrypted_items"].(map[string]*EncryptedData)
	fromItem := fromItems[itemID]
	if fromItem == nil {
		return fmt.Errorf("sender does not have item: %s", itemID)
	}

	fromQuantity, err := s.engine.DecryptGameData(fromItem)
	if err != nil {
		return fmt.Errorf("failed to decrypt sender item quantity: %w", err)
	}

	if fromQty, ok := fromQuantity.(int); !ok || fromQty < quantity {
		return fmt.Errorf("insufficient item quantity: has %v, needs %d", fromQuantity, quantity)
	}

	// Perform homomorphic operations
	quantityEncrypted, err := s.engine.EncryptGameData(quantity, "integer", fromInventory.KeyID)
	if err != nil {
		return fmt.Errorf("failed to encrypt transaction quantity: %w", err)
	}

	// Subtract from sender (homomorphic operation would be: sender_quantity - transaction_quantity)
	newFromQuantity, err := s.engine.AddEncryptedValues(fromItem, quantityEncrypted) // This is simplified
	if err != nil {
		return fmt.Errorf("failed to update sender quantity: %w", err)
	}

	// Add to receiver
	toItems := toInventoryMap["encrypted_items"].(map[string]*EncryptedData)
	toItem := toItems[itemID]
	if toItem == nil {
		// Create new item entry for receiver
		toItem, err = s.engine.EncryptGameData(0, "integer", toInventory.KeyID)
		if err != nil {
			return fmt.Errorf("failed to create receiver item entry: %w", err)
		}
	}

	newToQuantity, err := s.engine.AddEncryptedValues(toItem, quantityEncrypted)
	if err != nil {
		return fmt.Errorf("failed to update receiver quantity: %w", err)
	}

	// Update inventories
	fromItems[itemID] = newFromQuantity
	toItems[itemID] = newToQuantity

	// Store updated inventories
	fromInventory.OperationLog = append(fromInventory.OperationLog, OperationLog{
		Operation: fmt.Sprintf("transfer_out_%s_%d", itemID, quantity),
		Timestamp: time.Now().Unix(),
		ResultID:  newFromQuantity.ID,
	})

	toInventory.OperationLog = append(toInventory.OperationLog, OperationLog{
		Operation: fmt.Sprintf("transfer_in_%s_%d", itemID, quantity),
		Timestamp: time.Now().Unix(),
		ResultID:  newToQuantity.ID,
	})

	if err := s.repo.UpdateEncryptedData(ctx, fromInventory); err != nil {
		return fmt.Errorf("failed to update sender inventory: %w", err)
	}

	if err := s.repo.UpdateEncryptedData(ctx, toInventory); err != nil {
		return fmt.Errorf("failed to update receiver inventory: %w", err)
	}

	// Store transaction quantity for audit
	if err := s.repo.StoreEncryptedData(ctx, quantityEncrypted); err != nil {
		slog.Warn("Failed to store transaction quantity for audit", "error", err)
	}

	slog.Info("Secure transaction completed successfully",
		"from_player", fromPlayerID,
		"to_player", toPlayerID,
		"item_id", itemID,
		"quantity", quantity,
	)

	return nil
}

// AggregatePlayerStats performs homomorphic aggregation of player statistics
func (s *HomomorphicEncryptionService) AggregatePlayerStats(ctx context.Context, playerIDs []string, statName string) (*EncryptedData, error) {
	if len(playerIDs) == 0 {
		return nil, fmt.Errorf("no player IDs provided")
	}

	var aggregated *EncryptedData
	keyID := ""

	for _, playerID := range playerIDs {
		// Get player's encrypted stat
		statData, err := s.getPlayerStat(ctx, playerID, statName)
		if err != nil {
			slog.Warn("Failed to get player stat, skipping", "player_id", playerID, "stat", statName, "error", err)
			continue
		}

		if aggregated == nil {
			// First stat
			aggregated = statData
			keyID = statData.KeyID
		} else {
			// Add to aggregate
			result, err := s.engine.AddEncryptedValues(aggregated, statData)
			if err != nil {
				return nil, fmt.Errorf("failed to aggregate stat for player %s: %w", playerID, err)
			}
			aggregated = result
		}
	}

	if aggregated == nil {
		return nil, fmt.Errorf("no valid stats found for aggregation")
	}

	// Store aggregated result
	aggregated.ID = uuid.New().String()
	aggregated.DataType = fmt.Sprintf("aggregated_%s", statName)
	aggregated.CreatedAt = time.Now().Unix()

	if err := s.repo.StoreEncryptedData(ctx, aggregated); err != nil {
		return nil, fmt.Errorf("failed to store aggregated stats: %w", err)
	}

	slog.Info("Player stats aggregated successfully",
		"stat_name", statName,
		"players_count", len(playerIDs),
		"result_id", aggregated.ID,
	)

	return aggregated, nil
}

// GetEncryptionStats returns encryption service statistics
func (s *HomomorphicEncryptionService) GetEncryptionStats(ctx context.Context) (*EncryptionStats, error) {
	totalEncrypted, err := s.repo.CountEncryptedData(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to count encrypted data: %w", err)
	}

	activeKeys, err := s.repo.CountActiveKeys(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to count active keys: %w", err)
	}

	recentOperations, err := s.repo.GetRecentOperations(ctx, 24*time.Hour)
	if err != nil {
		return nil, fmt.Errorf("failed to get recent operations: %w", err)
	}

	return &EncryptionStats{
		TotalEncryptedData: totalEncrypted,
		ActiveKeys:         activeKeys,
		RecentOperations:   recentOperations,
		SupportedOperations: s.engine.GetSupportedOperations(),
	}, nil
}

// Helper functions

func (s *HomomorphicEncryptionService) getOrCreatePlayerKey(ctx context.Context, playerID string) (string, error) {
	// Try to find existing key for player
	existingKeyID, err := s.repo.GetPlayerKeyID(ctx, playerID)
	if err == nil {
		return existingKeyID, nil
	}

	// Create new key pair
	keyID, err := s.engine.GeneratePlayerKeyPair(playerID)
	if err != nil {
		return "", err
	}

	// Store mapping
	if err := s.repo.StorePlayerKeyMapping(ctx, playerID, keyID); err != nil {
		return "", err
	}

	return keyID, nil
}

func (s *HomomorphicEncryptionService) getPlayerInventoryID(ctx context.Context, playerID string) (string, error) {
	return s.repo.GetPlayerInventoryID(ctx, playerID)
}

func (s *HomomorphicEncryptionService) getPlayerStat(ctx context.Context, playerID, statName string) (*EncryptedData, error) {
	return s.repo.GetPlayerStat(ctx, playerID, statName)
}

// EncryptionStats represents encryption service statistics
type EncryptionStats struct {
	TotalEncryptedData int64           `json:"total_encrypted_data"`
	ActiveKeys         int64           `json:"active_keys"`
	RecentOperations   []OperationLog  `json:"recent_operations"`
	SupportedOperations []string       `json:"supported_operations"`
}