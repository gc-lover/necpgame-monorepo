package server

import (
	"time"

	"github.com/google/uuid"
)

// OPTIMIZATION: Issue #1950 - Memory-aligned structs for performance (large → small)
type CharacterInventory struct {
	CharacterID string                      `json:"character_id"` // 16 bytes
	Containers  map[string]*InventoryContainer `json:"containers"`  // 8 bytes (map)
	LastUpdated time.Time                   `json:"last_updated"` // 24 bytes
}

// OPTIMIZATION: Issue #1950 - Memory-aligned container struct
type InventoryContainer struct {
	ContainerID string                 `json:"container_id"` // 16 bytes
	Name        string                 `json:"name"`         // 16 bytes
	Type        string                 `json:"type"`         // 16 bytes
	Capacity    int                    `json:"capacity"`     // 8 bytes
	UsedSlots   int                    `json:"used_slots"`   // 8 bytes
	Rows        int                    `json:"rows"`         // 8 bytes
	Columns     int                    `json:"columns"`      // 8 bytes
	Items       map[string]*InventoryItem `json:"items"`       // 8 bytes (map)
	IsLocked    bool                   `json:"is_locked"`    // 1 byte
}

// OPTIMIZATION: Issue #1950 - Memory-aligned item struct
type InventoryItem struct {
	InventoryItemID string    `json:"inventory_item_id"` // 16 bytes
	ItemID          string    `json:"item_id"`           // 16 bytes
	CharacterID     string    `json:"character_id"`      // 16 bytes
	Container       string    `json:"container"`         // 16 bytes
	SlotX           int       `json:"slot_x"`            // 8 bytes
	SlotY           int       `json:"slot_y"`            // 8 bytes
	Quantity        int       `json:"quantity"`          // 8 bytes
	Durability      int       `json:"durability"`        // 8 bytes
	MaxDurability   int       `json:"max_durability"`    // 8 bytes
	IsEquipped      bool      `json:"is_equipped"`       // 1 byte
	IsLocked        bool      `json:"is_locked"`         // 1 byte
	AcquiredAt      time.Time `json:"acquired_at"`       // 24 bytes
	ExpiresAt       *time.Time `json:"expires_at,omitempty"` // 8 bytes (pointer)
}

// OPTIMIZATION: Issue #1950 - Memory-aligned equipment struct
type CharacterEquipment struct {
	CharacterID string                    `json:"character_id"` // 16 bytes
	Slots       map[string]*EquipmentSlot `json:"slots"`        // 8 bytes (map)
	LastUpdated time.Time                 `json:"last_updated"` // 24 bytes
}

// OPTIMIZATION: Issue #1950 - Memory-aligned equipment slot
type EquipmentSlot struct {
	SlotType         string `json:"slot_type"`          // 16 bytes
	InventoryItemID  string `json:"inventory_item_id"`  // 16 bytes
	ItemID           string `json:"item_id"`            // 16 bytes
	EquippedAt       time.Time `json:"equipped_at"`     // 24 bytes
}

// OPTIMIZATION: Issue #1950 - Memory-aligned item definition
type ItemDefinition struct {
	ItemID          string            `json:"item_id"`           // 16 bytes
	Name            string            `json:"name"`              // 16 bytes
	Description     string            `json:"description"`       // 16 bytes
	ItemType        string            `json:"item_type"`         // 16 bytes
	Rarity          string            `json:"rarity"`            // 16 bytes
	LevelReq        int               `json:"level_req"`         // 8 bytes
	MaxStack        int               `json:"max_stack"`         // 8 bytes
	SellPrice       int               `json:"sell_price"`        // 8 bytes
	BuyPrice        int               `json:"buy_price"`         // 8 bytes
	Stats           map[string]interface{} `json:"stats"`        // 8 bytes (map)
	Effects         []*ItemEffect     `json:"effects"`           // 24 bytes (slice)
	IconURL         string            `json:"icon_url"`          // 16 bytes
	ModelURL        string            `json:"model_url"`         // 16 bytes
}

// OPTIMIZATION: Issue #1950 - Memory-aligned item effect
type ItemEffect struct {
	EffectType  string      `json:"effect_type"`  // 16 bytes
	TargetStat  string      `json:"target_stat"`  // 16 bytes
	Value       interface{} `json:"value"`        // 16 bytes (interface)
	Duration    int         `json:"duration"`     // 8 bytes
	Conditions  []string    `json:"conditions"`   // 24 bytes (slice)
}

// OPTIMIZATION: Issue #1950 - Memory-aligned equipment stats
type EquipmentStats struct {
	TotalStats      map[string]interface{} `json:"total_stats"`       // 8 bytes (map)
	ActiveBonuses   []*ItemEffect          `json:"active_bonuses"`    // 24 bytes (slice)
	SetBonuses      map[string]string      `json:"set_bonuses"`       // 8 bytes (map)
	DefenseRating   int                    `json:"defense_rating"`    // 8 bytes
	MagicResistance int                    `json:"magic_resistance"`  // 8 bytes
	AttackPower     int                    `json:"attack_power"`      // 8 bytes
	SpellPower      int                    `json:"spell_power"`       // 8 bytes
}

