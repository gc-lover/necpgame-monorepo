package models

import (
	"time"

	"github.com/google/uuid"
)

type Inventory struct {
	ID          uuid.UUID `json:"id" db:"id"`
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`
	Capacity    int       `json:"capacity" db:"capacity"`
	UsedSlots   int       `json:"used_slots" db:"used_slots"`
	Weight      float64   `json:"weight" db:"weight"`
	MaxWeight   float64   `json:"max_weight" db:"max_weight"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

type InventoryItem struct {
	ID           uuid.UUID              `json:"id" db:"id"`
	InventoryID  uuid.UUID              `json:"inventory_id" db:"inventory_id"`
	ItemID       string                 `json:"item_id" db:"item_id"`
	SlotIndex    int                    `json:"slot_index" db:"slot_index"`
	StackCount   int                    `json:"stack_count" db:"stack_count"`
	MaxStackSize int                    `json:"max_stack_size" db:"max_stack_size"`
	IsEquipped   bool                   `json:"is_equipped" db:"is_equipped"`
	EquipSlot    string                 `json:"equip_slot,omitempty" db:"equip_slot"`
	Metadata     map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time              `json:"updated_at" db:"updated_at"`
}

type ItemTemplate struct {
	ID           string                 `json:"id" db:"id"`
	Name         string                 `json:"name" db:"name"`
	Type         string                 `json:"type" db:"type"`
	Rarity       string                 `json:"rarity" db:"rarity"`
	MaxStackSize int                    `json:"max_stack_size" db:"max_stack_size"`
	Weight       float64                `json:"weight" db:"weight"`
	CanEquip     bool                   `json:"can_equip" db:"can_equip"`
	EquipSlot    string                 `json:"equip_slot,omitempty" db:"equip_slot"`
	Requirements map[string]interface{} `json:"requirements,omitempty" db:"requirements"`
	Stats        map[string]interface{} `json:"stats,omitempty" db:"stats"`
	Metadata     map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
}

type AddItemRequest struct {
	ItemID     string `json:"item_id"`
	StackCount int    `json:"stack_count"`
}

type MoveItemRequest struct {
	ItemID   string `json:"item_id"`
	FromSlot int    `json:"from_slot,omitempty"`
	ToSlot   int    `json:"to_slot,omitempty"`
}

type EquipItemRequest struct {
	ItemID    string `json:"item_id"`
	EquipSlot string `json:"equip_slot"`
}

type InventoryResponse struct {
	Inventory Inventory       `json:"inventory"`
	Items     []InventoryItem `json:"items"`
}
