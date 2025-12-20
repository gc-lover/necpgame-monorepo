package models

import (
	"time"

	"github.com/google/uuid"
)

type CosmeticCategory string

type CosmeticType string

type CosmeticRarity string

type CosmeticSlot string

type CosmeticEventType string

type CosmeticItem struct {
	ID            uuid.UUID              `json:"id" db:"id"`
	Code          string                 `json:"code" db:"code"`
	Name          string                 `json:"name" db:"name"`
	Category      CosmeticCategory       `json:"category" db:"category"`
	CosmeticType  CosmeticType           `json:"cosmetic_type" db:"cosmetic_type"`
	Rarity        CosmeticRarity         `json:"rarity" db:"rarity"`
	Description   string                 `json:"description,omitempty" db:"description"`
	Cost          int64                  `json:"cost,omitempty" db:"cost"`
	CurrencyType  string                 `json:"currency_type,omitempty" db:"currency_type"`
	IsExclusive   bool                   `json:"is_exclusive" db:"is_exclusive"`
	Source        string                 `json:"source,omitempty" db:"source"`
	VisualEffects map[string]interface{} `json:"visual_effects,omitempty" db:"visual_effects"`
	Assets        map[string]interface{} `json:"assets,omitempty" db:"assets"`
	IsActive      bool                   `json:"is_active" db:"is_active"`
	CreatedAt     time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time              `json:"updated_at" db:"updated_at"`
}

type PlayerCosmetic struct {
	ID             uuid.UUID     `json:"id" db:"id"`
	PlayerID       uuid.UUID     `json:"player_id" db:"player_id"`
	CosmeticItemID uuid.UUID     `json:"cosmetic_item_id" db:"cosmetic_item_id"`
	CosmeticItem   *CosmeticItem `json:"cosmetic_item,omitempty"`
	Source         string        `json:"source,omitempty" db:"source"`
	ObtainedAt     time.Time     `json:"obtained_at" db:"obtained_at"`
	TimesUsed      int           `json:"times_used" db:"times_used"`
	LastUsedAt     *time.Time    `json:"last_used_at,omitempty" db:"last_used_at"`
	CreatedAt      time.Time     `json:"created_at" db:"created_at"`
}

// EquippedCosmeticsBase represents player identification and timestamp
type EquippedCosmeticsBase struct {
	PlayerID  uuid.UUID `json:"player_id" db:"player_id"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`
}

// EquippedCharacter represents equipped character cosmetics
type EquippedCharacter struct {
	CharacterSkinID *uuid.UUID    `json:"character_skin_id,omitempty" db:"character_skin_id"`
	CharacterSkin   *CosmeticItem `json:"character_skin,omitempty"`
}

// EquippedWeapon represents equipped weapon cosmetics
type EquippedWeapon struct {
	WeaponSkinID *uuid.UUID    `json:"weapon_skin_id,omitempty" db:"weapon_skin_id"`
	WeaponSkin   *CosmeticItem `json:"weapon_skin,omitempty"`
}

// EquippedTitle represents equipped title cosmetic
type EquippedTitle struct {
	TitleID *uuid.UUID    `json:"title_id,omitempty" db:"title_id"`
	Title   *CosmeticItem `json:"title,omitempty"`
}

// EquippedNamePlate represents equipped name plate cosmetic
type EquippedNamePlate struct {
	NamePlateID *uuid.UUID    `json:"name_plate_id,omitempty" db:"name_plate_id"`
	NamePlate   *CosmeticItem `json:"name_plate,omitempty"`
}

// EquippedEmotes represents equipped emote cosmetics (4 slots)
type EquippedEmotes struct {
	Emote1ID *uuid.UUID    `json:"emote_1_id,omitempty" db:"emote_1_id"`
	Emote1   *CosmeticItem `json:"emote_1,omitempty"`
	Emote2ID *uuid.UUID    `json:"emote_2_id,omitempty" db:"emote_2_id"`
	Emote2   *CosmeticItem `json:"emote_2,omitempty"`
	Emote3ID *uuid.UUID    `json:"emote_3_id,omitempty" db:"emote_3_id"`
	Emote3   *CosmeticItem `json:"emote_3,omitempty"`
	Emote4ID *uuid.UUID    `json:"emote_4_id,omitempty" db:"emote_4_id"`
	Emote4   *CosmeticItem `json:"emote_4,omitempty"`
}