// Vector3 for 3D positioning
type Vector3 struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// Request structs
type MoveItemRequest struct {
	InventoryItemID string `json:"inventory_item_id"`
	FromContainer   string `json:"from_container"`
	ToContainer     string `json:"to_container"`
	ToSlotX         int    `json:"to_slot_x"`
	ToSlotY         int    `json:"to_slot_y"`
	Quantity        int    `json:"quantity"`
}

type EquipItemRequest struct {
	InventoryItemID string `json:"inventory_item_id"`
	SlotType        string `json:"slot_type"`
}

type UnequipItemRequest struct {
	SlotType       string `json:"slot_type"`
	TargetContainer string `json:"target_container"`
}

type UseItemRequest struct {
	InventoryItemID   string `json:"inventory_item_id"`
	TargetCharacterID string `json:"target_character_id,omitempty"`
	Quantity          int    `json:"quantity"`
}

type DropItemRequest struct {
	InventoryItemID string   `json:"inventory_item_id"`
	Quantity        int      `json:"quantity"`
	Position        *Vector3 `json:"position"`
}

type SearchItemsRequest struct {
	Query     string `json:"query"`
	ItemType  string `json:"item_type,omitempty"`
	Rarity    string `json:"rarity,omitempty"`
	MinLevel  int    `json:"min_level,omitempty"`
	MaxLevel  int    `json:"max_level,omitempty"`
	Limit     int    `json:"limit,omitempty"`
	Offset    int    `json:"offset,omitempty"`
}

// Response structs for memory pooling
type GetInventoryResponse struct {
	CharacterID  string                `json:"character_id"`
	Containers   []*InventoryContainer `json:"containers"`
	TotalItems   int                   `json:"total_items"`
	TotalWeight  int                   `json:"total_weight"`
	MaxWeight    int                   `json:"max_weight"`
}

type ListItemsResponse struct {
	Items      []*InventoryItem `json:"items"`
	TotalCount int              `json:"total_count"`
	Limit      int              `json:"limit"`
	Offset     int              `json:"offset"`
}

type MoveItemResponse struct {
	InventoryItemID string `json:"inventory_item_id"`
	OldContainer    string `json:"old_container"`
	OldSlotX        int    `json:"old_slot_x"`
	OldSlotY        int    `json:"old_slot_y"`
	NewContainer    string `json:"new_container"`
	NewSlotX        int    `json:"new_slot_x"`
	NewSlotY        int    `json:"new_slot_y"`
	QuantityMoved   int    `json:"quantity_moved"`
	Success         bool   `json:"success"`
}

type EquipItemResponse struct {
	InventoryItemID string `json:"inventory_item_id"`
	SlotType        string `json:"slot_type"`
	OldItemID       string `json:"old_item_id,omitempty"`
	Success         bool   `json:"success"`
	Message         string `json:"message"`
}

type UnequipItemResponse struct {
	SlotType         string `json:"slot_type"`
	InventoryItemID  string `json:"inventory_item_id"`
	Container        string `json:"container"`
	SlotX            int    `json:"slot_x"`
	SlotY            int    `json:"slot_y"`
	Success          bool   `json:"success"`
	Message          string `json:"message"`
}

type UseItemResponse struct {
	InventoryItemID  string        `json:"inventory_item_id"`
	QuantityUsed     int           `json:"quantity_used"`
	EffectsApplied   []*ItemEffect `json:"effects_applied"`
	RemainingQuantity int          `json:"remaining_quantity"`
	Success          bool          `json:"success"`
	Message          string `json:"message"`
}

type DropItemResponse struct {
	InventoryItemID   string   `json:"inventory_item_id"`
	QuantityDropped   int      `json:"quantity_dropped"`
	Position          *Vector3 `json:"position"`
	RemainingQuantity int      `json:"remaining_quantity"`
	Success           bool     `json:"success"`
	Message           string   `json:"message"`
}

type GetItemResponse struct {
	Item *ItemDefinition `json:"item"`
}

type SearchItemsResponse struct {
	Items        []*ItemDefinition `json:"items"`
	TotalCount   int               `json:"total_count"`
	Query        string            `json:"query"`
	SearchTimeMs int               `json:"search_time_ms"`
}

type GetEquipmentResponse struct {
	CharacterID string                    `json:"character_id"`
	Equipment   map[string]*EquipmentSlot `json:"equipment"`
}

type EquipmentStatsResponse struct {
	CharacterID string          `json:"character_id"`
	Stats       *EquipmentStats `json:"stats"`
}
