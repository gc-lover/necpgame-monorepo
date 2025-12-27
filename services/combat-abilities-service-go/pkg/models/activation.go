package models

import (
	"time"

	"github.com/google/uuid"
)

type ActivationStatus string

const (
	ActivationStatusPending   ActivationStatus = "PENDING"
	ActivationStatusActive    ActivationStatus = "ACTIVE"
	ActivationStatusCompleted ActivationStatus = "COMPLETED"
	ActivationStatusFailed    ActivationStatus = "FAILED"
)

type AbilityActivation struct {
	ID                uuid.UUID        `json:"id"`
	AbilityID         uuid.UUID        `json:"ability_id"`
	CharacterID       uuid.UUID        `json:"character_id"`
	TargetEntityID    *uuid.UUID       `json:"target_entity_id,omitempty"`
	TargetPosition    Vector3          `json:"target_position"`
	SynergyAbilities  []uuid.UUID      `json:"synergy_abilities"`
	Status            ActivationStatus `json:"status"`
	ResourceCost      ResourceCost     `json:"resource_cost"`
	SynergyBonus      float64          `json:"synergy_bonus"`
	ValidationToken   string           `json:"validation_token"`
	ClientTimestamp   int64            `json:"client_timestamp"`
	ServerTimestamp   int64            `json:"server_timestamp"`
	CooldownEndTime   int64            `json:"cooldown_end_time"`
	CreatedAt         time.Time        `json:"created_at"`
	UpdatedAt         time.Time        `json:"updated_at"`
}

type Vector3 struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}