// EquippedCosmetics represents all equipped cosmetics for a player (composed of smaller structs)
type EquippedCosmetics struct {
	Base      EquippedCosmeticsBase `json:"base"`
	Character EquippedCharacter     `json:"character,omitempty"`
	Weapon    EquippedWeapon        `json:"weapon,omitempty"`
	Title     EquippedTitle         `json:"title,omitempty"`
	NamePlate EquippedNamePlate     `json:"name_plate,omitempty"`
	Emotes    EquippedEmotes        `json:"emotes,omitempty"`
}

type PurchaseCosmeticRequest struct {
	PlayerID   uuid.UUID `json:"player_id"`
	CosmeticID uuid.UUID `json:"cosmetic_id"`
}

type EquipCosmeticRequest struct {
	PlayerID uuid.UUID    `json:"player_id"`
	Slot     CosmeticSlot `json:"slot"`
}

type UnequipCosmeticRequest struct {
	PlayerID uuid.UUID    `json:"player_id"`
	Slot     CosmeticSlot `json:"slot"`
}

type CosmeticCatalogResponse struct {
	Items  []CosmeticItem `json:"items"`
	Total  int            `json:"total"`
	Limit  int            `json:"limit"`
	Offset int            `json:"offset"`
}

type CosmeticCategoryInfo struct {
	Category CosmeticCategory `json:"category"`
	Count    int              `json:"count"`
}

type CosmeticCategoriesResponse struct {
	Categories []CosmeticCategoryInfo `json:"categories"`
}

type DailyShop struct {
	RotationID     uuid.UUID      `json:"rotation_id"`
	RotationDate   time.Time      `json:"rotation_date"`
	Items          []CosmeticItem `json:"items"`
	NextRotationAt time.Time      `json:"next_rotation_at"`
}

type DailyShopResponse struct {
	RotationID     uuid.UUID      `json:"rotation_id"`
	RotationDate   time.Time      `json:"rotation_date"`
	Items          []CosmeticItem `json:"items"`
	NextRotationAt time.Time      `json:"next_rotation_at"`
}

type ShopRotation struct {
	ID           uuid.UUID      `json:"id"`
	RotationDate time.Time      `json:"rotation_date"`
	Items        []CosmeticItem `json:"items"`
	CreatedAt    time.Time      `json:"created_at"`
}

type ShopHistoryResponse struct {
	Rotations []ShopRotation `json:"rotations"`
	Total     int            `json:"total"`
	Limit     int            `json:"limit"`
	Offset    int            `json:"offset"`
}

type PurchaseRecord struct {
	ID             uuid.UUID     `json:"id" db:"id"`
	PlayerID       uuid.UUID     `json:"player_id" db:"player_id"`
	CosmeticItemID uuid.UUID     `json:"cosmetic_item_id" db:"cosmetic_item_id"`
	CosmeticItem   *CosmeticItem `json:"cosmetic_item,omitempty"`
	Cost           int64         `json:"cost" db:"cost"`
	CurrencyType   string        `json:"currency_type" db:"currency_type"`
	PurchasedAt    time.Time     `json:"purchased_at" db:"purchased_at"`
}

type PurchaseHistoryResponse struct {
	Purchases []PurchaseRecord `json:"purchases"`
	Total     int              `json:"total"`
	Limit     int              `json:"limit"`
	Offset    int              `json:"offset"`
}

type CosmeticInventoryResponse struct {
	PlayerID  uuid.UUID        `json:"player_id"`
	Cosmetics []PlayerCosmetic `json:"cosmetics"`
	Total     int              `json:"total"`
	Limit     int              `json:"limit"`
	Offset    int              `json:"offset"`
}

type OwnershipStatusResponse struct {
	PlayerID       uuid.UUID       `json:"player_id"`
	CosmeticID     uuid.UUID       `json:"cosmetic_id"`
	Owned          bool            `json:"owned"`
	PlayerCosmetic *PlayerCosmetic `json:"player_cosmetic,omitempty"`
}

type CosmeticEvent struct {
	ID         uuid.UUID              `json:"id" db:"id"`
	PlayerID   uuid.UUID              `json:"player_id" db:"player_id"`
	EventType  CosmeticEventType      `json:"event_type" db:"event_type"`
	CosmeticID *uuid.UUID             `json:"cosmetic_id,omitempty" db:"cosmetic_id"`
	EventData  map[string]interface{} `json:"event_data,omitempty" db:"event_data"`
	CreatedAt  time.Time              `json:"created_at" db:"created_at"`
}

type CosmeticEventsResponse struct {
	Events []CosmeticEvent `json:"events"`
	Total  int             `json:"total"`
	Limit  int             `json:"limit"`
	Offset int             `json:"offset"`
}
