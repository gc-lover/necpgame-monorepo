package models

import (
	"time"

	"github.com/google/uuid"
)

type LeaderboardMetric string

const (
	MetricOverallPower  LeaderboardMetric = "overall_power"
	MetricCombatScore   LeaderboardMetric = "combat_score"
	MetricLevel         LeaderboardMetric = "level"
	MetricAchievements  LeaderboardMetric = "achievements"
)

type LeaderboardScope string

const (
	ScopeGlobal   LeaderboardScope = "global"
	ScopeSeasonal LeaderboardScope = "seasonal"
	ScopeClass    LeaderboardScope = "class"
	ScopeFriends  LeaderboardScope = "friends"
	ScopeGuild    LeaderboardScope = "guild"
)

type LeaderboardType string

const (
	TypeGlobal   LeaderboardType = "global"
	TypeClass    LeaderboardType = "class"
	TypeSeasonal LeaderboardType = "seasonal"
	TypeFriend   LeaderboardType = "friend"
	TypeGuild    LeaderboardType = "guild"
)

type LeaderboardEntry struct {
	Rank         int       `json:"rank"`
	CharacterID  uuid.UUID `json:"character_id"`
	CharacterName string   `json:"character_name"`
	Score        int64     `json:"score"`
	Metric       LeaderboardMetric `json:"metric"`
	LastUpdated  time.Time `json:"last_updated"`
}

type LeaderboardResponse struct {
	Metric   LeaderboardMetric    `json:"metric"`
	Scope    LeaderboardScope     `json:"scope"`
	SeasonID *string              `json:"season_id,omitempty"`
	Entries  []LeaderboardEntry  `json:"entries"`
	Total    int                  `json:"total"`
	Limit    int                  `json:"limit"`
	Offset   int                  `json:"offset"`
}

type PlayerRank struct {
	CharacterID uuid.UUID         `json:"character_id"`
	Rank        int               `json:"rank"`
	Score       int64             `json:"score"`
	Metric      LeaderboardMetric `json:"metric"`
	Scope       LeaderboardScope  `json:"scope"`
	SeasonID    *string           `json:"season_id,omitempty"`
	TotalPlayers int              `json:"total_players"`
	LastUpdated  time.Time        `json:"last_updated"`
}

type Leaderboard struct {
	ID          uuid.UUID         `json:"id" db:"id"`
	Type        LeaderboardType   `json:"type" db:"type"`
	Metric      LeaderboardMetric `json:"metric" db:"metric"`
	SeasonID    *uuid.UUID       `json:"season_id,omitempty" db:"season_id"`
	ClassID     *uuid.UUID       `json:"class_id,omitempty" db:"class_id"`
	IsActive    bool             `json:"is_active" db:"is_active"`
	CreatedAt   time.Time        `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time        `json:"updated_at" db:"updated_at"`
}

type LeaderboardDetails struct {
	Leaderboard Leaderboard `json:"leaderboard"`
	TotalPlayers int         `json:"total_players"`
	LastUpdated  time.Time   `json:"last_updated"`
}

type LeaderboardListResponse struct {
	Leaderboards []Leaderboard `json:"leaderboards"`
	Total        int           `json:"total"`
	Limit        int           `json:"limit"`
	Offset       int           `json:"offset"`
}

type LeaderboardScore struct {
	ID           uuid.UUID         `json:"id" db:"id"`
	LeaderboardID uuid.UUID        `json:"leaderboard_id" db:"leaderboard_id"`
	CharacterID  uuid.UUID         `json:"character_id" db:"character_id"`
	Score        int64             `json:"score" db:"score"`
	Metric       LeaderboardMetric `json:"metric" db:"metric"`
	LastUpdated  time.Time         `json:"last_updated" db:"last_updated"`
	CreatedAt    time.Time         `json:"created_at" db:"created_at"`
}

