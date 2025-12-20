package models

import (
	"time"

	"github.com/google/uuid"
)

type TrialType string

type SessionStatus string

type ChallengeType string

type StartTimeTrialRequest struct {
	TrialType TrialType  `json:"trial_type"`
	ContentID uuid.UUID  `json:"content_id"`
	TeamID    *uuid.UUID `json:"team_id,omitempty"`
}

type CompleteTimeTrialRequest struct {
	SessionID         uuid.UUID `json:"session_id"`
	CompletionTimeMs  int64     `json:"completion_time_ms"`
	DeathsCount       int       `json:"deaths_count,omitempty"`
	AbilitiesUsed     []string  `json:"abilities_used,omitempty"`
	RouteOptimization *float64  `json:"route_optimization,omitempty"`
}

type TimeTrialSession struct {
	ID               uuid.UUID     `json:"id"`
	TrialType        TrialType     `json:"trial_type"`
	ContentID        uuid.UUID     `json:"content_id"`
	PlayerID         uuid.UUID     `json:"player_id"`
	TeamID           *uuid.UUID    `json:"team_id,omitempty"`
	StartTime        time.Time     `json:"start_time"`
	EndTime          *time.Time    `json:"end_time,omitempty"`
	ElapsedTimeMs    int64         `json:"elapsed_time_ms,omitempty"`
	CompletionTimeMs *int64        `json:"completion_time_ms,omitempty"`
	Status           SessionStatus `json:"status"`
}

type Achievement struct {
	ID          uuid.UUID `json:"id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
}

type TimeTrialCompletionResponse struct {
	SessionID        uuid.UUID     `json:"session_id"`
	CompletionTimeMs int64         `json:"completion_time_ms"`
	Rank             int           `json:"rank"`
	IsNewRecord      bool          `json:"is_new_record"`
	IsPersonalBest   bool          `json:"is_personal_best"`
	RewardModifier   float64       `json:"reward_modifier"`
	Achievements     []Achievement `json:"achievements,omitempty"`
	LeaderboardURL   string        `json:"leaderboard_url,omitempty"`
}

type WeeklyTimeChallenge struct {
	ID            uuid.UUID              `json:"id"`
	WeekStart     time.Time              `json:"week_start"`
	WeekEnd       time.Time              `json:"week_end"`
	ChallengeType ChallengeType          `json:"challenge_type"`
	ContentID     uuid.UUID              `json:"content_id"`
	TimeLimitMs   int64                  `json:"time_limit_ms"`
	Conditions    map[string]interface{} `json:"conditions,omitempty"`
	Rewards       map[string]interface{} `json:"rewards,omitempty"`
	CreatedAt     time.Time              `json:"created_at"`
}

type WeeklyChallengeSummary struct {
	ID            uuid.UUID     `json:"id"`
	WeekStart     time.Time     `json:"week_start"`
	WeekEnd       time.Time     `json:"week_end"`
	ChallengeType ChallengeType `json:"challenge_type"`
	ContentID     uuid.UUID     `json:"content_id"`
	CreatedAt     time.Time     `json:"created_at"`
}

type WeeklyChallengeHistoryResponse struct {
	Items  []WeeklyChallengeSummary `json:"items"`
	Total  int                      `json:"total"`
	Limit  int                      `json:"limit"`
	Offset int                      `json:"offset"`
}
