package models

import (
	"time"

	"github.com/google/uuid"
)

type WarPhase string

const (
	WarPhasePreparation WarPhase = "preparation"
	WarPhaseActive      WarPhase = "active"
	WarPhaseCompleted   WarPhase = "completed"
	WarPhaseCancelled   WarPhase = "cancelled"
)

type WarStatus string

const (
	WarStatusDeclared  WarStatus = "declared"
	WarStatusOngoing   WarStatus = "ongoing"
	WarStatusCompleted WarStatus = "completed"
	WarStatusCancelled WarStatus = "cancelled"
)

type BattleType string

const (
	BattleTypeTerritory BattleType = "territory"
	BattleTypeSiege     BattleType = "siege"
)

type BattleStatus string

const (
	BattleStatusScheduled BattleStatus = "scheduled"
	BattleStatusActive    BattleStatus = "active"
	BattleStatusCompleted BattleStatus = "completed"
)

type ClanWar struct {
	ID              uuid.UUID   `json:"id" db:"id"`
	AttackerGuildID uuid.UUID   `json:"attacker_guild_id" db:"attacker_guild_id"`
	DefenderGuildID uuid.UUID   `json:"defender_guild_id" db:"defender_guild_id"`
	Allies          []uuid.UUID `json:"allies" db:"allies"`
	Status          WarStatus   `json:"status" db:"status"`
	Phase           WarPhase    `json:"phase" db:"phase"`
	TerritoryID     *uuid.UUID  `json:"territory_id,omitempty" db:"territory_id"`
	AttackerScore   int         `json:"attacker_score" db:"attacker_score"`
	DefenderScore   int         `json:"defender_score" db:"defender_score"`
	WinnerGuildID   *uuid.UUID  `json:"winner_guild_id,omitempty" db:"winner_guild_id"`
	StartTime       time.Time   `json:"start_time" db:"start_time"`
	EndTime         *time.Time  `json:"end_time,omitempty" db:"end_time"`
	CreatedAt       time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time   `json:"updated_at" db:"updated_at"`
}

type WarBattle struct {
	ID            uuid.UUID    `json:"id" db:"id"`
	WarID         uuid.UUID    `json:"war_id" db:"war_id"`
	Type          BattleType   `json:"type" db:"type"`
	TerritoryID   *uuid.UUID   `json:"territory_id,omitempty" db:"territory_id"`
	Status        BattleStatus `json:"status" db:"status"`
	AttackerScore int          `json:"attacker_score" db:"attacker_score"`
	DefenderScore int          `json:"defender_score" db:"defender_score"`
	StartTime     time.Time    `json:"start_time" db:"start_time"`
	EndTime       *time.Time   `json:"end_time,omitempty" db:"end_time"`
	CreatedAt     time.Time    `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time    `json:"updated_at" db:"updated_at"`
}

type Territory struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	Name            string                 `json:"name" db:"name"`
	Region          string                 `json:"region" db:"region"`
	OwnerGuildID    *uuid.UUID             `json:"owner_guild_id,omitempty" db:"owner_guild_id"`
	Resources       map[string]interface{} `json:"resources" db:"resources"`
	DefenseLevel    int                    `json:"defense_level" db:"defense_level"`
	SiegeDifficulty int                    `json:"siege_difficulty" db:"siege_difficulty"`
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

type DeclareWarRequest struct {
	AttackerGuildID uuid.UUID   `json:"attacker_guild_id"`
	DefenderGuildID uuid.UUID   `json:"defender_guild_id"`
	TerritoryID     *uuid.UUID  `json:"territory_id,omitempty"`
	Allies          []uuid.UUID `json:"allies,omitempty"`
}

type CreateBattleRequest struct {
	WarID       uuid.UUID  `json:"war_id"`
	Type        BattleType `json:"type"`
	TerritoryID *uuid.UUID `json:"territory_id,omitempty"`
	StartTime   time.Time  `json:"start_time"`
}

type UpdateBattleScoreRequest struct {
	BattleID      uuid.UUID `json:"battle_id"`
	AttackerScore int       `json:"attacker_score"`
	DefenderScore int       `json:"defender_score"`
}

type WarListResponse struct {
	Wars  []ClanWar `json:"wars"`
	Total int       `json:"total"`
}

type BattleListResponse struct {
	Battles []WarBattle `json:"battles"`
	Total   int         `json:"total"`
}

type TerritoryListResponse struct {
	Territories []Territory `json:"territories"`
	Total       int         `json:"total"`
}

type WarDetailResponse struct {
	War       *ClanWar    `json:"war"`
	Battles   []WarBattle `json:"battles"`
	Territory *Territory  `json:"territory,omitempty"`
}
