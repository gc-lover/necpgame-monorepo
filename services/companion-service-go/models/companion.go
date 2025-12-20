package models

import (
	"time"

	"github.com/google/uuid"
)

type CompanionCategory string

type CompanionType struct {
	ID          string                 `json:"id" db:"id"`
	Category    CompanionCategory      `json:"category" db:"category"`
	Name        string                 `json:"name" db:"name"`
	Description string                 `json:"description" db:"description"`
	Stats       map[string]interface{} `json:"stats" db:"stats"`
	Abilities   []string               `json:"abilities" db:"abilities"`
	Cost        int64                  `json:"cost" db:"cost"`
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
}

type CompanionStatus string

type PlayerCompanion struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	CharacterID     uuid.UUID              `json:"character_id" db:"character_id"`
	CompanionTypeID string                 `json:"companion_type_id" db:"companion_type_id"`
	CustomName      *string                `json:"custom_name,omitempty" db:"custom_name"`
	Level           int                    `json:"level" db:"level"`
	Experience      int64                  `json:"experience" db:"experience"`
	Status          CompanionStatus        `json:"status" db:"status"`
	Equipment       map[string]interface{} `json:"equipment" db:"equipment"`
	Stats           map[string]interface{} `json:"stats" db:"stats"`
	SummonedAt      *time.Time             `json:"summoned_at,omitempty" db:"summoned_at"`
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

type CompanionAbility struct {
	ID                uuid.UUID  `json:"id" db:"id"`
	PlayerCompanionID uuid.UUID  `json:"player_companion_id" db:"player_companion_id"`
	AbilityID         string     `json:"ability_id" db:"ability_id"`
	IsActive          bool       `json:"is_active" db:"is_active"`
	CooldownUntil     *time.Time `json:"cooldown_until,omitempty" db:"cooldown_until"`
	LastUsedAt        *time.Time `json:"last_used_at,omitempty" db:"last_used_at"`
	UpdatedAt         time.Time  `json:"updated_at" db:"updated_at"`
}

type PurchaseCompanionRequest struct {
	CharacterID     uuid.UUID `json:"character_id"`
	CompanionTypeID string    `json:"companion_type_id"`
}

type SummonCompanionRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	CompanionID uuid.UUID `json:"companion_id"`
}

type DismissCompanionRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	CompanionID uuid.UUID `json:"companion_id"`
}

type RenameCompanionRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	CompanionID uuid.UUID `json:"companion_id"`
	CustomName  string    `json:"custom_name"`
}

type AddExperienceRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	CompanionID uuid.UUID `json:"companion_id"`
	Amount      int64     `json:"amount"`
	Source      string    `json:"source"`
}

type UseAbilityRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	CompanionID uuid.UUID `json:"companion_id"`
	AbilityID   string    `json:"ability_id"`
}

type CompanionTypeListResponse struct {
	Types []CompanionType `json:"types"`
	Total int             `json:"total"`
}

type PlayerCompanionListResponse struct {
	Companions []PlayerCompanion `json:"companions"`
	Total      int               `json:"total"`
}

type CompanionDetailResponse struct {
	Companion *PlayerCompanion   `json:"companion"`
	Type      *CompanionType     `json:"type,omitempty"`
	Abilities []CompanionAbility `json:"abilities,omitempty"`
}
