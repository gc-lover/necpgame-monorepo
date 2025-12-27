package models

import (
	"time"

	"github.com/google/uuid"
)

type CharacterAbility struct {
	ID         uuid.UUID `json:"id"`
	CharacterID uuid.UUID `json:"character_id"`
	AbilityID  uuid.UUID `json:"ability_id"`
	Slot       AbilitySlot `json:"slot"`
	IsEquipped bool       `json:"is_equipped"`
	IsUnlocked bool       `json:"is_unlocked"`
	CooldownRemaining int `json:"cooldown_remaining_ms"`
	LastUsed   *time.Time `json:"last_used,omitempty"`
	CreatedAt  time.Time  `json:"created_at"`
	UpdatedAt  time.Time  `json:"updated_at"`
}
