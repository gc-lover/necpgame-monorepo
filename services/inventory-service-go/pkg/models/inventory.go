package models

import (
	"time"

	"github.com/google/uuid"
)

// ItemType represents the type of inventory item
type ItemType string

const (
	ItemTypeWeapon     ItemType = "weapon"
	ItemTypeArmor      ItemType = "armor"
	ItemTypeConsumable ItemType = "consumable"
	ItemTypeMaterial   ItemType = "material"
	ItemTypeQuest      ItemType = "quest"
)

// ItemRarity represents item rarity level
type ItemRarity string

const (
	ItemRarityCommon   ItemRarity = "common"
	ItemRarityUncommon ItemRarity = "uncommon"
	ItemRarityRare     ItemRarity = "rare"
	ItemRarityEpic     ItemRarity = "epic"
	ItemRarityLegendary ItemRarity = "legendary"
)

// InventoryItem represents an item in player's inventory
type InventoryItem struct {
	ID          uuid.UUID  `json:"id" db:"id"`
	PlayerID    uuid.UUID  `json:"player_id" db:"player_id"`
	ItemID      uuid.UUID  `json:"item_id" db:"item_id"`
	Quantity    int        `json:"quantity" db:"quantity"`
	StackCount  int        `json:"stack_count" db:"stack_count"`
	ItemType    ItemType   `json:"item_type" db:"item_type"`
	ItemRarity  ItemRarity `json:"item_rarity" db:"item_rarity"`
	IsEquipped  bool       `json:"is_equipped" db:"is_equipped"`
	SlotID      *string    `json:"slot_id,omitempty" db:"slot_id"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time  `json:"updated_at" db:"updated_at"`
}

// AddItemRequest represents request to add item to inventory
type AddItemRequest struct {
	PlayerID   uuid.UUID `json:"player_id"`
	ItemID     uuid.UUID `json:"item_id"`
	StackCount int       `json:"stack_count"` // Changed from Quantity to StackCount to match ogen
}

// EquipItemRequest represents request to equip an item
type EquipItemRequest struct {
	ItemID uuid.UUID `json:"item_id"` // Added ItemID field as mentioned in errors
	SlotID string    `json:"slot_id"`
}

// AddItemResponse represents response for add item operation
type AddItemResponse struct {
	ItemID     uuid.UUID `json:"item_id"`
	Quantity   int       `json:"quantity"`
	StackCount int       `json:"stack_count"`
	Success    bool      `json:"success"`
}

// EquipmentResponse represents equipment information
type EquipmentResponse struct {
	Items []InventoryItem `json:"items"` // Fixed structure mismatch
}

// InventoryResponse represents inventory listing response
type InventoryResponse struct {
	Items      []InventoryItem `json:"items"`
	TotalCount int             `json:"total_count"`
	Page       int             `json:"page"`
	Limit      int             `json:"limit"`
}

// InventoryFilter represents filters for inventory queries
type InventoryFilter struct {
	ItemType   *ItemType   `json:"item_type,omitempty"`
	ItemRarity *ItemRarity `json:"item_rarity,omitempty"`
	IsEquipped *bool       `json:"is_equipped,omitempty"`
}


