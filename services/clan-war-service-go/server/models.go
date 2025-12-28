// Issue: #427
// Domain models for Clan War Service

package server

import (
	"time"

	"github.com/google/uuid"
)

// ClanWar represents a clan war entity
type ClanWar struct {
	ID            uuid.UUID  `json:"id" db:"id"`
	ClanID1       uuid.UUID  `json:"clan_id_1" db:"clan_id_1"`
	ClanID2       uuid.UUID  `json:"clan_id_2" db:"clan_id_2"`
	Status        string     `json:"status" db:"status"` // pending, active, completed, cancelled
	TerritoryID   uuid.UUID  `json:"territory_id" db:"territory_id"`
	StartTime     *time.Time `json:"start_time,omitempty" db:"start_time"`
	EndTime       *time.Time `json:"end_time,omitempty" db:"end_time"`
	WinnerClanID  *uuid.UUID `json:"winner_clan_id,omitempty" db:"winner_clan_id"`
	ScoreClan1    int        `json:"score_clan_1" db:"score_clan_1"`
	ScoreClan2    int        `json:"score_clan_2" db:"score_clan_2"`
	CreatedAt     time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time  `json:"updated_at" db:"updated_at"`
}

// Battle represents a battle within a clan war
type Battle struct {
	ID           uuid.UUID  `json:"id" db:"id"`
	WarID        uuid.UUID  `json:"war_id" db:"war_id"`
	TerritoryID  uuid.UUID  `json:"territory_id" db:"territory_id"`
	Status       string     `json:"status" db:"status"` // pending, active, completed
	StartTime    *time.Time `json:"start_time,omitempty" db:"start_time"`
	EndTime      *time.Time `json:"end_time,omitempty" db:"end_time"`
	WinnerClanID *uuid.UUID `json:"winner_clan_id,omitempty" db:"winner_clan_id"`
	ScoreClan1   int        `json:"score_clan_1" db:"score_clan_1"`
	ScoreClan2   int        `json:"score_clan_2" db:"score_clan_2"`
	CreatedAt    time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time  `json:"updated_at" db:"updated_at"`
}

// Territory represents a territory that can be contested in wars
type Territory struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	Description string    `json:"description" db:"description"`
	Type        string    `json:"type" db:"type"` // strategic, resource, border
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

// WarStatistics represents war statistics for analysis
type WarStatistics struct {
	WarID         uuid.UUID `json:"war_id"`
	TotalBattles  int       `json:"total_battles"`
	ActiveBattles int       `json:"active_battles"`
	Clan1Score    int       `json:"clan_1_score"`
	Clan2Score    int       `json:"clan_2_score"`
}
