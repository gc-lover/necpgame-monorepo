package models

import (
	"time"

	"github.com/google/uuid"
)

type PlayerAccount struct {
	ID         uuid.UUID  `json:"id" db:"id"`
	ExternalID *string    `json:"external_id,omitempty" db:"external_id"`
	Nickname   string     `json:"nickname" db:"nickname"`
	OriginCode *string    `json:"origin_code,omitempty" db:"origin_code"`
	CreatedAt  time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt  time.Time  `json:"updated_at" db:"updated_at"`
	DeletedAt  *time.Time `json:"deleted_at,omitempty" db:"deleted_at"`
}

type Character struct {
	ID          uuid.UUID  `json:"id" db:"id"`
	AccountID   uuid.UUID  `json:"account_id" db:"account_id"`
	Name        string     `json:"name" db:"name"`
	ClassCode   *string    `json:"class_code,omitempty" db:"class_code"`
	FactionCode *string    `json:"faction_code,omitempty" db:"faction_code"`
	Level       int        `json:"level" db:"level"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time  `json:"updated_at" db:"updated_at"`
	DeletedAt   *time.Time `json:"deleted_at,omitempty" db:"deleted_at"`
}

type CreateAccountRequest struct {
	ExternalID *string `json:"external_id,omitempty"`
	Nickname   string  `json:"nickname"`
	OriginCode *string `json:"origin_code,omitempty"`
}

type CreateCharacterRequest struct {
	AccountID   uuid.UUID `json:"account_id"`
	Name        string    `json:"name"`
	ClassCode   *string   `json:"class_code,omitempty"`
	FactionCode *string   `json:"faction_code,omitempty"`
	Level       *int      `json:"level,omitempty"`
}

type UpdateCharacterRequest struct {
	Name        *string `json:"name,omitempty"`
	ClassCode   *string `json:"class_code,omitempty"`
	FactionCode *string `json:"faction_code,omitempty"`
	Level       *int    `json:"level,omitempty"`
}

type CharacterListResponse struct {
	Characters []Character `json:"characters"`
	Total      int         `json:"total"`
}

type SwitchCharacterRequest struct {
	AccountID   uuid.UUID `json:"account_id"`
	CharacterID uuid.UUID `json:"character_id"`
}

type SwitchCharacterResponse struct {
	PreviousCharacterID *uuid.UUID `json:"previous_character_id,omitempty"`
	CurrentCharacter    *Character `json:"current_character"`
	Success             bool       `json:"success"`
}
