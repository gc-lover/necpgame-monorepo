// Package models contains request and response models for the League System API
package models

import (
	"time"
)

// CreateLeagueRequest represents a request to create a new league
type CreateLeagueRequest struct {
	Name        string                 `json:"name" validate:"required,min=3,max=50"`
	Description string                 `json:"description" validate:"max=500"`
	StartDate   time.Time              `json:"start_date" validate:"required"`
	EndDate     time.Time              `json:"end_date" validate:"required,gtfield=StartDate"`
	Phases      []LeaguePhase          `json:"phases" validate:"required,min=1"`
	Rewards     map[string]interface{} `json:"rewards,omitempty"`
}

// UpdateLeagueRequest represents a request to update an existing league
type UpdateLeagueRequest struct {
	Name        *string                `json:"name,omitempty" validate:"omitempty,min=3,max=50"`
	Description *string                `json:"description,omitempty" validate:"omitempty,max=500"`
	StartDate   *time.Time             `json:"start_date,omitempty"`
	EndDate     *time.Time             `json:"end_date,omitempty"`
	Phases      []LeaguePhase          `json:"phases,omitempty" validate:"omitempty,min=1"`
	Rewards     map[string]interface{} `json:"rewards,omitempty"`
	Status      *LeagueStatus          `json:"status,omitempty"`
}

// TriggerResetRequest represents a request to trigger a global reset
type TriggerResetRequest struct {
	ResetType     ResetType `json:"reset_type" validate:"required"`
	Reason        string    `json:"reason" validate:"required,min=10,max=200"`
	AnnounceReset bool      `json:"announce_reset"`
}

// LeagueListResponse represents a paginated list of leagues
type LeagueListResponse struct {
	Leagues    []League `json:"leagues"`
	Page       int      `json:"page"`
	PerPage    int      `json:"per_page"`
	Total      int      `json:"total"`
	TotalPages int      `json:"total_pages"`
}

// LeagueStatisticsResponse represents league statistics
type LeagueStatisticsResponse struct {
	LeagueID      string    `json:"league_id"`
	TotalPlayers  int       `json:"total_players"`
	ActivePlayers int       `json:"active_players"`
	AverageScore  float64   `json:"average_score"`
	TopScore      int       `json:"top_score"`
	TopPlayer     string    `json:"top_player"`
	CurrentPhase  string    `json:"current_phase"`
	TimeRemaining string    `json:"time_remaining"`
	PhaseProgress float64   `json:"phase_progress"`
	GeneratedAt   time.Time `json:"generated_at"`
}

// PlayerLegacyProgressResponse represents a player's legacy progress
type PlayerLegacyProgressResponse struct {
	PlayerID     string                     `json:"player_id"`
	TotalSeasons int                        `json:"total_seasons"`
	BestRank     int                        `json:"best_rank"`
	TotalWins    int                        `json:"total_wins"`
	TotalPoints  int                        `json:"total_points"`
	Achievements []string                   `json:"achievements"`
	LegacyItems  []PlayerLegacyProgressItem `json:"legacy_items"`
	LastUpdated  time.Time                  `json:"last_updated"`
}

// HallOfFameResponse represents the hall of fame entries
type HallOfFameResponse struct {
	Category    HallOfFameCategory `json:"category"`
	Season      string             `json:"season"`
	Entries     []HallOfFameEntry  `json:"entries"`
	GeneratedAt time.Time          `json:"generated_at"`
}

// ListLeaguesParams represents query parameters for listing leagues
type ListLeaguesParams struct {
	Status  *LeagueStatus `form:"status,omitempty"`
	Page    int           `form:"page,omitempty" validate:"min=1"`
	PerPage int           `form:"per_page,omitempty" validate:"min=1,max=100"`
}

// ListLeagueStatisticsParams represents query parameters for league statistics
type ListLeagueStatisticsParams struct {
	LeagueID *string `form:"league_id,omitempty"`
}

// ListPlayerLegacyProgressParams represents query parameters for player legacy progress
type ListPlayerLegacyProgressParams struct {
	PlayerID *string `form:"player_id,omitempty"`
}

// ListHallOfFameParams represents query parameters for hall of fame
type ListHallOfFameParams struct {
	Category *HallOfFameCategory `form:"category,omitempty"`
	Season   *string             `form:"season,omitempty"`
	Limit    int                 `form:"limit,omitempty" validate:"min=1,max=50"`
}

// RegisterForEndEventRequest represents a request to register for end event
type RegisterForEndEventRequest struct {
	PlayerID string `json:"player_id" validate:"required"`
	EventID  string `json:"event_id" validate:"required"`
}
