package server

import "time"

type Vector3 struct {
	X float64 `json:"x,omitempty"`
	Y float64 `json:"y,omitempty"`
	Z float64 `json:"z,omitempty"`
}

type AirDashRequest struct {
	CharacterID         string   `json:"character_id"`
	Direction           *Vector3 `json:"direction,omitempty"`
	StaminaCost         int      `json:"stamina_cost,omitempty"`
	AllowAttackDuringDash bool   `json:"allow_attack_during_dash,omitempty"`
}

type AirDashState struct {
	CharacterID      string    `json:"character_id"`
	CurrentCharges   int       `json:"current_charges"`
	MaxCharges       int       `json:"max_charges"`
	CooldownUntil    *time.Time `json:"cooldown_until,omitempty"`
	LastUsedAt       *time.Time `json:"last_used_at,omitempty"`
	LastUsedPosition *Vector3   `json:"last_used_position,omitempty"`
	LastUsedDirection *Vector3  `json:"last_used_direction,omitempty"`
	StaminaConsumed  int        `json:"stamina_consumed,omitempty"`
}

type WallKickRequest struct {
	CharacterID string   `json:"character_id"`
	Direction   *Vector3 `json:"direction,omitempty"`
	ChainCount  int      `json:"chain_count,omitempty"`
}

type WallKickState struct {
	CharacterID      string    `json:"character_id"`
	IsAvailable      bool      `json:"is_available"`
	ChainCount       int       `json:"chain_count"`
	MaxChainCount    int       `json:"max_chain_count"`
	LastUsedAt       *time.Time `json:"last_used_at,omitempty"`
	LastUsedPosition *Vector3   `json:"last_used_position,omitempty"`
	LastUsedDirection *Vector3  `json:"last_used_direction,omitempty"`
	StaminaConsumed  int        `json:"stamina_consumed,omitempty"`
}

type VaultRequest struct {
	CharacterID string   `json:"character_id"`
	ObstacleID  string   `json:"obstacle_id,omitempty"`
	ManualMode  bool     `json:"manual_mode,omitempty"`
	Direction   *Vector3 `json:"direction,omitempty"`
}

type VaultState struct {
	CharacterID    string     `json:"character_id"`
	IsActive       bool       `json:"is_active"`
	ObstacleID     string     `json:"obstacle_id,omitempty"`
	StartPosition  *Vector3   `json:"start_position,omitempty"`
	CurrentPosition *Vector3  `json:"current_position,omitempty"`
	Direction      *Vector3   `json:"direction,omitempty"`
	StaminaConsumed int       `json:"stamina_consumed,omitempty"`
	StartedAt      *time.Time `json:"started_at,omitempty"`
	CompletedAt    *time.Time `json:"completed_at,omitempty"`
}

type Obstacle struct {
	ID              string    `json:"id"`
	ObstacleType    string    `json:"obstacle_type"`
	Position        *Vector3  `json:"position,omitempty"`
	Dimensions      *Vector3  `json:"dimensions,omitempty"`
	Material        string    `json:"material,omitempty"`
	IsSuitableForVault bool   `json:"is_suitable_for_vault,omitempty"`
	DetectedAt      *time.Time `json:"detected_at,omitempty"`
}

type AdvancedAcrobaticsState struct {
	CharacterID string       `json:"character_id"`
	AirDash     AirDashState `json:"air_dash"`
	WallKick    WallKickState `json:"wall_kick"`
	Vault       VaultState   `json:"vault"`
}

